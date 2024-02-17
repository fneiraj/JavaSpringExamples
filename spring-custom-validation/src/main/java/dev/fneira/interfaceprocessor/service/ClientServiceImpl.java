package dev.fneira.interfaceprocessor.service;

import dev.fneira.interfaceprocessor.dto.ClientDTO;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

  @Override
  public ClientDTO createClient(final ClientDTO clientDTO) {
    // your business logic here

    // return the clientDTO
    return clientDTO;
  }
}
