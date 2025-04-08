package com.template.demo.service;

import static com.template.demo.constants.SystemMessages.*;

import com.template.demo.dto.request.ClientRequest;
import com.template.demo.dto.response.ClientResponse;
import com.template.demo.model.Client;
import com.template.demo.repository.ClientRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        client.setActive(true);
        clientRepository.save(client);
        return new ResponseEntity<>(
                new ClientResponse(request.getCpf(), request.getName(), request.getEmail()),
                HttpStatus.CREATED);
    }

    public ResponseEntity<ClientResponse> updateClient(Long id, ClientRequest request) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND);
        }
        client.get().setName(request.getName());
        client.get().setEmail(request.getEmail());
        client.get().setCpf(request.getCpf());
        clientRepository.save(client.get());
        return ResponseEntity.ok(
                new ClientResponse(request.getCpf(), request.getName(), request.getEmail()));
    }

    public ResponseEntity<ClientResponse> getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND);
        }
        return ResponseEntity.ok(
                new ClientResponse(client.get().getCpf(), client.get().getName(), client.get().getEmail()));
    }

    public ResponseEntity<List<ClientResponse>> getClients() {
        return ResponseEntity.ok(
                clientRepository.findAll().stream()
                        .map(client -> new ClientResponse(client.getCpf(), client.getName(), client.getEmail()))
                        .collect(Collectors.toList()));
    }

    public ResponseEntity<ClientResponse> deleteClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENT_NOT_FOUND);
        }
        client.get().setActive(false);
        // Deletar um objeto no banco de dados usando JPA seria deste modo, porém estamos realizando deleção lógica,
        // ou seja, apenas sinalizar que o usuario foi desativado para uso posterior.
        // clientRepository.delete(client.get());
        clientRepository.save(client.get());
        return ResponseEntity.ok(
                new ClientResponse(client.get().getCpf(), client.get().getName(), client.get().getEmail()));
    }
}
