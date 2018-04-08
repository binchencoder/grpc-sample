package com.chenbin.grpc.routeguide;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Interface exported by the server.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.3.1)",
    comments = "Source: src/main/proto/route_guide.proto")
public final class RouteGuideGrpc {

  private RouteGuideGrpc() {}

  public static final String SERVICE_NAME = "routeguide.RouteGuide";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<Point,
      Feature> METHOD_GET_FEATURE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "routeguide.RouteGuide", "GetFeature"),
          io.grpc.protobuf.ProtoUtils.marshaller(Point.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(Feature.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<Rectangle,
      Feature> METHOD_LIST_FEATURES =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "routeguide.RouteGuide", "ListFeatures"),
          io.grpc.protobuf.ProtoUtils.marshaller(Rectangle.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(Feature.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<Point,
      RouteSummary> METHOD_RECORD_ROUTE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING,
          generateFullMethodName(
              "routeguide.RouteGuide", "RecordRoute"),
          io.grpc.protobuf.ProtoUtils.marshaller(Point.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteSummary.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<RouteNote,
      RouteNote> METHOD_ROUTE_CHAT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "routeguide.RouteGuide", "RouteChat"),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteNote.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(RouteNote.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RouteGuideStub newStub(io.grpc.Channel channel) {
    return new RouteGuideStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RouteGuideBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RouteGuideBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static RouteGuideFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RouteGuideFutureStub(channel);
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static abstract class RouteGuideImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * 一个 简单 RPC, 客户端使用存根发送请求到服务器并等待响应返回, 就像平常的函数调用一样.
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public void getFeature(Point request,
        io.grpc.stub.StreamObserver<Feature> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_FEATURE, responseObserver);
    }

    /**
     * <pre>
     * 一个 服务器端流式 RPC, 客户端发送请求到服务器, 拿到一个流去读取返回的消息序列.
     * 客户端读取返回的流, 直到里面没有任何消息.
     * A server-to-client streaming RPC.
     * Obtains the Features available within the given Rectangle.  Results are
     * streamed rather than returned at once (e.g. in a response message with a
     * repeated field), as the rectangle may cover a large area and contain a
     * huge number of features.
     * </pre>
     */
    public void listFeatures(Rectangle request,
        io.grpc.stub.StreamObserver<Feature> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_FEATURES, responseObserver);
    }

    /**
     * <pre>
     * 一个 客户端流式 RPC, 客户端写入一个消息序列并将其发送到服务器, 同样也是使用流.
     * 一旦 客户端完成写入消息, 它等待服务器完成读取返回它的响应.
     * A client-to-server streaming RPC.
     * Accepts a stream of Points on a route being traversed, returning a
     * RouteSummary when traversal is completed.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<Point> recordRoute(
        io.grpc.stub.StreamObserver<RouteSummary> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_RECORD_ROUTE, responseObserver);
    }

    /**
     * <pre>
     * 一个 双向流式 RPC 是双方使用读写流去发送一个消息序列. 两个流独立操作,
     * 因此客户端和服务器 可以以任意喜欢的顺序读写:
     * 比如, 服务器可以在写入响应前等待接收所有的客户端消息,
     * 或者可以交替的读取和写入消息, 或者其他读写的组合.
     * A Bidirectional streaming RPC.
     * Accepts a stream of RouteNotes sent while a route is being traversed,
     * while receiving other RouteNotes (e.g. from other users).
     * </pre>
     */
    public io.grpc.stub.StreamObserver<RouteNote> routeChat(
        io.grpc.stub.StreamObserver<RouteNote> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_ROUTE_CHAT, responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_FEATURE,
            asyncUnaryCall(
              new MethodHandlers<
                Point,
                Feature>(
                  this, METHODID_GET_FEATURE)))
          .addMethod(
            METHOD_LIST_FEATURES,
            asyncServerStreamingCall(
              new MethodHandlers<
                Rectangle,
                Feature>(
                  this, METHODID_LIST_FEATURES)))
          .addMethod(
            METHOD_RECORD_ROUTE,
            asyncClientStreamingCall(
              new MethodHandlers<
                Point,
                RouteSummary>(
                  this, METHODID_RECORD_ROUTE)))
          .addMethod(
            METHOD_ROUTE_CHAT,
            asyncBidiStreamingCall(
              new MethodHandlers<
                RouteNote,
                RouteNote>(
                  this, METHODID_ROUTE_CHAT)))
          .build();
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class RouteGuideStub extends io.grpc.stub.AbstractStub<RouteGuideStub> {
    private RouteGuideStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RouteGuideStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RouteGuideStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RouteGuideStub(channel, callOptions);
    }

    /**
     * <pre>
     * 一个 简单 RPC, 客户端使用存根发送请求到服务器并等待响应返回, 就像平常的函数调用一样.
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public void getFeature(Point request,
        io.grpc.stub.StreamObserver<Feature> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_FEATURE, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 一个 服务器端流式 RPC, 客户端发送请求到服务器, 拿到一个流去读取返回的消息序列.
     * 客户端读取返回的流, 直到里面没有任何消息.
     * A server-to-client streaming RPC.
     * Obtains the Features available within the given Rectangle.  Results are
     * streamed rather than returned at once (e.g. in a response message with a
     * repeated field), as the rectangle may cover a large area and contain a
     * huge number of features.
     * </pre>
     */
    public void listFeatures(Rectangle request,
        io.grpc.stub.StreamObserver<Feature> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_LIST_FEATURES, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 一个 客户端流式 RPC, 客户端写入一个消息序列并将其发送到服务器, 同样也是使用流.
     * 一旦 客户端完成写入消息, 它等待服务器完成读取返回它的响应.
     * A client-to-server streaming RPC.
     * Accepts a stream of Points on a route being traversed, returning a
     * RouteSummary when traversal is completed.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<Point> recordRoute(
        io.grpc.stub.StreamObserver<RouteSummary> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(METHOD_RECORD_ROUTE, getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * 一个 双向流式 RPC 是双方使用读写流去发送一个消息序列. 两个流独立操作,
     * 因此客户端和服务器 可以以任意喜欢的顺序读写:
     * 比如, 服务器可以在写入响应前等待接收所有的客户端消息,
     * 或者可以交替的读取和写入消息, 或者其他读写的组合.
     * A Bidirectional streaming RPC.
     * Accepts a stream of RouteNotes sent while a route is being traversed,
     * while receiving other RouteNotes (e.g. from other users).
     * </pre>
     */
    public io.grpc.stub.StreamObserver<RouteNote> routeChat(
        io.grpc.stub.StreamObserver<RouteNote> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_ROUTE_CHAT, getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class RouteGuideBlockingStub extends io.grpc.stub.AbstractStub<RouteGuideBlockingStub> {
    private RouteGuideBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RouteGuideBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RouteGuideBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RouteGuideBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 一个 简单 RPC, 客户端使用存根发送请求到服务器并等待响应返回, 就像平常的函数调用一样.
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public Feature getFeature(Point request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_FEATURE, getCallOptions(), request);
    }

    /**
     * <pre>
     * 一个 服务器端流式 RPC, 客户端发送请求到服务器, 拿到一个流去读取返回的消息序列.
     * 客户端读取返回的流, 直到里面没有任何消息.
     * A server-to-client streaming RPC.
     * Obtains the Features available within the given Rectangle.  Results are
     * streamed rather than returned at once (e.g. in a response message with a
     * repeated field), as the rectangle may cover a large area and contain a
     * huge number of features.
     * </pre>
     */
    public java.util.Iterator<Feature> listFeatures(
        Rectangle request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_LIST_FEATURES, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Interface exported by the server.
   * </pre>
   */
  public static final class RouteGuideFutureStub extends io.grpc.stub.AbstractStub<RouteGuideFutureStub> {
    private RouteGuideFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RouteGuideFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RouteGuideFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RouteGuideFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 一个 简单 RPC, 客户端使用存根发送请求到服务器并等待响应返回, 就像平常的函数调用一样.
     * A simple RPC.
     * Obtains the feature at a given position.
     * A feature with an empty name is returned if there's no feature at the given
     * position.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<Feature> getFeature(
        Point request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_FEATURE, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_FEATURE = 0;
  private static final int METHODID_LIST_FEATURES = 1;
  private static final int METHODID_RECORD_ROUTE = 2;
  private static final int METHODID_ROUTE_CHAT = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RouteGuideImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RouteGuideImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_FEATURE:
          serviceImpl.getFeature((Point) request,
              (io.grpc.stub.StreamObserver<Feature>) responseObserver);
          break;
        case METHODID_LIST_FEATURES:
          serviceImpl.listFeatures((Rectangle) request,
              (io.grpc.stub.StreamObserver<Feature>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECORD_ROUTE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.recordRoute(
              (io.grpc.stub.StreamObserver<RouteSummary>) responseObserver);
        case METHODID_ROUTE_CHAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.routeChat(
              (io.grpc.stub.StreamObserver<RouteNote>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class RouteGuideDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RouteGuideProto.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RouteGuideGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RouteGuideDescriptorSupplier())
              .addMethod(METHOD_GET_FEATURE)
              .addMethod(METHOD_LIST_FEATURES)
              .addMethod(METHOD_RECORD_ROUTE)
              .addMethod(METHOD_ROUTE_CHAT)
              .build();
        }
      }
    }
    return result;
  }
}
