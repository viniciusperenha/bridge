package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 08/05/15.
 */
public class Plasubprojetosetorprojeto implements Serializable {

    private Long id;

    private int qtdePavimentos;

    private Long fkIdSetorProjeto;

    private Long fkIdSubprojeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQtdePavimentos() {
        return qtdePavimentos;
    }

    public void setQtdePavimentos(int qtdePavimentos) {
        this.qtdePavimentos = qtdePavimentos;
    }

    public Long getFkIdSetorProjeto() {
        return fkIdSetorProjeto;
    }

    public void setFkIdSetorProjeto(Long fkIdSetorProjeto) {
        this.fkIdSetorProjeto = fkIdSetorProjeto;
    }

    public Long getFkIdSubprojeto() {
        return fkIdSubprojeto;
    }

    public void setFkIdSubprojeto(Long fkIdSubprojeto) {
        this.fkIdSubprojeto = fkIdSubprojeto;
    }
}
