package br.com.turismoreligioso.SantuarioAparecida.repository;

import br.com.turismoreligioso.SantuarioAparecida.model.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaRepository extends JpaRepository<Etapa, Long> {
}