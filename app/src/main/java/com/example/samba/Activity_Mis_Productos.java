package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.adapter.Adapter_Camisetas_Usuario;
import com.example.samba.adapter.Adapter_Mis_Camisetas_Usuario;
import com.example.samba.databinding.ActivityMisProductosBinding;
import com.example.samba.databinding.ActivityProductosTiendaBinding;
import com.example.samba.model.Model_Camisetas_Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Mis_Productos extends AppCompatActivity {

    private ActivityMisProductosBinding binding;
    private ArrayList<Model_Camisetas_Usuario> camisetasUsuarioList;
    private Adapter_Mis_Camisetas_Usuario adapterCamisetasUsuario;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMisProductosBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarListaMisCamisetas();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void cargarListaMisCamisetas() {
        camisetasUsuarioList = new ArrayList<>();

        // Obtener la id del usuario actual
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
        databaseReference.orderByChild("uid").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        camisetasUsuarioList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Model_Camisetas_Usuario modelCamisetasUsuario = dataSnapshot.getValue(Model_Camisetas_Usuario.class);
                            camisetasUsuarioList.add(modelCamisetasUsuario);
                        }
                        adapterCamisetasUsuario = new Adapter_Mis_Camisetas_Usuario(Activity_Mis_Productos.this,camisetasUsuarioList);
                        binding.recyclerMisProductos.setAdapter(adapterCamisetasUsuario);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}