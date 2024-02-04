package dev.fneira.examples.interfaceprocessor.interfaces;

import dev.fneira.examples.interfaceprocessor.resourceprovider.ResourceProvider;
import dev.fneira.examples.interfaceprocessor.resourceprovider.ResourceValue;

@ResourceProvider
public interface IMyResource2Provider {

  @ResourceValue(key = "my-key-2", source = "datagrid")
  String getResource();
}
