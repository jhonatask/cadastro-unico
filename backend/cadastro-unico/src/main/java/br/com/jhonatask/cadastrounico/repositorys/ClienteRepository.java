package br.com.jhonatask.cadastrounico.repositorys;

import br.com.jhonatask.cadastrounico.entitys.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);

    boolean existsClienteByTelefonesNumero(String numero);

}
