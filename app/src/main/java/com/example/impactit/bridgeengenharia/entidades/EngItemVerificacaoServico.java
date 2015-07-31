package com.example.impactit.bridgeengenharia.entidades;

/**
 * Created by root on 29/07/15.
 */
public class EngItemVerificacaoServico {
    private Long id;
    private String item;
    private String verificacao;
    private String criterioAceitacao;
    private Long fkIdInstrucaoQualidadeServico;
    private Long fkIdTarefa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getVerificacao() {
        return verificacao;
    }

    public void setVerificacao(String verificacao) {
        this.verificacao = verificacao;
    }

    public String getCriterioAceitacao() {
        return criterioAceitacao;
    }

    public void setCriterioAceitacao(String criterioAceitacao) {
        this.criterioAceitacao = criterioAceitacao;
    }

    public Long getFkIdInstrucaoQualidadeServico() {
        return fkIdInstrucaoQualidadeServico;
    }

    public void setFkIdInstrucaoQualidadeServico(Long fkIdInstrucaoQualidadeServico) {
        this.fkIdInstrucaoQualidadeServico = fkIdInstrucaoQualidadeServico;
    }

    public Long getFkIdTarefa() {
        return fkIdTarefa;
    }

    public void setFkIdTarefa(Long fkIdTarefa) {
        this.fkIdTarefa = fkIdTarefa;
    }
}
