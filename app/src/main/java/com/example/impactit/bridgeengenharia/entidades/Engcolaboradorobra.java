package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Engcolaboradorobra implements Serializable {
    private Long id;

    private Long fkIdCargo;

    private Long fkIdColaborador;

    private Long fkIdObra;

    private Long fkIdSetorProjeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkIdCargo() {
        return fkIdCargo;
    }

    public void setFkIdCargo(Long fkIdCargo) {
        this.fkIdCargo = fkIdCargo;
    }

    public Long getFkIdColaborador() {
        return fkIdColaborador;
    }

    public void setFkIdColaborador(Long fkIdColaborador) {
        this.fkIdColaborador = fkIdColaborador;
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
}
