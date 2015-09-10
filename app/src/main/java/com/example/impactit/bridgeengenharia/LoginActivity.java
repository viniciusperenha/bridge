package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
import com.example.impactit.bridgeengenharia.controle.SincronizarAsyncTask;
import com.example.impactit.bridgeengenharia.controle.SincronizarInicialAsyncTask;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;
import java.lang.reflect.Field;
import java.math.BigInteger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import java.lang.Object;
import java.util.Locale;


public class LoginActivity extends Activity {

    private Button btLogin;
    private Spinner spUsuarios;
    public static SQLiteDatabase db;
    public ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.estiloSelecionado=R.style.AppTheme3;
        setTheme(R.style.AppTheme3);

        btLogin = (Button) findViewById(R.id.btLogin);
        spUsuarios = (Spinner) findViewById(R.id.spinnerLogin);

        criaBanco();

        if(db!=null) {
            ArrayAdapter<Sisusuario> adapter = new ArrayAdapter<Sisusuario>(getApplicationContext(), R.layout.spinner_item, consultaUsuarios());
            adapter.setDropDownViewResource(R.layout.item_lista);
            spUsuarios.setAdapter(adapter);
        }


    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sincronizar();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Deseja sair?");
        alertbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // finish used for destroyed activity
                if(db!=null){
                    db.close();
                }
                finish();
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
            GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
            if(usuarioGlobal.checkConexaoInternet(getApplicationContext())) {
                sincronizar();
            } else {
                Toast.makeText(getApplicationContext(), "Sem conexão com a internet.", Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Sisusuario loginUsuario(String login, String senha) {
        //TODO:verificar codificacao hash de senha (md5)
        Cursor c = db.rawQuery("SELECT id FROM sisusuario where login ='" + login + "' and senha= '" + senha + "' ORDER BY nome", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return (Sisusuario) consultarPorId(new Sisusuario(), String.valueOf(c.getLong(0)));
        }
        return null;
    }

    private void sincronizar() {
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(!verificaTabelasBanco()){
            SincronizarInicialAsyncTask sincronizarInicialAsyncTask = new SincronizarInicialAsyncTask(LoginActivity.this, db, spUsuarios,usuarioGlobal.servidor) ;
            sincronizarInicialAsyncTask.execute();
            return;
        }
        if(!verificaPendenciasEnvio()) {
            SincronizarAsyncTask sincronizarAsyncTask = new SincronizarAsyncTask(LoginActivity.this, db, true, spUsuarios, usuarioGlobal.servidor);
            sincronizarAsyncTask.execute();
            return;
        } else {
            SincronizarInicialAsyncTask sincronizarInicialAsyncTask = new SincronizarInicialAsyncTask(LoginActivity.this, db, spUsuarios, usuarioGlobal.servidor);
            sincronizarInicialAsyncTask.execute();
            return;
        }
    }

    public boolean verificaPendenciasEnvio(){
        String s = "Select * from Engproducao as p, EngVerificacaoQualidadeServico as q, EngOcorrenciaNaoPlanejada as o " +
                " where p.transmitir != '' and p.transmitir is not null " +
                " and q.transmitir != '' and q.transmitir is not null " +
                " and o.transmitir != '' and o.transmitir is not null";
        Cursor c = db.rawQuery(s,null);
        return c.getCount()>0;
    }

    public boolean verificaTabelasBanco(){
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name= 'sisusuario'",null);
        return c.moveToFirst();
    }

    public List<Sisusuario> consultaUsuarios() {

        ArrayList<Sisusuario> lista = new ArrayList<Sisusuario>();
        if(verificaTabelasBanco()){
            Cursor c = db.rawQuery("SELECT id FROM sisusuario ORDER BY login", null);
            ArrayList<String> usuarios = new ArrayList<String>();
            if ( c.moveToFirst()) {

                while (c.moveToNext()) {
                    lista.add((Sisusuario) consultarPorId(new Sisusuario(), String.valueOf(c.getLong(0))));
                }
                return lista;
            }
        }
        return lista;
    }

    public String md5Senha(String s) throws NoSuchAlgorithmException {
        MessageDigest m= MessageDigest.getInstance("MD5");
        m.update(s.getBytes(),0,s.length());
        return new BigInteger(1,m.digest()).toString(16);
    }


    public void login(View view) {

        if(spUsuarios.getCount()>0) {
            Sisusuario usu = new Sisusuario();
            usu = (Sisusuario) spUsuarios.getSelectedItem();
            EditText edtsenha = (EditText) findViewById(R.id.senha);
            if(!edtsenha.getText().toString().equals("")){
                usu = loginUsuario(usu.getLogin(),edtsenha.getText().toString());
                if (usu!=null) {
                    final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
                    Toast.makeText(getApplicationContext(), usu.getNome(), Toast.LENGTH_LONG).show();
                    usuarioGlobal.setUsuarioLogado(usu);
                    Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                    //fecha o banco
                    db.close();
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






}