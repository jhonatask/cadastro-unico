package br.com.jhonatask.cadastrounico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "O nome não pode ser nulo ou vazio")
    private String nome;

    @NotBlank(message = "O CPF não pode ser nulo ou vazio")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF inválido")
    private String cpf;

    private String endereco;

    private String bairro;

    private List<TelefoneDTO> telefones;
}
