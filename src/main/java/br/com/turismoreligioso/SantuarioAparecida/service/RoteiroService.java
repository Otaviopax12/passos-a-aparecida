package br.com.turismoreligioso.SantuarioAparecida.service;

import br.com.turismoreligioso.SantuarioAparecida.dto.EtapaDTO;
import br.com.turismoreligioso.SantuarioAparecida.dto.RoteiroDTO;
import br.com.turismoreligioso.SantuarioAparecida.model.*;
import br.com.turismoreligioso.SantuarioAparecida.repository.GuiaRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.LocalRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.PessoaRepository; // Importe este
import br.com.turismoreligioso.SantuarioAparecida.repository.RoteiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoteiroService {

    @Autowired
    private RoteiroRepository roteiroRepository;

    @Autowired
    private GuiaRepository guiaRepository;

    @Autowired
    private LocalRepository localRepository;


    @Autowired
    private PessoaRepository pessoaRepository;




    @Transactional
    public Roteiro criarRoteiro(RoteiroDTO roteiroDTO, String emailUsuarioLogado) {

        Pessoa pessoaLogada = pessoaRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado."));
        Guia guia = guiaRepository.findByPessoa(pessoaLogada)
                .orElseThrow(() -> new IllegalStateException("O usuário logado não possui um perfil de guia."));


        Roteiro novoRoteiro = new Roteiro();
        novoRoteiro.setTitulo(roteiroDTO.getTitulo());
        novoRoteiro.setDescricao(roteiroDTO.getDescricao());
        novoRoteiro.setGuia(guia);


        if (roteiroDTO.getEtapas() != null) {
            for (EtapaDTO etapaDTO : roteiroDTO.getEtapas()) {

                Local local = localRepository.findById(etapaDTO.getLocalId())
                        .orElseThrow(() -> new RuntimeException("Local não encontrado com ID: " + etapaDTO.getLocalId()));


                Etapa novaEtapa = new Etapa();
                novaEtapa.setOrdem(etapaDTO.getOrdem());
                novaEtapa.setDescricao(etapaDTO.getDescricao());
                novaEtapa.setDataHora(etapaDTO.getDataHora());
                novaEtapa.setLinkMaps(etapaDTO.getLinkMaps());
                novaEtapa.setLocal(local);
                novaEtapa.setRoteiro(novoRoteiro);

                novoRoteiro.getEtapas().add(novaEtapa);
            }
        }

        return roteiroRepository.save(novoRoteiro);
    }
    public List<Roteiro> findRoteirosByGuia(String emailUsuarioLogado) {
        Pessoa pessoaLogada = pessoaRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado."));
        Guia guia = guiaRepository.findByPessoa(pessoaLogada)
                .orElseThrow(() -> new IllegalStateException("O usuário logado não possui um perfil de guia."));

        return roteiroRepository.findAllByGuia(guia);
    }
    @Transactional
    public void deletarRoteiro(Long roteiroId, String emailUsuarioLogado) {
        Roteiro roteiro = roteiroRepository.findById(roteiroId)
                .orElseThrow(() -> new RuntimeException("Roteiro não encontrado!"));

        Pessoa pessoaLogada = pessoaRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado."));
        Guia guia = guiaRepository.findByPessoa(pessoaLogada)
                .orElseThrow(() -> new IllegalStateException("O usuário logado não possui um perfil de guia."));

        if (!roteiro.getGuia().getIdGuia().equals(guia.getIdGuia())) {
            throw new SecurityException("Você não tem permissão para excluir este roteiro.");
        }

        roteiroRepository.delete(roteiro);
    }
    @Transactional
    public Roteiro atualizarRoteiro(Long roteiroId, RoteiroDTO dadosAtualizadosDTO, String emailUsuarioLogado) {
        Roteiro roteiroExistente = findRoteiroById(roteiroId, emailUsuarioLogado);

        roteiroExistente.setTitulo(dadosAtualizadosDTO.getTitulo());
        roteiroExistente.setDescricao(dadosAtualizadosDTO.getDescricao());
        roteiroExistente.getEtapas().clear();


        if (dadosAtualizadosDTO.getEtapas() != null) {
            for (EtapaDTO etapaDTO : dadosAtualizadosDTO.getEtapas()) {

                Etapa novaEtapa = new Etapa();
                novaEtapa.setOrdem(etapaDTO.getOrdem());
                novaEtapa.setDescricao(etapaDTO.getDescricao());
                novaEtapa.setDataHora(etapaDTO.getDataHora());
                novaEtapa.setLinkMaps(etapaDTO.getLinkMaps());

                Local local = localRepository.findById(etapaDTO.getLocalId())
                        .orElseThrow(() -> new RuntimeException("Local não encontrado para a etapa!"));
                novaEtapa.setLocal(local);
                novaEtapa.setRoteiro(roteiroExistente);
                roteiroExistente.getEtapas().add(novaEtapa);
            }
        }

        return roteiroRepository.saveAndFlush(roteiroExistente);
    }
    @Transactional(readOnly = true)
    public Roteiro findRoteiroById(Long roteiroId, String emailUsuarioLogado) {

        Roteiro roteiro = roteiroRepository.findByIdWithEtapas(roteiroId)
                .orElseThrow(() -> new RuntimeException("Roteiro não encontrado!"));
        Pessoa pessoaLogada = pessoaRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado."));
        Guia guia = guiaRepository.findByPessoa(pessoaLogada)
                .orElseThrow(() -> new IllegalStateException("O usuário logado não possui um perfil de guia."));
        if (!roteiro.getGuia().getIdGuia().equals(guia.getIdGuia())) {
            throw new SecurityException("Você não tem permissão para ver este roteiro.");
        }

        return roteiro;
    }
    public List<Roteiro> findAllRoteirosDisponiveis() {
        return roteiroRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Roteiro findRoteiroByIdPublico(Long roteiroId) {
        return roteiroRepository.findByIdWithEtapas(roteiroId).orElseThrow(() -> new RuntimeException("Roteiro não encontrado!"));
    }
}