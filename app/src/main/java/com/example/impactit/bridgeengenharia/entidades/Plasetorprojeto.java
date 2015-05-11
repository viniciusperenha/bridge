package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 08/05/15.
 */
public class Plasetorprojeto implements Serializable{

    private Long id;
    private String codigo;
    private String nome;
    private Long fkIdProjeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getFkIdProjeto() {
        return fkIdProjeto;
    }

    public void setFkIdProjeto(Long fkIdProjeto) {
        this.fkIdProjeto = fkIdProjeto;
    }
}
