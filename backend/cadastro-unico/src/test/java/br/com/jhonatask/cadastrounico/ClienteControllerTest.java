package br.com.jhonatask.cadastrounico;


import br.com.jhonatask.cadastrounico.controllers.ClienteController;
import br.com.jhonatask.cadastrounico.dto.ClienteDTO;
import br.com.jhonatask.cadastrounico.dto.TelefoneDTO;
import br.com.jhonatask.cadastrounico.repositorys.ClienteRepository;
import br.com.jhonatask.cadastrounico.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClienteRepository clienteRepository;

    @Test
    void testCadastrarClienteComNomeVazio() throws Exception {

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)));


        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("O nome não pode ser nulo ou vazio."));

    }

    @Test
    void testCadastrarClienteComCPFIvalido() throws Exception {

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("John Doe");
        clienteDTO.setCpf("12345678901");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("CPF inválido."));
    }

    @Test
    void testCadastrarClienteComTelefoneNulo() throws Exception {

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("John Doe");
        clienteDTO.setCpf("12345678901");
        clienteDTO.setTelefones(null);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("O telefone não pode ser nulo ou vazio."));
    }

    @Test
    void testCadastrarClienteComTelefoneExistente() throws Exception {

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNome("John Doe");
        clienteDTO.setCpf("12345678901");
        clienteDTO.setTelefones(Collections.singletonList(new TelefoneDTO("999999999"))); // Número de telefone já existente

        when(clienteService.createCliente(any(ClienteDTO.class))).thenReturn(clienteDTO);
        when(clienteRepository.existsClienteByTelefonesNumero("999999999")).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Telefone já cadastrado para outro cliente."));
    }
}
