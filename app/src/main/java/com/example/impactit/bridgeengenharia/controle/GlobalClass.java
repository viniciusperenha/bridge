package com.example.impactit.bridgeengenharia.controle;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentoprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by root on 01/04/15.
 */
public class GlobalClass extends Application {

    public Sisusuario usuarioLogado;
    public Engobra obraselecionada;
    public Plaprojeto projetoselecionado;
    public Engempreiteira empreiteiraselecionada;
    public Rhcolaborador colaboradorselecionado;
    public Plaatividade atividadeselecionada;
    public Plasubprojeto subprojetoselecionado;
    public Plapavimentoprojeto pavimentoprojetoselecionado;


    public Sisusuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Sisusuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public Engobra getObraselecionada() {
        return obraselecionada;
    }

    public void setObraselecionada(Engobra obraselecionada) {
        this.obraselecionada = obraselecionada;
    }

    public Plaprojeto getProjetoselecionado() {
        return projetoselecionado;
    }

    public void setProjetoselecionado(Plaprojeto projetoselecionado) {
        this.projetoselecionado = projetoselecionado;
    }

    public Engempreiteira getEmpreiteiraselecionada() {
        return empreiteiraselecionada;
    }

    public void setEmpreiteiraselecionada(Engempreiteira empreiteiraselecionada) {
        this.empreiteiraselecionada = empreiteiraselecionada;
    }

    public Rhcolaborador getColaboradorselecionado() {
        return colaboradorselecionado;
    }

    public void setColaboradorselecionado(Rhcolaborador colaboradorselecionado) {
        this.colaboradorselecionado = colaboradorselecionado;
    }

    public Plapavimentoprojeto getPavimentoprojetoselecionado() {
        return pavimentoprojetoselecionado;
    }

    public void setPavimentoprojetoselecionado(Plapavimentoprojeto pavimentoprojetoselecionado) {
        this.pavimentoprojetoselecionado = pavimentoprojetoselecionado;
    }

    public Plaatividade getAtividadeselecionada() {
        return atividadeselecionada;
    }

    public void setAtividadeselecionada(Plaatividade atividadeselecionada) {
        this.atividadeselecionada = atividadeselecionada;
    }

    public Plasubprojeto getSubprojetoselecionado() {
        return subprojetoselecionado;
    }

    public void setSubprojetoselecionado(Plasubprojeto subprojetoselecionado) {
        this.subprojetoselecionado = subprojetoselecionado;
    }
}

