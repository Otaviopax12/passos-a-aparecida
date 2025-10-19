package br.com.turismoreligioso.SantuarioAparecida.repository;

import br.com.turismoreligioso.SantuarioAparecida.model.GerenteHospedaria;
import br.com.turismoreligioso.SantuarioAparecida.model.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
    List<Hospedagem> findByGerente(GerenteHospedaria gerente);
}