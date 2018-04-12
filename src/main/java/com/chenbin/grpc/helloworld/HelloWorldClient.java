package com.chenbin.grpc.helloworld;

import com.chenbin.grpc.atomix.AtomixNameResolverFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldClient {

  private static Logger logger = LoggerFactory.getLogger(HelloWorldClient.class);

  private ManagedChannel channel;
  private GreeterGrpc.GreeterBlockingStub blockingStub;

  /**
   * Construct client connecting to HelloWorld server at {@code host:port}.
   */
  // 首先, 我们需要为stub创建一个grpc的channel, 指定我们连接服务端的地址和端口
  // 使用ManagedChannelBuilder方法来创建channel
  public HelloWorldClient(String host, int port) {
//    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
    channel = ManagedChannelBuilder
        .forTarget("atomix://localhost:12345/service-helloworld")
        .nameResolverFactory(new AtomixNameResolverFactory())
        .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
        .usePlaintext(true)
        .build();
    blockingStub = GreeterGrpc.newBlockingStub(channel)
        /*.withDeadlineAfter(2000, TimeUnit.MILLISECONDS)*/;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void greet(String name) {
    logger.info("Will try to greet " + name + "...");
    HelloWorldReq request = HelloWorldReq.newBuilder().setName(name).build();
    HelloWorldResp resp;

    try {
      resp = blockingStub.sayHello(request);
    } catch (StatusRuntimeException ex) {
      logger.error("RPC failed: {}", ex.getStatus());
      return;
    }

    logger.info("Greeting, response message is: " + resp.getMessage());
  }

  /**
   * Greet server. If provided, the first element of {@code args} is the name to use in the
   * greeting.
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
