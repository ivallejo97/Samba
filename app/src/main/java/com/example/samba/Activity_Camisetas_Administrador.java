package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samba.adapter.Adapter_Camisetas_Administrador;
import com.example.samba.adapter.Adapter_Camisetas_Tienda;
import com.example.samba.databinding.ActivityCamisetasAdministradorBinding;
import com.example.samba.databinding.ActivityProductosTiendaBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Activity_Camisetas_Administrador extends AppCompatActivity {

    ActivityCamisetasAdministradorBinding binding;

    private ArrayList<Model_Camisetas_Tienda> modelCamisetasTiendaArrayList;
    private Adapter_Camisetas_Administrador adapterCamisetasAdministrador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCamisetasAdministradorBinding.inflate(getLayoutInflater())).getRoot());

        cargarListaCamisetas();

    }

    private void cargarListaCamisetas() {
        modelCamisetasTiendaArrayList  = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.orderByChild("numeroVisitas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelCamisetasTiendaArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Camisetas_Tienda camisetasTienda = dataSnapshot.getValue(Model_Camisetas_Tienda.class);
                    modelCamisetasTiendaArrayList.add(camisetasTienda);
                }
                Collections.reverse(modelCamisetasTiendaArrayList);
                adapterCamisetasAdministrador = new Adapter_Camisetas_Administrador(Activity_Camisetas_Administrador.this,modelCamisetasTiendaArrayList);
                binding.recyclerCamisetasTiendaAdministrador.setAdapter(adapterCamisetasAdministrador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}