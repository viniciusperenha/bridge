package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Plaprojeto implements Serializable {
    private Long id;
    private String codigo;
    private String nome;
    private String observacao;
    private Character status;
    private Long fkIdEmpresa;
    private Long fkIdRegional;

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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Long getFkIdEmpresa() {
        return fkIdEmpresa;
    }

    public void setFkIdEmpresa(Long fkIdEmpresa) {
        this.fkIdEmpresa = fkIdEmpresa;
    }

    public Long getFkIdRegional() {
        return fkIdRegional;
    }

    public void setFkIdRegional(Long fkIdRegional) {
        this.fkIdRegional = fkIdRegional;
    }
}
