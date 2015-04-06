package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 06/04/15.
 */
public class Sisparametro implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String grupo;
    private String nome;
    private String valor;

    public Sisparametro() {
    }

    public Sisparametro(Long id) {
        this.id = id;
    }

    public Sisparametro(Long id, String grupo, String nome, String valor) {
        this.id = id;
        this.grupo = grupo;
        this.nome = nome;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sisparametro)) {
            return false;
        }
        Sisparametro other = (Sisparametro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.entidades.Sisparametro[ id=" + id + " ]";
    }

}