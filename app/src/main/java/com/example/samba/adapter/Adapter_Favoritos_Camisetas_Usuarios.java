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
import com.example.samba.Activity_Camiseta_Usuario;
import com.example.samba.Activity_Favoritos_Camisetas_Usuario;
import com.example.samba.MetodosApp;
import com.example.samba.R;
import com.example.samba.databinding.EstiloCamisetasFavoritosBinding;
import com.example.samba.databinding.EstiloCamisetasFavoritosUsuarioBinding;
import com.example.samba.model.Model_Camisetas_Usuario;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Favoritos_Camisetas_Usuarios extends RecyclerView.Adapter<Adapter_Favoritos_Camisetas_Usuarios.HolderCamisetasFavoritosUsuario> {


    private Context context;
    private ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList;
    private EstiloCamisetasFavoritosUsuarioBinding binding;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public Adapter_Favoritos_Camisetas_Usuarios(Context context, ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList) {
        this.context = context;
        this.camisetasUsuariosList = camisetasUsuariosList;
    }

    @NonNull
    @Override
    public HolderCamisetasFavoritosUsuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCamisetasFavoritosUsuarioBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Favoritos_Camisetas_Usuarios.HolderCamisetasFavoritosUsuario(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCamisetasFavoritosUsuario holder, int position) {
        Model_Camisetas_Usuario modelCamisetasUsuario = camisetasUsuariosList.get(position);

        cargarDatosUsuario(modelCamisetasUsuario,holder);
        cargarDatosCamisetas(modelCamisetasUsuario,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , Activity_Camiseta_Usuario.class);
                intent.putExtra("id", "" + modelCamisetasUsuario.getId());
                intent.putExtra("uid", "" + modelCamisetasUsuario.getUid());
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
                                MetodosApp.eliminarFavoritosCamisetasUsuarios(context,modelCamisetasUsuario.getId());
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

    private void cargarDatosCamisetas(Model_Camisetas_Usuario modelCamisetasUsuario, HolderCamisetasFavoritosUsuario holder) {


        String idCamiseta = modelCamisetasUsuario.getId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
        databaseReference.child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombreCamiseta = "" + snapshot.child("titulo").getValue();
                        String precioCamiseta = "" + snapshot.child("precio").getValue();
                        String marcaCamiseta = "" + snapshot.child("marca").getValue();
                        String tallaCamiseta = "" + snapshot.child("talla").getValue();
                        String fotoCamiseta = "" + snapshot.child("fotoProducto").getValue();

                        //modelCamisetasUsuario.setFavorito(true);
                        modelCamisetasUsuario.setTitulo(nombreCamiseta);
                        modelCamisetasUsuario.setPrecio(precioCamiseta);
                        modelCamisetasUsuario.setMarca(marcaCamiseta);
                        modelCamisetasUsuario.setTalla(tallaCamiseta);
                        modelCamisetasUsuario.setFotoProducto(fotoCamiseta);


                        holder.nombreCamiseta.setText(nombreCamiseta);
                        holder.precioCamiseta.setText(precioCamiseta);
                        holder.marcaCamiseta.setText(marcaCamiseta);
                        holder.tallaCamiseta.setText(tallaCamiseta);
                        Glide.with(context.getApplicationContext()).load(fotoCamiseta).into(holder.fotoCamiseta);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return camisetasUsuariosList.size();
    }


    private void cargarDatosUsuario(Model_Camisetas_Usuario modelCamisetasUsuario, Adapter_Favoritos_Camisetas_Usuarios.HolderCamisetasFavoritosUsuario holder) {
        String idUser = "" + modelCamisetasUsuario.getUid();
        String idCamiseta = "" + modelCamisetasUsuario.getId();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombreUsuario = "" + snapshot.child("name").getValue();
                        holder.nombreUsuario.setText(nombreUsuario);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar el error de la base de datos
                    }
                });
    }


    class HolderCamisetasFavoritosUsuario extends RecyclerView.ViewHolder{

        ShapeableImageView fotoCamiseta;
        TextView nombreCamiseta, precioCamiseta, marcaCamiseta, tallaCamiseta, nombreUsuario;
        ImageView botonFavoritos;
        public HolderCamisetasFavoritosUsuario(@NonNull View itemView) {
            super(itemView);

            fotoCamiseta = binding.fotoProducto;
            nombreCamiseta = binding.nombreProducto;
            precioCamiseta = binding.precioProducto;
            botonFavoritos = binding.botonFavoritos;
            marcaCamiseta = binding.marcaProducto;
            tallaCamiseta = binding.talla;
            nombreUsuario = binding.nombreUsuario;
        }
    }




}
