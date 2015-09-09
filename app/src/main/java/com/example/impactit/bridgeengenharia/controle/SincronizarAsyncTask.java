package com.example.impactit.bridgeengenharia.controle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.entidades.EngOcorrenciaNaoPlanejada;
import com.example.impactit.bridgeengenharia.entidades.EngVerificacaoQualidadeServico;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vinicius on 09/09/15.
 */
public class SincronizarAsyncTask extends AsyncTask<String, String, String> {

    private Context context;
    private SQLiteDatabase db;


    public SincronizarAsyncTask(Context context, SQLiteDatabase db) {
        this.context = context;
        this.db = db;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            publishProgress("Conectando...");
            //conexao com banco de dados
            //db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

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
        return;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Toast.makeText(context, values[0], Toast.LENGTH_LONG).show();
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
                    //carrega os dados no json
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        //dados ocorrencia tratados
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
                        }
                        //dados verificacaoqualidadeservico
                        if(classe.getSimpleName().toLowerCase().equals("engverificacaoqualidadeservico")) {
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
                        //dados verificacaoqualidadeservico
                        if(classe.getSimpleName().toLowerCase().equals("engproducao")) {
                            if ((!c.getColumnName(i).equals("transmitir")) && (!c.getColumnName(i).equals("id"))) {
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

