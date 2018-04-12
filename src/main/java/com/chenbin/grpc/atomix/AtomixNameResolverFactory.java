package com.chenbin.grpc.atomix;

import com.google.common.base.Splitter;
import io.atomix.catalyst.transport.Address;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Created by chenbin on 18-4-11.
 */
public class AtomixNameResolverFactory extends NameResolver.Factory {

  @Nullable
  @Override
  public NameResolver newNameResolver(URI uri, Attributes attributes) {
    String authority = uri.getAuthority();
    List<Address> cluster = Splitter.on(",").withKeyValueSeparator(':').split(authority)
        .entrySet().stream()
        .map(entry -> new Address(entry.getKey(),
            entry.getValue() != null ? Integer.valueOf(entry.getValue()) : 12345))
        .collect(Collectors.toList());
    return new AtomixNameResolver(authority, cluster, uri.getPath().substring(1));
  }

  @Override
  public String getDefaultScheme() {
    return "atomix";
  }
}
