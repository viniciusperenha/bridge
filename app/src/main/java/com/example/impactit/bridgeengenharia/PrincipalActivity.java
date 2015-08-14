package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;


public class PrincipalActivity extends Activity {

    public Intent it;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //usuario global
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

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
        Intent intent = new Intent(getApplicationContext(), ArquivosActivity.class);
        startActivity(intent);
    }

    public void producao(View view) {
        Intent intent = new Intent(getApplicationContext(), ProducaoActivity.class);
        startActivity(intent);
    }

    public void qualidade(View view) {
        Intent intent = new Intent(getApplicationContext(), QualidadeActivity.class);
        startActivity(intent);
    }

    public void ocorrencia(View view) {
        Intent intent = new Intent(getApplicationContext(), OcorrenciaActivity.class);
        startActivity(intent);
    }

    public void sistema(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivity(intent);
    }


}

