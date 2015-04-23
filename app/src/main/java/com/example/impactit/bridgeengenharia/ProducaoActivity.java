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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
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
        ArrayAdapter<Engobra> adapter = new ArrayAdapter<Engobra>(getApplicationContext(), android.R.layout.simple_spinner_item, obrasDisponiveisUsuario(usuarioGlobal.getUsuarioLogado()));
        spinnerObra.setAdapter(adapter);

        //lista subprojetos
        ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), android.R.layout.simple_spinner_item, listaSubProjetos());
        subprojeto.setAdapter(adapterSubprojeto);

        //subprojeto
        subprojeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<Plaatividade> adapterSubprojetoAtividade = new ArrayAdapter<Plaatividade>(getApplicationContext(), android.R.layout.simple_spinner_item, listaAtividadesSubProjeto((Plasubprojeto) subprojeto.getSelectedItem()));
                atividade.setAdapter(adapterSubprojetoAtividade);
                //armazena subprojeto na camada global
                usuarioGlobal.setSubprojetoselecionado((Plasubprojeto) subprojeto.getSelectedItem());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        //atividade
        atividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //armazena na global a atividade
                usuarioGlobal.setAtividadeselecionada((Plaatividade) atividade.getSelectedItem());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        spinnerObra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //busca os dados da obra
                usuarioGlobal.setObraselecionada((Engobra) spinnerObra.getSelectedItem());
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
                ArrayAdapter<Plapavimentoprojeto> adapterPavimento = new ArrayAdapter<Plapavimentoprojeto>(getApplicationContext(), android.R.layout.simple_spinner_item, listaPavimentoProjeto());
                pavimento.setAdapter(adapterPavimento);


                //verifica a(s) empreiterias da obra pelo contrato e parametro
                ArrayAdapter<Engempreiteira> adapterEmpreiteria = new ArrayAdapter<Engempreiteira>(getApplicationContext(), android.R.layout.simple_spinner_item, listaEmpreiteirasContrato());
                empreiteira.setAdapter(adapterEmpreiteria);

                //busca colaboradores da obra
                ArrayAdapter<Rhcolaborador> adapteColaboradorEmpreiteira = new ArrayAdapter<Rhcolaborador>(getApplicationContext(), android.R.layout.simple_spinner_item, listaColaboradorObra());
                colaboradorempreiteira.setAdapter(adapteColaboradorEmpreiteira);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //selecionou empreiteira guarda como global
        empreiteira.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //armazena empreiteira
                usuarioGlobal.setEmpreiteiraselecionada((Engempreiteira) empreiteira.getSelectedItem());

                //busca colaboradores da obra
                ArrayAdapter<Rhcolaborador> adapteColaboradorEmpreiteira = new ArrayAdapter<Rhcolaborador>(getApplicationContext(), android.R.layout.simple_spinner_item, listaColaboradorObra());
                colaboradorempreiteira.setAdapter(adapteColaboradorEmpreiteira);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //selecionou colaborador guarda como global
        colaboradorempreiteira.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //armazena colaborador da empreiteira
                usuarioGlobal.setColaboradorselecionado( (Rhcolaborador) colaboradorempreiteira.getSelectedItem());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //selecionou pavimento guarda como global
        pavimento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //armazena o pavimento
                usuarioGlobal.setPavimentoprojetoselecionado((Plapavimentoprojeto) pavimento.getSelectedItem());
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

    public List<Engobra> obrasDisponiveisUsuario(Sisusuario usu){

        //busca pelo obra colaborador
        Cursor c = db.rawQuery("Select o.id from Engcolaboradorobra eco " +
                "inner join Engobra as o on eco.fkIdObra = o.id " +
                "where eco.fkIdColaborador = "+usu.getFkIdColaborador(),null);
        ArrayList<Engobra> lista = new ArrayList<Engobra>();
        if(c.getCount()>0){

            c.moveToFirst();

            for(int i=0; i<c.getCount();i++){
                Engobra aux = new Engobra();
                lista.add((Engobra) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }

        }
        return lista;

    }



    public List<Plasubprojeto> listaSubProjetos(){
        Cursor c = db.rawQuery("SELECT id FROM Plasubprojeto ", null);
        ArrayList<Plasubprojeto> lista = new ArrayList<Plasubprojeto>();
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                Plasubprojeto aux = new Plasubprojeto();
                lista.add((Plasubprojeto) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        return lista;


    }

    public ArrayList<Plaatividade> listaAtividadesSubProjeto(Plasubprojeto subprojeto){
        Cursor c = db.rawQuery("SELECT at.id FROM Plaatividade as at inner join " +
                " Plasubprojeto as sp on at.fkIdSubprojeto = sp.id " +
                " where sp.id =  '"+subprojeto.getId()+"'", null);
        ArrayList<Plaatividade> lista = new ArrayList<Plaatividade>();
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                Plaatividade aux = new Plaatividade();
                lista.add((Plaatividade) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        return lista;

    }

    public ArrayList<Plapavimentoprojeto> listaPavimentoProjeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("SELECT id FROM Plapavimentoprojeto " +
                "where fkIdProjeto = "+usuarioGlobal.getProjetoselecionado().getId().toString(), null);
        ArrayList<Plapavimentoprojeto> lista = new ArrayList<Plapavimentoprojeto>();
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                Plapavimentoprojeto aux = new Plapavimentoprojeto();
                lista.add((Plapavimentoprojeto) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        return lista;

    }

    public ArrayList<Rhcolaborador> listaColaboradorObra(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("Select col.id FROM Engcolaboradorobra as eco " +
                " inner join Rhcargo as c on eco.fkIdCargo = c.id " +
                " inner join Rhcolaborador as col on eco.fkIdColaborador = col.id " +
                " WHERE eco.fkIdObra = " + usuarioGlobal.getObraselecionada().getId().toString()
                , null);
        ArrayList<Rhcolaborador> lista = new ArrayList<Rhcolaborador>();
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                Rhcolaborador aux = new Rhcolaborador();
                lista.add((Rhcolaborador) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        return lista;
    }

    public ArrayList<Engempreiteira> listaEmpreiteirasContrato(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = null;
        //busca parametro pra tipo de contrato
        Cursor param = db.rawQuery("select * from Sisparametro where grupo='PARAMETROS' and nome='vincularEmpreiteiraPorContrato' and valor='S' ",null);
        if(param.getCount()==0) {
            System.out.println("Select e.id FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId().toString());

            c = db.rawQuery("Select e.id FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId(), null);
        }

        if(param.getCount()>0) {
            System.out.println("Select e.id FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " inner join Engcontratoservicoempreiteira as cse on ce.id = cse.fkIdContratoEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId().toString());
            c = db.rawQuery("Select e.id FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " inner join Engcontratoservicoempreiteira as cse on ce.id = cse.fkIdContratoEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId(), null);
        }

        ArrayList<Engempreiteira> lista = new ArrayList<Engempreiteira>();
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                Engempreiteira aux = new Engempreiteira();
                lista.add((Engempreiteira) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        return lista;
    }

    public Object consultarPorId(Object obj, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + obj.getClass().getSimpleName().toLowerCase()+" where id = "+id, null);
        return recuperarObjeto(obj,c);

    }
/*
    public ArrayList<String> populaSpinnerResultado(Cursor c){
        ArrayList<String> s = new ArrayList<>();
        c.moveToFirst();
        for(int i=0; i<c.getCount();i++){
            s.add(c.getString(0));
            c.moveToNext();
        }

        return s;
    }
*/
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

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                s+= c.getColumnName(i)+" - "+c.getString(i)+"   ";
            }

            //System.out.println(s);
        }
        return obj;

    }

    public void novaMedicao(View view) {
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        //verifica campos obrigatorios
        if(usuarioGlobal.getObraselecionada()==null){
            Toast.makeText(getApplicationContext(), "Selecione a Obra", Toast.LENGTH_LONG).show();
        }
        if(usuarioGlobal.getSubprojetoselecionado()==null){
            Toast.makeText(getApplicationContext(), "Selecione o subprojeto", Toast.LENGTH_LONG).show();
        }
        if(usuarioGlobal.getAtividadeselecionada()==null){
            Toast.makeText(getApplicationContext(), "Selecione a atividade", Toast.LENGTH_LONG).show();
        }
        if(usuarioGlobal.getPavimentoprojetoselecionado()==null){
            Toast.makeText(getApplicationContext(), "Selecione o pavimento", Toast.LENGTH_LONG).show();
        }
        if(usuarioGlobal.getEmpreiteiraselecionada()==null){
            Toast.makeText(getApplicationContext(), "Selecione a empreiteira", Toast.LENGTH_LONG).show();
        }
        if(usuarioGlobal.getColaboradorselecionado()==null){
            Toast.makeText(getApplicationContext(), "Selecione o colaborador", Toast.LENGTH_LONG).show();
        }


        Intent intent = new Intent(getApplicationContext(), DetalhesProducao.class);
        startActivity(intent);
    }
}
