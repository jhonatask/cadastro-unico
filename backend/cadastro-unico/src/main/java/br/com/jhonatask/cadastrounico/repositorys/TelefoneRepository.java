package br.com.jhonatask.cadastrounico.repositorys;

import br.com.jhonatask.cadastrounico.entitys.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
