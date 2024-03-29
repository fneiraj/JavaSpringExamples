package dev.fneira.interfaceprocessor.interfaces;

import dev.fneira.interfaceprocessor.restclient.RestClient;
import dev.fneira.interfaceprocessor.restclient.RestClientCfg;
import dev.fneira.interfaceprocessor.restclient.RestClientParam;

@RestClient(name = "my-rest-client", url = "http://localhost:8080")
public interface IMyRestClient {

  @RestClientCfg(method = "POST", path = "/login")
  String login(
      @RestClientParam(name = "username", type = "form-data") String username,
      @RestClientParam(name = "password", type = "form-data") String password);

  @RestClientCfg(method = "GET", path = "/profile")
  String getProfile(String id);
}
