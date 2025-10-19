package br.com.turismoreligioso.SantuarioAparecida.controller;

import br.com.turismoreligioso.SantuarioAparecida.model.Pessoa;
import br.com.turismoreligioso.SantuarioAparecida.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pessoas") // Define o prefixo da URL para todos os métodos deste controller
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/cadastrar")
    public String mostrarFormularioDeCadastro(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        return "cadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Pessoa pessoa, @RequestParam("confirmarSenha") String confirmarSenha) {
        try {
            pessoaService.cadastrarNovaPessoa(pessoa, confirmarSenha);
            return "redirect:/login?sucesso";
        } catch (IllegalStateException e) {
            return "redirect:/pessoas/cadastrar?erro=" + e.getMessage();
        }
    }
}