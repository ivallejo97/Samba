package com.example.samba;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.EstiloCategoriaBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class Adapter_Categoria extends RecyclerView.Adapter<Adapter_Categoria.HolderCategoria> implements Filterable {

    private Context context;
    public ArrayList<Model_Categoria> categoriaArrayList, listaCategorias;
    private EstiloCategoriaBinding binding;
    private Categoria_Filtrar filtrarCategoria;

    public Adapter_Categoria(Context context, ArrayList<Model_Categoria> categoriaArrayList) {
        this.context = context;
        this.categoriaArrayList = categoriaArrayList;
        this.listaCategorias = categoriaArrayList;
    }

    @NonNull
    @Override
    public HolderCategoria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCategoriaBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderCategoria(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategoria holder, int position) {
        Model_Categoria model = categoriaArrayList.get(position);
        String id = model.getId();
        String categoria = model.getCategoria();
        String descripcion_categoria = model.getDescripcionCategoria();
        String foto_categoria = model.getFotoCategoria();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        holder.categoria.setText(categoria);

        Glide.with(context).load(model.getFotoCategoria()).into(holder.foto_categoria);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Activity_Productos_Tienda.class);
                intent.putExtra("categoriaId" , id);
                intent.putExtra("categoria" , categoria);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriaArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filtrarCategoria == null){
            filtrarCategoria = new Categoria_Filtrar(listaCategorias,this);
        }
        return filtrarCategoria;
    }

    class HolderCategoria extends RecyclerView.ViewHolder{

        TextView categoria;
        ShapeableImageView foto_categoria;
        public HolderCategoria(@NonNull View itemView) {
            super(itemView);

            categoria = binding.nombreCategoria;
            foto_categoria = binding.fotoCategoria;
        }
    }
}
