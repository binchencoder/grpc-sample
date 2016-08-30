package com.jingoal.grpc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jingoal.grpc.helloworld.GreeterGrpc;
import com.jingoal.grpc.helloworld.HelloWorldReq;
import com.jingoal.grpc.helloworld.HelloWorldResp;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class HelloWorldServer {

  private static Logger log = LoggerFactory.getLogger(HelloWorldServer.class);

  /* The port on which the server should run */
  private int port = 50501;
  private Server server;

  private void start() throws IOException {
    System.out.println("Start HelloWorldServer.");
    server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();
    log.info("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        HelloWorldServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
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

  public static void main(String[] args) throws IOException, InterruptedException {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }

  private class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloWorldReq request, StreamObserver<HelloWorldResp> responseObserver) {
      HelloWorldResp resp =
          HelloWorldResp.newBuilder().setMessage("hello" + request.getName()).build();

      responseObserver.onNext(resp);
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
