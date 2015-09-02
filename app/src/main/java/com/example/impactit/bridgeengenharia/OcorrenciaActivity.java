package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.AdapterOcorrencia;
import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.EngOcorrenciaNaoPlanejada;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.OcorrenciaTO;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojetosetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OcorrenciaActivity extends PrincipalActivity {
    private static SQLiteDatabase db;
    private Spinner spinnerObra;
    private Spinner spinnerSetor;
    private Spinner spinnerSubprojeto;
    private EditText editResponsavel;
    private EditText editEngenheiro;
    private EditText editOcorrencia;
    private Rhcolaborador responsavelobra;
    private Rhcolaborador engenheiroresidente;
    private Plaprojeto projeto;
    private static Integer posicaoobra;
    private ListView listaocorrencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //usuario global
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(usuarioGlobal.estiloSelecionado>0) {
            setTheme(usuarioGlobal.estiloSelecionado);
        }
        setContentView(R.layout.activity_ocorrencia);
        ImageButton ib = (ImageButton) findViewById(R.id.ocorrencia);
        ib.setImageResource(R.drawable.ocorrenciaselecionado);

        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        usuarioGlobal.novoUsuarioGlobal();

        spinnerObra = (Spinner) findViewById(R.id.spinnerobraocorrencia);
        spinnerSetor = (Spinner) findViewById(R.id.spinnersetorocorrencia);
        spinnerSubprojeto = (Spinner) findViewById(R.id.spinnersubprojetoocorrencia);
        editResponsavel = (EditText) findViewById(R.id.editresponsavelocorrencia);
        editEngenheiro = (EditText) findViewById(R.id.editengenheiroocorrencia);
        editOcorrencia = (EditText) findViewById(R.id.editocorrencia);
        listaocorrencias = (ListView) findViewById(R.id.listaocorrencias);

        //carrega spinner de obras do usuario
        ArrayAdapter<Engobra> adapter = new ArrayAdapter<Engobra>(getApplicationContext(), R.layout.spinner_item, obrasDisponiveisUsuario(usuarioGlobal.getUsuarioLogado()));
        adapter.setDropDownViewResource(R.layout.item_lista);

        spinnerObra.setAdapter(adapter);

        spinnerObra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerObra.getSelectedItemPosition() > 0) {
                    usuarioGlobal.setObraselecionada((Engobra) spinnerObra.getSelectedItem());
                    //busca os dados da obra
                    responsavelobra = new Rhcolaborador();
                    responsavelobra = (Rhcolaborador) consultarPorId(responsavelobra, usuarioGlobal.getObraselecionada().getFkIdEngenheiroResidente().toString());
                    engenheiroresidente = new Rhcolaborador();
                    engenheiroresidente = (Rhcolaborador) consultarPorId(engenheiroresidente, usuarioGlobal.getObraselecionada().getFkIdGerenteEngenharia().toString());

                    editResponsavel.setText(responsavelobra.getNome());
                    editEngenheiro.setText(engenheiroresidente.getNome());
                    //armazena o projeto
                    projeto = new Plaprojeto();
                    projeto = (Plaprojeto) consultarPorId(projeto, usuarioGlobal.getObraselecionada().getFkIdProjeto().toString());
                    usuarioGlobal.setProjetoselecionado(projeto);
                    //carrega spinner de setores da obra
                    ArrayAdapter<Plasetorprojeto> adapterSetor = new ArrayAdapter<Plasetorprojeto>(getApplicationContext(), R.layout.spinner_item, setoresDisponiveisObra(projeto));
                    adapterSetor.setDropDownViewResource(R.layout.item_lista);
                    spinnerSetor.setAdapter(adapterSetor);
                } else {
                    editResponsavel.setText("");
                    editEngenheiro.setText("");
                    usuarioGlobal.setObraselecionada(null);
                    GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
                    usuarioGlobal.setProjetoselecionado(null);
                    limpaSetor();
                    limpaSubprojeto();

                }
                listaocorrencias.setAdapter(listaOcorrencias());
            }
            public void onNothingSelected(AdapterView<?> parent) { return; }

        });

        //onchange spinner setor
        spinnerSetor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerSetor.getSelectedItemPosition()>0) {
                    usuarioGlobal.setSetorprojetoselecionado((Plasetorprojeto) spinnerSetor.getSelectedItem());
                    //lista subprojetos
                    ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), R.layout.spinner_item, listaSubProjetos((Plasetorprojeto) spinnerSetor.getSelectedItem()));
                    adapterSubprojeto.setDropDownViewResource(R.layout.item_lista);
                    spinnerSubprojeto.setAdapter(adapterSubprojeto);
                } else {
                    usuarioGlobal.setSetorprojetoselecionado(null);

                    limpaSubprojeto();
                }
                listaocorrencias.setAdapter(listaOcorrencias());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //onchange spinner setor
        spinnerSubprojeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerSubprojeto.getSelectedItemPosition()>0) {
                    usuarioGlobal.setPlasubprojetosetorprojetoselecionado(buscaSubprojetoSetorProjeto(usuarioGlobal.getSetorprojetoselecionado(), (Plasubprojeto) spinnerSubprojeto.getSelectedItem()));
                } else {
                    usuarioGlobal.setPlasubprojetosetorprojetoselecionado(null);
                }
                listaocorrencias.setAdapter(listaOcorrencias());
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
        spinnerSetor.setAdapter(adapterSetor);
    }

    public void limpaSubprojeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setSubprojetoselecionado(null);
        usuarioGlobal.setPlasubprojetosetorprojetoselecionado(null);
        ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plasubprojeto>());
        spinnerSubprojeto.setAdapter(adapterSubprojeto);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ocorrencia, menu);
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

    public Plasubprojetosetorprojeto buscaSubprojetoSetorProjeto(Plasetorprojeto set, Plasubprojeto subprojeto){
        Cursor c = db.rawQuery("SELECT pspp.id FROM Plasubprojetosetorprojeto as pspp" +
                " where pspp.fkIdSetorProjeto = "+set.getId()+" " +
                " and pspp.fkIdSubprojeto = "+subprojeto.getId(), null);

        if(c.moveToFirst()){
            Plasubprojetosetorprojeto aux = new Plasubprojetosetorprojeto();
            aux = (Plasubprojetosetorprojeto) consultarPorId(aux,c.getString(0));
            c.close();
            return aux;
        }
        c.close();
        return null;
    }

    public AdapterOcorrencia listaOcorrencias(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(usuarioGlobal.getObraselecionada()!=null) {
            String s = "SELECT * FROM EngOcorrenciaNaoPlanejada where" +
                    " fkIdObra = " + usuarioGlobal.getObraselecionada().getId();
            if (usuarioGlobal.getSetorprojetoselecionado() != null) {
                s += " and fkIdSetorProjeto = " + usuarioGlobal.getSetorprojetoselecionado().getId();
            }
            if (usuarioGlobal.getPlasubprojetosetorprojetoselecionado() != null) {
                s += " and fkIdSubprojetoSetorProjeto = " + usuarioGlobal.getPlasubprojetosetorprojetoselecionado().getId();
            }
            s += " order by data";

            Cursor c = db.rawQuery(s, null);


            if (c.moveToFirst()) {
                ArrayList<OcorrenciaTO> ocorrenciaTOs = new ArrayList<>();
                OcorrenciaTO auxOcorrenciaTO;
                for (int i = 0; i < c.getCount(); i++) {
                    auxOcorrenciaTO = new OcorrenciaTO();
                    auxOcorrenciaTO.setEngobra((Engobra) consultarPorId(new Engobra(), c.getString(c.getColumnIndexOrThrow("fkIdObra"))));
                    auxOcorrenciaTO.setPlasetorprojeto((Plasetorprojeto) consultarPorId(new Plasetorprojeto(), c.getString(c.getColumnIndexOrThrow("fkIdSetorProjeto"))));
                    Plasubprojetosetorprojeto plasubprojetosetorprojeto = (Plasubprojetosetorprojeto) consultarPorId(new Plasubprojetosetorprojeto(), c.getString(c.getColumnIndexOrThrow("fkIdSubprojetoSetorProjeto")));
                    auxOcorrenciaTO.setPlasubprojeto((Plasubprojeto) consultarPorId(new Plasubprojeto(), String.valueOf(plasubprojetosetorprojeto.getFkIdSubprojeto())));
                    auxOcorrenciaTO.setEngOcorrenciaNaoPlanejada((EngOcorrenciaNaoPlanejada) consultarPorId(new EngOcorrenciaNaoPlanejada(),c.getString(c.getColumnIndexOrThrow("id"))));
                    ocorrenciaTOs.add(auxOcorrenciaTO);
                    c.moveToNext();
                }
                AdapterOcorrencia adapterOcorrencia = new AdapterOcorrencia(ocorrenciaTOs, this);
                c.close();
                return adapterOcorrencia;
            }
        }

        return null;
    }

    public void gravarOcorrencia(View v){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        String validacoes = "";
        //verifica campos obrigatorios
        if(usuarioGlobal.getObraselecionada()==null){
            validacoes = "Obra";
        }
        if(usuarioGlobal.getSetorprojetoselecionado()==null){
            validacoes += " Setor";
        }
        if(usuarioGlobal.getPlasubprojetosetorprojetoselecionado()==null){
            validacoes += " Subprojeto";
        }
        if("".equals(validacoes)) {

            if ("".equals(editOcorrencia.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Preencha a ocorrência", Toast.LENGTH_LONG).show();
                return;
            }
            EngOcorrenciaNaoPlanejada engOcorrenciaNaoPlanejada = new EngOcorrenciaNaoPlanejada();
            engOcorrenciaNaoPlanejada.setData(new Date());
            engOcorrenciaNaoPlanejada.setDescricao(editOcorrencia.getText().toString());
            engOcorrenciaNaoPlanejada.setFkIdObra(usuarioGlobal.getObraselecionada().getId());
            engOcorrenciaNaoPlanejada.setFkIdSetorProjeto(usuarioGlobal.getSetorprojetoselecionado().getId());
            engOcorrenciaNaoPlanejada.setFkIdSubprojetoSetorProjeto(usuarioGlobal.getPlasubprojetosetorprojetoselecionado().getId());
            engOcorrenciaNaoPlanejada.setFkIdResponsavel(usuarioGlobal.getUsuarioLogado().getId());
            engOcorrenciaNaoPlanejada.setId(buscaUltimoId(engOcorrenciaNaoPlanejada.getClass()));
            engOcorrenciaNaoPlanejada.setTransmitir(true);
            inserir(engOcorrenciaNaoPlanejada);
            Toast.makeText(getApplicationContext(), "Ocorrência gravada com sucesso!", Toast.LENGTH_LONG).show();
            db.close();
            OcorrenciaActivity.this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Selecione: "+validacoes+" para ocorrência", Toast.LENGTH_LONG).show();
        }
    }

    public Long buscaUltimoId(Class classe){
        Cursor c = db.rawQuery("Select id from "+classe.getSimpleName()+" order by id desc limit 1",null);
        if(c.moveToNext()){
            return c.getLong(0)+1;
        }
        return 1l;
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
                            SimpleDateFormat sdf2 = new SimpleDateFormat("E MMM dd HH:mm:ss zzzz yyyy", Locale.US);
                            String dataparse = c.getString(i);
                            dataparse = dataparse.replace("BRT","-0300");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
                            Date parse = sdf2.parse(dataparse);
                            f.set(obj, parse);
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

    public void inserir(Object obj) {
        try {

            Class classe = obj.getClass();
            ContentValues cv = new ContentValues();
            for (Field f : classe.getDeclaredFields()) {
                f.setAccessible(true);
                Object valor = f.get(obj);

                if (valor != null) {
                    cv.put(f.getName(), valor.toString());
                }
            }
            db.insert(classe.getSimpleName().toLowerCase(), null, cv);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Deseja cancelar a ocorrência?");
        alertbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // finish used for destroyed activity
                db.close();
                OcorrenciaActivity.this.finish();
            }

        });

        alertbox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Nothing will be happened when clicked on no button
                // of Dialog
            }
        });

        alertbox.show();

        return;
    }
}
