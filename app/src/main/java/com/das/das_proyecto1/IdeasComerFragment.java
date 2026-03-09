package com.das.das_proyecto1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IdeasComerFragment extends Fragment {

    private BaseDatosHelper dbHelper;
    private RecyclerView recyclerView;
    private PlatoAdapter adapter;
    private List<Plato> listaPlatos;
    private static final int CODIGO_GALERIA = 200;
    private ImageView imagenPreviaDialogo;
    private String rutaFoto = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ideas_comer, container, false);
        dbHelper = new BaseDatosHelper(requireContext());
        recyclerView = view.findViewById(R.id.recyclerViewPlatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cargarPlatos();

        Button btnNuevo = view.findViewById(R.id.btnNuevoPlato);
        btnNuevo.setOnClickListener(v -> mostrarDialogoNuevoPlato());
        return view;
    }

    private void cargarPlatos() {
        listaPlatos = dbHelper.obtenerPlatos();
        adapter = new PlatoAdapter(listaPlatos);
        recyclerView.setAdapter(adapter);
    }

    private void mostrarDialogoNuevoPlato() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_nuevo_plato, null);

        EditText etNombre = view.findViewById(R.id.etNombrePlato);
        EditText etDesc = view.findViewById(R.id.etDescPlato);
        Button btnGaleria = view.findViewById(R.id.btnHacerFoto);
        btnGaleria.setText("Elegir de la Galería");

        imagenPreviaDialogo = view.findViewById(R.id.ivFotoPrevia);
        rutaFoto = ""; //limpiamos por si aca

        //intent -> abrir galería
        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, CODIGO_GALERIA);
        });
        builder.setView(view)
                .setTitle("Nuevo Plato")
                .setPositiveButton("Guardar", (dialog, id) -> {
                    String nombre = etNombre.getText().toString();
                    String desc = etDesc.getText().toString();

                    if (!nombre.isEmpty()) {
                        dbHelper.insertarPlato(nombre, desc, rutaFoto);
                        cargarPlatos();
                    } else {
                        Toast.makeText(requireContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    //rdo galería
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_GALERIA && resultCode == Activity.RESULT_OK && data != null) {
            Uri uriSeleccionada = data.getData(); //obtener ruta directa
            if (uriSeleccionada != null) {
                rutaFoto = uriSeleccionada.toString(); //convertir a string para guardar en la bd
                //ventanita de la izq
                if (imagenPreviaDialogo != null) {
                    imagenPreviaDialogo.setImageURI(uriSeleccionada);
                }
            }
        }
    }

    private class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {
        private List<Plato> platos;
        public PlatoAdapter(List<Plato> platos) { this.platos = platos; }
        @NonNull
        @Override
        public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plato, parent, false);
            return new PlatoViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
            Plato plato = platos.get(position);
            holder.tvNombre.setText(plato.getNombre());
            holder.tvDesc.setText(plato.getDescripcion());

            if (plato.getFotoUri() != null && !plato.getFotoUri().isEmpty()) {
                Uri uriFoto = Uri.parse(plato.getFotoUri());//cargar foto directamente desde uri
                holder.ivFoto.setImageURI(uriFoto);
            } else {
                holder.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        @Override
        public int getItemCount() { return platos.size(); }

        class PlatoViewHolder extends RecyclerView.ViewHolder {
            ImageView ivFoto; TextView tvNombre, tvDesc;
            public PlatoViewHolder(@NonNull View itemView) {
                super(itemView);
                ivFoto = itemView.findViewById(R.id.ivFilaFoto);
                tvNombre = itemView.findViewById(R.id.tvFilaNombre);
                tvDesc = itemView.findViewById(R.id.tvFilaDesc);
            }
        }
    }
}