package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Orcunidademedida;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentosubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojetosetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Platarefa;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;


public class detalhes_producao_gravada extends ActionBarActivity {
    public static SQLiteDatabase db;
    public EditText subprojeto;
    public EditText atividade;
    public EditText pavimento;
    public EditText empreiteira;
    public EditText colaborador;
    public EditText tarefa;
    public EditText servicos;
    public EditText unidademedida;
    public EditText elementoproducao;
    public EditText totalproduzido;
    public EditText producao;
    public EditText observacao;
    public EditText setor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_producao_gravada);

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);
        //global que armazena na sessao
        GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();

        //busca setor
        Plasetorprojeto setorprojetoobj = new Plasetorprojeto();
        setorprojetoobj = (Plasetorprojeto) consultarPorId(setorprojetoobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdSetorProjeto());
        setor = (EditText) findViewById(R.id.setor);
        setor.setText(setorprojetoobj.getNome());


        //busca subprojetosetorprojeto
        Plasubprojetosetorprojeto subprojetosetorprojetoobj = new Plasubprojetosetorprojeto();
        subprojetosetorprojetoobj = (Plasubprojetosetorprojeto) consultarPorId(subprojetosetorprojetoobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdSubprojetoSetorProjeto());

        //busca subprojeto
        Plasubprojeto subprojetoobj = new Plasubprojeto();
        subprojetoobj = (Plasubprojeto) consultarPorId(subprojetoobj, subprojetosetorprojetoobj.getFkIdSubprojeto());
        subprojeto = (EditText) findViewById(R.id.subprojeto);
        subprojeto.setText(subprojetoobj.getDescricao());

        //busca atividade
        Plaatividade atividadeobj = new Plaatividade();
        atividadeobj = (Plaatividade) consultarPorId(atividadeobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdAtividade());
        atividade = (EditText) findViewById(R.id.atividade);
        atividade.setText(atividadeobj.getNome());

        //busca pavimento
        Plapavimentosubprojeto pavimentosubprojetoobj = new Plapavimentosubprojeto();
        pavimentosubprojetoobj = (Plapavimentosubprojeto) consultarPorId(pavimentosubprojetoobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdPavimentoSubprojeto());
        pavimento = (EditText) findViewById(R.id.pavimento);
        pavimento.setText(pavimentosubprojetoobj.getNome());

        //busca empreiteira
        Engempreiteira empreiteiraobj = new Engempreiteira();
        empreiteiraobj = (Engempreiteira) consultarPorId(empreiteiraobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdEmpreiteira());
        empreiteira = (EditText) findViewById(R.id.empreiteira);
        empreiteira.setText(empreiteiraobj.getNomeFantasia());

        //busca colaborador
        Rhcolaborador colaboradorobj = new Rhcolaborador();
        colaboradorobj = (Rhcolaborador) consultarPorId(colaboradorobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdColaborador());
        colaborador = (EditText) findViewById(R.id.colaborador);
        colaborador.setText(colaboradorobj.getNome());

        //busca tarefa
        Platarefa tarefaobj = new Platarefa();
        tarefaobj = (Platarefa) consultarPorId(tarefaobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdTarefa());
        tarefa = (EditText) findViewById(R.id.tarefa);
        tarefa.setText(tarefaobj.getNome());

        //busca servico
        Orcservico servicoobj = new Orcservico();
        servicoobj = (Orcservico) consultarPorId(servicoobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdServico());
        servicos = (EditText) findViewById(R.id.servico);
        servicos.setText(servicoobj.getNome());

        //busca unidademedida
        Orcunidademedida unidademedidaobj = new Orcunidademedida();
        unidademedidaobj = (Orcunidademedida) consultarPorId(unidademedidaobj, Long.valueOf(servicoobj.getFkIdUnidadeMedida()));
        unidademedida = (EditText) findViewById(R.id.unidademedida);
        unidademedida.setText(unidademedidaobj.getNome());

        //busca elementoproducao
        Orcelementoproducao elementoproducaoobj = new Orcelementoproducao();
        elementoproducaoobj = (Orcelementoproducao) consultarPorId(elementoproducaoobj, usuarioglobal.getProducaoselecionadamostrar().getFkIdElementoProducao());
        elementoproducao = (EditText) findViewById(R.id.elementoproducao);
        elementoproducao.setText(elementoproducaoobj.getCodigo());

        totalproduzido = (EditText) findViewById(R.id.totalproduzido);
        totalproduzido.setText(String.valueOf(buscaTotalProduzido(elementoproducaoobj,
                usuarioglobal.getObraselecionada(),
                subprojetosetorprojetoobj,
                atividadeobj,
                pavimentosubprojetoobj,
                servicoobj,
                empreiteiraobj
                )));

        System.out.println(usuarioglobal.getProducaoselecionadamostrar().getQuantidade());

        producao = (EditText) findViewById(R.id.producao);
        producao.setText(String.valueOf(usuarioglobal.getProducaoselecionadamostrar().getQuantidade()));

        observacao = (EditText) findViewById(R.id.observacoes);
        observacao.setText(usuarioglobal.getProducaoselecionadamostrar().getObservacao());
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

    public void fechar(View view){

        detalhes_producao_gravada.this.finish();

    }

    public void excluirapontamento(View view){


        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Deseja EXCLUIR o apontamento?");
        alertbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                ContentValues values = new ContentValues();
                values.put("status","E");

                GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();
                db.update("Engproducao", values, "id=?", new String[] {String.valueOf(usuarioglobal.getProducaoselecionadamostrar().getId())});

                Toast.makeText(getApplicationContext(), "Apontamento excluido com sucesso!", Toast.LENGTH_LONG).show();
                detalhes_producao_gravada.this.finish();

                // finish used for destroyed activity
                detalhes_producao_gravada.this.finish();
            }

        });

        alertbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Nothing will be happened when clicked on no button
                // of Dialog
            }
        });

        alertbox.show();


    }


    public Object consultarPorId(Object obj, Long id) {
        Cursor c = db.rawQuery("SELECT * FROM " + obj.getClass().getSimpleName().toLowerCase()+" where id = "+id.toString(), null);
        return recuperarObjeto(obj,c);
    }


    public Object recuperarObjeto(Object obj, Cursor c) {

        if(c.getCount()>0) {
            c.moveToFirst();
            String s = "";
            //System.out.println(obj.getClass().getSimpleName());
            for (int i = 0; i < c.getColumnCount(); i++) {
                try{
                    Field f= obj.getClass().getDeclaredField(c.getColumnName(i));
                    f.setAccessible(true);
                    if((!"".equals(c.getString(i)))&&(c.getString(i)!=null)) {
                        //System.out.println(f.getType());

                        if (f.getType().equals(Date.class)) {

                            //TODO: criar conversao para data

                        }
                        if (f.getType().equals(Long.class)) {
                            f.set(obj, Long.parseLong(c.getString(i)));
                        }
                        if (f.getType().equals(String.class)) {
                            f.set(obj, c.getString(i));
                        }
                        if (f.getType().equals(Character.class)) {
                            f.set(obj, c.getString(i).charAt(0));
                        }
                        if (f.getType().equals(BigInteger.class)) {
                            f.set(obj, BigInteger.valueOf(Long.parseLong(c.getString(i))));
                        }
                        if (f.getType().equals(int.class)) {
                            f.set(obj, Integer.parseInt(c.getString(i)));
                        }
                        if (f.getType().equals(double.class)) {
                            f.set(obj, Double.parseDouble(c.getString(i)));
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                s+= c.getColumnName(i)+" - "+c.getString(i)+"   ";
            }


        }
        return obj;

    }

    public Double buscaTotalProduzido(Orcelementoproducao elementoproducao, Engobra obra,
                                      Plasubprojetosetorprojeto subprojeto,
                                      Plaatividade atividade,
                                      Plapavimentosubprojeto pavimento,
                                      Orcservico servico,
                                      Engempreiteira empreiteira){
        GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();

        Cursor c = db.rawQuery("SELECT SUM(pro.quantidade) FROM Engproducao as pro  " +
                " inner join Plasubprojetosetorprojeto pssp on pssp.id = pro.fkIdSubprojetoSetorProjeto " +
                " where pro.status is null " +
                " and pro.fkIdObra = " + obra.getId() + " " +
                " and pssp.fkIdSetorProjeto = " + String.valueOf(subprojeto.getFkIdSetorProjeto()) + " " +
                " and pssp.fkIdSubprojeto = " + String.valueOf(subprojeto.getFkIdSubprojeto()) + " " +
                " and pro.fkIdAtividade = " + atividade.getId() + " " +
                " and pro.fkIdPavimentoSubprojeto = " + pavimento.getId() + " " +
                " and pro.fkIdServico = " + servico.getId() + " " +
                " and pro.fkIdElementoProducao = " + elementoproducao.getId() + " " +
                " and pro.fkIdEmpreiteira = " + empreiteira.getId(), null);


        if(c.moveToNext()){
            c.moveToFirst();
            return c.getDouble(0);
        }
        c.close();
        return 0.00;
    }
}
