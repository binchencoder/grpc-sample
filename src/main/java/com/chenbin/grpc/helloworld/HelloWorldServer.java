package com.chenbin.grpc.helloworld;

import com.google.protobuf.InvalidProtocolBufferException;
import io.atomix.Atomix;
import io.atomix.AtomixClient;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.group.DistributedGroup;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jdk.internal.cmm.SystemResourcePressureImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldServer {

  private static Logger logger = LoggerFactory.getLogger(HelloWorldServer.class);

  /* The port on which the server should run */
  private final int port;
  /* The list of address of the Atomix servers */
  private final List<Address> cluster;
  private Server server;

  public HelloWorldServer(int port, List<Address> cluster) {
    this.port = port;
    this.cluster = cluster;
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws Exception {
    List<Address> cluster = new ArrayList<>();
    cluster.add(new Address("localhost", 12345));

    final HelloWorldServer server1 = new HelloWorldServer(50053, cluster);
    server1.start();

    final HelloWorldServer server2 = new HelloWorldServer(50054, cluster);
    server2.start();

    server1.blockUntilShutdown();
    server2.blockUntilShutdown();
  }

  private void start() throws Exception {
    InetSocketAddress publishAddress = new InetSocketAddress("localhost", port);

    // 使用ServerBuilder来构建和启动服务, 通过使用forPort方法来指定监听的地址和端口
    // 创建一个实现方法的服务GreeterImpl的实例, 并通过addService方法将该实例纳入
    // 调用build() start()方法构建和启动rpcserver
    System.out.println("Start HelloWorldServer.");
    server = ServerBuilder
        .forPort(port)
        .addService(new GreeterImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);

    // Register
    AtomixClient client = AtomixClient.builder().withTransport(new NettyTransport()).build();
    Atomix atomix = client.connect(cluster).get();
    DistributedGroup group = atomix.getGroup("service-helloworld").get();
    // Add the address in metadata
    group.join(Collections.singletonMap("address", publishAddress)).thenAccept(member -> {
      logger.info("{} joined the group!", member.id());
    }).get();

    // SDH
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      // Use stderr here since the logger may have been reset by its JVM shutdown hook.
      System.err.println("*** shutting down gRPC server since JVM is shutting down");
      HelloWorldServer.this.stop();
      System.err.println("*** server shut down");
    }));
  }

  private void stop() {
    if (server != null) {
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

  // 我们的服务GreeterImpl继承了生成抽象类GreeterGrpc.GreeterImplBase, 实现了服务的所有方法
  private class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloWorldReq request, StreamObserver<HelloWorldResp> responseObserver) {
      byte[] reqBytes = request.toByteArray();
      System.out.println("Request bytes: " + reqBytes);
      try {
        HelloWorldReq parseReq = HelloWorldReq.parseFrom(reqBytes);
        System.out.println("Parse request: " + parseReq);
      } catch (InvalidProtocolBufferException e) {
        e.printStackTrace();
      }

      HelloWorldResp resp =
          HelloWorldResp.newBuilder().setMessage("hello" + request.getName()).build();
      try {
        Thread.sleep(10000L);
      } catch (Exception e) {
      }

      // 使用响应监视器的onNext方法返回HelloReply
      responseObserver.onNext(resp);
      // 使用onCompleted方法指定本次调用已经完成
      responseObserver.onCompleted();
    }

    @Override
    public void sayHelloAgain(HelloWorldReq request,
        StreamObserver<HelloWorldResp> responseObserver) {
      HelloWorldResp resp =
          HelloWorldResp.newBuilder().setMessage("hello again" + request.getName()).build();

      responseObserver.onNext(resp);
      responseObserver.onCompleted();
    }
  }
}
