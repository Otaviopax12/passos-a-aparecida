package br.com.turismoreligioso.SantuarioAparecida.controller;

import br.com.turismoreligioso.SantuarioAparecida.model.Hospedagem;
import br.com.turismoreligioso.SantuarioAparecida.service.HospedagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Import novo
import org.springframework.web.bind.annotation.PostMapping; // Import novo
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Import novo

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HospedagemService hospedagemService;

    @GetMapping("/hospedagens/aprovar")
    public String mostrarPaginaAprovacao(Model model) {
        List<Hospedagem> pendentes = hospedagemService.buscarPendentes();
        model.addAttribute("hospedagensPendentes", pendentes);
        return "admin-aprovacoes";
    }

    // --- NOVO MÉTODO PARA APROVAR ---
    @PostMapping("/hospedagens/{id}/aprovar")
    public String aprovarHospedagem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            hospedagemService.aprovarHospedagem(id);
            redirectAttributes.addFlashAttribute("sucesso", "Hospedaria aprovada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao aprovar hospedaria.");
        }
        return "redirect:/admin/hospedagens/aprovar"; // Volta para a página de aprovações
    }

    // --- NOVO MÉTODO PARA REPROVAR ---
    @PostMapping("/hospedagens/{id}/reprovar")
    public String reprovarHospedagem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            hospedagemService.reprovarHospedagem(id);
            redirectAttributes.addFlashAttribute("sucesso", "Hospedaria reprovada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao reprovar hospedaria.");
        }
        return "redirect:/admin/hospedagens/aprovar"; // Volta para a página de aprovações
    }
}

