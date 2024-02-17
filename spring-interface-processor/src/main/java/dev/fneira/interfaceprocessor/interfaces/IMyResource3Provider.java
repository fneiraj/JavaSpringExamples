package dev.fneira.interfaceprocessor.interfaces;

import dev.fneira.interfaceprocessor.resourceprovider.ResourceValue;

public interface IMyResource3Provider {

  @ResourceValue(key = "my-key-2", source = "datagrid")
  String getResource();
}
