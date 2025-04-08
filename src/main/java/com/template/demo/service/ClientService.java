package com.template.demo.service;

import static com.template.demo.constants.SystemMessages.CLIENT_ALREADY_EXISTS;
import static com.template.demo.constants.SystemMessages.CLIENT_CREATED_SUCCESSFULLY;

import com.template.demo.dto.request.ClientRequest;
import com.template.demo.dto.response.ClientResponse;
import com.template.demo.model.Client;
import com.template.demo.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public ResponseEntity<ClientResponse> createClient(ClientRequest request) {
        if (clientRepository.findByCpfOrEmail(request.getCpf(), request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CLIENT_ALREADY_EXISTS);
        }
        Client client = new Client();
        client.setCpf(request.getCpf());
        client.setEmail(request.getEmail());
        client.setName(request.getName());
        clientRepository.save(client);
        return new ResponseEntity<>(
                new ClientResponse(CLIENT_CREATED_SUCCESSFULLY), HttpStatus.CREATED);
    }
}
