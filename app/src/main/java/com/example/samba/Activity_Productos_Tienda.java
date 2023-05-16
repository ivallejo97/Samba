package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.ActivityProductosTiendaBinding;
import com.example.samba.databinding.ActivityPublicarBinding;
import com.example.samba.databinding.DestinosPrincipalesProductosTiendaBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Productos_Tienda extends AppCompatActivity {

    private ActivityProductosTiendaBinding binding;
    private ArrayList<Model_Camisetas_Tienda> modelCamisetasTiendaArrayList;
    private Adapter_Camisetas_Tienda adapterCamisetasTienda;
    String idCategoria, categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityProductosTiendaBinding.inflate(getLayoutInflater())).getRoot());

        Intent intent = getIntent();
        idCategoria = intent.getStringExtra("categoriaId");
        categoria = intent.getStringExtra("categoria");


        cargarListaCamisetas();

        binding.titulo.setText(categoria);


        binding.buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterCamisetasTienda.getFilter().filter(s);
                } catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void cargarListaCamisetas() {
        modelCamisetasTiendaArrayList  = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.orderByChild("categoriaId").equalTo(idCategoria)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelCamisetasTiendaArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Model_Camisetas_Tienda camisetasTienda = snapshot.getValue(Model_Camisetas_Tienda.class);
                            modelCamisetasTiendaArrayList.add(camisetasTienda);
                        }
                        adapterCamisetasTienda = new Adapter_Camisetas_Tienda(Activity_Productos_Tienda.this,modelCamisetasTiendaArrayList);
                        binding.recyclerCamisetasTienda.setAdapter(adapterCamisetasTienda);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}