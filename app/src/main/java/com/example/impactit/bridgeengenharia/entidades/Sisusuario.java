/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author munif
 */

public class Sisusuario implements Serializable {

    private Long id;
    private String nome;
    private String login;

    private String senha;
    private Character status;
    private BigInteger fkIdColaborador;

    private Character utilizaAcessoRapido;


    private Date dataHoraCadastro;

    private Date dataHoraInativacao;

    private Date dataHoraAlteracaoSenha;

    private Long fkIdFuncao;

    public Sisusuario() {
    }

    public Sisusuario(Long id) {
        this.id = id;
    }

    public Sisusuario(Long id, String senha, Character utilizaAcessoRapido, Date dataHoraCadastro) {
        this.id = id;
        this.senha = senha;
        this.utilizaAcessoRapido = utilizaAcessoRapido;
        this.dataHoraCadastro = dataHoraCadastro;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public BigInteger getFkIdColaborador() {
        return fkIdColaborador;
    }

    public void setFkIdColaborador(BigInteger fkIdColaborador) {
        this.fkIdColaborador = fkIdColaborador;
    }

    public Character getUtilizaAcessoRapido() {
        return utilizaAcessoRapido;
    }

    public void setUtilizaAcessoRapido(Character utilizaAcessoRapido) {
        this.utilizaAcessoRapido = utilizaAcessoRapido;
    }

    public Date getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    public void setDataHoraCadastro(Date dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public Date getDataHoraInativacao() {
        return dataHoraInativacao;
    }

    public void setDataHoraInativacao(Date dataHoraInativacao) {
        this.dataHoraInativacao = dataHoraInativacao;
    }

    public Date getDataHoraAlteracaoSenha() {
        return dataHoraAlteracaoSenha;
    }

    public void setDataHoraAlteracaoSenha(Date dataHoraAlteracaoSenha) {
        this.dataHoraAlteracaoSenha = dataHoraAlteracaoSenha;
    }

    public Long getFkIdFuncao() {
        return fkIdFuncao;
    }

    public void setFkIdFuncao(Long fkIdFuncao) {
        this.fkIdFuncao = fkIdFuncao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sisusuario)) {
            return false;
        }
        Sisusuario other = (Sisusuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "teste.entidades.Sisusuario[ id=" + id + " ]";
    }
    
}
