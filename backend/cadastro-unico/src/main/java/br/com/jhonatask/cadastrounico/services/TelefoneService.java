package br.com.jhonatask.cadastrounico.services;


import br.com.jhonatask.cadastrounico.core.validator.TelefoneValidator;
import br.com.jhonatask.cadastrounico.dto.TelefoneDTO;
import br.com.jhonatask.cadastrounico.entitys.Telefone;
import br.com.jhonatask.cadastrounico.mapper.TelefoneMapperDTO;
import br.com.jhonatask.cadastrounico.repositorys.TelefoneRepository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelefoneService {

    private final TelefoneMapperDTO telefoneMapperDTO;

    private final DataSource dataSource;

    public TelefoneService(TelefoneMapperDTO telefoneMapperDTO, DataSource dataSource) {
        this.telefoneMapperDTO = telefoneMapperDTO;
        this.dataSource = dataSource;
    }

    public List<Telefone> inserirTelefone(List<TelefoneDTO> telefoneDTO, Long clienteId){
        List<Telefone> telefones = telefoneDTO.stream()
                .map(telefoneMapperDTO::telefoneDTOtoTelefone)
                .collect(Collectors.toList());
        if (temNumerosDuplicados(telefones)) {
            throw new IllegalArgumentException("Não é permitido salvar telefones com o mesmo número.");
        }

        inserirTelefonesNoBancoDeDados(telefones, clienteId);
        return telefones;
    }

    private boolean temNumerosDuplicados(List<Telefone> telefones) {
        return telefones.stream()
                .map(Telefone::getNumero)
                .collect(Collectors.toSet())
                .size() != telefones.size();
    }

    public void validarTelefone(List<TelefoneDTO> telefones){
        TelefoneValidator.validarTelefone(telefones);
    }

    private void inserirTelefonesNoBancoDeDados(List<Telefone> telefones, Long clienteId) {
        String sql = "INSERT INTO telefone (numero, cliente_id) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Telefone telefone : telefones) {
                preparedStatement.setString(1, telefone.getNumero());
                preparedStatement.setLong(2, clienteId);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir telefones no banco de dados.", e);
        }
    }
}
