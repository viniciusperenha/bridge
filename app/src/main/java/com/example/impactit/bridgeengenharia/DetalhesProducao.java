package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DetalhesProducao extends ActionBarActivity {

    public static SQLiteDatabase db;
    public EditText subprojeto;
    public EditText atividade;
    public EditText pavimento;
    public EditText empreiteira;
    public EditText colaborador;
    public Spinner spinnerServicos;
    public EditText codigoservico;
    public EditText unidademedida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_producao);

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        //carrega campos
        subprojeto = (EditText) findViewById(R.id.subprojeto);
        subprojeto.setText(usuarioGlobal.getSubprojetoselecionado().getDescricao());

        atividade = (EditText) findViewById(R.id.atividade);
        atividade.setText(usuarioGlobal.getAtividadeselecionada().getNome());

        pavimento = (EditText) findViewById(R.id.pavimento);
        pavimento.setText(usuarioGlobal.getPavimentoprojetoselecionado().getNome());

        empreiteira = (EditText) findViewById(R.id.empreiteira);
        empreiteira.setText(usuarioGlobal.getEmpreiteiraselecionada().getNomeFantasia());

        colaborador = (EditText) findViewById(R.id.colaborador);
        colaborador.setText(usuarioGlobal.getColaboradorselecionado().getNome());

        spinnerServicos = (Spinner) findViewById(R.id.servico);

        codigoservico = (EditText) findViewById(R.id.codigoservico);

        unidademedida = (EditText) findViewById(R.id.unidademedida);

        codigoservico.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code
                buscarServicos();

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        spinnerServicos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //busca unidade de medida do servico
                unidademedida.setText(buscaUnidadeMedida());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhes_producao, menu);
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

    public ArrayList<String> populaSpinnerResultado(Cursor c){
        ArrayList<String> s = new ArrayList<>();
        c.moveToFirst();
        for(int i=0; i<c.getCount();i++){
            s.add(c.getString(0));
            c.moveToNext();
        }

        return s;
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

    public List<Orcservico> servicosLista(Cursor c){
        List<Orcservico> s = new ArrayList<>();
        c.moveToFirst();
        Orcservico ser = new Orcservico();
        for(int i=0; i<c.getCount();i++){
            s.add((Orcservico) consultarPorId(ser,c.getString(0)));
            c.moveToNext();
        }
        return s;
    }

    public List<Orcservico> listaServicosBusca(){
        EditText codigo = (EditText) findViewById(R.id.codigoservico);
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("SELECT ser.id FROM Engcontratoservicoempreiteira ecse " +
                " inner join Orcservico ser on ser.id = ecse.fkIdServico " +
                " where ecse.fkIdSubprojeto = "+usuarioGlobal.getSubprojetoselecionado().getId().toString()+" " +
                " and ecse.fkIdAtividade = "+usuarioGlobal.getAtividadeselecionada().getId().toString()+" " +
                " and ser.codigo like '%"+codigo.getText()+"%'", null);
        return servicosLista(c);
    }

    public void buscarServicos(){
        //carrega spinner de servicos
        ArrayAdapter<Orcservico> adapter = new ArrayAdapter<Orcservico>(getApplicationContext(), android.R.layout.simple_spinner_item, listaServicosBusca());
        spinnerServicos.setAdapter(adapter);
    }

    public String buscaUnidadeMedida(){
        Orcservico servicoaux = (Orcservico) spinnerServicos.getSelectedItem();
        Cursor c = db.rawQuery("SELECT um.nome FROM Orcunidademedida um " +
                " inner join Orcservico ser on um.id = ser.fkIdUnidadeMedida " +
                " where ser.id = "+servicoaux.getId(), null);

        if(c.moveToNext()){
            c.moveToFirst();
            return c.getString(0);
        }
        return "";
    }
}
