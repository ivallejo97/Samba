package com.example.samba;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samba.databinding.DestinosPrincipalesInicioBinding;
import com.example.samba.databinding.DestinosPrincipalesProductosTiendaBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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
        intent.getStringExtra("categoriaId");
        intent.getStringExtra("categoria");


        cargarListaCamisetas();

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
                        adapterCamisetasTienda = new Adapter_Camisetas_Tienda(getContext(),modelCamisetasTiendaArrayList);
                        binding.recyclerCamisetasTienda.setAdapter(adapterCamisetasTienda);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}