package com.chenbin.grpc.atomix;

import io.atomix.AtomixReplica;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.storage.Storage;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by chenbin on 18-4-11.
 */
public class AtomixServer {

  public static void main(String[] args) throws Exception {
    System.err.println(System.getProperty("user.dir"));

    Address address = new Address("localhost", 12345);
    AtomixReplica replica = AtomixReplica.builder(address)
        .withTransport(new NettyTransport())
        .withStorage(Storage.builder()
            .withDirectory(System.getProperty("user.dir") + "/logs/" + UUID.randomUUID().toString())
            .build())
        .build();
    replica.bootstrap();
  }
}
