package com.example.impactit.bridgeengenharia;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.controle.AdapterProducao;
import com.example.impactit.bridgeengenharia.controle.GlobalClass;
import com.example.impactit.bridgeengenharia.entidades.Engempreiteira;
import com.example.impactit.bridgeengenharia.entidades.Engobra;
import com.example.impactit.bridgeengenharia.entidades.Engproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcelementoproducao;
import com.example.impactit.bridgeengenharia.entidades.Orcservico;
import com.example.impactit.bridgeengenharia.entidades.Orcunidademedida;
import com.example.impactit.bridgeengenharia.entidades.Plaatividade;
import com.example.impactit.bridgeengenharia.entidades.Plapavimentosubprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plaprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasetorprojeto;
import com.example.impactit.bridgeengenharia.entidades.Plasubprojeto;
import com.example.impactit.bridgeengenharia.entidades.ProducaoTO;
import com.example.impactit.bridgeengenharia.entidades.Rhcolaborador;
import com.example.impactit.bridgeengenharia.entidades.Sisusuario;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProducaoActivity extends PrincipalActivity {

    private static SQLiteDatabase db;
    private Spinner spinnerObra;
    private TextView engenheiro;
    private TextView responsavel;
    private Rhcolaborador responsavelobra;
    private Rhcolaborador engenheiroresidente;
    private Plaprojeto projeto;
    private Spinner subprojeto;
    private Spinner atividade;
    private Spinner pavimento;
    private Spinner empreiteira;
    private Spinner colaboradorempreiteira;
    private Spinner setor;
    private ListView listaApontamentosProducao;
    private static Integer posicaoobra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        if(usuarioGlobal.estiloSelecionado>0) {
            setTheme(usuarioGlobal.estiloSelecionado);
        }
        setContentView(R.layout.activity_producao);
        ImageButton ib = (ImageButton) findViewById(R.id.producao);
        ib.setImageResource(R.drawable.producaoselecionado);

        engenheiro = (TextView) findViewById(R.id.producaoengenheiro);
        responsavel = (TextView) findViewById(R.id.producaoresponsavel);
        subprojeto = (Spinner) findViewById(R.id.producaosubprojeto);
        atividade = (Spinner) findViewById(R.id.producaoatividade);
        spinnerObra = (Spinner) findViewById(R.id.spinnerObra);
        pavimento = (Spinner) findViewById(R.id.producaopavimento);
        empreiteira = (Spinner)  findViewById(R.id.empreiteiracontrato);
        colaboradorempreiteira = (Spinner)  findViewById(R.id.colaboradorempreiteira);
        setor = (Spinner) findViewById(R.id.spinnerSetor);
        listaApontamentosProducao = (ListView) findViewById(R.id.listaApontamentosProducao);



        usuarioGlobal.novoUsuarioGlobal();
        TextView tv = (TextView) findViewById(R.id.nomeusuario);
        tv.setText(usuarioGlobal.getUsuarioLogado().getNome());

        //conexao com banco de dados
        db = openOrCreateDatabase("bridge", Activity.MODE_PRIVATE, null);

        //carrega spinner de obras do usuario
        ArrayAdapter<Engobra> adapter = new ArrayAdapter<Engobra>(getApplicationContext(), R.layout.spinner_item, obrasDisponiveisUsuario(usuarioGlobal.getUsuarioLogado()));
        adapter.setDropDownViewResource(R.layout.item_lista);

        spinnerObra.setAdapter(adapter);

        spinnerObra.setSelection(posicaoobra);

        spinnerObra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerObra.getSelectedItemPosition()>0) {
                    usuarioGlobal.setObraselecionada((Engobra) spinnerObra.getSelectedItem());
                    //busca os dados da obra
                    responsavelobra = new Rhcolaborador();
                    responsavelobra = (Rhcolaborador) consultarPorId(responsavelobra, usuarioGlobal.getObraselecionada().getFkIdEngenheiroResidente().toString());
                    engenheiroresidente = new Rhcolaborador();
                    engenheiroresidente = (Rhcolaborador) consultarPorId(engenheiroresidente, usuarioGlobal.getObraselecionada().getFkIdGerenteEngenharia().toString());

                    responsavel.setText(responsavelobra.getNome());
                    engenheiro.setText(engenheiroresidente.getNome());
                    //armazena o projeto
                    projeto = new Plaprojeto();
                    projeto = (Plaprojeto) consultarPorId(projeto, usuarioGlobal.getObraselecionada().getFkIdProjeto().toString());
                    usuarioGlobal.setProjetoselecionado(projeto);
                    //carrega spinner de setores da obra
                    ArrayAdapter<Plasetorprojeto> adapterSetor = new ArrayAdapter<Plasetorprojeto>(getApplicationContext(), R.layout.spinner_item, setoresDisponiveisObra(projeto));
                    adapterSetor.setDropDownViewResource(R.layout.item_lista);
                    setor.setAdapter(adapterSetor);
                } else {
                    responsavel.setText("");
                    engenheiro.setText("");
                    usuarioGlobal.setObraselecionada(null);
                    GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
                    usuarioGlobal.setProjetoselecionado(null);
                    limpaSetor();
                    limpaSubprojeto();
                    limpaAtividade();
                    limpaPavimento();
                    limpaEmpreiteira();
                    limpaColaboradores();
                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        //onchange spinner setor
        setor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(setor.getSelectedItemPosition()>0) {
                    usuarioGlobal.setSetorprojetoselecionado((Plasetorprojeto) setor.getSelectedItem());
                    //lista subprojetos
                    ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), R.layout.spinner_item, listaSubProjetos((Plasetorprojeto) setor.getSelectedItem()));
                    adapterSubprojeto.setDropDownViewResource(R.layout.item_lista);
                    subprojeto.setAdapter(adapterSubprojeto);
                } else {
                    usuarioGlobal.setSetorprojetoselecionado(null);
                    limpaSubprojeto();
                    limpaAtividade();
                    limpaPavimento();
                    limpaEmpreiteira();
                    limpaColaboradores();
                }
                //carrega os apontamentos
                listaApontamentosProducao.setAdapter(carregaApontamentos());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        //onchange spinner subprojeto
        subprojeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(subprojeto.getSelectedItemPosition()>0) {

                    usuarioGlobal.setSubprojetoselecionado((Plasubprojeto) subprojeto.getSelectedItem());
                    //lista spinner atividades
                    ArrayAdapter<Plaatividade> adapterSubprojetoAtividade = new ArrayAdapter<Plaatividade>(getApplicationContext(), R.layout.spinner_item, listaAtividadesSubProjeto((Plasubprojeto) subprojeto.getSelectedItem()));
                    adapterSubprojetoAtividade.setDropDownViewResource(R.layout.item_lista);
                    atividade.setAdapter(adapterSubprojetoAtividade);

                    //lista spinner pavimentos
                    ArrayAdapter<Plapavimentosubprojeto> adapterPavimento = new ArrayAdapter<Plapavimentosubprojeto>(getApplicationContext(), R.layout.spinner_item, listaPavimentoProjeto());
                    adapterPavimento.setDropDownViewResource(R.layout.item_lista);
                    pavimento.setAdapter(adapterPavimento);
                } else {
                    usuarioGlobal.setSubprojetoselecionado(null);
                    limpaAtividade();
                    limpaPavimento();
                    limpaEmpreiteira();
                    limpaColaboradores();
                }
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //onchange spinner atividade
        atividade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(atividade.getSelectedItemPosition()>0) {
                    //armazena na global a atividade
                    usuarioGlobal.setAtividadeselecionada((Plaatividade) atividade.getSelectedItem());

                    //verifica a(s) empreiterias da obra pelo contrato e parametro
                    ArrayAdapter<Engempreiteira> adapterEmpreiteria = new ArrayAdapter<Engempreiteira>(getApplicationContext(), R.layout.spinner_item, listaEmpreiteirasContrato());
                    adapterEmpreiteria.setDropDownViewResource(R.layout.item_lista);
                    empreiteira.setAdapter(adapterEmpreiteria);
                } else {
                    usuarioGlobal.setAtividadeselecionada(null);
                    limpaEmpreiteira();
                    limpaColaboradores();
                }
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        //selecionou pavimento guarda como global
        pavimento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(pavimento.getSelectedItemPosition()>0) {
                    //armazena o pavimento
                    usuarioGlobal.setPavimentosubprojetoprojetoselecionado((Plapavimentosubprojeto) pavimento.getSelectedItem());
                } else {
                    usuarioGlobal.setPavimentosubprojetoprojetoselecionado(null);
                }
                listaApontamentosProducao.setAdapter(carregaApontamentos());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //selecionou empreiteira guarda como global
        empreiteira.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(empreiteira.getSelectedItemPosition()>0) {
                    //armazena empreiteira
                    usuarioGlobal.setEmpreiteiraselecionada((Engempreiteira) empreiteira.getSelectedItem());

                    //busca colaboradores da obra
                    ArrayAdapter<Rhcolaborador> adapteColaboradorEmpreiteira = new ArrayAdapter<Rhcolaborador>(getApplicationContext(), R.layout.spinner_item, listaColaboradorObra());
                    adapteColaboradorEmpreiteira.setDropDownViewResource(R.layout.item_lista);
                    colaboradorempreiteira.setAdapter(adapteColaboradorEmpreiteira);
                } else {
                    usuarioGlobal.setEmpreiteiraselecionada(null);
                    limpaColaboradores();
                }
                listaApontamentosProducao.setAdapter(carregaApontamentos());

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //selecionou colaborador guarda como global
        colaboradorempreiteira.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(colaboradorempreiteira.getSelectedItemPosition()>0) {
                    //armazena colaborador da empreiteira
                    usuarioGlobal.setColaboradorselecionado((Rhcolaborador) colaboradorempreiteira.getSelectedItem());
                } else {
                    usuarioGlobal.setColaboradorselecionado(null);
                }
                listaApontamentosProducao.setAdapter(carregaApontamentos());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        listaApontamentosProducao.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //armazena o servico na sessao
                ProducaoTO producaoTO = new ProducaoTO();
                producaoTO = (ProducaoTO) listaApontamentosProducao.getAdapter().getItem(position);

                usuarioGlobal.setProducaoselecionadamostrar(producaoTO.getEngproducao());

                Intent intent = new Intent(getApplicationContext(), detalhes_producao_gravada.class);
                startActivity(intent);

            }
        });

    }


    public void limpaSetor(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setSetorprojetoselecionado(null);
        ArrayAdapter<Plasetorprojeto> adapterSetor = new ArrayAdapter<Plasetorprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plasetorprojeto>());
        setor.setAdapter(adapterSetor);
    }

    public void limpaSubprojeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setSubprojetoselecionado(null);
        ArrayAdapter<Plasubprojeto> adapterSubprojeto = new ArrayAdapter<Plasubprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plasubprojeto>());
        subprojeto.setAdapter(adapterSubprojeto);
    }

    public void limpaAtividade(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setAtividadeselecionada(null);
        ArrayAdapter<Plaatividade> adapterSubprojetoAtividade = new ArrayAdapter<Plaatividade>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plaatividade>());
        atividade.setAdapter(adapterSubprojetoAtividade);
    }

    public void limpaPavimento(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setPavimentosubprojetoprojetoselecionado(null);
        ArrayAdapter<Plapavimentosubprojeto> adapterPavimento = new ArrayAdapter<Plapavimentosubprojeto>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Plapavimentosubprojeto>());
        pavimento.setAdapter(adapterPavimento);
    }

    public void limpaEmpreiteira(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setEmpreiteiraselecionada(null);
        ArrayAdapter<Engempreiteira> adapterEmpreiteria = new ArrayAdapter<Engempreiteira>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Engempreiteira>());
        empreiteira.setAdapter(adapterEmpreiteria);
    }

    public void limpaColaboradores(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        usuarioGlobal.setColaboradorselecionado(null);
        ArrayAdapter<Rhcolaborador> adapteColaboradorEmpreiteira = new ArrayAdapter<Rhcolaborador>(getApplicationContext(), R.layout.spinner_item, new ArrayList<Rhcolaborador>());
        colaboradorempreiteira.setAdapter(adapteColaboradorEmpreiteira);
    }


    @Override
    public void onResume(){
        super.onResume();
        listaApontamentosProducao.setAdapter(carregaApontamentos());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_producao, menu);
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

    public List<Engobra> obrasDisponiveisUsuario(Sisusuario usu){
        posicaoobra = 0;
        Engobra obraclienteselcionar = new Engobra();


        //busca pelo obra colaborador
        Cursor c = db.rawQuery("Select Distinct(o.id) from Engcolaboradorobra eco " +
                " inner join Engobra as o on eco.fkIdObra = o.id " +
                " where eco.fkIdColaborador = "+usu.getFkIdColaborador(),null);
        Engobra aux = new Engobra();
        if(c.getCount()>0){
            c.moveToFirst();
           obraclienteselcionar = (Engobra) consultarPorId(aux, c.getString(0));
        }
        c.close();

        //lista todas as obras
        Cursor ob = db.rawQuery("Select * from Engobra order by nome",null);
        ArrayList<Engobra> listaObras = new ArrayList<Engobra>();
        if(ob.getCount()>0){
            ob.moveToFirst();
            aux = new Engobra();
            aux.setNome("Selecione...");
            listaObras.add(aux);
            for(int i=0; i<ob.getCount();i++){
                aux = new Engobra();
                aux = (Engobra) consultarPorId(aux, ob.getString(ob.getColumnIndex("id")));
                if(aux.equals(obraclienteselcionar)){
                    posicaoobra = i+1;
                }
                listaObras.add(aux);

                ob.moveToNext();
            }
        }
        ob.close();

        return listaObras;

    }

    public List<Plasubprojeto> listaSubProjetos(Plasetorprojeto set){
        Cursor c = db.rawQuery("SELECT psp.id FROM Plasubprojeto as psp" +
                " Inner Join Plasubprojetosetorprojeto as pspp on psp.id = pspp.fkIdSubprojeto " +
                " where pspp.fkIdSetorProjeto = "+set.getId(), null);
        ArrayList<Plasubprojeto> lista = new ArrayList<Plasubprojeto>();
        if(c.getCount()>0){
            c.moveToFirst();
            Plasubprojeto aux = new Plasubprojeto();
            aux.setDescricao("Selecione...");
            lista.add(aux);
            for(int i=0; i<c.getCount();i++){
                aux = new Plasubprojeto();
                lista.add((Plasubprojeto) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;


    }

    public ArrayList<Plaatividade> listaAtividadesSubProjeto(Plasubprojeto subprojeto){
        Cursor c = db.rawQuery("SELECT at.id FROM Plaatividade as at inner join " +
                " Plasubprojeto as sp on at.fkIdSubprojeto = sp.id " +
                " where sp.id =  '"+subprojeto.getId()+"'", null);
        ArrayList<Plaatividade> lista = new ArrayList<Plaatividade>();
        if(c.getCount()>0){
            Plaatividade aux = new Plaatividade();
            aux.setNome("Selecione...");
            lista.add(aux);
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                aux = new Plaatividade();
                lista.add((Plaatividade) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;

    }

    public ArrayList<Plapavimentosubprojeto> listaPavimentoProjeto(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("SELECT psp.id FROM Plapavimentosubprojeto as psp " +
                " inner join Plasubprojetosetorprojeto as ssp on ssp.id = psp.fkIdSubprojetoSetorProjeto " +
                " where ssp.fkIdSubprojeto = "+usuarioGlobal.getSubprojetoselecionado().getId()+" " +
                " and psp.fkIdSetorProjeto = "+usuarioGlobal.getSetorprojetoselecionado().getId(), null);


        ArrayList<Plapavimentosubprojeto> lista = new ArrayList<Plapavimentosubprojeto>();
        if(c.getCount()>0){
            Plapavimentosubprojeto aux = new Plapavimentosubprojeto();
            aux.setNome("Selecione...");
            lista.add(aux);
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                aux = new Plapavimentosubprojeto();
                lista.add((Plapavimentosubprojeto) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;

    }

    public ArrayList<Rhcolaborador> listaColaboradorObra(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = db.rawQuery("Select Distinct(col.id) FROM Rhcolaborador as col " +
                " WHERE col.fkIdEmpresaEmpreiteira = " + usuarioGlobal.getEmpreiteiraselecionada().getId()
                , null);
        ArrayList<Rhcolaborador> lista = new ArrayList<Rhcolaborador>();
        if(c.getCount()>0){
            Rhcolaborador aux = new Rhcolaborador();
            aux.setNome("Selecione...");
            lista.add(aux);
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                aux = new Rhcolaborador();
                lista.add((Rhcolaborador) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;
    }

    public ArrayList<Engempreiteira> listaEmpreiteirasContrato(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        Cursor c = null;
        //busca parametro pra tipo de contrato
        Cursor param = db.rawQuery("select * from Sisparametro where grupo='PARAMETROS' and nome='vincularEmpreiteiraPorContrato' and valor='S' ",null);
        if(param.getCount()==0) {

            c = db.rawQuery("Select distinct(e.id) FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId(), null);
        }

        if(param.getCount()>0) {
            //de acordo com a atividade selecionada no subprojeto
            c = db.rawQuery("Select distinct(e.id) FROM Engempreiteira as e " +
                    " inner join Engcontratoempreiteira as ce on e.id = ce.fkIdEmpreiteira " +
                    " inner join Engcontratoservicoempreiteira as cse on ce.id = cse.fkIdContratoEmpreiteira " +
                    " WHERE ce.fkIdObra = " + usuarioGlobal.getObraselecionada().getId()+" " +
                    " and cse.fkIdAtividade = "+usuarioGlobal.getAtividadeselecionada().getId()+" " +
                    " and cse.fkIdSubprojeto = "+usuarioGlobal.getSubprojetoselecionado().getId(), null);
        }

        ArrayList<Engempreiteira> lista = new ArrayList<Engempreiteira>();
        if(c.getCount()>0){
            Engempreiteira aux = new Engempreiteira();
            aux.setNomeFantasia("Selecione...");
            lista.add(aux);
            c.moveToFirst();
            for(int i=0; i<c.getCount();i++){
                aux = new Engempreiteira();
                lista.add((Engempreiteira) consultarPorId(aux,c.getString(0)));
                c.moveToNext();
            }
        }
        c.close();
        return lista;
    }


    public List<Plasetorprojeto> setoresDisponiveisObra(Plaprojeto pro){

        //busca setor pelo projeto da obra
        Cursor c = db.rawQuery("Select setor.id from Plasetorprojeto setor " +
                "where setor.fkIdProjeto = "+pro.getId(),null);
        ArrayList<Plasetorprojeto> lista = new ArrayList<Plasetorprojeto>();
        if(c.getCount()>0){

            c.moveToFirst();
            Plasetorprojeto aux = new Plasetorprojeto();
            aux.setNome("Selecione...");
            lista.add(aux);

            for(int i=0; i<c.getCount();i++){
                aux = new Plasetorprojeto();
                lista.add((Plasetorprojeto) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }

        }
        c.close();
        return lista;

    }

    public List<Engproducao> apontamentosProducao(){


        Cursor c = db.rawQuery("Select pro.id from Engproducao pro " ,null);
        ArrayList<Engproducao> lista = new ArrayList<Engproducao>();
        if(c.getCount()>0){

            c.moveToFirst();

            for(int i=0; i<c.getCount();i++){
                Engproducao aux = new Engproducao();
                lista.add((Engproducao) consultarPorId(aux, c.getString(0)));
                c.moveToNext();
            }

        }
        c.close();
        return lista;
    }

    public AdapterProducao carregaApontamentos(){
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();

        if(usuarioGlobal.getObraselecionada()!=null){
            String s = "Select s.id as servico, pro.id as producao,ep.id as elementoproducao, um.id as unidademedida " +
                    " from Engproducao as pro " +
                    " inner join Orcservico as s on s.id=pro.fkIdServico " +
                    " inner join Orcelementoproducao as ep on ep.id=pro.fkIdElementoProducao " +
                    " inner join Orcunidademedida as um on um.id = s.fkIdUnidadeMedida " +
                    " Inner Join Plasubprojetosetorprojeto as pspp on pspp.id = pro.fkIdSubprojetoSetorProjeto " +
                    " where (pro.status is null OR pro.status = 'N' OR pro.status = '') " +
                    " and (pro.fkIdObra='" + usuarioGlobal.getObraselecionada().getId() + "')";

            if (usuarioGlobal.getSetorprojetoselecionado() != null) {
                s += " and (pro.fkIdSetorProjeto=" + usuarioGlobal.getSetorprojetoselecionado().getId()+")";
            }
            if (usuarioGlobal.getSubprojetoselecionado() != null) {
                s += " and (pspp.fkIdSubprojeto = "+usuarioGlobal.getSubprojetoselecionado().getId()+") " +
                " and (pspp.fkIdSetorProjeto = "+usuarioGlobal.getSetorprojetoselecionado().getId()+") ";
            }
            if (usuarioGlobal.getPavimentosubprojetoprojetoselecionado() != null) {
                s += " and (pro.fkIdPavimentoSubprojeto=" + usuarioGlobal.getPavimentosubprojetoprojetoselecionado().getId()+")";
            }
            if (usuarioGlobal.getAtividadeselecionada() != null) {
                s += " and (pro.fkIdAtividade=" + usuarioGlobal.getAtividadeselecionada().getId()+")";
            }
            if (usuarioGlobal.getEmpreiteiraselecionada() != null) {
                s += " and (pro.fkIdEmpreiteira=" + usuarioGlobal.getEmpreiteiraselecionada().getId()+")";
            }
            if (usuarioGlobal.getColaboradorselecionado() != null) {
                s += " and (pro.fkIdColaborador=" + usuarioGlobal.getColaboradorselecionado().getId()+")";
            }

            System.out.println("---------------------------------------\n"+s);
            Cursor c = db.rawQuery(s, null);
            // verifica resultado
            if (c.moveToFirst()) {
                ArrayList<ProducaoTO> itemsproducao = new ArrayList<>();
                ProducaoTO producaoTO;
                for(int i=0;i<c.getCount();i++){
                    producaoTO = new ProducaoTO();
                    producaoTO.setOrcservico((Orcservico) consultarPorId(new Orcservico(), String.valueOf(c.getLong(c.getColumnIndexOrThrow("servico")))));
                    producaoTO.setEngproducao((Engproducao) consultarPorId(new Engproducao(), String.valueOf(c.getLong(c.getColumnIndexOrThrow("producao")))));
                    producaoTO.setOrcunidademedida((Orcunidademedida) consultarPorId(new Orcunidademedida(), String.valueOf(c.getLong(c.getColumnIndexOrThrow("unidademedida")))));
                    producaoTO.setOrcelementoproducao((Orcelementoproducao) consultarPorId(new Orcelementoproducao(), String.valueOf(c.getLong(c.getColumnIndexOrThrow("elementoproducao")))));
                    itemsproducao.add(producaoTO);
                    c.moveToNext();
                }
                AdapterProducao adapterProducao = new AdapterProducao(this, itemsproducao);
                // retorna o adapter
                c.close();
                return adapterProducao;
            }
        }
        return null;
    }

    public Object consultarPorId(Object obj, String id) {
        Cursor c = db.rawQuery("SELECT * FROM " + obj.getClass().getSimpleName().toLowerCase()+" where id = "+id, null);
        return recuperarObjeto(obj,c);

    }

    public Object recuperarObjeto(Object obj, Cursor c) {

        if(c.getCount()>0) {
            c.moveToFirst();
            String s = "";

            for (int i = 0; i < c.getColumnCount(); i++) {
                try{
                    Field f= obj.getClass().getDeclaredField(c.getColumnName(i));
                    f.setAccessible(true);
                    if((!"".equals(c.getString(i)))&&(c.getString(i)!=null)) {
                        if (f.getType().equals(Date.class)) {
                            //System.out.println(c.getString(i));
                            //TODO: criar conversao para data

                        }
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
                        if (f.getType().equals(double.class)) {
                            f.set(obj, Double.parseDouble(c.getString(i)));
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                s+= c.getColumnName(i)+" - "+c.getString(i)+"   ";
            }


        }
        return obj;

    }

    public void novaMedicao(View view) {
        GlobalClass usuarioGlobal = (GlobalClass) getApplicationContext();
        String validacoes = "";
        //verifica campos obrigatorios
        if(usuarioGlobal.getObraselecionada()==null){
            validacoes = "Obra";

        }
        if(usuarioGlobal.getSetorprojetoselecionado()==null){
            validacoes += " Setor";
        }
        if(usuarioGlobal.getSubprojetoselecionado()==null){
            validacoes += " Subprojeto";

        }
        if(usuarioGlobal.getAtividadeselecionada()==null){
            validacoes += " Atividade";

        }
        if(usuarioGlobal.getPavimentosubprojetoprojetoselecionado()==null){
            validacoes += " Pavimento";

        }
        if(usuarioGlobal.getEmpreiteiraselecionada()==null){
            validacoes += " Empreiteira";

        }
        if(usuarioGlobal.getColaboradorselecionado()==null){
            validacoes += " Colaborador";

        }

        if("".equals(validacoes)) {
            Intent intent = new Intent(getApplicationContext(), DetalhesProducao.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Selecione: "+validacoes+" para novo apontamento", Toast.LENGTH_LONG).show();
        }
    }


}
