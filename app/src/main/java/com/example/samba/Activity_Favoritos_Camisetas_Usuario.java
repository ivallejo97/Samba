package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.samba.adapter.Adapter_Favoritos;
import com.example.samba.adapter.Adapter_Favoritos_Camisetas_Usuarios;
import com.example.samba.databinding.ActivityFavoritosBinding;
import com.example.samba.databinding.ActivityFavoritosCamisetasUsuarioBinding;
import com.example.samba.model.Model_Camisetas_Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Favoritos_Camisetas_Usuario extends AppCompatActivity {


    ActivityFavoritosCamisetasUsuarioBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList;
    private Adapter_Favoritos_Camisetas_Usuarios adapterFavoritos;

    String id, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityFavoritosCamisetasUsuarioBinding.inflate(getLayoutInflater())).getRoot());


        firebaseAuth = FirebaseAuth.getInstance();
        cargarCamisetasFavoritos();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        uid = intent.getStringExtra("uid");


        binding.botonCerrarVentana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void cargarCamisetasFavoritos() {
        camisetasUsuariosList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("FavoritosUsuarios")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        camisetasUsuariosList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String idCamiseta = "" + dataSnapshot.child("idCamiseta").getValue();
                            Model_Camisetas_Usuario modelCamisetasUsuario = new Model_Camisetas_Usuario();
                            modelCamisetasUsuario.setId(idCamiseta);

                            camisetasUsuariosList.add(modelCamisetasUsuario);
                        }

                        binding.numeroFavoritos.setText("(" + camisetasUsuariosList.size() + ")");
                        adapterFavoritos = new Adapter_Favoritos_Camisetas_Usuarios(Activity_Favoritos_Camisetas_Usuario.this, camisetasUsuariosList);
                        binding.recyclerCamisetasFavoritos.setAdapter(adapterFavoritos);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}