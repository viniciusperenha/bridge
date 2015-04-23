package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Plapavimentoprojeto implements Serializable {
    private Long id;
    private String nome;
    private int codigo;
    private Character areaComum;
    private Character apartamento;
    private Integer numeroApartamento;
    private Double area;
    private Integer pavimento;
    private int quantidade;
    private Character padrao;
    private Long fkIdProjeto;
    private Long fkIdTipoPavimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Character getAreaComum() {
        return areaComum;
    }

    public void setAreaComum(Character areaComum) {
        this.areaComum = areaComum;
    }

    public Character getApartamento() {
        return apartamento;
    }

    public void setApartamento(Character apartamento) {
        this.apartamento = apartamento;
    }

    public Integer getNumeroApartamento() {
        return numeroApartamento;
    }

    public void setNumeroApartamento(Integer numeroApartamento) {
        this.numeroApartamento = numeroApartamento;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getPavimento() {
        return pavimento;
    }

    public void setPavimento(Integer pavimento) {
        this.pavimento = pavimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Character getPadrao() {
        return padrao;
    }

    public void setPadrao(Character padrao) {
        this.padrao = padrao;
    }

    public Long getFkIdProjeto() {
        return fkIdProjeto;
    }

    public void setFkIdProjeto(Long fkIdProjeto) {
        this.fkIdProjeto = fkIdProjeto;
    }

    public Long getFkIdTipoPavimento() {
        return fkIdTipoPavimento;
    }

    public void setFkIdTipoPavimento(Long fkIdTipoPavimento) {
        this.fkIdTipoPavimento = fkIdTipoPavimento;
    }

    @Override
    public String toString() {
        return nome;
    }
}
