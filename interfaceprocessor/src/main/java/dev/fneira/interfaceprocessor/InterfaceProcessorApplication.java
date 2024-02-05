package dev.fneira.interfaceprocessor;

import dev.fneira.interfaceprocessor.imp.annotations.EnableInterfaceProxy;
import dev.fneira.interfaceprocessor.interfaces.IMyResourceProvider;
import dev.fneira.interfaceprocessor.interfaces.IMyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableInterfaceProxy
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
    System.out.println(myRestClient.getProfile("fneiraj"));
    System.exit(0);
  }
}
