package br.com.turismoreligioso.SantuarioAparecida.controller;

import br.com.turismoreligioso.SantuarioAparecida.model.Local;
import br.com.turismoreligioso.SantuarioAparecida.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DestinoController {

    @Autowired
    private LocalRepository localRepository;


    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }

        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        return nfdNormalizedString.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    @GetMapping("/destinos")
    public String mostrarPaginaDestinos(@RequestParam(value = "termo", required = false) String termo, Model model) {

        List<Local> todosOsLocais = localRepository.findAll();
        List<Local> locaisFiltrados;


        if (termo != null && !termo.trim().isEmpty()) {
            final String termoNormalizado = normalizeString(termo);

            locaisFiltrados = todosOsLocais.stream()
                    .filter(local -> {

                        String nomeLocalNormalizado = normalizeString(local.getNome());
                        return nomeLocalNormalizado.contains(termoNormalizado);
                    })
                    .collect(Collectors.toList());
        } else {

            locaisFiltrados = todosOsLocais;
        }

        model.addAttribute("locais", locaisFiltrados);
        model.addAttribute("termoBusca", termo); // Envia o termo original de volta para a view


        if (!locaisFiltrados.isEmpty()) {
            model.addAttribute("destinoDestaque", locaisFiltrados.get(0));
            if (locaisFiltrados.size() > 1) {
                model.addAttribute("outrosDestinos", locaisFiltrados.subList(1, locaisFiltrados.size()));
            } else {
                model.addAttribute("outrosDestinos", Collections.emptyList());
            }
        } else {
            model.addAttribute("destinoDestaque", null);
            model.addAttribute("outrosDestinos", Collections.emptyList());
        }

        return "destinos";
    }
}

