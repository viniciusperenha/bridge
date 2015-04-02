package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.content.ContentValues;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Empempresa;
import com.example.impactit.bridgeengenharia.entidades.Engcolaboradorobra;
import com.example.impactit.bridgeengenharia.entidades.Engcontratoempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engcontratoservicoempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcpavimentoelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Orcunidademedida;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentoprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Platarefa;
import com.example.impactit.bridgeengenharia.entidades.Platipopavimento;
import com.example.impactit.bridgeengenharia.entidades.Rhcargo;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;

import com.example.impactit.bridgeengenharia.entidades.Sisfuncao;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import java.lang.Object;
import java.util.TimeZone;


public class LoginActivity extends Activity {

    private Button btLogin;
    private Spinner spUsuarios;
    public static SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btLogin = (Button) findViewById(R.id.btLogin);


        spUsuarios = (Spinner) findViewById(R.id.spinnerLogin);

        try{
            db = SQLiteDatabase.openDatabase("bridge", null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }

        if(db!=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, consultaUsuarios());
            spUsuarios.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //clicou no botao sincronizar
        if (id == R.id.action_settings) {

            sincronizar();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String loginUsuario(String login, String senha) {
        Cursor c = db.rawQuery("SELECT id FROM sisusuario where login ='" + login + "' and senha='" + senha + "' ORDER BY nome", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getString(0);
        }
        return "";
    }

    private void sincronizar() {
        new Sincronizador().execute();
    }



    public ArrayList consultaUsuarios() {
        Cursor c = db.rawQuery("SELECT login FROM sisusuario ORDER BY login", null);
        ArrayList<String> usuarios = new ArrayList<String>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            usuarios.add(c.getString(0));
            while (c.moveToNext()) {
                usuarios.add(c.getString(0));
            }

        }
        return usuarios;

    }


    public void login(View view) {

        if(spUsuarios.getCount()>0) {

            EditText edtsenha = (EditText) findViewById(R.id.senha);
            if ((!spUsuarios.getSelectedItem().toString().equals("")) && (!edtsenha.getText().toString().equals(""))) {
                String id = loginUsuario((String) spUsuarios.getSelectedItem().toString(), (String) edtsenha.getText().toString());

                if (!"".equals(id)) {
                    final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
                    Sisusuario usu = new Sisusuario();
                    usu = (Sisusuario) consultarPorId(usu, id);

                    Toast.makeText(getApplicationContext(), usu.getNome(), Toast.LENGTH_LONG).show();

                    usuarioGlobal.setUsuarioLogado((Sisusuario) consultarPorId(usu, id));
                    Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                    //intent.putExtra("usuario", id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login e/ou senha incorretos", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Preencha o login e senha", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Banco de dados vazio, por favor sincronize.", Toast.LENGTH_LONG).show();
        }
    }




    private void criaBanco() {
        try {
            db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    class Sincronizador extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                publishProgress("Criando banco de dados...");
                criaBanco();
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

                //insere Plapavimentoprojeto
                lista = new JSONArray();
                lista = consultaGenerica(Plapavimentoprojeto.class);
                gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSz").create();
                listType = new TypeToken<List<Plapavimentoprojeto>>() {
                }.getType();
                inserir((List<Plapavimentoprojeto>) gson.fromJson(lista.toString(), listType));

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



                publishProgress("Sincronizado com sucesso!");
            } catch (Exception e) {
                System.out.println(e.toString());
                publishProgress(e.toString());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            btLogin.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {

            btLogin.setEnabled(true);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, consultaUsuarios());

            spUsuarios.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }


        public JSONArray consultaGenerica(Class classe) {

            try {
                DefaultHttpClient dhc = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://192.168.25.2:8080/" + classe.getSimpleName().toLowerCase());
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
                               System.out.println(c.getLong(i));
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
        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
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

                System.out.println(sb.toString());

                db.execSQL(sb.toString());


            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public String[] getClasses() {
        return new String[]{"Empempresa", "Orcpavimentoelementoproducao", "Platarefa", "Engcolaboradorobra", "Orcservico", "Platipopavimento", "Engcontratoempreiteira", "Orcunidademedida", "Rhcargo", "Engcontratoservicoempreiteira", "Plaatividade", "Rhcolaborador", "Engempreiteira", "Plapavimentoprojeto", "Sisfuncao", "Engobra", "Plaprojeto", "Sisusuario", "Orcelementoproducao", "Plasubprojeto"};
    }

}