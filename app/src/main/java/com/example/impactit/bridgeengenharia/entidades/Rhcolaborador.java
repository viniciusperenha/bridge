package com.example.impactit.bridgeengenharia.entidades;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by root on 20/03/15.
 */
public class Rhcolaborador implements Serializable {
    private Long id;
    private Long cpf;
    private String rg;
    private String nome;
    private Character status;
    private Character tipoMaoDeObra;
    private Long fkIdEmpresaEmpreiteira;
    private BigInteger fkIdFoto;
    private String cep;
    private String endereco;
    private String numero;
    private String complemento;
    private String telefone;
    private String contatoTelefone;
    private String email;
    private Long fkIdCargo;
    private Integer fkIdCidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Character getTipoMaoDeObra() {
        return tipoMaoDeObra;
    }

    public void setTipoMaoDeObra(Character tipoMaoDeObra) {
        this.tipoMaoDeObra = tipoMaoDeObra;
    }

    public Long getFkIdEmpresaEmpreiteira() {
        return fkIdEmpresaEmpreiteira;
    }

    public void setFkIdEmpresaEmpreiteira(Long fkIdEmpresaEmpreiteira) {
        this.fkIdEmpresaEmpreiteira = fkIdEmpresaEmpreiteira;
    }

    public BigInteger getFkIdFoto() {
        return fkIdFoto;
    }

    public void setFkIdFoto(BigInteger fkIdFoto) {
        this.fkIdFoto = fkIdFoto;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getContatoTelefone() {
        return contatoTelefone;
    }

    public void setContatoTelefone(String contatoTelefone) {
        this.contatoTelefone = contatoTelefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getFkIdCargo() {
        return fkIdCargo;
    }

    public void setFkIdCargo(Long fkIdCargo) {
        this.fkIdCargo = fkIdCargo;
    }

    public Integer getFkIdCidade() {
        return fkIdCidade;
    }

    public void setFkIdCidade(Integer fkIdCidade) {
        this.fkIdCidade = fkIdCidade;
    }

    @Override
    public String toString() {
        return nome;
    }
}
