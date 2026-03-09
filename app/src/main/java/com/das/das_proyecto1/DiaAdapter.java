package com.das.das_proyecto1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DiaAdapter extends RecyclerView.Adapter<DiaAdapter.DiaViewHolder> {
    private List<Dia> listaDias;
    private OnItemClickListener listener;
    public interface OnItemClickListener { //clivk para editar
        void onItemClick(Dia dia);
    }
    public DiaAdapter(List<Dia> listaDias, OnItemClickListener listener) {
        this.listaDias = listaDias;
        this.listener = listener;
    }
    @NonNull
    @Override
    public DiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dia, parent, false);
        return new DiaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaViewHolder holder, int position) {
        Dia dia = listaDias.get(position);
        //sacamos contexto porq lo necesitamos para traducir
        android.content.Context context = holder.itemView.getContext();
        //traducimos:
        holder.tvNombreDia.setText(traducirDia(context, dia.getNombreDia()));

        holder.tvDesayuno.setText(context.getString(R.string.desayuno)+": " + dia.getDesayuno());
        holder.tvAlmuerzo.setText(context.getString(R.string.almuerzo)+": " + dia.getAlmuerzo());
        holder.tvComida.setText(context.getString(R.string.comida)+": " + dia.getComida());
        holder.tvMerienda.setText(context.getString(R.string.merienda)+": " + dia.getMerienda());
        holder.tvCena.setText(context.getString(R.string.cena)+": " + dia.getCena());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(dia));
    }

    @Override
    public int getItemCount() {
        return listaDias.size();
    }
    private String traducirDia(android.content.Context context, String nombreDiaDB) {
        switch (nombreDiaDB) {
            case "Lunes": return context.getString(R.string.lunes);
            case "Martes": return context.getString(R.string.martes);
            case "Miércoles": return context.getString(R.string.miercoles);
            case "Jueves": return context.getString(R.string.jueves);
            case "Viernes": return context.getString(R.string.viernes);
            case "Sábado": return context.getString(R.string.sabado);
            case "Domingo": return context.getString(R.string.domiengo);
            default: return nombreDiaDB;
        }
    }
    public static class DiaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreDia, tvDesayuno, tvAlmuerzo, tvComida, tvMerienda, tvCena;
        public DiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreDia = itemView.findViewById(R.id.tvNombreDia);
            tvDesayuno = itemView.findViewById(R.id.tvDesayuno);
            tvAlmuerzo = itemView.findViewById(R.id.tvAlmuerzo);
            tvComida = itemView.findViewById(R.id.tvComida);
            tvMerienda = itemView.findViewById(R.id.tvMerienda);
            tvCena = itemView.findViewById(R.id.tvCena);
        }
    }
}