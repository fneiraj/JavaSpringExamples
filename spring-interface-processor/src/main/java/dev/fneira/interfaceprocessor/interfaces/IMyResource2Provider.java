package dev.fneira.interfaceprocessor.interfaces;

import dev.fneira.interfaceprocessor.resourceprovider.ResourceProvider;
import dev.fneira.interfaceprocessor.resourceprovider.ResourceValue;

@ResourceProvider
public interface IMyResource2Provider {

  @ResourceValue(key = "my-key-2", source = "datagrid")
  String getResource();
}
