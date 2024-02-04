package dev.fneira.examples.interfaceprocessor.interfaces;

import dev.fneira.examples.interfaceprocessor.resourceprovider.ResourceProvider;
import dev.fneira.examples.interfaceprocessor.resourceprovider.ResourceValue;

@ResourceProvider
public interface IMyResourceProvider {

  @ResourceValue(key = "my-key-1", source = "redis")
  String getResource();

  @ResourceValue(key = "my-other-resource-key", source = "redis")
  String getOtherResource();

  @ResourceValue(key = "my-other-resource-key", source = "redis")
  int getIntResource();
}
