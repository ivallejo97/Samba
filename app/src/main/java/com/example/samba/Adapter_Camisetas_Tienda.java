package com.example.samba;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.EstiloCamisetasBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Camisetas_Tienda extends RecyclerView.Adapter<Adapter_Camisetas_Tienda.HolderCamisetasTienda> implements Filterable {

    private Context context;
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
        String nombreUsuario = "" + camisetasTiendaArrayList.get(position).uid;
        String fotoCamiseta = "" + modelCamisetasTienda.getUrl();
        String nombreCamiseta = "" + modelCamisetasTienda.getTitulo();
        String nombrePrecioCamiseta = "" + modelCamisetasTienda.getPrecio();
        long timestamp = modelCamisetasTienda.getTimestamp();

        holder.nombreUsuario.setText(nombreUsuario);
        holder.nombreCamiseta.setText(nombreCamiseta);
        Glide.with(context).load(modelCamisetasTienda.getUrl()).into(holder.fotoCamiseta);
        holder.precioCamiseta.setText(nombrePrecioCamiseta);

        cargarCategoria(modelCamisetasTienda,holder);
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
                        holder.nombreCamiseta.setText(categoria);
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

        CircleImageView fotoUsuario;
        TextView nombreUsuario;
        ImageView fotoCamiseta;
        TextView nombreCamiseta;
        ImageButton botonFavoritos;
        TextView precioCamiseta;

        public HolderCamisetasTienda(@NonNull View itemView) {
            super(itemView);

            fotoUsuario = binding.iconoUsuario;
            nombreUsuario = binding.nombreUsuario;
            fotoCamiseta = binding.fotoProducto;
            nombreCamiseta = binding.nombreProducto;
            botonFavoritos = binding.botonFavoritos;
            precioCamiseta = binding.precioProducto;

        }
    }


}
