package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.example.impactit.bridgeengenharia.entidades.Sisfuncao;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.net.URLClassLoader;
import java.lang.Object;


public class LoginActivity extends Activity {

    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btLogin = (Button) findViewById(R.id.btLogin);


        //Spinner sp = (Spinner) findViewById(R.id.spinner);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,consultaUsuarios());
        //sp.setAdapter(adapter);

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
        Cursor c = db.rawQuery("SELECT nome FROM sisusuario where login ='" + login + "' and senha='" + senha + "' ORDER BY nome", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getString(0);
        }
        return "";
    }

    private void sincronizar() {
        new Sincronizador().execute();
    }

    public void consultar(Class classe) {
        Cursor c = db.rawQuery("SELECT * " +
                "FROM " + classe.getSimpleName().toLowerCase(), null);
        String s = "";
        while (c.moveToNext()) {
            for(int i=0;i<c.getColumnCount();i++){
                s+= c.getString(i)+" ";
            }
            //s += c.getString(0) + "-" + c.getString(1) + " "+ c.getString(5) + " ";
        }
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public ArrayList consultaUsuarios() {
        Cursor c = db.rawQuery("SELECT nome,id FROM sisfuncao ORDER BY nome", null);
        ArrayList<String> usuarios = new ArrayList<String>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (c.moveToNext()) {
                usuarios.add(c.getString(0));
            }
        }

        return usuarios;
    }


    public void login(View view) {
        //consultar(Engobra.class);

        EditText edtlogin = (EditText) findViewById(R.id.login);
        EditText edtsenha = (EditText) findViewById(R.id.senha);
        if((!edtlogin.getText().toString().equals(""))&&(!edtsenha.getText().toString().equals(""))) {
            String nome = loginUsuario((String) edtlogin.getText().toString(), (String) edtsenha.getText().toString());

            if(!"".equals(nome)) {
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                //intent.putExtra("usuario", nome);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Login e/ou senha incorretos", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Preencha o login e senha", Toast.LENGTH_LONG).show();
        }


    }


    public static SQLiteDatabase db;

    private void criaBanco() {
        try {
            db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);
            criaTabelasBanco();

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
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
        }


        public JSONArray consultaGenerica(Class classe) {

            try {
                DefaultHttpClient dhc = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://192.168.1.8:8080/" + classe.getSimpleName().toLowerCase());
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
                System.out.println(obj.getClass().toString());

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
                db.execSQL("delete from " + s);
            }

            for (String s : getClasses()) {
                Class<?> aClass = Class.forName("com.example.impactit.bridgeengenharia.entidades." + s);
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE if not exists " + s + "(");
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

                //System.out.println(sb.toString());

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