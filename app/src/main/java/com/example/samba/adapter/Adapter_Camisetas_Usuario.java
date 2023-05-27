package com.example.samba.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.Activity_Camiseta_Usuario;
import com.example.samba.R;
import com.example.samba.databinding.EstiloCamisetasUsuarioBinding;
import com.example.samba.model.Model_Camisetas_Usuario;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Camisetas_Usuario extends RecyclerView.Adapter<Adapter_Camisetas_Usuario.HolderCamisetasUsuario> {

    private Context context;
    public ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList;
    EstiloCamisetasUsuarioBinding binding;
    private FirebaseAuth firebaseAuth;

    public Adapter_Camisetas_Usuario(Context context, ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList) {
        this.context = context;
        this.camisetasUsuariosList = camisetasUsuariosList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderCamisetasUsuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloCamisetasUsuarioBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Camisetas_Usuario.HolderCamisetasUsuario(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCamisetasUsuario holder, int position) {
        Model_Camisetas_Usuario modelCamisetasUsuario = camisetasUsuariosList.get(position);

        String nombreUsuario= "" + modelCamisetasUsuario.getUid();
        String idCamiseta = "" + modelCamisetasUsuario.getId();
        String fotoCamiseta = "" + modelCamisetasUsuario.getFotoProducto();
        String tituloCamiseta = "" + modelCamisetasUsuario.getTitulo();
        String precioCamiseta = "" + modelCamisetasUsuario.getPrecio();
        String visitasCamiseta = "" + modelCamisetasUsuario.getNumeroVisitas();

        //holder.nombreUsuario.setText(nombreUsuario);
        holder.precioCamiseta.setText(precioCamiseta);
        holder.tituloCamiseta.setText(tituloCamiseta);
        Glide.with(context).load(fotoCamiseta).into(holder.fotoCamiseta);

        comprobarFavorito(idCamiseta,holder);
        cargarDatosUsuario(modelCamisetasUsuario,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Camiseta_Usuario.class);
                intent.putExtra("id",idCamiseta);
                intent.putExtra("numeroVisitas", visitasCamiseta);
                intent.putExtra("uid", "" + modelCamisetasUsuario.getUid());
                context.startActivity(intent);
            }
        });

    }
    String idUser;
    private void cargarDatosUsuario(Model_Camisetas_Usuario modelCamisetasUsuario, HolderCamisetasUsuario holder) {
        idUser =  "" + modelCamisetasUsuario.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(idUser)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nombreUsuario = "" + snapshot.child("name").getValue();
                    String fotoUsuario = "" + snapshot.child("profileImage").getValue();
                    holder.nombreUsuario.setText(nombreUsuario);
                    if (fotoUsuario.equals("")){
                        binding.iconoUsuario.setImageResource(R.drawable.icono_perfil_predeterminado);
                    } else {
                        Glide.with(context).load(fotoUsuario).into(holder.fotoUsuario);
                    }
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


    private void comprobarFavorito(String idCamiseta, Adapter_Camisetas_Usuario.HolderCamisetasUsuario holder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("FavoritosUsuarios").child(idCamiseta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean favorito = snapshot.exists();
                        if (favorito) {
                            holder.botonFavoritos.setImageResource(R.drawable.icono_favorito_marcado);
                        } else {
                            holder.botonFavoritos.setImageResource(R.drawable.icono_favoritos_perfil);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar error de base de datos
                    }
                });
    }


    class HolderCamisetasUsuario extends RecyclerView.ViewHolder{

        CircleImageView fotoUsuario;
        TextView nombreUsuario;
        ShapeableImageView fotoCamiseta;
        TextView tituloCamiseta;
        ImageView botonFavoritos;
        TextView precioCamiseta;

        public HolderCamisetasUsuario(@NonNull View itemView) {
            super(itemView);

            fotoUsuario = binding.iconoUsuario;
            nombreUsuario = binding.nombreUsuario;
            fotoCamiseta = binding.fotoProducto;
            tituloCamiseta = binding.nombreProducto;
            botonFavoritos = binding.botonFavoritos;
            precioCamiseta = binding.precioProducto;

        }
    }

}
