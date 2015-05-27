package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojetosetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Platarefa;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Timestamp;
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
    public Spinner tarefa;
    public Spinner spinnerServicos;
    public EditText codigoservico;
    public EditText unidademedida;
    public Spinner spinnerelementoproducao;
    public EditText totalproduzido;
    public EditText producao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.impactit.bridgeengenharia.R.layout.activity_detalhes_producao);

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        //carrega campos
        subprojeto = (EditText) findViewById(R.id.subprojeto);
        subprojeto.setText(usuarioGlobal.getSubprojetoselecionado().getDescricao());

        atividade = (EditText) findViewById(R.id.atividade);
        atividade.setText(usuarioGlobal.getAtividadeselecionada().getNome());

        pavimento = (EditText) findViewById(R.id.pavimento);
        pavimento.setText(usuarioGlobal.getPavimentosubprojetoprojetoselecionado().getNome());

        empreiteira = (EditText) findViewById(R.id.empreiteira);
        empreiteira.setText(usuarioGlobal.getEmpreiteiraselecionada().getNomeFantasia());

        colaborador = (EditText) findViewById(R.id.colaborador);
        colaborador.setText(usuarioGlobal.getColaboradorselecionado().getNome());

        tarefa = (Spinner) findViewById(R.id.tarefa);

        spinnerServicos = (Spinner) findViewById(R.id.servico);

        codigoservico = (EditText) findViewById(R.id.codigoservico);

        unidademedida = (EditText) findViewById(R.id.unidademedida);

        spinnerelementoproducao = (Spinner) findViewById(R.id.elementoproducao);

        totalproduzido = (EditText) findViewById(R.id.totalproduzido);

        producao = (EditText) findViewById(R.id.producao);

        ArrayAdapter<Platarefa> adapterTarefa = new ArrayAdapter<Platarefa>(getApplicationContext(), android.R.layout.simple_spinner_item, listaTarefa());
        adapterTarefa.setDropDownViewResource(R.layout.item_lista);
        tarefa.setAdapter(adapterTarefa);

        tarefa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //armazena tarefa
                usuarioGlobal.setTarefaselecionada((Platarefa) tarefa.getSelectedItem());

                //carrega servicos
                ArrayAdapter<Orcservico> adapter = new ArrayAdapter<Orcservico>(getApplicationContext(), android.R.layout.simple_spinner_item, listaServicosBusca());
                adapter.setDropDownViewResource(R.layout.item_lista);
                spinnerServicos.setAdapter(adapter);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        codigoservico.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //busca
                //carrega spinner de servicos
                ArrayAdapter<Orcservico> adapter = new ArrayAdapter<Orcservico>(getApplicationContext(), android.R.layout.simple_spinner_item, listaServicosBusca());
                adapter.setDropDownViewResource(R.layout.item_lista);
                spinnerServicos.setAdapter(adapter);

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

                //armazena servico
                usuarioGlobal.setServicoselecionado((Orcservico) spinnerServicos.getSelectedItem());

                ArrayAdapter<Orcelementoproducao> adapterElementoProducao = new ArrayAdapter<Orcelementoproducao>(getApplicationContext(), android.R.layout.simple_spinner_item, listaElementoProdutao());
                adapterElementoProducao.setDropDownViewResource(R.layout.item_lista);
                spinnerelementoproducao.setAdapter(adapterElementoProducao);

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spinnerelementoproducao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //armazena elementoproducao
                usuarioGlobal.setElementoproducaoselecionado((Orcelementoproducao) spinnerelementoproducao.getSelectedItem());

                //busca o total produzido
                totalproduzido.setText(String.valueOf(buscaTotalProduzido((Orcelementoproducao) spinnerelementoproducao.getSelectedItem())));

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

    @Override
    public void onBackPressed() {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Deseja cancelar o apontamento?");
            alertbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // finish used for destroyed activity
                    DetalhesProducao.this.finish();
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

    public List<Platarefa> listaTarefa(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("Select Distinct(tar.id) from Platarefa tar " +
                " where tar.fkIdAtividade = "+usuarioGlobal.getAtividadeselecionada().getId(),null);

        ArrayList<Platarefa> lista = new ArrayList<Platarefa>();
        if(c.getCount()>0){

            c.moveToFirst();

            for(int i=0; i<c.getCount();i++){
                Platarefa aux = new Platarefa();
                lista.add((Platarefa) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }

        }
        c.close();
        return lista;
    }

    public List<Orcservico> listaServicosBusca(){
        EditText codigo = (EditText) findViewById(R.id.codigoservico);
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c;
        if("".equals(codigo.getText())) {
            c = db.rawQuery("Select Distinct(ser.id) from Orcservico ser " +
                    " inner join Engcontratoservicoempreiteira cse on cse.fkIdServico = ser.id " +
                    " inner join Engcontratoempreiteira cemp on cemp.id = cse.fkIdContratoEmpreiteira " +
                    " where cemp.fkIdObra = " + usuarioGlobal.getObraselecionada().getId() + " " +
                    " and cse.fkIdSubprojeto = " + usuarioGlobal.getSubprojetoselecionado().getId() + " " +
                    " and cse.fkIdAtividade = " + usuarioGlobal.getAtividadeselecionada().getId() + " " +
                    " and cse.fkIdTarefa = " + usuarioGlobal.getTarefaselecionada().getId(), null);
        } else {
            c = db.rawQuery("Select Distinct(ser.id) from Orcservico ser " +
                    " inner join Engcontratoservicoempreiteira cse on cse.fkIdServico = ser.id " +
                    " inner join Engcontratoempreiteira cemp on cemp.id = cse.fkIdContratoEmpreiteira " +
                    " where cemp.fkIdObra = " + usuarioGlobal.getObraselecionada().getId() + " " +
                    " and cse.fkIdSubprojeto = " + usuarioGlobal.getSubprojetoselecionado().getId() + " " +
                    " and cse.fkIdAtividade = " + usuarioGlobal.getAtividadeselecionada().getId() + " " +
                    " and cse.fkIdTarefa = " + usuarioGlobal.getTarefaselecionada().getId() + " " +
                    " and ser.codigo like '%" + codigo.getText() + "%'", null);
        }
        ArrayList<Orcservico> lista = new ArrayList<Orcservico>();
        if(c.getCount()>0){

            c.moveToFirst();

            for(int i=0; i<c.getCount();i++){
                Orcservico aux = new Orcservico();
                lista.add((Orcservico) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }

        }
        c.close();
        return lista;
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
        c.close();
        return "";
    }

    public List<Orcelementoproducao> listaElementoProdutao(){
        GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();
        Orcservico ser = (Orcservico) spinnerServicos.getSelectedItem();
        Cursor c = db.rawQuery("Select Distinct(eco.id) from Orcelementoproducao eco " +
                " inner join Plasubprojetosetorprojeto ssp on ssp.id = eco.fkIdSubprojetoSetorProjeto " +
                " inner join Orcpavimentoelementoproducao opep on opep.fkIdElementoProducao = eco.id " +
                " where eco.fkIdAtividade = "+usuarioglobal.getAtividadeselecionada().getId()+" " +
                " and eco.fkIdProjeto = "+usuarioglobal.getProjetoselecionado().getId()+" " +
                " and ssp.fkIdSubprojeto = "+usuarioglobal.getSubprojetoselecionado().getId()+" " +
                " and eco.fkIdServico = "+ser.getId(),null);


        ArrayList<Orcelementoproducao> lista = new ArrayList<Orcelementoproducao>();
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                Orcelementoproducao aux = new Orcelementoproducao();
                lista.add((Orcelementoproducao) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;
    }

    public Double buscaTotalProduzido(Orcelementoproducao elementoproducao){
        GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();

        Cursor c = db.rawQuery("SELECT SUM(pro.quantidade) FROM Engproducao as pro  " +
                " inner join Plasubprojetosetorprojeto pssp on pssp.id = pro.fkIdSubprojetoSetorProjeto " +
                " where pro.fkIdObra = " + usuarioglobal.getObraselecionada().getId() + " " +
                " and pssp.fkIdSetorProjeto = " + usuarioglobal.getSetorprojetoselecionado().getId() + " " +
                " and pssp.fkIdSubprojeto = " + usuarioglobal.getSubprojetoselecionado().getId() + " " +
                " and pro.fkIdAtividade = " + usuarioglobal.getAtividadeselecionada().getId() + " " +
                " and pro.fkIdPavimentoSubprojeto = " + usuarioglobal.getPavimentosubprojetoprojetoselecionado().getId() + " " +
                " and pro.fkIdServico = " + usuarioglobal.getServicoselecionado().getId() + " " +
                " and pro.fkIdElementoProducao = " + usuarioglobal.getElementoproducaoselecionado().getId() + " " +
                " and pro.fkIdEmpreiteira = " + usuarioglobal.getEmpreiteiraselecionada().getId(), null);


        if(c.moveToNext()){
            c.moveToFirst();
            return c.getDouble(0);
        }
        c.close();
        return 0.00;
    }


    public void gravaApontamento(View view){
        if("".equals(producao.getText().toString())){
            Toast.makeText(getApplicationContext(), "Preencha a produção", Toast.LENGTH_LONG).show();
        } else {
            GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();
            //grava o apontamento
            Engproducao pro = new Engproducao();
            pro.setFkIdObra(usuarioglobal.getObraselecionada().getId());
            pro.setFkIdSetorProjeto(usuarioglobal.getSetorprojetoselecionado().getId());
            pro.setFkIdSubprojetoSetorProjeto(buscaSubprojetoSetorProjetoRetornaId(usuarioglobal.getSetorprojetoselecionado(), usuarioglobal.getSubprojetoselecionado()));
            pro.setFkIdAtividade(usuarioglobal.getAtividadeselecionada().getId());
            pro.setFkIdPavimentoSubprojeto(usuarioglobal.getPavimentosubprojetoprojetoselecionado().getId());
            pro.setFkIdServico(usuarioglobal.getServicoselecionado().getId());
            pro.setFkIdElementoProducao(usuarioglobal.getElementoproducaoselecionado().getId());
            pro.setFkIdEmpreiteira(usuarioglobal.getEmpreiteiraselecionada().getId());
            pro.setFkIdColaborador(usuarioglobal.getColaboradorselecionado().getId());
            pro.setData(new Timestamp(System.currentTimeMillis()));
            pro.setDataRegistro(new Timestamp(System.currentTimeMillis()));
            pro.setQuantidade(Double.parseDouble(producao.getText().toString()));
            pro.setFkIdTarefa(usuarioglobal.getServicoselecionado().getFkIdTarefa());

            //busca id
            pro.setId(buscaUltimoId(pro.getClass()));

            inserir(pro);
            Toast.makeText(getApplicationContext(), "Apontamento inserido com sucesso!", Toast.LENGTH_LONG).show();
            DetalhesProducao.this.finish();

        }
    }

    public Long buscaUltimoId(Class classe){
        Long id = 0L;
        Cursor c = db.rawQuery("Select id from "+classe.getSimpleName()+" order by id desc limit 1",null);
        if(c.moveToNext()){
            return id = c.getLong(0)+1;
        }
        return null;
    }

    public void cancelaApontamento(View view){
        onBackPressed();
    }

    public void gravaNovoApontamento(View view){
        if("".equals(producao.getText().toString())){
            Toast.makeText(getApplicationContext(), "Preencha a produção", Toast.LENGTH_LONG).show();
        } else {
            GlobalClass usuarioglobal = (GlobalClass) getApplicationContext();
            //grava o apontamento
            Engproducao pro = new Engproducao();
            pro.setFkIdObra(usuarioglobal.getObraselecionada().getId());
            pro.setFkIdSetorProjeto(usuarioglobal.getSetorprojetoselecionado().getId());
            pro.setFkIdSubprojetoSetorProjeto(buscaSubprojetoSetorProjetoRetornaId(usuarioglobal.getSetorprojetoselecionado(), usuarioglobal.getSubprojetoselecionado()));
            pro.setFkIdAtividade(usuarioglobal.getAtividadeselecionada().getId());
            pro.setFkIdPavimentoSubprojeto(usuarioglobal.getPavimentosubprojetoprojetoselecionado().getId());
            pro.setFkIdServico(usuarioglobal.getServicoselecionado().getId());
            pro.setFkIdElementoProducao(usuarioglobal.getElementoproducaoselecionado().getId());
            pro.setFkIdEmpreiteira(usuarioglobal.getEmpreiteiraselecionada().getId());
            pro.setFkIdColaborador(usuarioglobal.getColaboradorselecionado().getId());
            pro.setData(new Timestamp(System.currentTimeMillis()));
            pro.setDataRegistro(new Timestamp(System.currentTimeMillis()));
            pro.setQuantidade(Double.parseDouble(producao.getText().toString()));
            pro.setFkIdTarefa(usuarioglobal.getServicoselecionado().getFkIdTarefa());

            //busca id
            pro.setId(buscaUltimoId(pro.getClass()));

            inserir(pro);
            Toast.makeText(getApplicationContext(), "Apontamento inserido com sucesso!", Toast.LENGTH_LONG).show();
            producao.setText("");
            producao.requestFocus();
            //busca o total produzido
            totalproduzido.setText(String.valueOf(buscaTotalProduzido((Orcelementoproducao) spinnerelementoproducao.getSelectedItem())));

        }

    }

    public Long buscaSubprojetoSetorProjetoRetornaId(Plasetorprojeto setor, Plasubprojeto subprojeto){
        Cursor c = db.rawQuery("SELECT id FROM Plasubprojetosetorprojeto as pssp  " +
                " where pssp.fkIdSetorProjeto = " + setor.getId() +
                " and pssp.fkIdSubprojeto = " + subprojeto.getId(),null);

        if(c.moveToNext()){
            return c.getLong(0);
        }
        c.close();
        return null;

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


        }
        return obj;

    }

}
