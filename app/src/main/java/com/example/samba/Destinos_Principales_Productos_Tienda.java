package com.example.samba;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samba.adapter.Adapter_Camisetas_Tienda;
import com.example.samba.databinding.DestinosPrincipalesProductosTiendaBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Destinos_Principales_Productos_Tienda extends Fragment {

    private DestinosPrincipalesProductosTiendaBinding binding;
    private ArrayList<Model_Camisetas_Tienda> modelCamisetasTiendaArrayList;
    private Adapter_Camisetas_Tienda adapterCamisetasTienda;
    String idCategoria, categoria;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = DestinosPrincipalesProductosTiendaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        idCategoria = intent.getStringExtra("categoriaId");
        categoria = intent.getStringExtra("categoria");

        cargarListaCamisetas();

        binding.botonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Activity_Favoritos.class));
            }
        });
    }
    //Mostrar todas las camisetas que hayan sido creadas por el usuario administrador, ya que la referencia de Camisetas dentro de la
    // base de datos se corresponde a las camisetas creadas por el usuario administrador, estas camisetas se ordenan dentro del
    // fragmento por el número de visitas, de mayor a menor.
    private void cargarListaCamisetas() {
        modelCamisetasTiendaArrayList  = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.orderByChild("numeroVisitas").addValueEventListener(new ValueEventListener() {
            //Obtener el valor de las camisetas creadas por el admintrador, añadirlo a un ArrayList para despues usar este arrayList
            // en el adaptor para configurar el recycler view que mostrará las camisetas
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelCamisetasTiendaArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Camisetas_Tienda camisetasTienda = dataSnapshot.getValue(Model_Camisetas_Tienda.class);
                    modelCamisetasTiendaArrayList.add(camisetasTienda);
                }
                // Este collection.reverse se usa para invertir el orden de las camisetas, ya que por defecto se ordenaban de menor
                // a mayor numero de visitas.
                Collections.reverse(modelCamisetasTiendaArrayList);
                adapterCamisetasTienda = new Adapter_Camisetas_Tienda(getContext(),modelCamisetasTiendaArrayList);
                binding.recyclerCamisetasTienda.setAdapter(adapterCamisetasTienda);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}