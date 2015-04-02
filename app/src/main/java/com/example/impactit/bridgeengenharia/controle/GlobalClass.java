package com.example.impactit.bridgeengenharia.controle;

import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
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
}