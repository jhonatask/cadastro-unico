package br.com.jhonatask.cadastrounico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelefoneDTO {

    private Long id;

    @NotBlank(message = "O telefone não pode ser nulo ou vazio")
    @Pattern(regexp = "^(?!([0-9])\\1+$)\\d{10,}$", message = "Telefone inválido")
    private String numero;

    public TelefoneDTO(String number) {
    }
}
