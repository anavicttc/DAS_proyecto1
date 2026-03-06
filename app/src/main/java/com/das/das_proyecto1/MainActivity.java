package com.das.das_proyecto1;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BaseDatosHelper dbHelper;
    private DiaAdapter adapter;
    private List<Dia> listaDias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new BaseDatosHelper(this);
        listaDias = dbHelper.obtenerMenu();

        RecyclerView rv = findViewById(R.id.recyclerViewMenu);
        rv.setLayoutManager(new LinearLayoutManager(this)); //

        // Al hacer clic en un día, abrimos el diálogo de edición [cite: 57]
        adapter = new DiaAdapter(listaDias, dia -> mostrarDialogoEdicion(dia));
        rv.setAdapter(adapter);
    }
    private void mostrarDialogoEdicion(Dia dia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo, null);
        //referencias xml:
        EditText etDes = view.findViewById(R.id.etDesayuno);
        EditText etAlm = view.findViewById(R.id.etAlmuerzo);
        EditText etCom = view.findViewById(R.id.etComida);
        EditText etMer = view.findViewById(R.id.etMerienda);
        EditText etCen = view.findViewById(R.id.etCena);

        //cargar datos
        etDes.setText(dia.getDesayuno());
        etAlm.setText(dia.getAlmuerzo());
        etCom.setText(dia.getComida());
        etMer.setText(dia.getMerienda());
        etCen.setText(dia.getCena());

        builder.setView(view)
                .setTitle("Editar " + dia.getNombreDia())
                .setPositiveButton("Guardar", (dialog, id) -> {
                    // Actualizar objeto y base de datos
                    dia.setDesayuno(etDes.getText().toString());
                    dia.setAlmuerzo(etAlm.getText().toString());
                    dia.setComida(etCom.getText().toString());
                    dia.setMerienda(etMer.getText().toString());
                    dia.setCena(etCen.getText().toString());

                    dbHelper.actualizarDia(dia);
                    adapter.notifyDataSetChanged(); // Refrescar lista
                })
                .setNegativeButton("Cancelar", null);
        builder.create().show();
    }
}