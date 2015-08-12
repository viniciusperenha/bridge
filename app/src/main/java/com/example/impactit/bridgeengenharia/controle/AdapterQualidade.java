package com.example.impactit.bridgeengenharia.controle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.impactit.bridgeengenharia.R;
import com.example.impactit.bridgeengenharia.entidades.QualidadeTO;

import java.util.ArrayList;

/**
 * Created by vinicius on 11/08/15.
 */
public class AdapterQualidade extends BaseAdapter {

    public ArrayList<QualidadeTO> items;
    private LayoutInflater mInflater;

    public AdapterQualidade(Context context, ArrayList<QualidadeTO> items) {
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
            convertView = mInflater.inflate(R.layout.listaproducoesqualidade, null);
            holder = new ViewHolder();
            holder.iditem = (TextView) convertView.findViewById(R.id.iditem);
            holder.verificacao = (TextView) convertView.findViewById(R.id.verificacao);
            holder.criterio = (TextView) convertView.findViewById(R.id.criterio);
            holder.ap = (CheckBox) convertView.findViewById(R.id.ap);
            holder.rp = (CheckBox) convertView.findViewById(R.id.rp);
            holder.acr = (CheckBox) convertView.findViewById(R.id.acr);
            holder.asr = (CheckBox) convertView.findViewById(R.id.asr);

            holder.iditem.setText(items.get(position).getEngItemVerificacaoServico().getId().toString());
            holder.verificacao.setText(items.get(position).getEngItemVerificacaoServico().getVerificacao());
            holder.criterio.setText(items.get(position).getEngItemVerificacaoServico().getCriterioAceitacao());


            holder.ap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Aprovado ativado", Toast.LENGTH_SHORT).show();
                    }
                    items.get(position).setAp(cb.isChecked());
                }
            });

            holder.rp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Reprovado ativado", Toast.LENGTH_SHORT).show();
                    }
                    items.get(position).setRp(cb.isChecked());
                }
            });

            holder.acr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Aprovado com Reparos ativado", Toast.LENGTH_SHORT).show();
                    }
                    items.get(position).setAcr(cb.isChecked());
                }
            });

            holder.asr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Aprovado sem Reparos ativado", Toast.LENGTH_SHORT).show();
                    }
                    items.get(position).setAsr(cb.isChecked());
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView iditem;
        TextView verificacao;
        TextView criterio;
        CheckBox ap;
        CheckBox rp;
        CheckBox acr;
        CheckBox asr;
    }
}
