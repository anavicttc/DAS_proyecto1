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
        Button btnGaleria = view.findViewById(R.id.btnFoto);
        btnGaleria.setText(R.string.galeria);

        imagenPreviaDialogo = view.findViewById(R.id.ivFotoPrevia);
        rutaFoto = ""; //limpiamos por si aca

        //intent -> abrir galería
        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, CODIGO_GALERIA);
        });
        builder.setView(view)
                .setTitle(R.string.nuevo_plato)
                .setPositiveButton(R.string.guardar, (dialog, id) -> {
                    String nombre = etNombre.getText().toString();
                    String desc = etDesc.getText().toString();

                    if (!nombre.isEmpty()) {
                        dbHelper.insertarPlato(nombre, desc, rutaFoto);
                        cargarPlatos();
                    } else {
                        Toast.makeText(requireContext(), R.string.anadir_plato_error, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancelar, null);
        builder.create().show();
    }

    //rdo galería
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_GALERIA && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData(); //obtener ruta directa
            if (uri != null) {
                //añadimos permiso para conocer la uri eternamente, si no, la app crashea
                requireActivity().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                rutaFoto = uri.toString(); //convertir a string para guardar en la bd
                //ventanita de la izq
                if (imagenPreviaDialogo != null) {
                    imagenPreviaDialogo.setImageURI(uri);
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
                try { //try catch necesario para lo del permiso eterno, porq como no es completamente legal guardar la foto así
                    Uri uriFoto = Uri.parse(plato.getFotoUri());//cargar foto directamente desde uri
                    holder.ivFoto.setImageURI(uriFoto);
                }
                catch (SecurityException e){
                    //si no hay permiso o la foto se ha borrado del móvil, ponemos la de por defecto
                    holder.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
                }
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