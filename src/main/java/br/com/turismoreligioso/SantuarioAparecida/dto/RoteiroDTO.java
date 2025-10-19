package br.com.turismoreligioso.SantuarioAparecida.dto;

import java.util.List;

public class RoteiroDTO {
    private Long idRoteiro;
    private String titulo;
    private String descricao;
    private List<EtapaDTO> etapas;

    public Long getIdRoteiro() {
        return idRoteiro;
    }

    public void setIdRoteiro(Long idRoteiro) {
        this.idRoteiro = idRoteiro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<EtapaDTO> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<EtapaDTO> etapas) {
        this.etapas = etapas;
    }
}