package br.com.turismoreligioso.SantuarioAparecida.repository;

import br.com.turismoreligioso.SantuarioAparecida.model.GerenteHospedaria;
import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GerenteHospedariaRepository extends JpaRepository<GerenteHospedaria, Long> {
    Optional<GerenteHospedaria> findByPessoa(Pessoa pessoa);
}