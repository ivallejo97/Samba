package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.samba.adapter.Adapter_Camisetas_Carrito;
import com.example.samba.databinding.ActivityPedidoFinalizadoBinding;
import com.example.samba.databinding.ActivityTramitandoPedidoBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Pedido_Finalizado extends AppCompatActivity {

    private ActivityPedidoFinalizadoBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    private Adapter_Camisetas_Carrito adapterCamisetasCarrito;
    double precioTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPedidoFinalizadoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCamisetasCarrito();

        binding.botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciarCarrito();
                MetodosApp.addCamisetaPedidos(Activity_Pedido_Finalizado.this,"" + camisetasTiendaList.size(),"" + precioTotal);
                startActivity(new Intent(Activity_Pedido_Finalizado.this, Activity_Destinos_Principales.class));
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
                        binding.numero.setText("" + camisetasTiendaList.size());
                        adapterCamisetasCarrito = new Adapter_Camisetas_Carrito(Activity_Pedido_Finalizado.this, camisetasTiendaList);
                        binding.recyclerCamisetasCarrito.setAdapter(adapterCamisetasCarrito);

                        binding.importe.setText(String.valueOf(precioTotal) + "€");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void vaciarCarrito(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Carrito")
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Activity_Pedido_Finalizado.this,"Camiseta eliminada del carrito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Pedido_Finalizado.this,"Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onBackPressed() {

    }
}