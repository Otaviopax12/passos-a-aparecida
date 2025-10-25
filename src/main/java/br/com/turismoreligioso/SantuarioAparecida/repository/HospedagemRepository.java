package br.com.turismoreligioso.SantuarioAparecida.repository;

import br.com.turismoreligioso.SantuarioAparecida.model.GerenteHospedaria;
import br.com.turismoreligioso.SantuarioAparecida.model.Hospedagem;
import br.com.turismoreligioso.SantuarioAparecida.model.StatusHospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
    List<Hospedagem> findByGerente(GerenteHospedaria gerente);

    @Query("SELECT h FROM Hospedagem h LEFT JOIN FETCH h.galeria WHERE h.idHospedagem = :id")
    Optional<Hospedagem> findByIdWithGaleria(@Param("id") Long id);

    List<Hospedagem> findAllByStatus(StatusHospedagem status);
}