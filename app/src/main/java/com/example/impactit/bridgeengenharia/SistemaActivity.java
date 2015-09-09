package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.controle.SincronizarAsyncTask;



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
        SincronizarAsyncTask sincronizarAsyncTask = new SincronizarAsyncTask(this, db);
        sincronizarAsyncTask.execute();
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



}
