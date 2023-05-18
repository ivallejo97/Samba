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
import com.example.samba.adapter.Adapter_Chat;
import com.example.samba.databinding.DestinosPrincipalesChatBinding;
import com.example.samba.model.Model_Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Destinos_Principales_Chat extends Fragment {
    private DestinosPrincipalesChatBinding binding;
    private FirebaseAuth firebaseAuth;
    Adapter_Chat adapterChat;
    ArrayList<Model_Chat> chatList;

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
                        Glide.with(getContext())
                                .load(profileImage)
                                .placeholder(R.drawable.icono_aceptar)
                                .into(binding.fotoUsuario);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }

    private void getAllUsers() {
        chatList = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Chat modelChat = dataSnapshot.getValue(Model_Chat.class);

                    if (!modelChat.getUid().equals(firebaseUser.getUid())){
                        chatList.add(modelChat);
                    }

                    adapterChat = new Adapter_Chat(getContext(),chatList);
                    binding.recyclerChat.setAdapter(adapterChat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}