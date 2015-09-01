package com.example.impactit.bridgeengenharia.controle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.impactit.bridgeengenharia.R;
import com.example.impactit.bridgeengenharia.entidades.OcorrenciaTO;
import java.util.ArrayList;

/**
 * Created by vinicius on 01/09/15.
 */
public class AdapterOcorrencia extends BaseAdapter {
    public ArrayList<OcorrenciaTO> items;
    private LayoutInflater mInflater;

    public AdapterOcorrencia(ArrayList<OcorrenciaTO> items, Context context) {
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.listaocorrencia, null);
            holder = new ViewHolder();
            holder.obra = (TextView) convertView.findViewById(R.id.ocorrenciaobra);
            holder.setor = (TextView) convertView.findViewById(R.id.ocorrenciasetor);
            holder.subprojeto = (TextView) convertView.findViewById(R.id.ocorrenciasubprojeto);
            holder.ocorrencia = (TextView) convertView.findViewById(R.id.ocorrenciatexto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.obra.setText(items.get(position).getEngobra().getNome());
        holder.setor.setText(items.get(position).getPlasetorprojeto().getNome());
        holder.subprojeto.setText(items.get(position).getPlasubprojeto().getDescricao());
        holder.ocorrencia.setText(items.get(position).getEngOcorrenciaNaoPlanejada().getDescricao());
        return convertView;
    }

    private class ViewHolder {
        TextView obra;
        TextView setor;
        TextView subprojeto;
        TextView ocorrencia;

    }
}
