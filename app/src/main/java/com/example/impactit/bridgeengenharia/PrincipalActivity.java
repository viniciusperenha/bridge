package com.example.impactit.bridgeengenharia;

        import android.app.Activity;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.TextView;


public class PrincipalActivity extends Activity {

    public Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        it = getIntent();

        tv.setText(it.getStringExtra("usuario"));

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
        intent.putExtra("usuario",it.getStringExtra("usuario"));
        startActivity(intent);
    }

    public void producao(View view) {
        Intent intent = new Intent(getApplicationContext(), ProducaoActivity.class);
        intent.putExtra("usuario",it.getStringExtra("usuario"));
        startActivity(intent);
    }

    public void qualidade(View view) {
        Intent intent = new Intent(getApplicationContext(), QualidadeActivity.class);
        intent.putExtra("usuario",it.getStringExtra("usuario"));
        startActivity(intent);
    }

    public void ocorrencia(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        intent.putExtra("usuario",it.getStringExtra("usuario"));
        startActivity(intent);
    }

    public void sistema(View view) {
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        intent.putExtra("usuario",it.getStringExtra("usuario"));
        startActivity(intent);
    }


}

