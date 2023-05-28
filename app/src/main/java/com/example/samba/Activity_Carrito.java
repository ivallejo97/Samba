package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.samba.adapter.Adapter_Camisetas_Carrito;
import com.example.samba.adapter.Adapter_Favoritos;
import com.example.samba.databinding.ActivityCarritoBinding;
import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Carrito extends AppCompatActivity {

    ActivityCarritoBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private Adapter_Camisetas_Carrito adapterCamisetasCarrito;
    double precioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCarritoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCamisetasCarrito();


        binding.botonCerrarVentana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(Activity_Carrito.this, Activity_Comprar.class));
                startActivity(intent);
            }
        });
    }

    private void cargarCamisetasCarrito() {
        camisetasTiendaList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Carrito")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        camisetasTiendaList.clear();
                        precioTotal = 0.0;


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String idCamiseta = "" + dataSnapshot.child("idCamiseta").getValue();
                            Model_Camisetas_Tienda modelCamisetasTienda = new Model_Camisetas_Tienda();
                            modelCamisetasTienda.setId(idCamiseta);
                            double precio = Double.parseDouble("" + dataSnapshot.child("precioTotal").getValue());

                            precioTotal += precio;

                            camisetasTiendaList.add(modelCamisetasTienda);
                        }
                        binding.numeroFavoritos.setText("(" + camisetasTiendaList.size() + ")");
                        adapterCamisetasCarrito = new Adapter_Camisetas_Carrito(Activity_Carrito.this, camisetasTiendaList);
                        binding.recyclerCamisetasCarrito.setAdapter(adapterCamisetasCarrito);

                        binding.precioTotal.setText(String.valueOf(precioTotal));

                        if (camisetasTiendaList.size() > 0){
                            binding.botonFinalizarCompra.setVisibility(View.VISIBLE);
                        } else {
                            binding.botonFinalizarCompra.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}