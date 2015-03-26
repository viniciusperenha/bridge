package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Orcelementoproducao implements Serializable {
    private Long id;
    private String codigo;
    private double quantidade;
    private Long fkIdAtividade;
    private Long fkIdLocal;
    private Long fkIdProjeto;
    private Long fkIdServico;
    private Long fkIdSubprojeto;
    private Long fkIdTarefa;

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

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Long getFkIdAtividade() {
        return fkIdAtividade;
    }

    public void setFkIdAtividade(Long fkIdAtividade) {
        this.fkIdAtividade = fkIdAtividade;
    }

    public Long getFkIdLocal() {
        return fkIdLocal;
    }

    public void setFkIdLocal(Long fkIdLocal) {
        this.fkIdLocal = fkIdLocal;
    }

    public Long getFkIdProjeto() {
        return fkIdProjeto;
    }

    public void setFkIdProjeto(Long fkIdProjeto) {
        this.fkIdProjeto = fkIdProjeto;
    }

    public Long getFkIdServico() {
        return fkIdServico;
    }

    public void setFkIdServico(Long fkIdServico) {
        this.fkIdServico = fkIdServico;
    }

    public Long getFkIdSubprojeto() {
        return fkIdSubprojeto;
    }

    public void setFkIdSubprojeto(Long fkIdSubprojeto) {
        this.fkIdSubprojeto = fkIdSubprojeto;
    }

    public Long getFkIdTarefa() {
        return fkIdTarefa;
    }

    public void setFkIdTarefa(Long fkIdTarefa) {
        this.fkIdTarefa = fkIdTarefa;
    }

}
