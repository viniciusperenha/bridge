package com.example.impactit.bridgeengenharia.entidades;

/**
 * Created by root on 01/09/15.
 */
public class OcorrenciaTO {
    private Engobra engobra;
    private Plasetorprojeto plasetorprojeto;
    private Plasubprojeto plasubprojeto;
    private EngOcorrenciaNaoPlanejada engOcorrenciaNaoPlanejada;

    public Engobra getEngobra() {
        return engobra;
    }

    public void setEngobra(Engobra engobra) {
        this.engobra = engobra;
    }

    public Plasetorprojeto getPlasetorprojeto() {
        return plasetorprojeto;
    }

    public void setPlasetorprojeto(Plasetorprojeto plasetorprojeto) {
        this.plasetorprojeto = plasetorprojeto;
    }

    public Plasubprojeto getPlasubprojeto() {
        return plasubprojeto;
    }

    public void setPlasubprojeto(Plasubprojeto plasubprojeto) {
        this.plasubprojeto = plasubprojeto;
    }

    public EngOcorrenciaNaoPlanejada getEngOcorrenciaNaoPlanejada() {
        return engOcorrenciaNaoPlanejada;
    }

    public void setEngOcorrenciaNaoPlanejada(EngOcorrenciaNaoPlanejada engOcorrenciaNaoPlanejada) {
        this.engOcorrenciaNaoPlanejada = engOcorrenciaNaoPlanejada;
    }
}
