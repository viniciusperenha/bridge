package com.example.impactit.bridgeengenharia.controle;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentosubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Platarefa;
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
    public Plapavimentosubprojeto pavimentosubprojetoprojetoselecionado;
    public Plasetorprojeto setorprojetoselecionado;
    public Orcelementoproducao elementoproducaoselecionado;
    public Orcservico servicoselecionado;
    public Platarefa tarefaselecionada;

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

    public Plasetorprojeto getSetorprojetoselecionado() {
        return setorprojetoselecionado;
    }

    public void setSetorprojetoselecionado(Plasetorprojeto setorprojetoselecionado) {
        this.setorprojetoselecionado = setorprojetoselecionado;
    }

    public Plapavimentosubprojeto getPavimentosubprojetoprojetoselecionado() {
        return pavimentosubprojetoprojetoselecionado;
    }

    public void setPavimentosubprojetoprojetoselecionado(Plapavimentosubprojeto pavimentosubprojetoprojetoselecionado) {
        this.pavimentosubprojetoprojetoselecionado = pavimentosubprojetoprojetoselecionado;
    }

    public Orcelementoproducao getElementoproducaoselecionado() {
        return elementoproducaoselecionado;
    }

    public void setElementoproducaoselecionado(Orcelementoproducao elementoproducaoselecionado) {
        this.elementoproducaoselecionado = elementoproducaoselecionado;
    }

    public Orcservico getServicoselecionado() {
        return servicoselecionado;
    }

    public void setServicoselecionado(Orcservico servicoselecionado) {
        this.servicoselecionado = servicoselecionado;
    }

    public Platarefa getTarefaselecionada() {
        return tarefaselecionada;
    }

    public void setTarefaselecionada(Platarefa tarefaselecionada) {
        this.tarefaselecionada = tarefaselecionada;
    }
}

