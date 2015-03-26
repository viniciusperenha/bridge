package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Engcolaboradorobra implements Serializable {
    private Long id;

    private Long fkIdCargo;
    private Long fkIdColaborador;
    private Long fkIdLocal;
    private Long fkIdObra;

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

    public Long getFkIdLocal() {
        return fkIdLocal;
    }

    public void setFkIdLocal(Long fkIdLocal) {
        this.fkIdLocal = fkIdLocal;
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
}
