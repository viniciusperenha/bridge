package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.EngOcorrenciaNaoPlanejada;
import com.example.impactit.bridgeengenharia.entidades.EngVerificacaoQualidadeServico;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class SistemaActivity extends PrincipalActivity {
    private static SQLiteDatabase db;
    private Spinner spinnerTemas;
    private int estilos[]={R.style.AppTheme1, R.style.AppTheme2, R.style.AppTheme3, R.style.AppTheme4, R.style.AppTheme5};
    private int posicao=0;
    private Button bntSincronizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(usuarioGlobal.estiloSelecionado>0) {
            setTheme(usuarioGlobal.estiloSelecionado);
        }
        setContentView(R.layout.activity_sistema);
        ImageButton ib = (ImageButton) findViewById(R.id.sistema);
        ib.setImageResource(R.drawable.sistemaselecionado);
        bntSincronizar = (Button) findViewById(R.id.buttontransmitir);
        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);
        carregaDadosView();
    }

    public void carregaDadosView(){
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());
        spinnerTemas = (Spinner) findViewById(R.id.spinnerTemas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaTemas());
        adapter.setDropDownViewResource(R.layout.item_lista);
        spinnerTemas.setAdapter(adapter);

        if(usuarioGlobal.estiloSelecionado>=0) {
            for (int i = 0; i < estilos.length; i++) {
                if (estilos[i] == usuarioGlobal.estiloSelecionado) {
                    posicao = i;
                }
            }
        }
        spinnerTemas.setSelection(posicao);

    }

    public void transmiteDados(View v){
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(!usuarioGlobal.checkConexaoInternet(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Impossível transmitir dados, verifique a conexão!", Toast.LENGTH_LONG).show();
            return;
        }
        sincronizar();
    }

    private void sincronizar() {
        new Sincronizador().execute();
    }

    public void mudarTema(View v){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.estiloSelecionado = estilos[spinnerTemas.getSelectedItemPosition()];
        setTheme(usuarioGlobal.estiloSelecionado);
        setContentView(R.layout.activity_sistema);
        carregaDadosView();
    }

    private String[] listaTemas(){
        return new String[]{"Muito Pequeno","Pequeno","Normal","Grande","Muito Grande"};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sistema, menu);
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
            bntSincronizar.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            bntSincronizar.setEnabled(true);
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
