package br.com.turismoreligioso.SantuarioAparecida.service;

import br.com.turismoreligioso.SantuarioAparecida.dto.HospedagemDTO;
import br.com.turismoreligioso.SantuarioAparecida.model.*; // Import genérico para StatusHospedagem
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

    // --- MÉTODOS PÚBLICOS ---

    public List<Hospedagem> findAll() {
        return hospedagemRepository.findAllByStatus(StatusHospedagem.APROVADO);
    }

    public Optional<Hospedagem> findById(Long id) {
        return hospedagemRepository.findByIdWithGaleria(id);
    }

    // --- MÉTODOS PARA O GERENTE ---

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
    public Hospedagem salvarHospedagem(HospedagemDTO hospedagemDTO, String emailUsuarioLogado) {
        Pessoa pessoa = pessoaRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        GerenteHospedaria gerente = gerenteHospedariaRepository.findByPessoa(pessoa)
                .orElseThrow(() -> new IllegalStateException("O usuário logado não possui um perfil de gerente."));

        Hospedagem hospedagem = new Hospedagem(); // Status PENDENTE por padrão
        // Mapeia os dados do DTO para a entidade
        hospedagem.setNome(hospedagemDTO.getNome());
        hospedagem.setDescricao(hospedagemDTO.getDescricao());
        hospedagem.setEndereco(hospedagemDTO.getEndereco());
        hospedagem.setTelefone(hospedagemDTO.getTelefone());
        hospedagem.setEmailContato(hospedagemDTO.getEmailContato());
        hospedagem.setInstagram(hospedagemDTO.getInstagram());
        hospedagem.setUrlImagem(hospedagemDTO.getUrlImagem());
        hospedagem.setTipo(hospedagemDTO.getTipo());
        hospedagem.setGerente(gerente);

        // Mapeia as URLs da galeria
        hospedagem.getGaleria().clear();
        if (hospedagemDTO.getGaleriaUrls() != null) {
            for (String url : hospedagemDTO.getGaleriaUrls()) {
                if (url != null && !url.trim().isEmpty()) {
                    ImagemHospedagem imagem = new ImagemHospedagem();
                    imagem.setUrl(url);
                    imagem.setHospedagem(hospedagem);
                    hospedagem.getGaleria().add(imagem);
                }
            }
        }

        return hospedagemRepository.save(hospedagem);
    }

    @Transactional
    public Hospedagem atualizarHospedagem(Long id, HospedagemDTO hospedagemDTO, String emailGerente) {
        Hospedagem hospedagemExistente = hospedagemRepository.findByIdWithGaleria(id)
                .orElseThrow(() -> new RuntimeException("Hospedaria não encontrada"));

        if (!hospedagemExistente.getGerente().getPessoa().getEmail().equals(emailGerente)) {
            throw new SecurityException("Acesso negado.");
        }

        // Atualiza os dados
        hospedagemExistente.setNome(hospedagemDTO.getNome());
        hospedagemExistente.setDescricao(hospedagemDTO.getDescricao());
        hospedagemExistente.setEndereco(hospedagemDTO.getEndereco());
        hospedagemExistente.setTelefone(hospedagemDTO.getTelefone());
        hospedagemExistente.setEmailContato(hospedagemDTO.getEmailContato());
        hospedagemExistente.setInstagram(hospedagemDTO.getInstagram());
        hospedagemExistente.setUrlImagem(hospedagemDTO.getUrlImagem());
        hospedagemExistente.setTipo(hospedagemDTO.getTipo());

        // Limpa a galeria antiga e adiciona a nova
        hospedagemExistente.getGaleria().clear();
        if (hospedagemDTO.getGaleriaUrls() != null) {
            for (String url : hospedagemDTO.getGaleriaUrls()) {
                if (url != null && !url.trim().isEmpty()) {
                    ImagemHospedagem imagem = new ImagemHospedagem();
                    imagem.setUrl(url);
                    imagem.setHospedagem(hospedagemExistente);
                    hospedagemExistente.getGaleria().add(imagem);
                }
            }
        }

        // IMPORTANTE: Ao editar, o status volta para PENDENTE para re-aprovação
        hospedagemExistente.setStatus(StatusHospedagem.PENDENTE);

        return hospedagemRepository.save(hospedagemExistente);
    }

    @Transactional
    public void deletarHospedagem(Long hospedagemId, String emailGerente) {
        Hospedagem hospedagem = hospedagemRepository.findById(hospedagemId)
                .orElseThrow(() -> new RuntimeException("Hospedaria não encontrada."));

        if (!hospedagem.getGerente().getPessoa().getEmail().equals(emailGerente)) {
            throw new SecurityException("Você não tem permissão para excluir esta hospedaria.");
        }

        hospedagemRepository.delete(hospedagem);
    }

    // --- MÉTODOS PARA ADMINISTRAÇÃO ---

    /**
     * Busca todas as hospedarias que estão pendentes de aprovação.
     * @return Lista de hospedarias com status PENDENTE.
     */
    @Transactional(readOnly = true)
    public List<Hospedagem> buscarPendentes() {
        return hospedagemRepository.findAllByStatus(StatusHospedagem.PENDENTE);
    }

    /**
     * Aprova uma hospedaria, mudando seu status para APROVADO.
     * @param id O ID da hospedaria a ser aprovada.
     */
    @Transactional
    public void aprovarHospedagem(Long id) {
        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaria não encontrada."));
        hospedagem.setStatus(StatusHospedagem.APROVADO);
        hospedagemRepository.save(hospedagem);
    }

    /**
     * Reprova uma hospedaria, mudando seu status para REPROVADO.
     * @param id O ID da hospedaria a ser reprovada.
     */
    @Transactional
    public void reprovarHospedagem(Long id) {
        Hospedagem hospedagem = hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaria não encontrada."));
        hospedagem.setStatus(StatusHospedagem.REPROVADO);
        hospedagemRepository.save(hospedagem);
    }
}

