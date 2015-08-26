package com.example.impactit.bridgeengenharia.entidades;

/**
 * Created by root on 25/08/15.
 */
public class ElementoTO {
    private Orcelementoproducao orcelementoproducao;
    private Orcservico orcservico;
    private Engproducao engproducao;
    private Orcunidademedida orcunidademedida;
    private Double quantidade;
    private String status;

    public Orcelementoproducao getOrcelementoproducao() {
        return orcelementoproducao;
    }

    public void setOrcelementoproducao(Orcelementoproducao orcelementoproducao) {
        this.orcelementoproducao = orcelementoproducao;
    }

    public Orcservico getOrcservico() {
        return orcservico;
    }

    public void setOrcservico(Orcservico orcservico) {
        this.orcservico = orcservico;
    }

    public Engproducao getEngproducao() {
        return engproducao;
    }

    public void setEngproducao(Engproducao engproducao) {
        this.engproducao = engproducao;
    }

    public Orcunidademedida getOrcunidademedida() {
        return orcunidademedida;
    }

    public void setOrcunidademedida(Orcunidademedida orcunidademedida) {
        this.orcunidademedida = orcunidademedida;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
