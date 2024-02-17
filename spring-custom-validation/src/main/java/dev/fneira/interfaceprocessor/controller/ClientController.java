package dev.fneira.interfaceprocessor.controller;

import dev.fneira.interfaceprocessor.dto.ClientDTO;
import dev.fneira.interfaceprocessor.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

  @Autowired private ClientService clientService;

  @PostMapping
  public ClientDTO createClient(final @Valid @RequestBody ClientDTO clientDTO) {
    return clientService.createClient(clientDTO);
  }
}
