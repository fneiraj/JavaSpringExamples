package dev.fneira.examples.interfaceprocessor;

import dev.fneira.examples.interfaceprocessor.imp.EnableInterfaceProxy;
import dev.fneira.examples.interfaceprocessor.interfaces.IMyResourceProvider;
import dev.fneira.examples.interfaceprocessor.interfaces.IMyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableInterfaceProxy(
    basePackages = "interfaces-proxy.basePackages",
    annotations = "interfaces-proxy.annotations")
public class InterfaceProcessorApplication implements CommandLineRunner {

  private final IMyResourceProvider myResourceProvider;
  private final IMyRestClient myRestClient;

  @Autowired
  public InterfaceProcessorApplication(
      final IMyResourceProvider myResourceProvider, final IMyRestClient myRestClient) {
    this.myResourceProvider = myResourceProvider;
    this.myRestClient = myRestClient;
  }

  public static void main(final String[] args) {
    SpringApplication.run(InterfaceProcessorApplication.class, args);
  }

  @Override
  public void run(final String... args) throws Exception {
    System.out.println(myResourceProvider.getResource());
    System.out.println(myResourceProvider.getIntResource());
    System.out.println(myResourceProvider.getOtherResource());
    System.out.println(myRestClient.login("my-user", "my-password"));
    System.out.println(myRestClient.getCreditCards("fneiraj"));
  }
}
