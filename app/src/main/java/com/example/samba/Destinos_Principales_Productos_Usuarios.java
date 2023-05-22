package com.example.samba;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samba.adapter.Adapter_Camisetas_Usuario;
import com.example.samba.databinding.DestinosPrincipalesProductosTiendaBinding;
import com.example.samba.databinding.DestinosPrincipalesProductosUsuariosBinding;
import com.example.samba.model.Model_Camisetas_Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Destinos_Principales_Productos_Usuarios extends Fragment {

    DestinosPrincipalesProductosUsuariosBinding binding;
    private ArrayList<Model_Camisetas_Usuario> camisetasUsuarioArrayList;
    private Adapter_Camisetas_Usuario adapterCamisetasUsuario;
    private String numeroVisitas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = DestinosPrincipalesProductosUsuariosBinding.inflate(inflater, container, false)).getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarListaCamisetas();

        Intent intent = getActivity().getIntent();
        numeroVisitas = intent.getStringExtra("numeroVisitas");
    }

    private void cargarListaCamisetas() {
        camisetasUsuarioArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
        databaseReference.orderByChild("numeroVisitas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                camisetasUsuarioArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Camisetas_Usuario modelCamisetasUsuario = dataSnapshot.getValue(Model_Camisetas_Usuario.class);
                    camisetasUsuarioArrayList.add(modelCamisetasUsuario);
                }
                Collections.reverse(camisetasUsuarioArrayList); // Invierte el orden de la lista
                adapterCamisetasUsuario = new Adapter_Camisetas_Usuario(getContext(),camisetasUsuarioArrayList);
                binding.recyclerCamisetasUsuarios.setAdapter(adapterCamisetasUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}