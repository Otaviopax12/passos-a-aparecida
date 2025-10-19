package br.com.turismoreligioso.SantuarioAparecida.controller;

import br.com.turismoreligioso.SantuarioAparecida.dto.EtapaDTO;
import br.com.turismoreligioso.SantuarioAparecida.dto.RoteiroDTO;
import br.com.turismoreligioso.SantuarioAparecida.service.RoteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import br.com.turismoreligioso.SantuarioAparecida.model.Local;
import br.com.turismoreligioso.SantuarioAparecida.model.Roteiro;
import br.com.turismoreligioso.SantuarioAparecida.repository.LocalRepository;
import org.springframework.ui.Model;

import java.util.List;
import org.springframework.security.core.Authentication;
import br.com.turismoreligioso.SantuarioAparecida.model.Roteiro;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/roteiros")
public class RoteiroController {

    @Autowired
    private RoteiroService roteiroService;
    @Autowired
    private LocalRepository localRepository;

    @GetMapping("/novo")
    public String mostrarFormularioDeCriacao(Model model) {
        List<Local> todosOsLocais = localRepository.findAll();
        model.addAttribute("roteiro", new RoteiroDTO());
        model.addAttribute("todosOsLocais", todosOsLocais);
        return "roteiro-form";
    }

    @PostMapping("/novo")
    public String criarRoteiro(RoteiroDTO roteiroDTO, Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        roteiroService.criarRoteiro(roteiroDTO, emailUsuarioLogado);
        return "redirect:/roteiros/meus-roteiros?sucesso";
    }
    @GetMapping("/meus-roteiros")
    public String mostrarMeusRoteiros(Model model, Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        List<Roteiro> roteiros = roteiroService.findRoteirosByGuia(emailUsuarioLogado);
        model.addAttribute("roteiros", roteiros);
        return "meus-roteiros";
    }
    @PostMapping("/{id}/excluir")
    public String excluirRoteiro(@PathVariable("id") Long roteiroId, Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        try {
            roteiroService.deletarRoteiro(roteiroId, emailUsuarioLogado);
            return "redirect:/roteiros/meus-roteiros?excluido";
        } catch (Exception e) {
            return "redirect:/roteiros/meus-roteiros?erroExcluir";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioDeEdicao(@PathVariable("id") Long roteiroId, Model model, Authentication authentication) {
        try {
            String emailUsuarioLogado = authentication.getName();
            Roteiro roteiro = roteiroService.findRoteiroById(roteiroId, emailUsuarioLogado);

            RoteiroDTO roteiroDTO = new RoteiroDTO();

            roteiroDTO.setIdRoteiro(roteiro.getIdRoteiro());
            roteiroDTO.setTitulo(roteiro.getTitulo());
            roteiroDTO.setDescricao(roteiro.getDescricao());

            List<EtapaDTO> etapasDTO = roteiro.getEtapas().stream().map(etapa -> {
                EtapaDTO dto = new EtapaDTO();
                dto.setOrdem(etapa.getOrdem());
                dto.setDescricao(etapa.getDescricao());
                dto.setLinkMaps(etapa.getLinkMaps());
                dto.setLocalId(etapa.getLocal().getIdLocal());
                dto.setDataHora(etapa.getDataHora());

                return dto;
            }).collect(Collectors.toList());

            roteiroDTO.setEtapas(etapasDTO);
            model.addAttribute("roteiro", roteiroDTO);
            model.addAttribute("todosOsLocais", localRepository.findAll());

            return "roteiro-form";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/roteiros/meus-roteiros?erro";
        }
    }
    @PostMapping("/{id}/editar")
    public String processarEdicao(@PathVariable("id") Long roteiroId, @ModelAttribute RoteiroDTO roteiroDTO, Authentication authentication) {
        try {
            String emailUsuarioLogado = authentication.getName();
            roteiroService.atualizarRoteiro(roteiroId, roteiroDTO, emailUsuarioLogado);

            return "redirect:/roteiros/" + roteiroId + "?sucesso";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/roteiros/" + roteiroId + "/editar?erro";
        }
    }
    @GetMapping("/disponiveis")
    public String mostrarRoteirosDisponiveis(Model model) {
        List<Roteiro> todosOsRoteiros = roteiroService.findAllRoteirosDisponiveis();
        model.addAttribute("roteiros", todosOsRoteiros);
        return "roteiros-disponiveis";
    }
    @GetMapping("/{id}")
    public String mostrarDetalheRoteiro(@PathVariable("id") Long roteiroId, Model model) {
        try {
            Roteiro roteiro = roteiroService.findRoteiroByIdPublico(roteiroId);
            model.addAttribute("roteiro", roteiro);
            return "roteiro-detalhe";
        } catch (Exception e) {
            e.printStackTrace();

            return "redirect:/roteiros/disponiveis?erro";
        }
    }
}