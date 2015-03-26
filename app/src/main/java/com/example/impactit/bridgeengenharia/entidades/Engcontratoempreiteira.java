package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Engcontratoempreiteira implements Serializable {
    private Long id;
    private long codigo;
    private String descricao;
    private Long fkIdObra;
    private Long fkIdEmpreiteira;
    private Long fkIdResponsavel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getFkIdObra() {
        return fkIdObra;
    }

    public void setFkIdObra(Long fkIdObra) {
        this.fkIdObra = fkIdObra;
    }

    public Long getFkIdEmpreiteira() {
        return fkIdEmpreiteira;
    }

    public void setFkIdEmpreiteira(Long fkIdEmpreiteira) {
        this.fkIdEmpreiteira = fkIdEmpreiteira;
    }

    public Long getFkIdResponsavel() {
        return fkIdResponsavel;
    }

    public void setFkIdResponsavel(Long fkIdResponsavel) {
        this.fkIdResponsavel = fkIdResponsavel;
    }
}
