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
import com.example.samba.Activity_Camiseta_Tienda;
import com.example.samba.Activity_Editar_Camiseta_Administrador;
import com.example.samba.Activity_Editar_Camiseta_Usuario;
import com.example.samba.MetodosApp;
import com.example.samba.R;
import com.example.samba.databinding.EstiloCamisetasAdministradorBinding;
import com.example.samba.databinding.EstiloCamisetasBinding;
import com.example.samba.databinding.EstiloCategoriaAdministradorBinding;
import com.example.samba.filtros.Filtro_Camisetas;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Camisetas_Administrador extends RecyclerView.Adapter<Adapter_Camisetas_Administrador.HolderCamisetasAdministrador> {

    private Context context;
    boolean favorito;
    public ArrayList<Model_Camisetas_Tienda> camisetasTiendaArrayList;
    private EstiloCamisetasAdministradorBinding binding;

    public Adapter_Camisetas_Administrador(Context context, ArrayList<Model_Camisetas_Tienda> camisetasTiendaArrayList) {
        this.context = context;
        this.camisetasTiendaArrayList = camisetasTiendaArrayList;
    }

    @NonNull
    @Override
    public HolderCamisetasAdministrador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCamisetasAdministradorBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderCamisetasAdministrador(binding.getRoot());
    }
    @Override
    public void onBindViewHolder(@NonNull HolderCamisetasAdministrador holder, int position) {
        Model_Camisetas_Tienda modelCamisetasTienda = camisetasTiendaArrayList.get(position);
        String idCamiseta = "" + modelCamisetasTienda.getId();
        String fotoCamiseta = "" + modelCamisetasTienda.getUrl();
        String categoriaId = "" + modelCamisetasTienda.getCategoriaId();
        String nombreCamiseta = "" + modelCamisetasTienda.getTitulo();
        String nombrePrecioCamiseta = "" + modelCamisetasTienda.getPrecio();
        String visitasCamiseta = "" + modelCamisetasTienda.getNumeroVisitas();
        favorito = modelCamisetasTienda.isFavorito();
        long timestamp = modelCamisetasTienda.getTimestamp();

        //holder.nombreUsuario.setText(nombreUsuario);
        holder.nombreCamiseta.setText(nombreCamiseta);
        Glide.with(context).load(fotoCamiseta).into(holder.fotoCamiseta);
        holder.precioCamiseta.setText(nombrePrecioCamiseta);

        cargarCategoria(modelCamisetasTienda,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Editar_Camiseta_Administrador.class);
                intent.putExtra("id" , modelCamisetasTienda.getId());
                context.startActivity(intent);
            }
        });

        holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Camiseta")
                        .setMessage("¿Estas seguro de que quieres eliminar esta camiseta?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarCamiseta(modelCamisetasTienda,holder);
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

    private void eliminarCamiseta(Model_Camisetas_Tienda modelCamisetasTienda, HolderCamisetasAdministrador holder) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.child(modelCamisetasTienda.getId())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Camiseta eliminada", Toast.LENGTH_SHORT).show();
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
        return camisetasTiendaArrayList.size();
    }

    private void cargarCategoria(Model_Camisetas_Tienda model, HolderCamisetasAdministrador holder) {
        String categoriaId = model.getCategoriaId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.child(categoriaId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String categoria = "" + snapshot.child("categoria").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    class HolderCamisetasAdministrador extends RecyclerView.ViewHolder {

        ShapeableImageView fotoCamiseta;
        TextView nombreCamiseta;
        ImageView botonEliminar;
        TextView precioCamiseta;

        public HolderCamisetasAdministrador(@NonNull View itemView) {
            super(itemView);

            fotoCamiseta = binding.fotoProducto;
            nombreCamiseta = binding.nombreProducto;
            botonEliminar = binding.botonEliminar;
            precioCamiseta = binding.precioProducto;

        }
    }


}
