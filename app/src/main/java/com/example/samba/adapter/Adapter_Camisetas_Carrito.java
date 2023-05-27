package com.example.samba.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.MetodosApp;
import com.example.samba.R;
import com.example.samba.databinding.EstiloCamisetasCarritoBinding;
import com.example.samba.databinding.EstiloCamisetasFavoritosBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Camisetas_Carrito extends RecyclerView.Adapter<Adapter_Camisetas_Carrito.HolderCamisetasCarrito> {

    private Context context;
    private ArrayList<Model_Camisetas_Tienda> camisetasCarritoList;
    private EstiloCamisetasCarritoBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public Adapter_Camisetas_Carrito(Context context, ArrayList<Model_Camisetas_Tienda> camisetasCarritoList) {
        this.context = context;
        this.camisetasCarritoList = camisetasCarritoList;
    }

    @NonNull
    @Override
    public HolderCamisetasCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCamisetasCarritoBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Camisetas_Carrito.HolderCamisetasCarrito(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCamisetasCarrito holder, int position) {
        Model_Camisetas_Tienda modelCamisetasTienda = camisetasCarritoList.get(position);

        cargarDatosCamisetas(modelCamisetasTienda,holder);
        cargarDatosCarrito(modelCamisetasTienda, holder);

        holder.botonEliminarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar del carrito")
                        .setMessage("¿Estas seguro de que quieres eliminar este producto del carrito?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MetodosApp.eliminarCarrito(context,modelCamisetasTienda.getId(),cantidad,talla,personalizacion,precioTotal);
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
    String cantidad, talla, personalizacion, precioTotal;
    private void cargarDatosCarrito(Model_Camisetas_Tienda modelCamisetasTienda, HolderCamisetasCarrito holder) {
        String idCamiseta = "" + modelCamisetasTienda.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Carrito").child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         cantidad = "" + snapshot.child("cantidad").getValue();
                         talla = "" + snapshot.child("talla").getValue();
                         personalizacion = "" + snapshot.child("personalizacion").getValue();
                         precioTotal = "" + snapshot.child("precioTotal").getValue();

                        holder.cantidad.setText(cantidad);
                        holder.talla.setText(talla);
                        holder.personalizacion.setText(personalizacion);
                        holder.precio.setText(precioTotal);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void cargarDatosCamisetas(Model_Camisetas_Tienda modelCamisetasTienda, HolderCamisetasCarrito holder) {

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
                        //holder.precio.setText(precioCamiseta);
                        Glide.with(context.getApplicationContext()).load(fotoCamiseta).into(holder.fotoCamiseta);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return camisetasCarritoList.size();
    }


    class HolderCamisetasCarrito extends RecyclerView.ViewHolder{

        TextView precio, nombreCamiseta, talla, cantidad, personalizacion;
        ShapeableImageView fotoCamiseta;
        ImageView botonEliminarCarrito;


        public HolderCamisetasCarrito(@NonNull View itemView) {
            super(itemView);

            precio = binding.precioProducto;
            nombreCamiseta = binding.nombreProducto;
            talla = binding.tallaProducto;
            cantidad = binding.cantidadProducto;
            personalizacion = binding.personalizacionProducto;
            fotoCamiseta = binding.fotoProducto;
            botonEliminarCarrito = binding.botonCarrito;
        }
    }
}
