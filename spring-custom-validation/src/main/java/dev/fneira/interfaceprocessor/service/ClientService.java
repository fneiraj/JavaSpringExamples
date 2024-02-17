package dev.fneira.interfaceprocessor.service;

import dev.fneira.interfaceprocessor.dto.ClientDTO;

public interface ClientService {

  ClientDTO createClient(final ClientDTO clientDTO);
}
