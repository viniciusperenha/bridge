package com.example.impactit.bridgeengenharia;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;

import java.lang.reflect.Array;


public class SistemaActivity extends PrincipalActivity {
    private Spinner spinnerTemas;
    private int estilos[]={R.style.AppTheme1, R.style.AppTheme2, R.style.AppTheme3, R.style.AppTheme4, R.style.AppTheme5};
    private int posicao=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        setContentView(R.layout.activity_sistema);
        ImageButton ib = (ImageButton) findViewById(R.id.sistema);
        ib.setImageResource(R.drawable.sistemaselecionado);

        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

        carregaDadosView();
    }

    public void carregaDadosView(){
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        spinnerTemas = (Spinner) findViewById(R.id.spinnerTemas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaTemas());
        adapter.setDropDownViewResource(R.layout.item_lista);
        spinnerTemas.setAdapter(adapter);

        if(usuarioGlobal.estiloSelecionado>=0) {
            for (int i = 0; i < estilos.length; i++) {
                if (i == usuarioGlobal.estiloSelecionado) {
                    posicao = i;
                }
            }
        }
        spinnerTemas.setSelection(posicao);
        //onchange spinner setor
        spinnerTemas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                usuarioGlobal.estiloSelecionado = estilos[spinnerTemas.getSelectedItemPosition()];
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


    }


    public void mudarTema(View v){
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        setTheme( usuarioGlobal.estiloSelecionado);
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


}
