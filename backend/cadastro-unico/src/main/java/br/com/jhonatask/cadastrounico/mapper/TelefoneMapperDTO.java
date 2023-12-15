package br.com.jhonatask.cadastrounico.mapper;

import br.com.jhonatask.cadastrounico.dto.TelefoneDTO;
import br.com.jhonatask.cadastrounico.entitys.Telefone;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TelefoneMapperDTO {
    TelefoneDTO telefoneToTelefoneDTO(Telefone entity);
    Telefone telefoneDTOtoTelefone(TelefoneDTO entity);
}
