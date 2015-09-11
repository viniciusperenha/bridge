package com.example.impactit.bridgeengenharia.controle;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.LoginActivity;
import com.example.impactit.bridgeengenharia.R;
import com.example.impactit.bridgeengenharia.entidades.Empempresa;
import com.example.impactit.bridgeengenharia.entidades.EngInstrucaoQualidadeServico;
import com.example.impactit.bridgeengenharia.entidades.EngItemVerificacaoServico;
import com.example.impactit.bridgeengenharia.entidades.EngOcorrenciaNaoPlanejada;
import com.example.impactit.bridgeengenharia.entidades.EngVerificacaoQualidadeServico;
import com.example.impactit.bridgeengenharia.entidades.Engcolaboradorobra;
import com.example.impactit.bridgeengenharia.entidades.Engcontratoempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engcontratoservicoempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcpavimentoelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Orcunidademedida;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentosubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojetosetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Platarefa;
import com.example.impactit.bridgeengenharia.entidades.Platipopavimento;
import com.example.impactit.bridgeengenharia.entidades.Rhcargo;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.example.impactit.bridgeengenharia.entidades.Sisfuncao;
import com.example.impactit.bridgeengenharia.entidades.Sisparametro;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vinicius on 10/09/15.
 */
public class SincronizarInicialAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private SQLiteDatabase db;
    public ProgressDialog mDialog;
    public Spinner sp;
    public final String servidor;

    public SincronizarInicialAsyncTask(Context context, SQLiteDatabase db, Spinner sp, String servidor) {
        this.context = context;
        this.db = db;
        this.sp = sp;
        this.servidor = servidor;
    }

    public String[] getClasses() {
        return new String[]{"Empempresa", "Orcpavimentoelementoproducao", "Platarefa",
                "Engcolaboradorobra", "Orcservico", "Platipopavimento",
                "Engcontratoempreiteira", "Orcunidademedida", "Rhcargo",
                "Engcontratoservicoempreiteira", "Plaatividade", "Rhcolaborador",
                "Engempreiteira", "Sisfuncao",
                "Engobra", "Plaprojeto", "Sisusuario", "Orcelementoproducao",
                "Plasubprojeto", "Sisparametro", "Engproducao", "Plapavimentosubprojeto",
                "Plasetorprojeto", "Plasubprojetosetorprojeto", "EngInstrucaoQualidadeServico",
                "EngVerificacaoQualidadeServico", "EngItemVerificacaoServico", "EngOcorrenciaNaoPlanejada"
        };
    }

    public void inserir(List l) {
        if (l.isEmpty()) {
            return;
        }
        try {

            for (Object obj : l) {
                //System.out.println(obj.getClass().toString());

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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void deletaTabelasBanco(){
        try {
            for (String s : getClasses()) {
                db.execSQL("DROP TABLE IF EXISTS " + s.toLowerCase());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String tipoSql(Class tipo){

        if (tipo.equals(String.class)) return "varchar(100)";
        if (tipo.equals(Character.class)) return "char(1)";
        if (tipo.equals(Date.class)) return "date";
        if (tipo.equals(Double.class)) return "double(7,2)";
        if (tipo.equals(double.class)) return "double(7,2)";
        if (tipo.equals(BigInteger.class)) return "bigint";
        if (tipo.equals(Integer.class)) return "int";

        //return tipo.toString();
        return "long";
    }

    public void criaTabelasBanco()  {
        try {

            for (String s : getClasses()) {
                Class<?> aClass = Class.forName("com.example.impactit.bridgeengenharia.entidades." + s);
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE if not exists " + s.toLowerCase() + "(");
                for (Field f : aClass.getDeclaredFields()) {
                    Class<?> type = f.getType();
                    sb.append(f.getName() + " " + tipoSql(type));
                    if (f.getName().equals("id")) {
                        sb.append(" NOT NULL PRIMARY KEY");
                    }
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                sb.append(")");

                db.execSQL(sb.toString());

            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            publishProgress("Criando banco de dados...");
            deletaTabelasBanco();
            criaTabelasBanco();

            publishProgress("Sincronizando...");

            //insere sisfuncao
            JSONArray lista = new JSONArray();
            lista = consultaGenerica(Sisfuncao.class);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Sisfuncao>>() {
            }.getType();
            inserir((List<Sisfuncao>) gson.fromJson(lista.toString(), listType));

            //insere sisusuario
            lista = new JSONArray();
            lista = consultaGenerica(Sisusuario.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Sisusuario>>() {
            }.getType();
            inserir((List<Sisusuario>) gson.fromJson(lista.toString(), listType));

            //insere empempresa
            lista = new JSONArray();
            lista = consultaGenerica(Empempresa.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Empempresa>>() {
            }.getType();
            inserir((List<Empempresa>) gson.fromJson(lista.toString(), listType));

            //insere Orcpavimentoelementoproducao
            lista = new JSONArray();
            lista = consultaGenerica(Orcpavimentoelementoproducao.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Orcpavimentoelementoproducao>>() {
            }.getType();
            inserir((List<Orcpavimentoelementoproducao>) gson.fromJson(lista.toString(), listType));

            //insere Platarefa
            lista = new JSONArray();
            lista = consultaGenerica(Platarefa.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Platarefa>>() {
            }.getType();
            inserir((List<Platarefa>) gson.fromJson(lista.toString(), listType));

            //insere Engcolaboradorobra
            lista = new JSONArray();
            lista = consultaGenerica(Engcolaboradorobra.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Engcolaboradorobra>>() {
            }.getType();
            inserir((List<Engcolaboradorobra>) gson.fromJson(lista.toString(), listType));


            //insere Orcservico
            lista = new JSONArray();
            lista = consultaGenerica(Orcservico.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Orcservico>>() {
            }.getType();
            inserir((List<Orcservico>) gson.fromJson(lista.toString(), listType));

            //insere Platipopavimento
            lista = new JSONArray();
            lista = consultaGenerica(Platipopavimento.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Platipopavimento>>() {
            }.getType();
            inserir((List<Platipopavimento>) gson.fromJson(lista.toString(), listType));


            //insere Engcontratoempreiteira
            lista = new JSONArray();
            lista = consultaGenerica(Engcontratoempreiteira.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Engcontratoempreiteira>>() {
            }.getType();
            inserir((List<Engcontratoempreiteira>) gson.fromJson(lista.toString(), listType));

            //insere Orcunidademedida
            lista = new JSONArray();
            lista = consultaGenerica(Orcunidademedida.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Orcunidademedida>>() {
            }.getType();
            inserir((List<Orcunidademedida>) gson.fromJson(lista.toString(), listType));

            //insere Rhcargo
            lista = new JSONArray();
            lista = consultaGenerica(Rhcargo.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Rhcargo>>() {
            }.getType();
            inserir((List<Rhcargo>) gson.fromJson(lista.toString(), listType));

            //insere Engcontratoservicoempreiteira
            lista = new JSONArray();
            lista = consultaGenerica(Engcontratoservicoempreiteira.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Engcontratoservicoempreiteira>>() {
            }.getType();
            inserir((List<Engcontratoservicoempreiteira>) gson.fromJson(lista.toString(), listType));

            //insere Plaatividade
            lista = new JSONArray();
            lista = consultaGenerica(Plaatividade.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Plaatividade>>() {
            }.getType();
            inserir((List<Plaatividade>) gson.fromJson(lista.toString(), listType));

            //insere Rhcolaborador
            lista = new JSONArray();
            lista = consultaGenerica(Rhcolaborador.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Rhcolaborador>>() {
            }.getType();
            inserir((List<Rhcolaborador>) gson.fromJson(lista.toString(), listType));

            //insere Engempreiteira
            lista = new JSONArray();
            lista = consultaGenerica(Engempreiteira.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Engempreiteira>>() {
            }.getType();
            inserir((List<Engempreiteira>) gson.fromJson(lista.toString(), listType));



            //insere Engobra
            lista = new JSONArray();
            lista = consultaGenerica(Engobra.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Engobra>>() {
            }.getType();
            inserir((List<Engobra>) gson.fromJson(lista.toString(), listType));

            //insere Plaprojeto
            lista = new JSONArray();
            lista = consultaGenerica(Plaprojeto.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Plaprojeto>>() {
            }.getType();
            inserir((List<Plaprojeto>) gson.fromJson(lista.toString(), listType));

            //insere Orcelementoproducao
            lista = new JSONArray();
            lista = consultaGenerica(Orcelementoproducao.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Orcelementoproducao>>() {
            }.getType();
            inserir((List<Orcelementoproducao>) gson.fromJson(lista.toString(), listType));

            //insere Plasubprojeto
            lista = new JSONArray();
            lista = consultaGenerica(Plasubprojeto.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Plasubprojeto>>() {
            }.getType();
            inserir((List<Plasubprojeto>) gson.fromJson(lista.toString(), listType));

            //insere Sisparametro
            lista = new JSONArray();
            lista = consultaGenerica(Sisparametro.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Sisparametro>>() {
            }.getType();
            inserir((List<Sisparametro>) gson.fromJson(lista.toString(), listType));

            //insere engproducao
            lista = new JSONArray();
            lista = consultaGenerica(Engproducao.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Engproducao>>() {
            }.getType();
            inserir((List<Engproducao>) gson.fromJson(lista.toString(), listType));

            //insere plapavimentosubprojeto
            lista = new JSONArray();
            lista = consultaGenerica(Plapavimentosubprojeto.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Plapavimentosubprojeto>>() {
            }.getType();
            inserir((List<Plapavimentosubprojeto>) gson.fromJson(lista.toString(), listType));

            //insere plasetorprojeto
            lista = new JSONArray();
            lista = consultaGenerica(Plasetorprojeto.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Plasetorprojeto>>() {
            }.getType();
            inserir((List<Plasetorprojeto>) gson.fromJson(lista.toString(), listType));

            //insere Plasubprojetosetorprojeto
            lista = new JSONArray();
            lista = consultaGenerica(Plasubprojetosetorprojeto.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<Plasubprojetosetorprojeto>>() {
            }.getType();
            inserir((List<Plasubprojetosetorprojeto>) gson.fromJson(lista.toString(), listType));

            //insere EngInstrucaoQualidadeServico
            lista = new JSONArray();
            lista = consultaGenerica(EngInstrucaoQualidadeServico.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<EngInstrucaoQualidadeServico>>() {
            }.getType();
            inserir((List<EngInstrucaoQualidadeServico>) gson.fromJson(lista.toString(), listType));

            //insere EngVerificacaoQualidadeServico
            lista = new JSONArray();
            lista = consultaGenerica(EngVerificacaoQualidadeServico.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<EngVerificacaoQualidadeServico>>() {
            }.getType();
            inserir((List<EngVerificacaoQualidadeServico>) gson.fromJson(lista.toString(), listType));

            //insere EngItemVerificacaoServico
            lista = new JSONArray();
            lista = consultaGenerica(EngItemVerificacaoServico.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<EngItemVerificacaoServico>>() {
            }.getType();
            inserir((List<EngItemVerificacaoServico>) gson.fromJson(lista.toString(), listType));

            //insere EngOcorrenciaNaoPlanejada
            lista = new JSONArray();
            lista = consultaGenerica(EngOcorrenciaNaoPlanejada.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
            listType = new TypeToken<List<EngOcorrenciaNaoPlanejada>>() {
            }.getType();
            inserir((List<EngOcorrenciaNaoPlanejada>) gson.fromJson(lista.toString(), listType));

            publishProgress("Sincronizado com sucesso!");

            Toast.makeText(context, "Sincronizado com sucesso!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            publishProgress(e.toString());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("Aguarde...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        mDialog.dismiss();
        if(sp!=null) {
            ArrayAdapter<Sisusuario> adapter = new ArrayAdapter<Sisusuario>(context, R.layout.spinner_item, consultaUsuarios());
            adapter.setDropDownViewResource(R.layout.item_lista);
            sp.setAdapter(adapter);
        }
    }

    public Object consultarPorId(Object obj, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + obj.getClass().getSimpleName().toLowerCase()+" where id = "+id, null);

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
                            f.set(obj, parse);                           }
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
                        if (f.getType().equals(Integer.class)) {
                            f.set(obj, Integer.parseInt(c.getString(i)));
                        }
                        if (f.getType().equals(int.class)) {
                            f.set(obj, Integer.parseInt(c.getString(i)));
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                s+= c.getColumnName(i)+" - "+c.getString(i)+"   ";
            }

        }
        return obj;
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public List<Sisusuario> consultaUsuarios() {

        ArrayList<Sisusuario> lista = new ArrayList<Sisusuario>();

            Cursor c = db.rawQuery("SELECT id FROM sisusuario ORDER BY login", null);
            ArrayList<String> usuarios = new ArrayList<String>();
            if ( c.moveToFirst()) {

                while (c.moveToNext()) {

                    lista.add((Sisusuario) consultarPorId(new Sisusuario(), String.valueOf(c.getLong(0))));
                }
                return lista;
            }

        return lista;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mDialog.setMessage(values[0]);
    }


    public JSONArray consultaGenerica(Class classe) {

        try {
            DefaultHttpClient dhc = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(servidor + classe.getSimpleName().toLowerCase());
            HttpResponse resposta = null;
            resposta = dhc.execute(httpGet);
            String res = EntityUtils.toString(resposta.getEntity());
            JSONObject jso = new JSONObject(res);
            JSONArray lista = jso.optJSONArray("content");

            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}



