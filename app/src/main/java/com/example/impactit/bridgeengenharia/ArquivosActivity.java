package com.example.impactit.bridgeengenharia;


        import android.content.Intent;
        import android.content.res.AssetManager;
        import android.net.Uri;
        import android.os.Bundle;

        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;

        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.example.impactit.bridgeengenharia.controle.GlobalClass;

        import java.io.File;

        import java.util.ArrayList;


public class ArquivosActivity extends PrincipalActivity {
    String valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquivos);
        ImageButton ib = (ImageButton) findViewById(R.id.documentos);
        ib.setImageResource(R.drawable.documentosselecionado);

        //usuario global
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

        ListView lv = (ListView) findViewById(R.id.listView2);
        //colocar o caminho dos pdfs
        File f = new File("/sdcard/Pictures");

        File[] filelist = f.listFiles();
        ArrayList<String> lista = new ArrayList<>() ;
        for(int i=0;i<filelist.length;i++){
            if(filelist[i].getName().contains(".pdf")) {
                lista.add(filelist[i].getName());
            }
        }
        String[] theNamesOfFiles = new String[0];

        theNamesOfFiles = lista.toArray(theNamesOfFiles);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theNamesOfFiles));
        AssetManager asset = getAssets();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                valor = (String)parent.getItemAtPosition(position);
                File file = new File("/sdcard/Pictures/"+valor);
                //file.getAbsolutePath("/sdcard/Pictures/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }


        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arquivos, menu);
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
