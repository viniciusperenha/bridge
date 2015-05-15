package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;

/**
 * Created by root on 08/05/15.
 */
public class Plapavimentosubprojeto implements Serializable {
    private Long id;

    private int codigo;

    private String nome;

    private short pavimento;

    private short quantidade;

    private Long fkIdSetorProjeto;

    private Long fkIdSubprojetoSetorProjeto;

    private Long fkIdTipoPavimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public short getPavimento() {
        return pavimento;
    }

    public void setPavimento(short pavimento) {
        this.pavimento = pavimento;
    }

    public short getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(short quantidade) {
        this.quantidade = quantidade;
    }

    public Long getFkIdSetorProjeto() {
        return fkIdSetorProjeto;
    }

    public void setFkIdSetorProjeto(Long fkIdSetorProjeto) {
        this.fkIdSetorProjeto = fkIdSetorProjeto;
    }

    public Long getFkIdSubprojetoSetorProjeto() {
        return fkIdSubprojetoSetorProjeto;
    }

    public void setFkIdSubprojetoSetorProjeto(Long fkIdSubprojetoSetorProjeto) {
        this.fkIdSubprojetoSetorProjeto = fkIdSubprojetoSetorProjeto;
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
