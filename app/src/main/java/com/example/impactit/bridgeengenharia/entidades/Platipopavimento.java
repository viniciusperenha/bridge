package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Platipopavimento implements Serializable {

    private Long id;

    private int codigo;

    private String nome;

    private Long fkIdSubprojeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getFkIdSubprojeto() {
        return fkIdSubprojeto;
    }

    public void setFkIdSubprojeto(Long fkIdSubprojeto) {
        this.fkIdSubprojeto = fkIdSubprojeto;
    }

    @Override
    public String toString() {
        return nome;
    }
}
