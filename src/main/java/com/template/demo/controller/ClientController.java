package com.template.demo.controller;

import com.template.demo.dto.request.ClientRequest;
import com.template.demo.dto.response.ClientResponse;
import com.template.demo.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/client"))
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/")
    public ResponseEntity<ClientResponse> client(@Valid @RequestBody ClientRequest request) {
        return clientService.createClient(request);
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientResponse>> findAll() {
        return clientService.getClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return clientService.updateClient(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientResponse> delete(@PathVariable Long id) {
        return clientService.deleteClientById(id);
    }
}
