package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Engcontratoservicoempreiteira implements Serializable {
    private Long id;
    private double quantidade;
    private Long fkIdAtividade;
    private Long fkIdContratoEmpreiteira;
    private Long fkIdServico;
    private Long fkIdSubprojeto;
    private Long fkIdTarefa;
    private Long fkIdTipoPavimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getFkIdContratoEmpreiteira() {
        return fkIdContratoEmpreiteira;
    }

    public void setFkIdContratoEmpreiteira(Long fkIdContratoEmpreiteira) {
        this.fkIdContratoEmpreiteira = fkIdContratoEmpreiteira;
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

    public Long getFkIdTipoPavimento() {
        return fkIdTipoPavimento;
    }

    public void setFkIdTipoPavimento(Long fkIdTipoPavimento) {
        this.fkIdTipoPavimento = fkIdTipoPavimento;
    }
}
