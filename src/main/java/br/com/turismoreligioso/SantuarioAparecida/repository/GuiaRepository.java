package br.com.turismoreligioso.SantuarioAparecida.repository;

import br.com.turismoreligioso.SantuarioAparecida.model.Guia;
import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuiaRepository extends JpaRepository<Guia, Long> {
    Optional<Guia> findByPessoa(Pessoa pessoa);
}