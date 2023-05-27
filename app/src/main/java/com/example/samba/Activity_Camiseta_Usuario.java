package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.ActivityCamisetaUsuarioBinding;
import com.example.samba.databinding.ActivityDestinosPrincipalesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Activity_Camiseta_Usuario extends AppCompatActivity {

    ActivityCamisetaUsuarioBinding binding;
    String idCamiseta, idUser;
    boolean favorito = false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCamisetaUsuarioBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        idCamiseta = intent.getStringExtra("id");
        idUser = intent.getStringExtra("uid");

        comprobarFavoritos();
        aumentarNumeroDeVisitas(idCamiseta);
        cargarInformacionCamiseta();
        cargarDatosUsuario();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonIniciarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Camiseta_Usuario.this, Activity_Chat.class);
                intent.putExtra("uid",idUser);
                startActivity(intent);
            }
        });

        binding.botonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorito){
                    MetodosApp.eliminarFavoritosCamisetasUsuarios(getApplicationContext(),idCamiseta);
                    intent.putExtra("uid",idUser);
                } else {
                    MetodosApp.addCamisetaFavoritosUsuarios(getApplicationContext(),idCamiseta);
                    intent.putExtra("uid",idUser);
                }
            }
        });

    }


    private void aumentarNumeroDeVisitas(String idCamiseta) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");

        databaseReference.child(idCamiseta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String numeroVisitas = "" + snapshot.child("numeroVisitas").getValue();
                        if (numeroVisitas.equals("") || numeroVisitas.equals("null")){
                            numeroVisitas = "0";
                        }

                        long nuevoNumeroVisitas = Long.parseLong(numeroVisitas) + 1;
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("numeroVisitas",nuevoNumeroVisitas);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
                        reference.child(idCamiseta)
                                .updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void cargarInformacionCamiseta() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
        databaseReference.child(idCamiseta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_camiseta = "" + snapshot.child("titulo").getValue();
                        String talla_camiseta = "" + snapshot.child("talla").getValue();
                        String marca_camiseta = "" + snapshot.child("marca").getValue();
                        String precio_camiseta = "" + snapshot.child("precio").getValue();
                        String descripcion_camiseta = "" + snapshot.child("descripcion").getValue();
                        String foto_camiseta = "" + snapshot.child("fotoProducto").getValue();
                        String visitas_camiseta = "" + snapshot.child("numeroVisitas").getValue();

                        binding.nombreCamiseta.setText(nombre_camiseta);
                        binding.tallaCamiseta.setText(talla_camiseta);
                        binding.marcaCamiseta.setText(marca_camiseta);
                        binding.precioCamiseta.setText(precio_camiseta);
                        binding.descripcionCamiseta.setText(descripcion_camiseta);
                        binding.numeroVisitasCamiseta.setText(visitas_camiseta.replace("null","N/A"));
                        Glide.with(Activity_Camiseta_Usuario.this).load(foto_camiseta).into(binding.fotoCamiseta);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void cargarDatosUsuario() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(idUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_usuario = "" + snapshot.child("name").getValue();
                        String foto_usuario = "" + snapshot.child("profileImage").getValue();


                        binding.nombreUsuario.setText(nombre_usuario);
                        if (foto_usuario.equals("")){
                            binding.fotoUsuario.setImageResource(R.drawable.icono_perfil_predeterminado);
                        } else {
                            Glide.with(Activity_Camiseta_Usuario.this).load(foto_usuario).into(binding.fotoUsuario);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void comprobarFavoritos() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("FavoritosUsuarios").child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favorito = snapshot.exists();
                        if (favorito){
                            binding.botonFavoritos.setImageResource(R.drawable.icono_favorito_marcado);
                        } else {
                            binding.botonFavoritos.setImageResource(R.drawable.icono_favoritos_perfil);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}