package com.chenbin.grpc.routeguide;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenbin on 18-4-9.
 */
public class RouteGuideServer {

  private static final Logger logger = LoggerFactory.getLogger(RouteGuideServer.class);

  private int port;
  private Server server;

  public RouteGuideServer(int port) throws IOException {
    this(port, RouteGuideUtil.getDefaultFeaturesFile());
  }

  /**
   * Create a RouteGuide server listening on {@code port} using {@code featureFile} database.
   */
  public RouteGuideServer(int port, URL featureFile) throws IOException {
    this(ServerBuilder.forPort(port), port, RouteGuideUtil.parseFeatures(featureFile));
  }

  /**
   * Create a RouteGuide server using serverBuilder as a base and features as data.
   */
  public RouteGuideServer(ServerBuilder<?> serverBuilder, int port, Collection<Feature> features) {
    this.port = port;
    server = serverBuilder.addService(new RouteGuideService(features))
        .build();
  }

  /**
   * Start serving requests.
   */
  public void start() throws IOException {
    server.start();
    logger.info("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      // Use stderr here since the logger may has been reset by its JVM shutdown hook.
      System.err.println("*** shutting down gRPC server since JVM is shutting down");
      RouteGuideServer.this.stop();
      System.err.println("*** server shut down");
    }));
  }

  /**
   * Stop serving requests and shutdown resources.
   */
  public void stop() {
    if (null != server) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main method. This comment makes the linter happy.
   */
  public static void main(String[] args) throws Exception {
    RouteGuideServer server = new RouteGuideServer(8980);
    server.start();
    server.blockUntilShutdown();
  }

  /**
   * Our implementation of RouteGuide service.
   */
  private static class RouteGuideService extends RouteGuideGrpc.RouteGuideImplBase {

    private Collection<Feature> features;
    private final ConcurrentMap<Point, List<RouteNote>> routeNotes = new ConcurrentHashMap<Point, List<RouteNote>>();

    public RouteGuideService() {
      super();
    }

    public RouteGuideService(Collection<Feature> features) {
      this.features = features;
    }

    @Override
    public void getFeature(Point request, StreamObserver<Feature> responseObserver) {
      // TODO(chenbin): a simple RPC.
      super.getFeature(request, responseObserver);
    }

    @Override
    public void listFeatures(Rectangle request, StreamObserver<Feature> responseObserver) {
      // TODO(chenbin): A server-to-client streaming RPC.
      for (Feature feature : features) {
        if (feature.hasLocation()) {
          responseObserver.onNext(feature);
        }
      }
      responseObserver.onCompleted();
    }

    /**
     * Gets a stream of points, and responds with statistics about the "trip": number of points,
     * number of known features visited, total distance traveled, and total time spent.
     *
     * @param responseObserver an observer to receive the response summary.
     * @return an observer to receive the requested route points.
     */
    @Override
    public StreamObserver<Point> recordRoute(StreamObserver<RouteSummary> responseObserver) {
      return new StreamObserver<Point>() {
        @Override
        public void onNext(Point point) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {

        }
      };
    }

    /**
     * Gets a stream of points, and responds with statistics about the "trip": number of points,
     * number of known features visited, total distance traveled, and total time spent.
     *
     * @param responseObserver an observer to receive the response summary.
     * @return an observer to receive the requested route points.
     */
    @Override
    public StreamObserver<RouteNote> routeChat(StreamObserver<RouteNote> responseObserver) {
      // 一个 双向流式 RPC 是双方使用读写流去发送一个消息序列. 两个流独立操作,
      // 因此客户端和服务器 可以以任意喜欢的顺序读写:
      // 比如, 服务器可以在写入响应前等待接收所有的客户端消息,
      // 或者可以交替的读取和写入消息, 或者其他读写的组合.
      // A Bidirectional streaming RPC.
      return new StreamObserver<RouteNote>() {
        @Override
        public void onNext(RouteNote routeNote) {
          List<RouteNote> notes = getOrCreateNotes(routeNote.getLocation());

          // Respond with all previous notes at this location.
          for (RouteNote preNote : notes.toArray(new RouteNote[0])) {
            responseObserver.onNext(preNote);
          }

          // Now add the new note to the list.
          notes.add(routeNote);
        }

        @Override
        public void onError(Throwable throwable) {
          logger.error("routeChat cancelled", throwable.getMessage());
        }

        @Override
        public void onCompleted() {
          responseObserver.onCompleted();
        }
      };
    }

    /**
     * Get the notes list for the given location. If missing, create it.
     */
    private List<RouteNote> getOrCreateNotes(Point location) {
      List<RouteNote> notes = Collections.synchronizedList(new ArrayList<RouteNote>());
      List<RouteNote> preNotes = routeNotes.putIfAbsent(location, notes);
      return preNotes != null ? preNotes : notes;
    }

    /**
     * Gets the feature at the given point.
     *
     * @param location the location to check.
     * @return The feature object at the point. Note that an empty name indicates no feature.
     */
    private Feature checkFeature(Point location) {
      for (Feature feature : features) {
        if (feature.getLocation().getLatitude() == location.getLatitude()
            && feature.getLocation().getLongitude() == location.getLongitude()) {
          return feature;
        }
      }

      // No feature was found, return an unnamed feature.
      return Feature.newBuilder().setName("").setLocation(location).build();
    }
  }
}
