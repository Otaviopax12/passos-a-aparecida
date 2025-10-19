package br.com.turismoreligioso.SantuarioAparecida.service;

import br.com.turismoreligioso.SantuarioAparecida.model.GerenteHospedaria;
import br.com.turismoreligioso.SantuarioAparecida.model.Hospedagem;
import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import br.com.turismoreligioso.SantuarioAparecida.repository.GerenteHospedariaRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.HospedagemRepository;
import br.com.turismoreligioso.SantuarioAparecida.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HospedagemService {

    @Autowired
    private HospedagemRepository hospedagemRepository;

    @Autowired
    private GerenteHospedariaRepository gerenteHospedariaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Hospedagem> findAll() {
        return hospedagemRepository.findAll();
    }

    public Optional<Hospedagem> findById(Long id) {
        return hospedagemRepository.findById(id);
    }

    /**
     * Busca a LISTA de hospedarias associadas a um gerente pelo e-mail.
     */
    public List<Hospedagem> findListByGerenteEmail(String email) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(email);
        if (pessoaOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<GerenteHospedaria> gerenteOpt = gerenteHospedariaRepository.findByPessoa(pessoaOpt.get());
        if (gerenteOpt.isEmpty()) {
            return Collections.emptyList();
        }

        return hospedagemRepository.findByGerente(gerenteOpt.get());
    }

    @Transactional
    public Hospedagem salvarHospedagem(Hospedagem hospedagem, String emailUsuarioLogado) {
        Pessoa pessoa = pessoaRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        GerenteHospedaria gerente = gerenteHospedariaRepository.findByPessoa(pessoa)
                .orElseThrow(() -> new IllegalStateException("O usuário logado não possui um perfil de gerente."));

        hospedagem.setGerente(gerente);
        return hospedagemRepository.save(hospedagem);
    }
    @Transactional
    public Hospedagem atualizarHospedagem(Long id, Hospedagem dadosAtualizados, String emailGerente) {
        Hospedagem hospedagemExistente = hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaria não encontrada."));

        // Verificação de segurança
        if (!hospedagemExistente.getGerente().getPessoa().getEmail().equals(emailGerente)) {
            throw new SecurityException("Você não tem permissão para editar esta hospedaria.");
        }

        // Atualiza os campos
        hospedagemExistente.setNome(dadosAtualizados.getNome());
        hospedagemExistente.setTipo(dadosAtualizados.getTipo());
        hospedagemExistente.setDescricao(dadosAtualizados.getDescricao());
        hospedagemExistente.setEndereco(dadosAtualizados.getEndereco());
        hospedagemExistente.setTelefone(dadosAtualizados.getTelefone());
        hospedagemExistente.setEmailContato(dadosAtualizados.getEmailContato());
        hospedagemExistente.setInstagram(dadosAtualizados.getInstagram());
        hospedagemExistente.setUrlImagem(dadosAtualizados.getUrlImagem());

        return hospedagemRepository.save(hospedagemExistente);
    }
    @Transactional
    public void deletarHospedagem(Long hospedagemId, String emailGerente) {
        // Busca a hospedaria pelo ID
        Hospedagem hospedagem = hospedagemRepository.findById(hospedagemId)
                .orElseThrow(() -> new RuntimeException("Hospedaria não encontrada."));

        // Verificação de segurança: garante que o usuário logado é o dono da hospedaria
        if (!hospedagem.getGerente().getPessoa().getEmail().equals(emailGerente)) {
            throw new SecurityException("Você não tem permissão para excluir esta hospedaria.");
        }

        hospedagemRepository.delete(hospedagem);
    }
}

