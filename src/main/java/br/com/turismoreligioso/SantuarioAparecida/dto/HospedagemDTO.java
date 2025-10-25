package br.com.turismoreligioso.SantuarioAparecida.dto;

import br.com.turismoreligioso.SantuarioAparecida.model.TipoHospedagem;
import java.util.ArrayList;
import java.util.List;

public class HospedagemDTO {

    private Long idHospedagem;
    private String nome;
    private String descricao;
    private String endereco;
    private String telefone;
    private String emailContato;
    private String instagram;
    private String urlImagem; // Imagem de Capa
    private TipoHospedagem tipo;


    private List<String> galeriaUrls = new ArrayList<>();



    public Long getIdHospedagem() {
        return idHospedagem;
    }

    public void setIdHospedagem(Long idHospedagem) {
        this.idHospedagem = idHospedagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public TipoHospedagem getTipo() {
        return tipo;
    }

    public void setTipo(TipoHospedagem tipo) {
        this.tipo = tipo;
    }

    public List<String> getGaleriaUrls() {
        return galeriaUrls;
    }

    public void setGaleriaUrls(List<String> galeriaUrls) {
        this.galeriaUrls = galeriaUrls;
    }
}
