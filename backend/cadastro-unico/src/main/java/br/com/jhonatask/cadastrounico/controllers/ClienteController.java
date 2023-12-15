package br.com.jhonatask.cadastrounico.controllers;

import br.com.jhonatask.cadastrounico.dto.ClienteDTO;
import br.com.jhonatask.cadastrounico.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> cadastrarCliente(@Valid @RequestBody ClienteDTO cliente) {
        ClienteDTO newCliente = clienteService.createClienteJdbc(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCliente);
    }
}
