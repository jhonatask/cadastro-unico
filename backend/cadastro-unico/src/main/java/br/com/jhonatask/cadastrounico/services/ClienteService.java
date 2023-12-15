package br.com.jhonatask.cadastrounico.services;

import br.com.jhonatask.cadastrounico.core.validator.CpfValidator;
import br.com.jhonatask.cadastrounico.dto.ClienteDTO;
import br.com.jhonatask.cadastrounico.dto.TelefoneDTO;
import br.com.jhonatask.cadastrounico.entitys.Cliente;
import br.com.jhonatask.cadastrounico.mapper.ClienteMapperDTO;
import br.com.jhonatask.cadastrounico.repositorys.ClienteRepository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteMapperDTO clienteMapperDTO;
    private final ClienteRepository clienteRepository;
    private final DataSource dataSource;
    private final TelefoneService telefoneService;

    public ClienteService(ClienteMapperDTO clienteMapperDTO, ClienteRepository clienteRepository, DataSource dataSource, TelefoneService telefoneService) {
        this.clienteMapperDTO = clienteMapperDTO;
        this.clienteRepository = clienteRepository;
        this.dataSource = dataSource;
        this.telefoneService = telefoneService;
    }


    public ClienteDTO createCliente(ClienteDTO cliente) {
        validarCpf(cliente.getCpf());
        validarTelefoneUnico(cliente.getTelefones());
        telefoneService.validarTelefone(cliente.getTelefones());
        Cliente newCliente = clienteMapperDTO.clienteDTOtoCliente(cliente);
        newCliente.setNome(cliente.getNome());
        newCliente.setCpf(cliente.getCpf());
        newCliente.setBairro(cliente.getBairro());
        newCliente.setEndereco(cliente.getEndereco());
        clienteRepository.save(newCliente);
        newCliente.setTelefones(telefoneService.inserirTelefone(cliente.getTelefones(), newCliente.getId()));
        return clienteMapperDTO.clienteToClienteDTO(newCliente);
    }

    public ClienteDTO createClienteJdbc(ClienteDTO cliente) {
        validarCpf(cliente.getCpf());
        validarTelefoneUnico(cliente.getTelefones());
        telefoneService.validarTelefone(cliente.getTelefones());
        Long clienteId = inserirCliente(cliente);
        telefoneService.inserirTelefone(cliente.getTelefones(), clienteId);
        return  obterClientePorId(clienteId);

    }

    private Long inserirCliente(ClienteDTO cliente) {
        String sql = "INSERT INTO cliente (nome, cpf, endereco, bairro) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"})) {

            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getCpf());
            preparedStatement.setString(3, cliente.getEndereco());
            preparedStatement.setString(4, cliente.getBairro());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir o cliente, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Falha ao recuperar o ID gerado para o cliente.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir o cliente.", e);
        }
    }

    private ClienteDTO obterClientePorId(Long clienteId) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, clienteId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return ClienteDTO.builder()
                            .id(resultSet.getLong("id"))
                            .nome(resultSet.getString("nome"))
                            .bairro(resultSet.getString("bairro"))
                            .cpf(resultSet.getString("cpf"))
                            .endereco(resultSet.getString("endereco"))
                            .build();
                } else {
                    throw new SQLException("Cliente não encontrado com ID: " + clienteId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao recuperar o cliente por ID.", e);
        }
    }

    private void validarCpf(String cpf) {
        if (clienteRepository.existsByCpf(cpf)){
            throw new RuntimeException("CPF já cadastrado");
        }
        if(!CpfValidator.validarCPF(cpf)){
            throw new RuntimeException("CPF invalido");
        }
    }

    private void validarTelefoneUnico(List<TelefoneDTO> telefones) {
        for (TelefoneDTO telefoneDTO : telefones) {
            if (clienteRepository.existsClienteByTelefonesNumero(telefoneDTO.getNumero())) {
                throw new IllegalArgumentException("Telefone já cadastrado para outro cliente.");
            }
        }
    }
}
