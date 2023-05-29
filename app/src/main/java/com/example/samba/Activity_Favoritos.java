package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.adapter.Adapter_Favoritos;
import com.example.samba.databinding.ActivityFavoritosBinding;
import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Favoritos extends AppCompatActivity {

    ActivityFavoritosBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private Adapter_Favoritos adapterFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityFavoritosBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCamisetasFavoritas();

        binding.botonCerrarVentana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    // Cargar todas las camisetas a las cuales el usuario actual ha dado like, estas camisetas se guardan dentro de un nuevo apartado
    // en el usuario en el cual se guarda la id de la camiseta y la fecha en la que se le ha dado like a ella.
    private void cargarCamisetasFavoritas() {
        camisetasTiendaList = new ArrayList<>();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Favoritos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        camisetasTiendaList.clear();
                        //Obtener el valor de cada camiseta que se haya añadido a favoritos y añadirlo a una ArryList para despues
                        // mostrar con el adaptador esas camiseta en un recycler view.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String idCamiseta = "" + dataSnapshot.child("idCamiseta").getValue();
                            Model_Camisetas_Tienda modelCamisetasTienda = new Model_Camisetas_Tienda();
                            modelCamisetasTienda.setId(idCamiseta);

                            camisetasTiendaList.add(modelCamisetasTienda);
                        }

                        binding.numeroFavoritos.setText("(" + camisetasTiendaList.size() + ")");
                        adapterFavoritos = new Adapter_Favoritos(Activity_Favoritos.this, camisetasTiendaList);
                        binding.recyclerCamisetasFavoritos.setAdapter(adapterFavoritos);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}