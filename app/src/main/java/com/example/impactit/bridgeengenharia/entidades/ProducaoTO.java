package com.example.impactit.bridgeengenharia.entidades;

/**
 * Created by root on 28/08/15.
 */
public class ProducaoTO {
    private Orcservico orcservico;
    private Orcunidademedida orcunidademedida;
    private Engproducao engproducao;
    private Orcelementoproducao orcelementoproducao;

    public Orcservico getOrcservico() {
        return orcservico;
    }

    public void setOrcservico(Orcservico orcservico) {
        this.orcservico = orcservico;
    }

    public Orcunidademedida getOrcunidademedida() {
        return orcunidademedida;
    }

    public void setOrcunidademedida(Orcunidademedida orcunidademedida) {
        this.orcunidademedida = orcunidademedida;
    }

    public Engproducao getEngproducao() {
        return engproducao;
    }

    public void setEngproducao(Engproducao engproducao) {
        this.engproducao = engproducao;
    }

    public Orcelementoproducao getOrcelementoproducao() {
        return orcelementoproducao;
    }

    public void setOrcelementoproducao(Orcelementoproducao orcelementoproducao) {
        this.orcelementoproducao = orcelementoproducao;
    }
}
