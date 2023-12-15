package br.com.jhonatask.cadastrounico.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O telefone não pode ser nulo ou vazio")
    @Pattern(regexp = "^(?!([0-9])\\1+$)\\d{10,}$", message = "Telefone inválido")
    @Column(nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
