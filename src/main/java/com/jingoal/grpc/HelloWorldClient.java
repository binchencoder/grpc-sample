package com.jingoal.grpc;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jingoal.grpc.helloworld.GreeterGrpc;
import com.jingoal.grpc.helloworld.HelloWorldReq;
import com.jingoal.grpc.helloworld.HelloWorldResp;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class HelloWorldClient {
  private static Logger log = LoggerFactory.getLogger(HelloWorldClient.class);

  private ManagedChannel channel;
  private GreeterGrpc.GreeterBlockingStub blockingStub;

  public HelloWorldClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void greet(String name) {
    log.info("Will try to greet " + name + "...");
    HelloWorldReq request = HelloWorldReq.newBuilder().setName(name).build();
    HelloWorldResp resp;

    try {
      resp = blockingStub.sayHello(request);
    } catch (StatusRuntimeException ex) {
      log.error("RPC failed: {0}", ex.getStatus());
      return;
    }

    log.info("Greeting, response message is: " + resp.getMessage());
  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting.
   * 
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException {
    HelloWorldClient client = new HelloWorldClient("localhost", 50501);
    try {
      String user = "world";
      client.greet(user);
    } finally {
      client.shutdown();
    }
  }
}
