package br.com.jhonatask.cadastrounico.core.validator;

import br.com.jhonatask.cadastrounico.dto.TelefoneDTO;

import java.util.List;
import java.util.regex.Pattern;

public class TelefoneValidator {

    private TelefoneValidator() {
        throw new IllegalStateException("Utility class");
    }
    public static void validarTelefone(List<TelefoneDTO> telefones) {
        for (TelefoneDTO telefoneDTO : telefones) {
            validarTelefoneNulo(telefoneDTO);
            validarCaracteresDiferentes(telefoneDTO);
        }
    }
    private static void validarTelefoneNulo(TelefoneDTO telefoneDTO) {
        if (telefoneDTO.getNumero() == null || telefoneDTO.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone não pode ser nulo ou vazio.");
        }
    }

    private static void validarCaracteresDiferentes(TelefoneDTO telefoneDTO) {
        String numero = telefoneDTO.getNumero().replaceAll("\\D", "");
        if (Pattern.matches("^(.)\\1*$", numero)) {
            throw new IllegalArgumentException("Os caracteres do telefone não podem ser todos iguais.");
        }
    }
}
