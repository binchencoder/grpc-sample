package com.chenbin.grpc.routeguide;

import com.chenbin.grpc.routeguide.RouteGuideGrpc.RouteGuideBlockingStub;
import com.chenbin.grpc.routeguide.RouteGuideGrpc.RouteGuideStub;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenbin on 18-4-9.
 */
public class RouteGuideClient {

  private static final Logger logger = LoggerFactory.getLogger(RouteGuideClient.class);

  private ManagedChannel channel;
  private RouteGuideBlockingStub blockingStub;
  private RouteGuideStub asyncStub;

  private Random random = new Random();
  private TestHelper testHelper;

  /**
   * Construct client for accessing RouteGuide server at {@code host:port}.
   */
  public RouteGuideClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
  }

  /**
   * Construct client for accessing RouteGuide server using the existing channel.
   */
  public RouteGuideClient(ManagedChannelBuilder<?> channelBuilder) {
    channel = channelBuilder.build();
    blockingStub = RouteGuideGrpc.newBlockingStub(channel);
    asyncStub = RouteGuideGrpc.newStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public CountDownLatch routeChat() {
    logger.info("*** RouteChat");
    final CountDownLatch finishLatch = new CountDownLatch(1);
    StreamObserver<RouteNote> requestObserver = asyncStub.routeChat(
        new StreamObserver<RouteNote>() {
          @Override
          public void onNext(RouteNote routeNote) {
            logger.info("Got message \"{}\" at {}, {}", routeNote.getMessage(),
                routeNote.getLocation().getLatitude(), routeNote.getLocation().getLongitude());
            if (null != testHelper) {
              testHelper.onMessage(routeNote);
            }
          }

          @Override
          public void onError(Throwable throwable) {
            logger.error("RouteChat Failed: {}", Status.fromThrowable(throwable));
            if (null != testHelper) {
              testHelper.onRpcError(throwable);
            }

            finishLatch.countDown();
          }

          @Override
          public void onCompleted() {
            logger.info("Finished RouteChat");
            finishLatch.countDown();
          }
        });

    try {
      RouteNote[] requests = {newNote("First message", 0, 0), newNote("Second message", 0, 1),
          newNote("Third message", 1, 0), newNote("Fourth message", 1, 1)};
      for (RouteNote request : requests) {
        logger.info("Sending message \"{}\" at {}, {}", request.getMessage(), request.getLocation()
            .getLatitude(), request.getLocation().getLongitude());
        requestObserver.onNext(request);
        Thread.sleep(1000L);
      }
    } catch (RuntimeException e) {
      // Cancel RPC
      requestObserver.onError(e);
      throw e;
    } catch (Exception ex) {
      requestObserver.onError(ex);
      ex.printStackTrace();
    }

    // Mark the end of requests.
    requestObserver.onCompleted();

    // Return the latch while receiving happens asynchronously.
    return finishLatch;
  }

  public static void main(String[] args) throws InterruptedException {
    List<Feature> features;
    try {
      features = RouteGuideUtil.parseFeatures(RouteGuideUtil.getDefaultFeaturesFile());
    } catch (IOException ex) {
      ex.printStackTrace();
      return;
    }

    RouteGuideClient client = new RouteGuideClient("localhost", 8980);
    try {
      // Send and receive some notes.
      CountDownLatch finishLatch = client.routeChat();

      if (!finishLatch.await(1, TimeUnit.MINUTES)) {
        logger.warn("RouteChat can not finish within 1 minutes");
      }
    } finally {
      client.shutdown();
    }
  }

  private RouteNote newNote(String message, int lat, int lon) {
    return RouteNote.newBuilder().setMessage(message)
        .setLocation(Point.newBuilder().setLatitude(lat).setLongitude(lon).build()).build();
  }

  @VisibleForTesting
  interface TestHelper {

    /**
     * Used for verify/inspect message received from server.
     */
    void onMessage(Message message);

    /**
     * Used for verify/inspect error received from server.
     */
    void onRpcError(Throwable exception);
  }

  @VisibleForTesting
  void setTestHelper(TestHelper helper) {
    this.testHelper = helper;
  }
}
