package dev.fneira.examples.interfaceprocessor.interfaces;

import dev.fneira.examples.interfaceprocessor.resourceprovider.ResourceValue;

public interface IMyResource3Provider {

  @ResourceValue(key = "my-key-2", source = "datagrid")
  String getResource();
}
