package com.example.impactit.bridgeengenharia;

import android.app.Activity;
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
                //listaApontamentosProducao.setAdapter(carregaApontamentos());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


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
                aux = (Engobra) consultarPorId(aux, ob.getString(0));
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
