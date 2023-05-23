package com.example.samba;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.samba.adapter.Adapter_Usuarios;
import com.example.samba.databinding.DestinosPrincipalesChatBinding;
import com.example.samba.model.Model_Chat;
import com.example.samba.model.Model_Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Destinos_Principales_Chat extends Fragment {
    private DestinosPrincipalesChatBinding binding;
    private FirebaseAuth firebaseAuth;
    Adapter_Usuarios adapterChat;
    ArrayList<Model_Usuarios> chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = DestinosPrincipalesChatBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.recyclerChat.setHasFixedSize(true);
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllUsers();

    }

    private void loadUserInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = "" + snapshot.child("username").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();

                        binding.nombreUsuario.setText("@"+name);
                        if (isAdded()){
                            Glide.with(getContext())
                                    .load(profileImage)
                                    .placeholder(R.drawable.icono_aceptar)
                                    .into(binding.fotoUsuario);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }

    private void getAllUsers() {
        chatList = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Usuarios modelUsuarios = dataSnapshot.getValue(Model_Usuarios.class);

                    if (!modelUsuarios.getUid().equals(firebaseUser.getUid())){
                        chatList.add(modelUsuarios);
                    }
                    adapterChat = new Adapter_Usuarios(getContext(),chatList);
                    binding.recyclerChat.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}