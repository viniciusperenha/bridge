package com.example.impactit.bridgeengenharia.controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.impactit.bridgeengenharia.R;
import com.example.impactit.bridgeengenharia.entidades.ElementoTO;
import com.example.impactit.bridgeengenharia.entidades.QualidadeTO;

import java.util.ArrayList;

/**
 * Created by root on 25/08/15.
 */
public class AdapterElemento extends BaseAdapter {
    public ArrayList<ElementoTO> items;
    private LayoutInflater mInflater;

    public AdapterElemento(Context context, ArrayList<ElementoTO> items) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listaqualidade, null);
            holder = new ViewHolder();
            holder.idservico = (TextView) convertView.findViewById(R.id.idservico);
            holder.servico = (TextView) convertView.findViewById(R.id.servico);
            holder.idproducao = (TextView) convertView.findViewById(R.id.idproducao);
            holder.elementoproducao = (TextView) convertView.findViewById(R.id.elementoproducao);
            holder.quantidade = (TextView) convertView.findViewById(R.id.quantidade);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ElementoTO elementoTO = items.get(position);
        holder.idservico.setText(items.get(position).getOrcservico().getId().toString());
        holder.servico.setText(items.get(position).getOrcservico().getNome());
        holder.idproducao.setText(items.get(position).getOrcelementoproducao().getId().toString());
        holder.elementoproducao.setText(items.get(position).getOrcelementoproducao().getCodigo().toString());
        holder.quantidade.setText(items.get(position).getQuantidade().toString().concat(" ".concat(items.get(position).getOrcunidademedida().getSigla())));
        holder.status.setText(items.get(position).getStatus());
        return convertView;
    }


    private class ViewHolder {
        TextView idservico;
        TextView servico;
        TextView idproducao;
        TextView elementoproducao;
        TextView quantidade;
        TextView status;
    }
}
