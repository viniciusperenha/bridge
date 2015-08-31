package com.example.impactit.bridgeengenharia.controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.R;
import com.example.impactit.bridgeengenharia.entidades.ProducaoTO;
import com.example.impactit.bridgeengenharia.entidades.QualidadeTO;

import java.util.ArrayList;

/**
 * Created by vinicius on 26/08/15.
 */
public class AdapterProducao extends BaseAdapter {
    public ArrayList<ProducaoTO> items;
    private LayoutInflater mInflater;

    public AdapterProducao(Context context, ArrayList<ProducaoTO> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listaapontamentos, null);
            holder = new ViewHolder();
            holder.servico = (TextView) convertView.findViewById(R.id.servicoproducao);
            holder.elemento = (TextView) convertView.findViewById(R.id.elementoproducaoproducao);
            holder.producao = (TextView) convertView.findViewById(R.id.quantidadeproducao);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProducaoTO producaoTO = items.get(position);
        holder.servico.setText(producaoTO.getOrcservico().getNome());
        holder.elemento.setText(producaoTO.getOrcelementoproducao().getCodigo());
        holder.producao.setText(producaoTO.getEngproducao().getQuantidade()+" "+producaoTO.getOrcunidademedida().getSigla());

        return convertView;
    }



    private class ViewHolder {
        TextView servico;
        TextView elemento;
        TextView producao;
    }
}
