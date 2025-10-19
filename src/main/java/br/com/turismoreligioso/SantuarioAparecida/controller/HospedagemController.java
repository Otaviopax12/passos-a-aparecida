package br.com.turismoreligioso.SantuarioAparecida.controller;

import br.com.turismoreligioso.SantuarioAparecida.model.Hospedagem;
import br.com.turismoreligioso.SantuarioAparecida.model.TipoHospedagem;
import br.com.turismoreligioso.SantuarioAparecida.service.HospedagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        model.addAttribute("hospedagem", new Hospedagem());
        model.addAttribute("tiposHospedagem", TipoHospedagem.values());
        return "hospedagem-form";
    }

    @PostMapping("/cadastrar")
    public String salvarNovaHospedagem(@ModelAttribute Hospedagem hospedagem, Authentication authentication) {
        String emailGerente = authentication.getName();
        hospedagemService.salvarHospedagem(hospedagem, emailGerente);
        return "redirect:/hospedagens/minha-hospedaria?sucesso=true";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicao(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Optional<Hospedagem> hospedagemOpt = hospedagemService.findById(id);
        // Validação de segurança
        if (hospedagemOpt.isPresent() && hospedagemOpt.get().getGerente().getPessoa().getEmail().equals(authentication.getName())) {
            model.addAttribute("hospedagem", hospedagemOpt.get());
            model.addAttribute("tiposHospedagem", TipoHospedagem.values());
            return "hospedagem-form";
        }
        return "redirect:/hospedagens/minha-hospedaria?erro=true";
    }

    @PostMapping("/{id}/editar")
    public String salvarEdicaoHospedagem(@PathVariable("id") Long id, @ModelAttribute Hospedagem hospedagem, Authentication authentication) {
        String emailGerente = authentication.getName();
        try {
            hospedagemService.atualizarHospedagem(id, hospedagem, emailGerente);
            return "redirect:/hospedagens/minha-hospedaria?sucessoEdit=true";
        } catch (Exception e) {
            return "redirect:/hospedagens/minha-hospedaria?erroEdit=true";
        }
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

