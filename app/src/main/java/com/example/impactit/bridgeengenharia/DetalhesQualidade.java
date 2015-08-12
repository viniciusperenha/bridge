package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.impactit.bridgeengenharia.controle.AdapterQualidade;
import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.EngItemVerificacaoServico;
import com.example.impactit.bridgeengenharia.entidades.EngVerificacaoQualidadeServico;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Orcunidademedida;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentosubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojetosetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Platarefa;
import com.example.impactit.bridgeengenharia.entidades.QualidadeTO;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetalhesQualidade extends ActionBarActivity {
    public static SQLiteDatabase db;
    public EditText subprojeto;
    public EditText atividade;
    public EditText pavimento;
    public EditText tarefa;
    public EditText servico;
    public EditText elementoproducao;
    public EditText totalproduzido;
    public EditText observacao;
    public EditText setor;
    public EditText empreiteira;
    public EditText colaborador;
    public ListView listaApontamentosProducao;
    public ArrayList<QualidadeTO> itemsqualidade = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_qualidade);

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        //usuario global
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();

        //carrega campos da view
        setor = (EditText) findViewById(R.id.setor);
        subprojeto = (EditText) findViewById(R.id.subprojeto);
        atividade = (EditText) findViewById(R.id.atividade);
        pavimento = (EditText) findViewById(R.id.pavimento);
        empreiteira = (EditText) findViewById(R.id.empreiteira);
        colaborador = (EditText) findViewById(R.id.colaborador);
        tarefa = (EditText) findViewById(R.id.tarefa);
        servico = (EditText) findViewById(R.id.servico);

        elementoproducao = (EditText) findViewById(R.id.elementoproducao);
        totalproduzido = (EditText) findViewById(R.id.totalproduzido);

        listaApontamentosProducao = (ListView) findViewById(R.id.listaApontamentosProducao);


        Engempreiteira emp = ultimaProducaoEmpreiteira(usuarioGlobal.getElementoproducaoselecionado());
        empreiteira.setText(emp.getNomeFantasia());

        Plasetorprojeto se = new Plasetorprojeto();
        se = (Plasetorprojeto) consultarPorId(se, usuarioGlobal.getElementoproducaoselecionado().getFkIdSetorProjeto().toString());
        setor.setText(se.getNome());

        Plasubprojeto sub = new Plasubprojeto();
        Plasubprojetosetorprojeto subsetor = new Plasubprojetosetorprojeto();
        subsetor = (Plasubprojetosetorprojeto) consultarPorId(subsetor, usuarioGlobal.getElementoproducaoselecionado().getFkIdSubprojetoSetorProjeto().toString());
        sub = (Plasubprojeto) consultarPorId(sub, subsetor.getFkIdSubprojeto().toString());
        subprojeto.setText(sub.getDescricao());

        Plaatividade ati = new Plaatividade();
        ati = (Plaatividade) consultarPorId(ati, usuarioGlobal.getElementoproducaoselecionado().getFkIdAtividade().toString());
        atividade.setText(ati.getNome());

        Plapavimentosubprojeto pav = buscaPavimentoSubProjeto(subsetor, se,usuarioGlobal.getElementoproducaoselecionado());
        pavimento.setText(pav.getNome());

        Platarefa tar = new Platarefa();
        tar = (Platarefa) consultarPorId(tar, usuarioGlobal.getElementoproducaoselecionado().getFkIdTarefa().toString());
        tarefa.setText(tar.getNome());

        Orcservico ser = new Orcservico();
        ser = (Orcservico) consultarPorId(ser, usuarioGlobal.getElementoproducaoselecionado().getFkIdServico().toString());
        servico.setText(ser.getNome());

        Orcunidademedida um = new Orcunidademedida();
        um = (Orcunidademedida) consultarPorId(um, ser.getFkIdUnidadeMedida().toString());


        elementoproducao.setText(usuarioGlobal.getElementoproducaoselecionado().getCodigo());

        totalproduzido = (EditText) findViewById(R.id.totalproduzido);
        totalproduzido.setText(String.valueOf(buscaTotalProduzido(usuarioGlobal.getElementoproducaoselecionado(),
                usuarioGlobal.getObraselecionada(),
                subsetor,
                ati,
                pav,
                ser,
                emp
        ))+" "+um.getNome());

        //listar os apontamentos
        listaApontamentosProducao.setAdapter(carregaApontamentos(usuarioGlobal.getElementoproducaoselecionado(),
                pav,tar,ser,usuarioGlobal.getObraselecionada(),ati,se,subsetor
                ));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhes_qualidade, menu);
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

    public Double buscaTotalProduzido(Orcelementoproducao elementoproducao, Engobra obra,
                                      Plasubprojetosetorprojeto subprojeto,
                                      Plaatividade atividade,
                                      Plapavimentosubprojeto pavimento,
                                      Orcservico servico,
                                      Engempreiteira empreiteira){
        GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("SELECT SUM(pro.quantidade) FROM Engproducao as pro  " +
                " inner join Plasubprojetosetorprojeto pssp on pssp.id = pro.fkIdSubprojetoSetorProjeto " +
                " where (pro.status is null OR pro.status = 'N' OR pro.status = '') " +
                " and (pro.fkIdAtividade = " + atividade.getId() + ") " +
                " and (pssp.fkIdSetorProjeto = " + String.valueOf(subprojeto.getFkIdSetorProjeto()) + ") " +
                " and (pssp.fkIdSubprojeto = " + String.valueOf(subprojeto.getFkIdSubprojeto()) + ") " +
                " and (pro.fkIdServico = " + servico.getId() + ") " +
                " and (pro.fkIdElementoProducao = " + elementoproducao.getId() + ") ", null);

        if(c.moveToNext()){
            c.moveToFirst();
            return c.getDouble(0);
        }
        c.close();
        return 0.00;
    }


    public Engempreiteira ultimaProducaoEmpreiteira(Orcelementoproducao elemento){
        Cursor c = db.rawQuery("Select p.fkIdEmpreiteira from Engproducao as p " +
                "where p.fkIdElementoProducao = "+elemento.getId()+" order by p.id desc limit 1",null);
        if(c.getCount()>0){
            c.moveToFirst();
            Engempreiteira aux = new Engempreiteira();
            return (Engempreiteira) consultarPorId(aux, c.getString(0));
        }
        c.close();
        return null;
    }


    public Plapavimentosubprojeto buscaPavimentoSubProjeto(Plasubprojetosetorprojeto subsetor, Plasetorprojeto setor, Orcelementoproducao orcelementoproducao){
        Cursor c = db.rawQuery("Select p.id from Plapavimentosubprojeto as p " +
                " inner join Orcpavimentoelementoproducao as pep on pep.fkIdPavimentoSubprojeto = p.id " +
                " where p.fkIdSubprojetoSetorProjeto = "+subsetor.getId()+"" +
                " and p.fkIdSetorProjeto = "+setor.getId()+"" +
                " and pep.fkIdElementoProducao = "+orcelementoproducao.getId(),null);
        if(c.getCount()>0){
            c.moveToFirst();
            Plapavimentosubprojeto aux = new Plapavimentosubprojeto();
            return (Plapavimentosubprojeto) consultarPorId(aux, c.getString(0));
        }
        c.close();
        return null;
    }

    public AdapterQualidade carregaApontamentos(Orcelementoproducao orcelementoproducao,
                                                Plapavimentosubprojeto plapavimentosubprojeto,
                                                Platarefa platarefa,
                                                Orcservico orcservico,
                                                Engobra engobra,
                                                Plaatividade plaatividade,
                                                Plasetorprojeto plasetorprojeto,
                                                Plasubprojetosetorprojeto plasubprojetosetorprojeto){

        String s = ("SELECT item.id " +
                " FROM EngItemVerificacaoServico as item " +
                " where item.fkIdTarefa = " + platarefa.getId());

        Cursor c = db.rawQuery(s, null);

        //verifica se vieram registros para evitar nullpointer
        if (c.moveToFirst()) {
            QualidadeTO qualidadeaux;
            EngItemVerificacaoServico itemaux;

            for (int i = 0; i < c.getCount(); i++) {
                qualidadeaux = new QualidadeTO();
                itemaux = new EngItemVerificacaoServico();
                itemaux = (EngItemVerificacaoServico) consultarPorId(itemaux,String.valueOf(c.getLong(0)));
                qualidadeaux.setEngItemVerificacaoServico(itemaux);
                qualidadeaux.setEngVerificacaoQualidadeServico(qualidadesApontadas(itemaux, orcelementoproducao, plapavimentosubprojeto, platarefa, orcservico, engobra, plaatividade, plasetorprojeto,plasubprojetosetorprojeto));
                itemsqualidade.add(qualidadeaux);
                c.moveToNext();
            }

            AdapterQualidade adapterQualidade = new AdapterQualidade(this, itemsqualidade);
            return adapterQualidade;
        }
        return null;
    }

    public EngVerificacaoQualidadeServico qualidadesApontadas(EngItemVerificacaoServico engItemVerificacaoServico,
                                                                    Orcelementoproducao orcelementoproducao,
                                                                    Plapavimentosubprojeto plapavimentosubprojeto,
                                                                    Platarefa platarefa,
                                                                    Orcservico orcservico,
                                                                    Engobra engobra,
                                                                    Plaatividade plaatividade,
                                                                    Plasetorprojeto plasetorprojeto,
                                                                    Plasubprojetosetorprojeto plasubprojetosetorprojeto
                                                                    ){
        String s = "SELECT id FROM EngVerificacaoQualidadeServico where " +
                " fkIdItemVerificacaoServico = "+engItemVerificacaoServico.getId()+" " +
                " AND fkIdElementoProducao = "+orcelementoproducao.getId()+" " +
                " AND fkIdPavimentoSubprojeto = "+plapavimentosubprojeto.getId()+ " " +
                " AND fkIdTarefa = "+platarefa.getId()+" " +
                " AND fkIdServico = "+orcservico.getId()+" " +
                " AND fkIdObra = "+engobra.getId()+" " +
                " AND fkIdAtividade = "+plaatividade.getId()+" " +
                " AND fkIdSetorProjeto = "+plasetorprojeto.getId()+" " +
                " AND fkIdSubprojetoSetorProjeto = "+plasubprojetosetorprojeto.getId();

        Cursor c = db.rawQuery(s, null);
        if(c.moveToFirst()){
            EngVerificacaoQualidadeServico engVerificacaoretorno = new EngVerificacaoQualidadeServico();
            engVerificacaoretorno = (EngVerificacaoQualidadeServico) consultarPorId(engVerificacaoretorno,String.valueOf(c.getLong(0)));
            return engVerificacaoretorno;
        }

        return null;
    }


    public Object consultarPorId(Object obj, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + obj.getClass().getSimpleName().toLowerCase()+" where id = "+id, null);
        return recuperarObjeto(obj,c);
    }

    public Object recuperarObjeto(Object obj, Cursor c) {
        if(c.getCount()>0) {
            c.moveToFirst();
            String s = "";
            for (int i = 0; i < c.getColumnCount(); i++) {
                try{
                    Field f= obj.getClass().getDeclaredField(c.getColumnName(i));
                    f.setAccessible(true);
                    if((!"".equals(c.getString(i)))&&(c.getString(i)!=null)) {
                        if (f.getType().equals(Date.class)) {
                            //System.out.println(c.getString(i));
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
}
