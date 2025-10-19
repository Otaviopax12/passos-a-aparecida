package br.com.turismoreligioso.SantuarioAparecida.model;

import jakarta.persistence.*;
import java.util.ArrayList; // Import novo
import java.util.List;      // Import novo

@Entity
@Table(name = "hospedagens")
public class Hospedagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHospedagem;

    @Column(nullable = false, length = 150)
    private String nome;

    @Lob
    private String descricao;

    @Column(length = 255)
    private String endereco;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String emailContato;

    @Column(length = 100)
    private String instagram;

    @Column(name = "url_imagem", length = 500)
    private String urlImagem; // Esta será a foto de capa

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private TipoHospedagem tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerente_id", nullable = false)
    private GerenteHospedaria gerente;


    @OneToMany(mappedBy = "hospedagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemHospedagem> galeria = new ArrayList<>();





    public List<ImagemHospedagem> getGaleria() {
        return galeria;
    }

    public void setGaleria(List<ImagemHospedagem> galeria) {
        this.galeria = galeria;
    }


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

    public GerenteHospedaria getGerente() {
        return gerente;
    }

    public void setGerente(GerenteHospedaria gerente) {
        this.gerente = gerente;
    }
}

