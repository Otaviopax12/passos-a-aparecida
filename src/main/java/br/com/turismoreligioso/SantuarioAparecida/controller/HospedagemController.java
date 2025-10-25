package br.com.turismoreligioso.SantuarioAparecida.controller;

import br.com.turismoreligioso.SantuarioAparecida.dto.HospedagemDTO;
import br.com.turismoreligioso.SantuarioAparecida.model.Hospedagem;
import br.com.turismoreligioso.SantuarioAparecida.model.ImagemHospedagem;
import br.com.turismoreligioso.SantuarioAparecida.model.TipoHospedagem;
import br.com.turismoreligioso.SantuarioAparecida.service.HospedagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hospedagens")
public class HospedagemController {

    @Autowired
    private HospedagemService hospedagemService;

    @GetMapping
    public String listarHospedagens(Model model) {
        List<Hospedagem> hospedagens = hospedagemService.findAll();
        model.addAttribute("hospedagens", hospedagens);
        return "hospedagens-lista";
    }

    @GetMapping("/{id}")
    public String detalheHospedagem(@PathVariable("id") Long id, Model model) {
        Optional<Hospedagem> hospedagemOpt = hospedagemService.findById(id);
        if (hospedagemOpt.isPresent()) {
            model.addAttribute("hospedagem", hospedagemOpt.get());
            return "hospedagem-detalhes";
        }
        return "redirect:/hospedagens";
    }

    @GetMapping("/minha-hospedaria")
    public String mostrarMinhaHospedaria(Model model, Authentication authentication) {
        String emailGerente = authentication.getName();
        List<Hospedagem> minhasHospedagens = hospedagemService.findListByGerenteEmail(emailGerente);
        model.addAttribute("hospedagens", minhasHospedagens);
        return "minha-hospedaria";
    }

    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {

        model.addAttribute("hospedagem", new HospedagemDTO());
        model.addAttribute("tiposHospedagem", TipoHospedagem.values());
        return "hospedagem-form";
    }

    @PostMapping("/cadastrar")
    public String salvarNovaHospedagem(@ModelAttribute HospedagemDTO hospedagemDTO, Authentication authentication) {
        String emailGerente = authentication.getName();

        hospedagemService.salvarHospedagem(hospedagemDTO, emailGerente);
        return "redirect:/hospedagens/minha-hospedaria";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicao(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Optional<Hospedagem> hospedagemOpt = hospedagemService.findById(id);

        if (hospedagemOpt.isPresent()) {
            Hospedagem hospedagem = hospedagemOpt.get();

            HospedagemDTO dto = new HospedagemDTO();
            dto.setIdHospedagem(hospedagem.getIdHospedagem());
            dto.setNome(hospedagem.getNome());
            dto.setDescricao(hospedagem.getDescricao());
            dto.setEndereco(hospedagem.getEndereco());
            dto.setTelefone(hospedagem.getTelefone());
            dto.setEmailContato(hospedagem.getEmailContato());
            dto.setInstagram(hospedagem.getInstagram());
            dto.setUrlImagem(hospedagem.getUrlImagem());
            dto.setTipo(hospedagem.getTipo());

            List<String> urls = hospedagem.getGaleria().stream()
                    .map(ImagemHospedagem::getUrl)
                    .collect(Collectors.toList());
            dto.setGaleriaUrls(urls);

            model.addAttribute("hospedagem", dto);
            model.addAttribute("tiposHospedagem", TipoHospedagem.values());
            return "hospedagem-form";
        }
        return "redirect:/hospedagens/minha-hospedaria";
    }

    @PostMapping("/{id}/editar")
    public String processarEdicao(@PathVariable("id") Long id, @ModelAttribute HospedagemDTO hospedagemDTO, Authentication authentication) {
        String emailGerente = authentication.getName();
        hospedagemService.atualizarHospedagem(id, hospedagemDTO, emailGerente);
        return "redirect:/hospedagens/minha-hospedaria";
    }

    @PostMapping("/{id}/excluir")
    public String excluirHospedaria(@PathVariable("id") Long id, Authentication authentication) {
        String emailGerente = authentication.getName();
        try {
            hospedagemService.deletarHospedagem(id, emailGerente);
            return "redirect:/hospedagens/minha-hospedaria?excluido=true";
        } catch (Exception e) {
            return "redirect:/hospedagens/minha-hospedaria?erroExcluir=true";
        }
    }
}

