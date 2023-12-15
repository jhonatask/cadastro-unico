package br.com.jhonatask.cadastrounico.entitys;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode ser nulo ou vazio")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O CPF não pode ser nulo ou vazio")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF inválido")
    @Column(nullable = false, unique = true)
    private String cpf;

    private String endereco;

    private String bairro;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id")
    private List<Telefone> telefones;

}
