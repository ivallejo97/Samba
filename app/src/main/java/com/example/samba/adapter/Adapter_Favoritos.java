package com.example.samba.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.Activity_Camiseta_Tienda;
import com.example.samba.MetodosApp;
import com.example.samba.databinding.EstiloCamisetasBinding;
import com.example.samba.databinding.EstiloCamisetasFavoritosBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Favoritos extends RecyclerView.Adapter<Adapter_Favoritos.HolderFavoritos> {

    private Context context;
    private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private EstiloCamisetasFavoritosBinding binding;

    public Adapter_Favoritos(Context context, ArrayList<Model_Camisetas_Tienda> camisetasTiendaList) {
        this.context = context;
        this.camisetasTiendaList = camisetasTiendaList;
    }

    @NonNull
    @Override
    public HolderFavoritos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCamisetasFavoritosBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Favoritos.HolderFavoritos(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFavoritos holder, int position) {
        Model_Camisetas_Tienda modelCamisetasTienda = camisetasTiendaList.get(position);

        cargarDatosCamisetas(modelCamisetasTienda,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , Activity_Camiseta_Tienda.class);
                intent.putExtra("id",modelCamisetasTienda.getId());
                intent.putExtra("categoriaId", modelCamisetasTienda.getCategoriaId());
                context.startActivity(intent);
            }
        });

        holder.botonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar de favoritos")
                        .setMessage("¿Estas seguro de que quieres eliminar este producto de favoritos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MetodosApp.eliminarFavoritos(context,modelCamisetasTienda.getId());
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
    private void cargarDatosCamisetas(Model_Camisetas_Tienda modelCamisetasTienda, HolderFavoritos holder) {
        String idCamiseta = modelCamisetasTienda.getId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombreCamiseta = "" + snapshot.child("titulo").getValue();
                        String precioCamiseta = "" + snapshot.child("precio").getValue();
                        String marcaCamiseta = "" + snapshot.child("marca").getValue();
                        String categoria = "" + snapshot.child("categoriaId").getValue();
                        String fotoCamiseta = "" + snapshot.child("url").getValue();

                        modelCamisetasTienda.setFavorito(true);
                        modelCamisetasTienda.setTitulo(nombreCamiseta);
                        modelCamisetasTienda.setPrecio(precioCamiseta);
                        modelCamisetasTienda.setMarca(marcaCamiseta);
                        modelCamisetasTienda.setFotoCategoria(fotoCamiseta);
                        modelCamisetasTienda.setCategoriaId(categoria);


                        holder.nombreCamiseta.setText(nombreCamiseta);
                        holder.precioCamiseta.setText(precioCamiseta);
                        holder.marcaCamiseta.setText(marcaCamiseta);
                        Glide.with(context.getApplicationContext()).load(fotoCamiseta).into(holder.fotoCamiseta);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    @Override
    public int getItemCount() {
        return camisetasTiendaList.size();
    }

    class HolderFavoritos extends RecyclerView.ViewHolder{

        ShapeableImageView fotoCamiseta;
        TextView nombreCamiseta, precioCamiseta, marcaCamiseta;
        ImageView botonFavoritos;

        public HolderFavoritos(@NonNull View itemView) {
            super(itemView);

            fotoCamiseta = binding.fotoProducto;
            nombreCamiseta = binding.nombreProducto;
            precioCamiseta = binding.precioProducto;
            botonFavoritos = binding.botonFavoritos;
            marcaCamiseta = binding.marcaProducto;
        }
    }
}
