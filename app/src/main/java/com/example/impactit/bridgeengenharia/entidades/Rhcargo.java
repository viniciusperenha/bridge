package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Rhcargo implements Serializable {
    private Long id;
    private String nome;
    private String descricao;
    private Long fkIdAreaEmpresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getFkIdAreaEmpresa() {
        return fkIdAreaEmpresa;
    }

    public void setFkIdAreaEmpresa(Long fkIdAreaEmpresa) {
        this.fkIdAreaEmpresa = fkIdAreaEmpresa;
    }
}
