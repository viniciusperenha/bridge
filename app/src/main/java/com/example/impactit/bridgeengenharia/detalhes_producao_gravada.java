package com.example.impactit.bridgeengenharia;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class detalhes_producao_gravada extends ActionBarActivity {

    public EditText subprojeto;
    public EditText atividade;
    public EditText pavimento;
    public EditText empreiteira;
    public EditText colaborador;
    public EditText tarefa;
    public EditText spinnerServicos;
    public EditText unidademedida;
    public EditText spinnerelementoproducao;
    public EditText totalproduzido;
    public EditText producao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_producao_gravada);

        subprojeto = (EditText) findViewById(R.id.subprojeto);
        subprojeto.setText("");

        atividade = (EditText) findViewById(R.id.atividade);
        atividade.setText("");

        pavimento = (EditText) findViewById(R.id.pavimento);
        pavimento.setText("");

        empreiteira = (EditText) findViewById(R.id.empreiteira);
        empreiteira.setText("");

        colaborador = (EditText) findViewById(R.id.colaborador);
        colaborador.setText("");

        tarefa = (EditText) findViewById(R.id.tarefa);
        tarefa.setText("");

        spinnerServicos = (EditText) findViewById(R.id.servico);
        spinnerServicos.setText("");


        unidademedida = (EditText) findViewById(R.id.unidademedida);
        unidademedida.setText("");

        spinnerelementoproducao = (EditText) findViewById(R.id.elementoproducao);
        spinnerelementoproducao.setText("");

        totalproduzido = (EditText) findViewById(R.id.totalproduzido);
        totalproduzido.setText("");

        producao = (EditText) findViewById(R.id.producao);
        producao.setText("");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhes_producao_gravada, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
