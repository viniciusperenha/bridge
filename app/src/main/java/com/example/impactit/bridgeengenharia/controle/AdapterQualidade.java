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
            convertView.setTag(holder);
            final ViewHolder finalholder = holder;
            holder.ap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    QualidadeTO qualidadeTO = (QualidadeTO) v.getTag();
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Aprovado ativado", Toast.LENGTH_SHORT).show();
                    }
                    qualidadeTO.setAp(cb.isChecked());
                    finalholder.rp.setChecked(false);
                    validacheckbox(finalholder);
                }
            });

            holder.rp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    QualidadeTO qualidadeTO = (QualidadeTO) v.getTag();
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Reprovado ativado", Toast.LENGTH_SHORT).show();
                    }
                    finalholder.ap.setChecked(false);
                    qualidadeTO.setRp(cb.isChecked());
                    validacheckbox(finalholder);
                }
            });

            holder.acr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    QualidadeTO qualidadeTO = (QualidadeTO) v.getTag();
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Aprovado com Reparos ativado", Toast.LENGTH_SHORT).show();
                    }
                    finalholder.asr.setChecked(false);
                    qualidadeTO.setAcr(cb.isChecked());
                    validacheckbox(finalholder);
                }
            });

            holder.asr.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    QualidadeTO qualidadeTO = (QualidadeTO) v.getTag();
                    if(cb.isChecked()) {
                        Toast.makeText(mInflater.getContext(), "Aprovado sem Reparos ativado", Toast.LENGTH_SHORT).show();
                    }
                    finalholder.acr.setChecked(false);
                    qualidadeTO.setAsr(cb.isChecked());
                    validacheckbox(finalholder);
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QualidadeTO qualidadeTO = items.get(position);
        holder.iditem.setText(items.get(position).getEngItemVerificacaoServico().getId().toString());
        holder.verificacao.setText(items.get(position).getEngItemVerificacaoServico().getVerificacao());
        holder.criterio.setText(items.get(position).getEngItemVerificacaoServico().getCriterioAceitacao());
        holder.ap.setChecked(qualidadeTO.isAp());
        holder.rp.setChecked(qualidadeTO.isRp());
        holder.acr.setChecked(qualidadeTO.isAcr());
        holder.asr.setChecked(qualidadeTO.isAsr());
        holder.ap.setTag(qualidadeTO);
        holder.rp.setTag(qualidadeTO);
        holder.acr.setTag(qualidadeTO);
        holder.asr.setTag(qualidadeTO);
        validacheckbox(holder);
        return convertView;
    }

    private void validacheckbox(ViewHolder finalholder) {
        //todos estao desmarcados
        if((!finalholder.ap.isChecked())&&(!finalholder.rp.isChecked())&&(!finalholder.acr.isChecked())&&(!finalholder.asr.isChecked())){
            finalholder.acr.setEnabled(false);
            finalholder.asr.setEnabled(false);
            finalholder.rp.setEnabled(true);
            finalholder.ap.setEnabled(true);
            return;
        } else {
            if (finalholder.ap.isChecked()) { //aprovado desabilita acr e asr
                finalholder.acr.setEnabled(false);
                finalholder.asr.setEnabled(false);
                finalholder.ap.setEnabled(true);
                finalholder.rp.setEnabled(true);
                return;
            }
            if (finalholder.rp.isChecked()) { //reprovado desabilita ap
                finalholder.ap.setEnabled(false);
                finalholder.acr.setEnabled(true);
                finalholder.asr.setEnabled(true);
                finalholder.rp.setEnabled(true);
                return;
            }

        }

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
