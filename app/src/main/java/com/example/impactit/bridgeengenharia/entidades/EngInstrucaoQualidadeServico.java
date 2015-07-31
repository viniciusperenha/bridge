package com.example.impactit.bridgeengenharia.entidades;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by root on 29/07/15.
 */
public class EngInstrucaoQualidadeServico {

    private Long id;

    private String codigo;

    private String metodoExecutivo;

    private Date dataCriacao;

    private Date dataAlteracao;

    private int versao;

    private BigInteger fkIdArquivoInstrucao;

    private Long fkIdAtividade;

    private Long fkIdSetor;

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

    public String getMetodoExecutivo() {
        return metodoExecutivo;
    }

    public void setMetodoExecutivo(String metodoExecutivo) {
        this.metodoExecutivo = metodoExecutivo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public BigInteger getFkIdArquivoInstrucao() {
        return fkIdArquivoInstrucao;
    }

    public void setFkIdArquivoInstrucao(BigInteger fkIdArquivoInstrucao) {
        this.fkIdArquivoInstrucao = fkIdArquivoInstrucao;
    }

    public Long getFkIdAtividade() {
        return fkIdAtividade;
    }

    public void setFkIdAtividade(Long fkIdAtividade) {
        this.fkIdAtividade = fkIdAtividade;
    }

    public Long getFkIdSetor() {
        return fkIdSetor;
    }

    public void setFkIdSetor(Long fkIdSetor) {
        this.fkIdSetor = fkIdSetor;
    }
}
