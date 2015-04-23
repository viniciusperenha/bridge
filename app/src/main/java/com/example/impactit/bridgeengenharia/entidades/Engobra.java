package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Engobra implements Serializable {
    private Long id;
    private String codigo;
    private String nome;
    private Long fkIdCoordenadorObra;
    private Long fkIdEngenheiroResidente;
    private Long fkIdGerenteEngenharia;
    private Long fkIdProjeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getFkIdCoordenadorObra() {
        return fkIdCoordenadorObra;
    }

    public void setFkIdCoordenadorObra(Long fkIdCoordenadorObra) {
        this.fkIdCoordenadorObra = fkIdCoordenadorObra;
    }

    public Long getFkIdEngenheiroResidente() {
        return fkIdEngenheiroResidente;
    }

    public void setFkIdEngenheiroResidente(Long fkIdEngenheiroResidente) {
        this.fkIdEngenheiroResidente = fkIdEngenheiroResidente;
    }

    public Long getFkIdGerenteEngenharia() {
        return fkIdGerenteEngenharia;
    }

    public void setFkIdGerenteEngenharia(Long fkIdGerenteEngenharia) {
        this.fkIdGerenteEngenharia = fkIdGerenteEngenharia;
    }

    public Long getFkIdProjeto() {
        return fkIdProjeto;
    }

    public void setFkIdProjeto(Long fkIdProjeto) {
        this.fkIdProjeto = fkIdProjeto;
    }

    @Override
    public String toString() {
        return  nome;
    }
}
