package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;

import java.util.List;


public class PrincipalActivity extends Activity {

    public Intent it;
    private List<ActivityManager.RunningTaskInfo> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(usuarioGlobal.estiloSelecionado>0) {
            setTheme(usuarioGlobal.estiloSelecionado);
        }

        setContentView(R.layout.activity_principal);

        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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

    public void documentos(View view) {
        if(!tasks.get(0).topActivity.toString().equals("ComponentInfo{com.example.impactit.bridgeengenharia/com.example.impactit.bridgeengenharia.ArquivosActivity}")) {
            GlobalClass gb = (GlobalClass) getApplicationContext();

            Toast.makeText(getApplicationContext(), "internet: "+gb.checkConexaoInternet(this), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ArquivosActivity.class);
            startActivity(intent);
        }
    }

    public void producao(View view) {
        if(!tasks.get(0).topActivity.toString().equals("ComponentInfo{com.example.impactit.bridgeengenharia/com.example.impactit.bridgeengenharia.ProducaoActivity}")) {
            Intent intent = new Intent(getApplicationContext(), ProducaoActivity.class);
            startActivity(intent);
        }
    }

    public void qualidade(View view) {
        if(!tasks.get(0).topActivity.toString().equals("ComponentInfo{com.example.impactit.bridgeengenharia/com.example.impactit.bridgeengenharia.QualidadeActivity}")) {
            Intent intent = new Intent(getApplicationContext(), QualidadeActivity.class);
            startActivity(intent);
        }
    }

    public void ocorrencia(View view) {
        if(!tasks.get(0).topActivity.toString().equals("ComponentInfo{com.example.impactit.bridgeengenharia/com.example.impactit.bridgeengenharia.OcorrenciaActivity}")) {
            Intent intent = new Intent(getApplicationContext(), OcorrenciaActivity.class);
            startActivity(intent);
        }
    }

    public void sistema(View view) {
        if(!tasks.get(0).topActivity.toString().equals("ComponentInfo{com.example.impactit.bridgeengenharia/com.example.impactit.bridgeengenharia.SistemaActivity}")) {
            Intent intent = new Intent(getApplicationContext(), SistemaActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if(this.getClass().equals(PrincipalActivity.class)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Deseja sair?");
            alertbox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // finish used for destroyed activity
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
        } else {
            super.onBackPressed();
        }
    }

}

