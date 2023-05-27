package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samba.databinding.ActivityComprarBinding;
import com.example.samba.databinding.ActivityTramitandoPedidoBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Tramitando_Pedido extends AppCompatActivity {

    private ActivityTramitandoPedidoBinding binding;
    private ArrayList<Model_Camisetas_Tienda> camisetasTiendaList;
    double precioTotal;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityTramitandoPedidoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCamisetasCarrito();



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

                        binding.importe.setText(String.valueOf(precioTotal) + " €");
                        binding.numero.setText(""+camisetasTiendaList.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}