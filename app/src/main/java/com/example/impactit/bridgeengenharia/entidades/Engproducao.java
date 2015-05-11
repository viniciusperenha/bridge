package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by root on 08/05/15.
 */
public class Engproducao implements Serializable {
    private Long id;

    private long fkIdSubprojetoSetorProjeto;

    private Timestamp data;

    private Timestamp dataRegistro;

    private double quantidade;

    private Long fkIdAtividade;

    private Long fkIdColaborador;

    private Long fkIdElementoProducao;

    private Long fkIdEmpreiteira;

    private Long fkIdObra;

    private Long fkIdPavimentoSubprojeto;

    private Long fkIdServico;

    private Long fkIdSetorProjeto;

    private Long fkIdTarefa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFkIdSubprojetoSetorProjeto() {
        return fkIdSubprojetoSetorProjeto;
    }

    public void setFkIdSubprojetoSetorProjeto(long fkIdSubprojetoSetorProjeto) {
        this.fkIdSubprojetoSetorProjeto = fkIdSubprojetoSetorProjeto;
    }

    public Timestamp getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Timestamp dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
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

    public Long getFkIdColaborador() {
        return fkIdColaborador;
    }

    public void setFkIdColaborador(Long fkIdColaborador) {
        this.fkIdColaborador = fkIdColaborador;
    }

    public Long getFkIdElementoProducao() {
        return fkIdElementoProducao;
    }

    public void setFkIdElementoProducao(Long fkIdElementoProducao) {
        this.fkIdElementoProducao = fkIdElementoProducao;
    }

    public Long getFkIdEmpreiteira() {
        return fkIdEmpreiteira;
    }

    public void setFkIdEmpreiteira(Long fkIdEmpreiteira) {
        this.fkIdEmpreiteira = fkIdEmpreiteira;
    }

    public Long getFkIdObra() {
        return fkIdObra;
    }

    public void setFkIdObra(Long fkIdObra) {
        this.fkIdObra = fkIdObra;
    }

    public Long getFkIdPavimentoSubprojeto() {
        return fkIdPavimentoSubprojeto;
    }

    public void setFkIdPavimentoSubprojeto(Long fkIdPavimentoSubprojeto) {
        this.fkIdPavimentoSubprojeto = fkIdPavimentoSubprojeto;
    }

    public Long getFkIdServico() {
        return fkIdServico;
    }

    public void setFkIdServico(Long fkIdServico) {
        this.fkIdServico = fkIdServico;
    }

    public Long getFkIdSetorProjeto() {
        return fkIdSetorProjeto;
    }

    public void setFkIdSetorProjeto(Long fkIdSetorProjeto) {
        this.fkIdSetorProjeto = fkIdSetorProjeto;
    }

    public Long getFkIdTarefa() {
        return fkIdTarefa;
    }

    public void setFkIdTarefa(Long fkIdTarefa) {
        this.fkIdTarefa = fkIdTarefa;
    }
}
