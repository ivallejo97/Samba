package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samba.adapter.Adapter_Categoria;
import com.example.samba.adapter.Adapter_Categoria_Administrador;
import com.example.samba.databinding.ActivityCategoriasAdministradorBinding;
import com.example.samba.databinding.ActivityMisProductosBinding;
import com.example.samba.model.Model_Categoria;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Categorias_Administrador extends AppCompatActivity {

    private ActivityCategoriasAdministradorBinding binding;
    private ArrayList<Model_Categoria> modelCategorias;
    private Adapter_Categoria_Administrador adapterCategoriaAdministrador;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCategoriasAdministradorBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCategorias();
    }

    private void cargarCategorias() {
        modelCategorias = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelCategorias.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Categoria modelCategoria = dataSnapshot.getValue(Model_Categoria.class);
                    modelCategorias.add(modelCategoria);
                }
                adapterCategoriaAdministrador = new Adapter_Categoria_Administrador(Activity_Categorias_Administrador.this,modelCategorias);
                binding.recyclerCategoriasAdmin.setAdapter(adapterCategoriaAdministrador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}