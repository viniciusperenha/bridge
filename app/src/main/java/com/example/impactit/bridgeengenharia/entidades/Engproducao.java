package com.example.impactit.bridgeengenharia.entidades;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by root on 08/05/15.
 */
public class Engproducao implements Serializable {
    private Long id;

    private Long fkIdSubprojetoSetorProjeto;

    private Date data;

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

    private String status;

    private String observacao;

    private Long fkIdUsuarioApontador;

    private Boolean transmitir;

    public Boolean getTransmitir() {
        return transmitir;
    }

    public void setTransmitir(Boolean transmitir) {
        this.transmitir = transmitir;
    }

    public Long getFkIdUsuarioApontador() {
        return fkIdUsuarioApontador;
    }

    public void setFkIdUsuarioApontador(Long fkIdUsuarioApontador) {
        this.fkIdUsuarioApontador = fkIdUsuarioApontador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkIdSubprojetoSetorProjeto() {
        return fkIdSubprojetoSetorProjeto;
    }

    public void setFkIdSubprojetoSetorProjeto(Long fkIdSubprojetoSetorProjeto) {
        this.fkIdSubprojetoSetorProjeto = fkIdSubprojetoSetorProjeto;
    }

    public Timestamp getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Timestamp dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
