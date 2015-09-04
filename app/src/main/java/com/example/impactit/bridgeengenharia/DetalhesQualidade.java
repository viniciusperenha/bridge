package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.AdapterQualidade;
import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.EngItemVerificacaoServico;
import com.example.impactit.bridgeengenharia.entidades.EngOcorrenciaNaoPlanejada;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DetalhesQualidade extends ActionBarActivity {
    private static SQLiteDatabase db;
    private EditText subprojeto;
    private EditText atividade;
    private EditText pavimento;
    private EditText tarefa;
    private EditText servico;
    private EditText elementoproducao;
    private EditText totalproduzido;
    private EditText observacao;
    private EditText setor;
    private EditText empreiteira;
    private EditText colaborador;
    private ListView listaApontamentosProducao;
    private ArrayList<QualidadeTO> itemsqualidade = new ArrayList<>();
    private Engempreiteira emp;
    private Plasetorprojeto se;
    private Plasubprojeto sub;
    private Plasubprojetosetorprojeto subsetor;
    private Plaatividade ati;
    private Plapavimentosubprojeto pav;
    private Platarefa tar;
    private Orcservico ser;
    private Orcunidademedida um;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(usuarioGlobal.estiloSelecionado>0) {
            setTheme(usuarioGlobal.estiloSelecionado);
        }
        setContentView(R.layout.activity_detalhes_qualidade);
        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);



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


        emp = ultimaProducaoEmpreiteira(usuarioGlobal.getElementoproducaoselecionado());
        empreiteira.setText(emp.getNomeFantasia());

        se = new Plasetorprojeto();
        se = (Plasetorprojeto) consultarPorId(se, usuarioGlobal.getElementoproducaoselecionado().getFkIdSetorProjeto().toString());
        setor.setText(se.getNome());

        sub = new Plasubprojeto();
        subsetor = new Plasubprojetosetorprojeto();
        subsetor = (Plasubprojetosetorprojeto) consultarPorId(subsetor, usuarioGlobal.getElementoproducaoselecionado().getFkIdSubprojetoSetorProjeto().toString());
        sub = (Plasubprojeto) consultarPorId(sub, subsetor.getFkIdSubprojeto().toString());
        subprojeto.setText(sub.getDescricao());

        ati = new Plaatividade();
        ati = (Plaatividade) consultarPorId(ati, usuarioGlobal.getElementoproducaoselecionado().getFkIdAtividade().toString());
        atividade.setText(ati.getNome());

        pav = buscaPavimentoSubProjeto(subsetor, se,usuarioGlobal.getElementoproducaoselecionado());
        pavimento.setText(pav.getNome());

        tar = new Platarefa();
        tar = (Platarefa) consultarPorId(tar, usuarioGlobal.getElementoproducaoselecionado().getFkIdTarefa().toString());
        tarefa.setText(tar.getNome());

        ser = new Orcservico();
        ser = (Orcservico) consultarPorId(ser, usuarioGlobal.getElementoproducaoselecionado().getFkIdServico().toString());
        servico.setText(ser.getNome());

        um = new Orcunidademedida();
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
    public void onBackPressed() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Deseja cancelar o apontamento?");
        alertbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // finish used for destroyed activity
                db.close();
                DetalhesQualidade.this.finish();
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

                if(qualidadeaux.getEngVerificacaoQualidadeServico()!=null) {
                    if(qualidadeaux.getEngVerificacaoQualidadeServico().getStatus()!=null) {
                        if (qualidadeaux.getEngVerificacaoQualidadeServico().getStatus().equals("AP")) {
                            qualidadeaux.setAp(true);
                        }
                        if (qualidadeaux.getEngVerificacaoQualidadeServico().getStatus().equals("RP")) {
                            qualidadeaux.setRp(true);
                        }
                        if (qualidadeaux.getEngVerificacaoQualidadeServico().getStatus().equals("ACR")) {
                            qualidadeaux.setAcr(true);
                            qualidadeaux.setRp(true);
                        }
                        if (qualidadeaux.getEngVerificacaoQualidadeServico().getStatus().equals("ASR")) {
                            qualidadeaux.setAsr(true);
                            qualidadeaux.setRp(true);
                        }
                    }
                }
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

    public void gravarQualidade(View v){
        observacao = (EditText) findViewById(R.id.observacoes);
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        for(QualidadeTO qto:itemsqualidade){
            //verifica se há registro
            if(qto.getEngVerificacaoQualidadeServico()==null){
                String status="";
                if(qto.isAp()){
                    status = "AP";
                }
                if(qto.isRp()){
                    status = "RP";
                }
                if(qto.isAcr()){
                    status = "ACR";
                }
                if(qto.isAsr()){
                    status = "ASR";
                }
                if(!"".equals(status)) { //verifica se foi apontado algo



                    EngVerificacaoQualidadeServico engVerificacaoQualidadeServico = new EngVerificacaoQualidadeServico();
                    //dados tela
                    engVerificacaoQualidadeServico.setDataInspecao(new Date());
                    engVerificacaoQualidadeServico.setStatus(status);
                    engVerificacaoQualidadeServico.setObservacao(observacao.getText().toString());
                    //preenche fks
                    engVerificacaoQualidadeServico.setFkIdItemVerificacaoServico(qto.getEngItemVerificacaoServico().getId());
                    engVerificacaoQualidadeServico.setFkIdElementoProducao(usuarioGlobal.getElementoproducaoselecionado().getId());
                    engVerificacaoQualidadeServico.setFkIdPavimentoSubprojeto(pav.getId());
                    engVerificacaoQualidadeServico.setFkIdSubprojetoSetorProjeto(subsetor.getId());
                    engVerificacaoQualidadeServico.setFkIdSetorProjeto(se.getId());
                    engVerificacaoQualidadeServico.setFkIdAtividade(ati.getId());
                    engVerificacaoQualidadeServico.setFkIdConferente(usuarioGlobal.getUsuarioLogado().getId());
                    engVerificacaoQualidadeServico.setFkIdObra(usuarioGlobal.getObraselecionada().getId());
                    engVerificacaoQualidadeServico.setFkIdServico(ser.getId());
                    engVerificacaoQualidadeServico.setFkIdTarefa(tar.getId());
                    engVerificacaoQualidadeServico.setId(buscaUltimoId(engVerificacaoQualidadeServico.getClass()));
                    engVerificacaoQualidadeServico.setTransmitir(true);
                    inserir(engVerificacaoQualidadeServico);
                }
            } else {
                String status="";
                if(qto.isAp()){
                    status = "AP";
                }
                if(qto.isRp()){
                    status = "RP";
                }
                if(qto.isAcr()){
                    status = "ACR";
                }
                if(qto.isAsr()){
                    status = "ASR";
                }
                if(qto.getEngVerificacaoQualidadeServico().getStatus()!=null) {
                    //foi alterado o status
                    if (!qto.getEngVerificacaoQualidadeServico().getStatus().equals(status)) {
                        ContentValues cv = new ContentValues();
                        cv.put("status", status);
                        cv.put("observacao", observacao.getText().toString());
                        cv.put("transmitir", true);
                        db.update("EngVerificacaoQualidadeServico", cv, "id=" + qto.getEngVerificacaoQualidadeServico().getId(), null);
                    }
                } else{
                    ContentValues cv = new ContentValues();
                    cv.put("status", status);
                    cv.put("observacao", observacao.getText().toString());
                    cv.put("dataAlteracaoInspecao",String.valueOf(new Date()));
                    cv.put("transmitir", true);
                    db.update("EngVerificacaoQualidadeServico", cv, "id=" + qto.getEngVerificacaoQualidadeServico().getId(), null);
                }
            }

        }
        if(usuarioGlobal.checkConexaoInternet(getApplicationContext())) {
            sincronizar();
        } else {
            db.close();
            Toast.makeText(getApplicationContext(), "Qualidade apontada com sucesso!", Toast.LENGTH_LONG).show();
            DetalhesQualidade.this.finish();
        }

    }

    public Long buscaUltimoId(Class classe){
        Cursor c = db.rawQuery("Select id from "+classe.getSimpleName()+" order by id desc limit 1",null);
        if(c.moveToNext()){
            return c.getLong(0)+1;
        }
        return 1l;
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

    private void sincronizar() {
        new Sincronizador().execute();
    }

    class Sincronizador extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                publishProgress("Conectando...");

                String s = "Select * from Engproducao where transmitir != '' and transmitir is not null";
                //conexao com banco de dados

                Cursor c = db.rawQuery(s,null);
                if(c.moveToFirst()){
                    enviaGenerico(new Engproducao().getClass(), c);
                }
                c.close();

                s = "Select * from EngVerificacaoQualidadeServico where transmitir != '' and transmitir is not null";
                c = db.rawQuery(s,null);
                if(c.moveToFirst()){
                    enviaGenerico(new EngVerificacaoQualidadeServico().getClass(), c);
                }
                c.close();

                s = "Select * from EngOcorrenciaNaoPlanejada where transmitir != '' and transmitir is not null";
                c = db.rawQuery(s,null);
                if(c.moveToFirst()){
                    enviaGenerico(new EngOcorrenciaNaoPlanejada().getClass(), c);
                }

                publishProgress("Sincronizado com sucesso!");
            } catch (Exception e) {
                System.out.println(e.toString());
                publishProgress(e.toString());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            return;
        }

        @Override
        protected void onPostExecute(String s) {
            db.close();
            Toast.makeText(getApplicationContext(), "Qualidade apontada com sucesso!", Toast.LENGTH_LONG).show();
            DetalhesQualidade.this.finish();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }


        public void enviaGenerico(Class classe, Cursor c) {

            try {
                if(c.moveToFirst()) {
                    int qt = 0;
                    for(int j=0;j<c.getCount();j++) {
                        HttpResponse response;
                        HttpClient client = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://192.168.25.2:8080/" + classe.getSimpleName().toLowerCase());
                        httpPost.setHeader("Content-Type", "application/json");
                        String json = "{";
                        for (int i = 0; i < c.getColumnCount(); i++) {

                            if(classe.getSimpleName().toLowerCase().equals("engocorrencianaoplanejada")) {
                                if ((!c.getColumnName(i).equals("transmitir")) && (!c.getColumnName(i).equals("id"))) {
                                    if (c.getColumnName(i).startsWith("data")) {
                                        SimpleDateFormat sdf2 = new SimpleDateFormat("E MMM dd HH:mm:ss zzzz yyyy", Locale.US);
                                        String dataparse = c.getString(i);
                                        dataparse = dataparse.replace("BRT", "-0300");
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
                                        Date parse = sdf2.parse(dataparse);
                                        json += "\"" + c.getColumnName(i) + "\":\"" + sdf.format(parse) + " \",";
                                        continue;
                                    }
                                    json += "\"" + c.getColumnName(i) + "\":\"" + c.getString(i) + "\",";
                                }
                            } else {
                                if (!c.getColumnName(i).equals("transmitir")) {
                                    if (c.getColumnName(i).startsWith("data")) {

                                        try {

                                            if(c.getString(i).length()>23) {

                                                SimpleDateFormat sdf2 = new SimpleDateFormat("E MMM dd HH:mm:ss zzzz yyyy", Locale.US);
                                                String dataparse = c.getString(i);
                                                dataparse = dataparse.replace("BRT", "-0300");
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
                                                Date parse = sdf2.parse(dataparse);
                                                json += "\"" + c.getColumnName(i) + "\":\"" + sdf.format(parse) + " \",";
                                            } else {

                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.US);
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
                                                Date parse = sdf2.parse(c.getString(i));
                                                json += "\"" + c.getColumnName(i) + "\":\"" + sdf.format(parse) + " \",";
                                            }
                                        }catch (Exception e){
                                            System.out.println(e.toString());
                                        }


                                        continue;
                                    }
                                    json += "\"" + c.getColumnName(i) + "\":\"" + c.getString(i) + "\",";
                                }
                            }
                        }
                        json = json.substring(0, json.length() - 1);
                        json += "}";
                        System.out.println("-----------------------------------"+json);
                        httpPost.setEntity(new StringEntity(json));
                        //verificar status se enviado editar
                        response = client.execute(httpPost);
                        System.out.println(response.getStatusLine().getStatusCode());
                        if(response.getStatusLine().getStatusCode()==201){
                            ContentValues cv = new ContentValues();
                            cv.put("transmitir", "");
                            db.update(classe.getSimpleName().toLowerCase(), cv, "id=" + String.valueOf(c.getLong(c.getColumnIndexOrThrow("id"))), null);
                        }
                        qt++;
                        c.moveToNext();
                    }
                    publishProgress(classe.getSimpleName().toLowerCase()+" enviadas: "+qt);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
