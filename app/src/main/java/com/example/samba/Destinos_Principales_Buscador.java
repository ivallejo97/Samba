package com.example.samba;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samba.adapter.Adapter_Categoria;
import com.example.samba.databinding.DestinosPrincipalesBuscadorBinding;
import com.example.samba.model.Model_Categoria;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Destinos_Principales_Buscador extends Fragment {

    DestinosPrincipalesBuscadorBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_Categoria> categoriaArrayList;
    private Adapter_Categoria adapterCategoria;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = DestinosPrincipalesBuscadorBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCategorias();

        binding.buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterCategoria.getFilter().filter(s);
                } catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void cargarCategorias() {
        categoriaArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriaArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Categoria modelCategoria = dataSnapshot.getValue(Model_Categoria.class);
                    categoriaArrayList.add(modelCategoria);
                }
                adapterCategoria = new Adapter_Categoria(getContext(),categoriaArrayList);
                binding.reciclerCategorias.setAdapter(adapterCategoria);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}