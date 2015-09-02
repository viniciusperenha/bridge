package com.example.impactit.bridgeengenharia.entidades;

import java.util.Date;

/**
 * Created by vinicius on 14/08/15.
 */
public class EngOcorrenciaNaoPlanejada {
    private Long id;

    private Long fkIdObra;

    private Long fkIdSetorProjeto;

    private Long fkIdSubprojetoSetorProjeto;

    private Date data;

    private String descricao;

    private Long fkIdResponsavel;

    private Boolean transmitir;

    public Boolean getTransmitir() {
        return transmitir;
    }

    public void setTransmitir(Boolean transmitir) {
        this.transmitir = transmitir;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkIdObra() {
        return fkIdObra;
    }

    public void setFkIdObra(Long fkIdObra) {
        this.fkIdObra = fkIdObra;
    }

    public Long getFkIdSetorProjeto() {
        return fkIdSetorProjeto;
    }

    public void setFkIdSetorProjeto(Long fkIdSetorProjeto) {
        this.fkIdSetorProjeto = fkIdSetorProjeto;
    }

    public Long getFkIdSubprojetoSetorProjeto() {
        return fkIdSubprojetoSetorProjeto;
    }

    public void setFkIdSubprojetoSetorProjeto(Long fkIdSubprojetoSetorProjeto) {
        this.fkIdSubprojetoSetorProjeto = fkIdSubprojetoSetorProjeto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getFkIdResponsavel() {
        return fkIdResponsavel;
    }

    public void setFkIdResponsavel(Long fkIdResponsavel) {
        this.fkIdResponsavel = fkIdResponsavel;
    }
}
