package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentosubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class QualidadeActivity extends PrincipalActivity {

    public static SQLiteDatabase db;
    public Spinner spinnerObra;
    public TextView engenheiro;
    public TextView responsavel;
    public Rhcolaborador responsavelobra;
    public Rhcolaborador engenheiroresidente;
    public Plaprojeto projeto;
    public Spinner subprojeto;
    public Spinner atividade;
    public Spinner pavimento;
    public Spinner setor;
    public ListView listaApontamentosProducao;

    public static Integer posicaoobra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualidade);
        ImageButton ib = (ImageButton) findViewById(R.id.qualidade);
        ib.setImageResource(R.drawable.qualidadeselecionado);

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        //usuario global
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.novoUsuarioGlobal();
        setTheme(usuarioGlobal.estiloSelecionado);
        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

        //carrega campos da view
        engenheiro = (TextView) findViewById(R.id.producaoengenheiro);
        responsavel = (TextView) findViewById(R.id.producaoresponsavel);
        subprojeto = (Spinner) findViewById(R.id.producaosubprojeto);
        atividade = (Spinner) findViewById(R.id.producaoatividade);
        spinnerObra = (Spinner) findViewById(R.id.spinnerObra);
        pavimento = (Spinner) findViewById(R.id.producaopavimento);
        setor = (Spinner) findViewById(R.id.spinnerSetor);
        listaApontamentosProducao = (ListView) findViewById(R.id.listaApontamentosProducao);

        //carrega spinner de obras do usuario
        ArrayAdapter<Engobra> adapter = new ArrayAdapter<Engobra>(getApplicationContext(), R.layout.spinner_item, obrasDisponiveisUsuario(usuarioGlobal.getUsuarioLogado()));
        adapter.setDropDownViewResource(R.layout.item_lista);

        spinnerObra.setAdapter(adapter);

        spinnerObra.setSelection(posicaoobra);

        spinnerObra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerObra.getSelectedItemPosition()>0) {
                    usuarioGlobal.setObraselecionada((Engobra) spinnerObra.getSelectedItem());
                    //busca os dados da obra
                    responsavelobra = new Rhcolaborador();
                    responsavelobra = (Rhcolaborador) consultarPorId(responsavelobra, usuarioGlobal.getObraselecionada().getFkIdEngenheiroResidente().toString());
                    engenheiroresidente = new Rhcolaborador();
                    engenheiroresidente = (Rhcolaborador) consultarPorId(engenheiroresidente, usuarioGlobal.getObraselecionada().getFkIdGerenteEngenharia().toString());

                    responsavel.setText(responsavelobra.getNome());
                    engenheiro.setText(engenheiroresidente.getNome());
                    //armazena o projeto
                    projeto = new Plaprojeto();
                    projeto = (Plaprojeto) consultarPorId(projeto, usuarioGlobal.getObraselecionada().getFkIdProjeto().toString());
                    usuarioGlobal.setProjetoselecionado(projeto);
                    //carrega spinner de setores da obra
                    ArrayAdapter<Plasetorprojeto> adapterSetor = new ArrayAdapter<Plasetorprojeto>(getApplicationContext(), R.layout.spinner_item, setoresDisponiveisObra(projeto));
                    adapterSetor.setDropDownViewResource(R.layout.item_lista);
                    setor.setAdapter(adapterSetor);
                } else {
                    responsavel.setText("");
                    engenheiro.setText("");
                    usuarioGlobal.setObraselecionada(null);
                    GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
                    usuarioGlobal.setProjetoselecionado(null);
                    limpaSetor();
                    limpaSubprojeto();
                    limpaAtividade();
                    limpaPavimento();

                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        //onchange spinner setor
        setor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(setor.getSelectedItemPosition()>0) {
                    usuarioGlobal.setSetorprojetoselecionado((Plasetorprojeto) setor.getSelectedItem());
                    //lista subprojetos
                    ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), R.layout.spinner_item, listaSubProjetos((Plasetorprojeto) setor.getSelectedItem()));
                    adapterSubprojeto.setDropDownViewResource(R.layout.item_lista);
                    subprojeto.setAdapter(adapterSubprojeto);
                } else {
                    usuarioGlobal.setSetorprojetoselecionado(null);
                    limpaSubprojeto();
                    limpaAtividade();
                    limpaPavimento();
                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        //onchange spinner subprojeto
        subprojeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(subprojeto.getSelectedItemPosition()>0) {

                    usuarioGlobal.setSubprojetoselecionado((Plasubprojeto) subprojeto.getSelectedItem());
                    //lista spinner atividades
                    ArrayAdapter<Plaatividade> adapterSubprojetoAtividade = new ArrayAdapter<Plaatividade>(getApplicationContext(), R.layout.spinner_item, listaAtividadesSubProjeto((Plasubprojeto) subprojeto.getSelectedItem()));
                    adapterSubprojetoAtividade.setDropDownViewResource(R.layout.item_lista);
                    atividade.setAdapter(adapterSubprojetoAtividade);

                    //lista spinner pavimentos
                    ArrayAdapter<Plapavimentosubprojeto> adapterPavimento = new ArrayAdapter<Plapavimentosubprojeto>(getApplicationContext(), R.layout.spinner_item, listaPavimentoProjeto());
                    adapterPavimento.setDropDownViewResource(R.layout.item_lista);
                    pavimento.setAdapter(adapterPavimento);
                } else {
                    usuarioGlobal.setSubprojetoselecionado(null);
                    limpaAtividade();
                    limpaPavimento();
                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        //onchange spinner subprojeto
        atividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(atividade.getSelectedItemPosition()>0) {

                    usuarioGlobal.setAtividadeselecionada((Plaatividade) atividade.getSelectedItem());

                } else {
                    usuarioGlobal.setAtividadeselecionada(null);

                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //onchange spinner subprojeto
        pavimento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(atividade.getSelectedItemPosition()>0) {

                    usuarioGlobal.setPavimentosubprojetoprojetoselecionado((Plapavimentosubprojeto) pavimento.getSelectedItem());

                } else {
                    usuarioGlobal.setPavimentosubprojetoprojetoselecionado(null);

                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        listaApontamentosProducao.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);
                Orcelementoproducao elementoselecionado = new Orcelementoproducao();
                elementoselecionado = (Orcelementoproducao) consultarPorId(elementoselecionado , String.valueOf(c.getLong(c.getColumnIndexOrThrow("_id"))));
                usuarioGlobal.setElementoproducaoselecionado(elementoselecionado);
                Intent intent = new Intent(getApplicationContext(), DetalhesQualidade.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        //recupera os dados
        usuarioGlobal.setObraselecionada((Engobra) spinnerObra.getSelectedItem());
        usuarioGlobal.setSetorprojetoselecionado((Plasetorprojeto) setor.getSelectedItem());
        usuarioGlobal.setSubprojetoselecionado((Plasubprojeto) subprojeto.getSelectedItem());
        usuarioGlobal.setAtividadeselecionada((Plaatividade) atividade.getSelectedItem());
        usuarioGlobal.setPavimentosubprojetoprojetoselecionado((Plapavimentosubprojeto) pavimento.getSelectedItem());

    }


    public void limpaSetor(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setSetorprojetoselecionado(null);
        ArrayAdapter<Plasetorprojeto> adapterSetor = new ArrayAdapter<Plasetorprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plasetorprojeto>());
        setor.setAdapter(adapterSetor);
    }

    public void limpaSubprojeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setSubprojetoselecionado(null);
        ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plasubprojeto>());
        subprojeto.setAdapter(adapterSubprojeto);
    }

    public void limpaAtividade(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setAtividadeselecionada(null);
        ArrayAdapter<Plaatividade> adapterSubprojetoAtividade = new ArrayAdapter<Plaatividade>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plaatividade>());
        atividade.setAdapter(adapterSubprojetoAtividade);
    }

    public void limpaPavimento(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setPavimentosubprojetoprojetoselecionado(null);
        ArrayAdapter<Plapavimentosubprojeto> adapterPavimento = new ArrayAdapter<Plapavimentosubprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plapavimentosubprojeto>());
        pavimento.setAdapter(adapterPavimento);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qualidade, menu);
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


    public List<Engobra> obrasDisponiveisUsuario(Sisusuario usu){
        posicaoobra = 0;
        Engobra obraclienteselcionar = new Engobra();
        //busca pelo obra colaborador
        Cursor c = db.rawQuery("Select Distinct(o.id) from Engcolaboradorobra eco " +
                " inner join Engobra as o on eco.fkIdObra = o.id " +
                " where eco.fkIdColaborador = "+usu.getFkIdColaborador(),null);
        Engobra aux = new Engobra();
        if(c.getCount()>0){
            c.moveToFirst();
            obraclienteselcionar = (Engobra) consultarPorId(aux, c.getString(0));
        }
        c.close();

        //lista todas as obras
        Cursor ob = db.rawQuery("Select * from Engobra order by nome",null);
        ArrayList<Engobra> listaObras = new ArrayList<Engobra>();
        if(ob.getCount()>0){
            ob.moveToFirst();
            aux = new Engobra();
            aux.setNome("Selecione...");
            listaObras.add(aux);
            for(int i=0; i<ob.getCount();i++){
                aux = new Engobra();
                aux = (Engobra) consultarPorId(aux, ob.getString(ob.getColumnIndex("id")));
                if(aux.equals(obraclienteselcionar)){
                    posicaoobra = i+1;
                }
                listaObras.add(aux);
                ob.moveToNext();
            }
        }
        ob.close();

        return listaObras;

    }

    public List<Plasetorprojeto> setoresDisponiveisObra(Plaprojeto pro){

        //busca setor pelo projeto da obra
        Cursor c = db.rawQuery("Select setor.id from Plasetorprojeto setor " +
                "where setor.fkIdProjeto = "+pro.getId(),null);
        ArrayList<Plasetorprojeto> lista = new ArrayList<Plasetorprojeto>();
        if(c.getCount()>0){

            c.moveToFirst();
            Plasetorprojeto aux = new Plasetorprojeto();
            aux.setNome("Selecione...");
            lista.add(aux);

            for(int i=0; i<c.getCount();i++){
                aux = new Plasetorprojeto();
                lista.add((Plasetorprojeto) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }

        }
        c.close();
        return lista;

    }

    public List<Plasubprojeto> listaSubProjetos(Plasetorprojeto set){
        Cursor c = db.rawQuery("SELECT psp.id FROM Plasubprojeto as psp" +
                " Inner Join Plasubprojetosetorprojeto as pspp on psp.id = pspp.fkIdSubprojeto " +
                " where pspp.fkIdSetorProjeto = "+set.getId(), null);
        ArrayList<Plasubprojeto> lista = new ArrayList<Plasubprojeto>();
        if(c.getCount()>0){
            c.moveToFirst();
            Plasubprojeto aux = new Plasubprojeto();
            aux.setDescricao("Selecione...");
            lista.add(aux);
            for(int i=0; i<c.getCount();i++){
                aux = new Plasubprojeto();
                lista.add((Plasubprojeto) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;


    }

    public ArrayList<Plaatividade> listaAtividadesSubProjeto(Plasubprojeto subprojeto){
        Cursor c = db.rawQuery("SELECT at.id FROM Plaatividade as at inner join " +
                " Plasubprojeto as sp on at.fkIdSubprojeto = sp.id " +
                " where sp.id =  '"+subprojeto.getId()+"'", null);
        ArrayList<Plaatividade> lista = new ArrayList<Plaatividade>();
        if(c.getCount()>0){
            Plaatividade aux = new Plaatividade();
            aux.setNome("Selecione...");
            lista.add(aux);
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                aux = new Plaatividade();
                lista.add((Plaatividade) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;

    }

    public ArrayList<Plapavimentosubprojeto> listaPavimentoProjeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("SELECT psp.id FROM Plapavimentosubprojeto as psp " +
                " inner join Plasubprojetosetorprojeto as ssp on ssp.id = psp.fkIdSubprojetoSetorProjeto " +
                " where ssp.fkIdSubprojeto = "+usuarioGlobal.getSubprojetoselecionado().getId()+" " +
                " and psp.fkIdSetorProjeto = "+usuarioGlobal.getSetorprojetoselecionado().getId(), null);


        ArrayList<Plapavimentosubprojeto> lista = new ArrayList<Plapavimentosubprojeto>();
        if(c.getCount()>0){
            Plapavimentosubprojeto aux = new Plapavimentosubprojeto();
            aux.setNome("Selecione...");
            lista.add(aux);
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                aux = new Plapavimentosubprojeto();
                lista.add((Plapavimentosubprojeto) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;

    }



    public SimpleCursorAdapter carregaApontamentos(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();

        if(usuarioGlobal.getObraselecionada()!=null){
            String s = "Select ep.id as _id, s.nome as nomeservico, pro.id as proid, ep.codigo, um.nome as nomeunidade, SUM(pro.quantidade) as quantidade" +
                    " from Engproducao as pro " +
                    " inner join Orcservico as s on s.id=pro.fkIdServico " +
                    " inner join Orcelementoproducao as ep on ep.id=pro.fkIdElementoProducao " +
                    " inner join Orcunidademedida as um on um.id = s.fkIdUnidadeMedida " +
                    " Inner Join Plasubprojetosetorprojeto as pspp on pspp.id = pro.fkIdSubprojetoSetorProjeto " +
                    " where (pro.status is null OR pro.status = 'N' OR pro.status = '') " +
                    " and (pro.fkIdObra='" + usuarioGlobal.getObraselecionada().getId() + "')";

            if (usuarioGlobal.getSetorprojetoselecionado() != null) {
                s += " and (pro.fkIdSetorProjeto=" + usuarioGlobal.getSetorprojetoselecionado().getId()+")";
            }
            if (usuarioGlobal.getSubprojetoselecionado() != null) {
                s += " and (pspp.fkIdSubprojeto = "+usuarioGlobal.getSubprojetoselecionado().getId()+") " +
                        " and (pspp.fkIdSetorProjeto = "+usuarioGlobal.getSetorprojetoselecionado().getId()+") ";
            }
            if (usuarioGlobal.getPavimentosubprojetoprojetoselecionado() != null) {
                s += " and (pro.fkIdPavimentoSubprojeto=" + usuarioGlobal.getPavimentosubprojetoprojetoselecionado().getId()+")";
            }
            if (usuarioGlobal.getAtividadeselecionada() != null) {
                s += " and (pro.fkIdAtividade=" + usuarioGlobal.getAtividadeselecionada().getId()+")";
            }
            s+= " GROUP BY (_id)";

            Cursor c = db.rawQuery(s, null);

            // The desired columns to be bound
            if (c.moveToFirst()) {
                String[] columns = new String[]{
                        c.getColumnName(0), c.getColumnName(1), c.getColumnName(2), c.getColumnName(3), c.getColumnName(4), c.getColumnName(5)
                };

                // the XML defined views which the data will be bound to
                int[] to = new int[]{
                        R.id.idservico,
                        R.id.servico,
                        R.id.idproducao,
                        R.id.elementoproducao,
                        R.id.unidademedida,
                        R.id.quantidade
                };

                // create the adapter using the cursor pointing to the desired data
                //as well as the layout information
                SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                        this, R.layout.listaapontamentos,
                        c,
                        columns,
                        to,
                        0);

                // Assign adapter to ListView
                return dataAdapter;
            }
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
