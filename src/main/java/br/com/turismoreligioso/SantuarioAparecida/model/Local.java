package br.com.turismoreligioso.SantuarioAparecida.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locais")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocal;

    @Column(nullable = false, length = 100)
    private String nome;

    @Lob // Para textos que podem ser longos
    private String descricao;

    @Column(length = 200)
    private String endereco;

    @Column(length = 255)
    private String linkMaps;

    // --- NOVO CAMPO ADICIONADO ---
    @Column(name = "url_imagem", length = 500) // Coluna para a URL da imagem
    private String urlImagem;
    // --- FIM DO NOVO CAMPO ---

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public String getLinkMaps() {
        return linkMaps;
    }

    public void setLinkMaps(String linkMaps) {
        this.linkMaps = linkMaps;
    }


    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

}