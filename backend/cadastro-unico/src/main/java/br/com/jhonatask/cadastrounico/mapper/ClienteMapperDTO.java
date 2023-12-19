package br.com.jhonatask.cadastrounico.mapper;


import br.com.jhonatask.cadastrounico.dto.ClienteDTO;
import br.com.jhonatask.cadastrounico.dto.TelefoneDTO;
import br.com.jhonatask.cadastrounico.entitys.Cliente;
import br.com.jhonatask.cadastrounico.entitys.Telefone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface ClienteMapperDTO {


    @Mapping(target = "telefones", expression = "java(getTelefones(entity))")

    ClienteDTO clienteToClienteDTO(Cliente entity);
    Cliente clienteDTOtoCliente(ClienteDTO entity);

    default List<TelefoneDTO> getTelefones(Cliente entity){
        List<Telefone> telefones = entity.getTelefones();
        return telefones.stream().map( telefone ->  TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .build()
        ).collect(Collectors.toList());
    }
}
