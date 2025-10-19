package br.com.turismoreligioso.SantuarioAparecida.model;

import jakarta.persistence.*;

@Entity
@Table(name = "guias")

public class Guia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuia;

    @Column(length = 100)
    private String instagram;

    @OneToOne
    @JoinColumn(name = "pessoa_id", referencedColumnName = "idPessoa")
    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public Long getIdGuia() {
        return idGuia;
    }

    public void setIdGuia(Long idGuia) {
        this.idGuia = idGuia;
    }
}
