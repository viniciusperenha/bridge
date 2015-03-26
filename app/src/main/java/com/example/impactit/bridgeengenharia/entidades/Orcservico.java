package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 20/03/15.
 */
public class Orcservico implements Serializable {
    private Long id;
    private String codigo;
    private String nome;
    private int fkIdUnidadeMedida;
    private Double razaoUnitariaProducao;
    private Double horasTrabalhadasDia;
    private Long fkIdGrupoServico;
    private Long fkIdSubprojeto;
    private Long fkIdTarefa;

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

    public int getFkIdUnidadeMedida() {
        return fkIdUnidadeMedida;
    }

    public void setFkIdUnidadeMedida(int fkIdUnidadeMedida) {
        this.fkIdUnidadeMedida = fkIdUnidadeMedida;
    }

    public Double getRazaoUnitariaProducao() {
        return razaoUnitariaProducao;
    }

    public void setRazaoUnitariaProducao(Double razaoUnitariaProducao) {
        this.razaoUnitariaProducao = razaoUnitariaProducao;
    }

    public Double getHorasTrabalhadasDia() {
        return horasTrabalhadasDia;
    }

    public void setHorasTrabalhadasDia(Double horasTrabalhadasDia) {
        this.horasTrabalhadasDia = horasTrabalhadasDia;
    }

    public Long getFkIdGrupoServico() {
        return fkIdGrupoServico;
    }

    public void setFkIdGrupoServico(Long fkIdGrupoServico) {
        this.fkIdGrupoServico = fkIdGrupoServico;
    }

    public Long getFkIdSubprojeto() {
        return fkIdSubprojeto;
    }

    public void setFkIdSubprojeto(Long fkIdSubprojeto) {
        this.fkIdSubprojeto = fkIdSubprojeto;
    }

    public Long getFkIdTarefa() {
        return fkIdTarefa;
    }

    public void setFkIdTarefa(Long fkIdTarefa) {
        this.fkIdTarefa = fkIdTarefa;
    }
}
