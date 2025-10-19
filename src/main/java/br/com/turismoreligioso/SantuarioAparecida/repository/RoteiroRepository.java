package br.com.turismoreligioso.SantuarioAparecida.repository;

import br.com.turismoreligioso.SantuarioAparecida.model.Guia;
import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import br.com.turismoreligioso.SantuarioAparecida.model.Roteiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoteiroRepository extends JpaRepository<Roteiro, Long> {
    List<Roteiro> findAllByGuia(Guia guia);
    @Query("SELECT r FROM Roteiro r LEFT JOIN FETCH r.etapas WHERE r.idRoteiro = :id")
    Optional<Roteiro> findByIdWithEtapas(@Param("id") Long id);

}