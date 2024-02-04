package dev.fneira.examples.interfaceprocessor;

import org.springframework.stereotype.Component;

@Component
public class FakeDataProviderStub {
  public String getResource(String key) {
    return "Resource: " + key;
  }
}
