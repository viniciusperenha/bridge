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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentoprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProducaoActivity extends PrincipalActivity {

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
    public Plapavimentoprojeto pavimentoprojeto;
    public Spinner empreiteira;
    public Spinner colaboradorempreiteira;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao);
        ImageButton ib = (ImageButton) findViewById(R.id.producao);
        ib.setImageResource(R.drawable.producaoselecionado);

        engenheiro = (TextView) findViewById(R.id.producaoengenheiro);
        responsavel = (TextView) findViewById(R.id.producaoresponsavel);
        subprojeto = (Spinner) findViewById(R.id.producaosubprojeto);
        atividade = (Spinner) findViewById(R.id.producaoatividade);
        spinnerObra = (Spinner) findViewById(R.id.spinnerObra);
        pavimento = (Spinner) findViewById(R.id.producaopavimento);
        empreiteira = (Spinner)  findViewById(R.id.empreiteiracontrato);
        colaboradorempreiteira = (Spinner)  findViewById(R.id.colaboradorempreiteira);

        //usuario global
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        //carrega spinner de obras do usuario
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, obrasDisponiveisUsuario(usuarioGlobal.getUsuarioLogado()));
        spinnerObra.setAdapter(adapter);

        //lista subprojetos
        ArrayAdapter<String> adapterSubprojeto = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaSubProjetos());
        subprojeto.setAdapter(adapterSubprojeto);


        subprojeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> adapterSubprojetoAtividade = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaAtividadesSubProjeto(subprojeto.getSelectedItem().toString()));
                atividade.setAdapter(adapterSubprojetoAtividade);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        spinnerObra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //busca os dados da obra
                usuarioGlobal.setObraselecionada((Engobra) recuperaObraSelecionada(spinnerObra.getSelectedItem().toString()));
                responsavelobra = new Rhcolaborador();
                responsavelobra = (Rhcolaborador) consultarPorId(responsavelobra , usuarioGlobal.getObraselecionada().getFkIdEngenheiroResidente().toString());

                engenheiroresidente = new Rhcolaborador();
                engenheiroresidente = (Rhcolaborador) consultarPorId(engenheiroresidente , usuarioGlobal.getObraselecionada().getFkIdGerenteEngenharia().toString());

                responsavel.setText(responsavelobra.getNome());
                engenheiro.setText(engenheiroresidente.getNome());

                projeto = new Plaprojeto();
                projeto = (Plaprojeto) consultarPorId(projeto , usuarioGlobal.getObraselecionada().getFkIdProjeto().toString());
                usuarioGlobal.setProjetoselecionado(projeto);

                //lista pavimentos
                ArrayAdapter<String> adapterPavimento = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaPavimentoProjeto());
                pavimento.setAdapter(adapterPavimento);


                //verifica a(s) empreiterias da obra pelo contrato e parametro
                ArrayAdapter<String> adapterEmpreiteria = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaEmpreiteirasContrato());
                empreiteira.setAdapter(adapterEmpreiteria);

                //busca colaboradores da obra
                ArrayAdapter<String> adapteColaboradorEmpreiteira = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listaColaboradorObra());
                colaboradorempreiteira.setAdapter(adapteColaboradorEmpreiteira);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        empreiteira.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Engempreiteira emp = new Engempreiteira();
                emp = (Engempreiteira) recuperaEmpreiteiraSelecionada(empreiteira.getSelectedItem().toString());
                usuarioGlobal.setEmpreiteiraselecionada(emp);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_producao, menu);
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

    public ArrayList obrasDisponiveisUsuario(Sisusuario usu){
        ArrayList<String> lista = new ArrayList<String>();
        //busca pelo obra colaborador
        Cursor c = db.rawQuery("Select o.nome from Engcolaboradorobra eco " +
                "inner join Engobra as o on eco.fkIdObra = o.id " +
                "where eco.fkIdColaborador = "+usu.getFkIdColaborador(),null);
        if(c.getCount()>0){
            return populaSpinnerResultado(c);
        }
        lista.add("Obra nao encontrada");

        return lista;
    }



    public Engobra recuperaObraSelecionada(String obra){
        Engobra engobra = new Engobra();
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();

        Cursor c = db.rawQuery("Select o.* from Engcolaboradorobra eco " +
                "inner join Engobra as o on eco.fkIdObra = o.id " +
                "where eco.fkIdColaborador = " + usuarioGlobal.getUsuarioLogado().getFkIdColaborador() + " " +
                " and o.nome = '" + obra+"'", null);

        return (Engobra) recuperarObjeto(engobra,c);

    }

    public Engempreiteira recuperaEmpreiteiraSelecionada(String empreiteira){
        Engempreiteira emp = new Engempreiteira();
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("Select * from Engempreiteira as e " +
                " where e.nomeFantasia = '" + empreiteira+"'", null);
        return (Engempreiteira) recuperarObjeto(emp,c);
    }



    public ArrayList<String> listaSubProjetos(){
        Cursor c = db.rawQuery("SELECT descricao FROM Plasubprojeto ", null);
        return populaSpinnerResultado(c);
    }

    public ArrayList<String> listaAtividadesSubProjeto(String subprojeto){
        Cursor c = db.rawQuery("SELECT at.nome FROM Plaatividade as at inner join " +
                " Plasubprojeto as sp on at.fkIdSubprojeto = sp.id " +
                " where sp.descricao =  '"+subprojeto+"'", null);
        return populaSpinnerResultado(c);
    }

    public ArrayList<String> listaPavimentoProjeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("SELECT nome FROM Plapavimentoprojeto " +
                "where fkIdProjeto = "+usuarioGlobal.getProjetoselecionado().getId().toString(), null);
        return populaSpinnerResultado(c);
    }

    public ArrayList<String> listaColaboradorObra(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("Select c.nome, col.nome FROM Engcolaboradorobra as eco " +
                " inner join Rhcargo as c on eco.fkIdCargo = c.id " +
                " inner join Rhcolaborador as col on eco.fkIdColaborador = col.id " +
                " WHERE eco.fkIdObra = " + usuarioGlobal.getObraselecionada().getId().toString()
                , null);

        ArrayList<String> s = new ArrayList<>();
        c.moveToFirst();
        for(int i=0; i<c.getCount();i++){
            s.add(c.getString(0)+" - ");
            c.moveToNext();
        }

        return populaSpinnerResultado(c);
    }

    public ArrayList<String> listaEmpreiteirasContrato(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = null;
        //busca parametro pra tipo de contrato
        Cursor param = db.rawQuery("select * from Sisparametro where grupo='PARAMETROS' and nome='vincularEmpreiteiraPorContrato' ",null);
        if(param.getCount()>0) {

            c = db.rawQuery("Select e.nomeFantasia FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId().toString(), null);
        }

        if(param.getCount()==0) {

            c = db.rawQuery("Select e.nomeFantasia FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " inner join Engcontratoservicoempreiteira as cse on ce.id = cse.fkIdContratoEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId().toString(), null);
        }


        return populaSpinnerResultado(c);
    }

    public Object consultarPorId(Object obj, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + obj.getClass().getSimpleName().toLowerCase()+" where id = "+id, null);
        return recuperarObjeto(obj,c);

    }

    public ArrayList<String> populaSpinnerResultado(Cursor c){
        ArrayList<String> s = new ArrayList<>();
        c.moveToFirst();
        for(int i=0; i<c.getCount();i++){
            s.add(c.getString(0));
            c.moveToNext();
        }

        return s;
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
                            System.out.println(c.getString(i));
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

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                s+= c.getColumnName(i)+" - "+c.getString(i)+"   ";
            }

            System.out.println(s);
        }
        return obj;

    }
}
