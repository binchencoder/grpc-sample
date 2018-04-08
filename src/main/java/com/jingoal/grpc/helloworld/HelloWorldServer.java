package com.jingoal.grpc.helloworld;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class HelloWorldServer {

  private static Logger log = LoggerFactory.getLogger(HelloWorldServer.class);

  /* The port on which the server should run */
  private int port = 50501;
  private Server server;

  private void start() throws IOException {
    // 使用ServerBuilder来构建和启动服务, 通过使用forPort方法来指定监听的地址和端口
    // 创建一个实现方法的服务GreeterImpl的实例, 并通过addService方法将该实例纳入
    // 调用build() start()方法构建和启动rpcserver
    System.out.println("Start HelloWorldServer.");
    server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();
    log.info("Server started, listening on " + port);

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

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }

  // 我们的服务GreeterImpl继承了生成抽象类GreeterGrpc.GreeterImplBase, 实现了服务的所有方法
  private class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloWorldReq request, StreamObserver<HelloWorldResp> responseObserver) {
      HelloWorldResp resp =
          HelloWorldResp.newBuilder().setMessage("hello" + request.getName()).build();
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
