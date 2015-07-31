package com.example.impactit.bridgeengenharia.entidades;

import java.util.Date;

/**
 * Created by root on 29/07/15.
 */
public class EngVerificacaoQualidadeServico {
    private Long id;

    private Date dataInspecao;

    private Date dataAlteracaoInspecao;

    private String status;

    private String observacao;

    private Long fkIdItemVerificacaoServico;

    private Long fkIdElementoProducao;

    private Long fkIdPavimentoSubprojeto;

    private Long fkIdSubprojetoSetorProjeto;

    private Long fkIdSetorProjeto;

    private Long fkIdAtividade;

    private Long fkIdConferente;

    private Long fkIdObra;

    private Long fkIdServico;

    private Long fkIdTarefa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataInspecao() {
        return dataInspecao;
    }

    public void setDataInspecao(Date dataInspecao) {
        this.dataInspecao = dataInspecao;
    }

    public Date getDataAlteracaoInspecao() {
        return dataAlteracaoInspecao;
    }

    public void setDataAlteracaoInspecao(Date dataAlteracaoInspecao) {
        this.dataAlteracaoInspecao = dataAlteracaoInspecao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getFkIdItemVerificacaoServico() {
        return fkIdItemVerificacaoServico;
    }

    public void setFkIdItemVerificacaoServico(Long fkIdItemVerificacaoServico) {
        this.fkIdItemVerificacaoServico = fkIdItemVerificacaoServico;
    }

    public Long getFkIdElementoProducao() {
        return fkIdElementoProducao;
    }

    public void setFkIdElementoProducao(Long fkIdElementoProducao) {
        this.fkIdElementoProducao = fkIdElementoProducao;
    }

    public Long getFkIdPavimentoSubprojeto() {
        return fkIdPavimentoSubprojeto;
    }

    public void setFkIdPavimentoSubprojeto(Long fkIdPavimentoSubprojeto) {
        this.fkIdPavimentoSubprojeto = fkIdPavimentoSubprojeto;
    }

    public Long getFkIdSubprojetoSetorProjeto() {
        return fkIdSubprojetoSetorProjeto;
    }

    public void setFkIdSubprojetoSetorProjeto(Long fkIdSubprojetoSetorProjeto) {
        this.fkIdSubprojetoSetorProjeto = fkIdSubprojetoSetorProjeto;
    }

    public Long getFkIdSetorProjeto() {
        return fkIdSetorProjeto;
    }

    public void setFkIdSetorProjeto(Long fkIdSetorProjeto) {
        this.fkIdSetorProjeto = fkIdSetorProjeto;
    }

    public Long getFkIdAtividade() {
        return fkIdAtividade;
    }

    public void setFkIdAtividade(Long fkIdAtividade) {
        this.fkIdAtividade = fkIdAtividade;
    }

    public Long getFkIdConferente() {
        return fkIdConferente;
    }

    public void setFkIdConferente(Long fkIdConferente) {
        this.fkIdConferente = fkIdConferente;
    }

    public Long getFkIdObra() {
        return fkIdObra;
    }

    public void setFkIdObra(Long fkIdObra) {
        this.fkIdObra = fkIdObra;
    }

    public Long getFkIdServico() {
        return fkIdServico;
    }

    public void setFkIdServico(Long fkIdServico) {
        this.fkIdServico = fkIdServico;
    }

    public Long getFkIdTarefa() {
        return fkIdTarefa;
    }

    public void setFkIdTarefa(Long fkIdTarefa) {
        this.fkIdTarefa = fkIdTarefa;
    }
}
