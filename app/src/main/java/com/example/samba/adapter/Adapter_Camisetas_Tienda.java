package com.example.samba.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.Activity_Camiseta_Tienda;
import com.example.samba.MetodosApp;
import com.example.samba.R;
import com.example.samba.filtros.Filtro_Camisetas;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.example.samba.databinding.EstiloCamisetasBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Camisetas_Tienda extends RecyclerView.Adapter<Adapter_Camisetas_Tienda.HolderCamisetasTienda> implements Filterable {

    private Context context;
    boolean favorito;
    public ArrayList<Model_Camisetas_Tienda> camisetasTiendaArrayList, filtrarListaCamisetas;
    private Filtro_Camisetas filtroCamisetas;
    private EstiloCamisetasBinding binding;

    public Adapter_Camisetas_Tienda(Context context, ArrayList<Model_Camisetas_Tienda> camisetasTiendaArrayList) {
        this.context = context;
        this.camisetasTiendaArrayList = camisetasTiendaArrayList;
        this.filtrarListaCamisetas = camisetasTiendaArrayList;
    }

    @NonNull
    @Override
    public HolderCamisetasTienda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCamisetasBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderCamisetasTienda(binding.getRoot());
    }
    @Override
    public void onBindViewHolder(@NonNull HolderCamisetasTienda holder, int position) {
        Model_Camisetas_Tienda modelCamisetasTienda = camisetasTiendaArrayList.get(position);
        String nombreUsuario = "" + modelCamisetasTienda.getUid();
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
        Glide.with(context).load(modelCamisetasTienda.getUrl()).into(holder.fotoCamiseta);
        holder.precioCamiseta.setText(nombrePrecioCamiseta);

        if (modelCamisetasTienda.isFavorito()){
            holder.botonFavoritos.setImageResource(R.drawable.icono_favorito_marcado);
        } else {
            holder.botonFavoritos.setImageResource(R.drawable.icono_favoritos_perfil);
        }

        cargarCategoria(modelCamisetasTienda,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Camiseta_Tienda.class);
                intent.putExtra("id",idCamiseta);
                intent.putExtra("numeroVisitas", visitasCamiseta);
                intent.putExtra("categoriaId",categoriaId);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return camisetasTiendaArrayList.size();
    }

    private void cargarCategoria(Model_Camisetas_Tienda model, HolderCamisetasTienda holder) {
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

    @Override
    public Filter getFilter() {
        if (filtroCamisetas == null){
            filtroCamisetas = new Filtro_Camisetas(filtrarListaCamisetas,this);
        }

        return filtroCamisetas;
    }



    class HolderCamisetasTienda extends RecyclerView.ViewHolder {

        ShapeableImageView fotoCamiseta;
        TextView nombreCamiseta;
        ImageView botonFavoritos;
        TextView precioCamiseta;

        public HolderCamisetasTienda(@NonNull View itemView) {
            super(itemView);

            fotoCamiseta = binding.fotoProducto;
            nombreCamiseta = binding.nombreProducto;
            botonFavoritos = binding.botonFavoritos;
            precioCamiseta = binding.precioProducto;

        }
    }




}
