package com.example.impactit.bridgeengenharia.entidades;

/**
 * Created by root on 11/08/15.
 */
public class QualidadeTO {
    private EngItemVerificacaoServico engItemVerificacaoServico;
    private boolean ap;
    private boolean rp;
    private boolean acr;
    private boolean asr;
    private EngVerificacaoQualidadeServico engVerificacaoQualidadeServico;

    public EngItemVerificacaoServico getEngItemVerificacaoServico() {
        return engItemVerificacaoServico;
    }

    public void setEngItemVerificacaoServico(EngItemVerificacaoServico engItemVerificacaoServico) {
        this.engItemVerificacaoServico = engItemVerificacaoServico;
    }

    public EngVerificacaoQualidadeServico getEngVerificacaoQualidadeServico() {
        return engVerificacaoQualidadeServico;
    }

    public void setEngVerificacaoQualidadeServico(EngVerificacaoQualidadeServico engVerificacaoQualidadeServico) {
        this.engVerificacaoQualidadeServico = engVerificacaoQualidadeServico;
    }

    public boolean isAp() {
        return ap;
    }

    public void setAp(boolean ap) {
        this.ap = ap;
    }

    public boolean isRp() {
        return rp;
    }

    public void setRp(boolean rp) {
        this.rp = rp;
    }

    public boolean isAcr() {
        return acr;
    }

    public void setAcr(boolean acr) {
        this.acr = acr;
    }

    public boolean isAsr() {
        return asr;
    }

    public void setAsr(boolean asr) {
        this.asr = asr;
    }
}
