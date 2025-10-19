package br.com.turismoreligioso.SantuarioAparecida.model;

import jakarta.persistence.*;

@Entity
    @Table(name = "gerentes_hospedaria")
    public class GerenteHospedaria {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idGerenteHospedaria;

        @OneToOne
        @JoinColumn(name = "pessoa_id", referencedColumnName = "idPessoa")
        private Pessoa pessoa;

    public Long getIdGerenteHospedaria() {
        return idGerenteHospedaria;
    }

    public void setIdGerenteHospedaria(Long idGerenteHospedaria) {
        this.idGerenteHospedaria = idGerenteHospedaria;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
