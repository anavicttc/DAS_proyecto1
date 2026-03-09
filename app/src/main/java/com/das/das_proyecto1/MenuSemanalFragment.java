package com.das.das_proyecto1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuSemanalFragment extends Fragment {

    private BaseDatosHelper dbHelper;
    private DiaAdapter adapter;
    private List<Dia> listaDias;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_semanal, container, false);
        dbHelper = new BaseDatosHelper(requireContext());
        listaDias = dbHelper.obtenerMenu();
        RecyclerView rv = view.findViewById(R.id.recyclerViewMenu);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        //click editar
        adapter = new DiaAdapter(listaDias, dia -> dialogoEditar(dia));
        rv.setAdapter(adapter);
        return view;
    }
    private void dialogoEditar(Dia dia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo, null);

        EditText etDes = view.findViewById(R.id.etDesayuno);
        EditText etAlm = view.findViewById(R.id.etAlmuerzo);
        EditText etCom = view.findViewById(R.id.etComida);
        EditText etMer = view.findViewById(R.id.etMerienda);
        EditText etCen = view.findViewById(R.id.etCena);

        etDes.setText(dia.getDesayuno());
        etAlm.setText(dia.getAlmuerzo());
        etCom.setText(dia.getComida());
        etMer.setText(dia.getMerienda());
        etCen.setText(dia.getCena());

        builder.setView(view)
                .setTitle(getString(R.string.editar_menu) +": " + traducirDia(dia.getNombreDia()))
                .setPositiveButton(R.string.guardar, (dialog, id) -> {
                    dia.setDesayuno(etDes.getText().toString());
                    dia.setAlmuerzo(etAlm.getText().toString());
                    dia.setComida(etCom.getText().toString());
                    dia.setMerienda(etMer.getText().toString());
                    dia.setCena(etCen.getText().toString());

                    dbHelper.actualizarDia(dia);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancelar, null);
        builder.create().show();
    }
    private String traducirDia(String nombreDiaDB) {
        switch (nombreDiaDB) {
            case "Lunes": return getString(R.string.lunes);
            case "Martes": return getString(R.string.martes);
            case "Miércoles": return getString(R.string.miercoles);
            case "Jueves": return getString(R.string.jueves);
            case "Viernes": return getString(R.string.viernes);
            case "Sábado": return getString(R.string.sabado);
            case "Domingo": return getString(R.string.domiengo);
            default: return nombreDiaDB;
        }
    }
}