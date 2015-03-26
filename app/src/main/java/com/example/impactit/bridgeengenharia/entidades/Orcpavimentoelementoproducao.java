package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Orcpavimentoelementoproducao implements Serializable {
    private Long id;
    private Long fkIdElementoProducao;
    private Long fkIdPavimentoProjeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkIdElementoProducao() {
        return fkIdElementoProducao;
    }

    public void setFkIdElementoProducao(Long fkIdElementoProducao) {
        this.fkIdElementoProducao = fkIdElementoProducao;
    }

    public Long getFkIdPavimentoProjeto() {
        return fkIdPavimentoProjeto;
    }

    public void setFkIdPavimentoProjeto(Long fkIdPavimentoProjeto) {
        this.fkIdPavimentoProjeto = fkIdPavimentoProjeto;
    }
}
