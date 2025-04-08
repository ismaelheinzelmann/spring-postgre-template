package com.template.demo.controller;

import com.template.demo.dto.request.ClientRequest;
import com.template.demo.dto.response.ClientResponse;
import com.template.demo.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/client"))
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/")
    public ResponseEntity<ClientResponse> client(@Valid @RequestBody ClientRequest request) {
        return clientService.createClient(request);
    }
}
