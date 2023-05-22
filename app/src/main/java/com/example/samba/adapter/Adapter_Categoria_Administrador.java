package com.example.samba.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.Activity_Productos_Tienda;
import com.example.samba.databinding.EstiloCategoriaAdministradorBinding;
import com.example.samba.databinding.EstiloCategoriaBinding;
import com.example.samba.filtros.Categoria_Filtrar;
import com.example.samba.model.Model_Categoria;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter_Categoria_Administrador extends RecyclerView.Adapter<Adapter_Categoria_Administrador.HolderCategoriaAdministrador> {

    private Context context;
    public ArrayList<Model_Categoria> categoriaArrayList ;
    private EstiloCategoriaAdministradorBinding binding;

    public Adapter_Categoria_Administrador(Context context, ArrayList<Model_Categoria> categoriaArrayList) {
        this.context = context;
        this.categoriaArrayList = categoriaArrayList;
    }

    @NonNull
    @Override
    public HolderCategoriaAdministrador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCategoriaAdministradorBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderCategoriaAdministrador(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategoriaAdministrador holder, int position) {
        Model_Categoria model = categoriaArrayList.get(position);
        String id = model.getId();
        String categoria = model.getCategoria();
        String descripcion_categoria = model.getDescripcionCategoria();
        String foto_categoria = model.getFotoCategoria();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        holder.categoria.setText(categoria);


        holder.boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Categoria")
                        .setMessage("¿Estas seguro de que quieres eliminar esta categoria?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarCategoria(model,holder);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    private void eliminarCategoria(Model_Categoria model, HolderCategoriaAdministrador holder) {

        String idCategoria = model.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.child(idCategoria)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Categoria eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoriaArrayList.size();
    }



    class HolderCategoriaAdministrador extends RecyclerView.ViewHolder{

        TextView categoria;
        ImageView boton_eliminar;
        public HolderCategoriaAdministrador(@NonNull View itemView) {
            super(itemView);

            categoria = binding.nombreCategoria;
            boton_eliminar = binding.botonEliminar;
        }
    }
}
