package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.samba.adapter.Adapter_Chat;
import com.example.samba.databinding.ActivityChatBinding;
import com.example.samba.model.Model_Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Chat extends AppCompatActivity {

    ActivityChatBinding binding;
    private FirebaseAuth firebaseAuth;
    ArrayList<Model_Chat> chatArrayList;
    Adapter_Chat adapterChat;
    String idUser,idMyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityChatBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        binding.recyclerChat.setHasFixedSize(true);
        binding.recyclerChat.setLayoutManager(linearLayout);

        Intent intent = getIntent();
        idUser = intent.getStringExtra("uid");
        idMyUser = firebaseAuth.getCurrentUser().getUid();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = binding.mensajeAEnviar.getText().toString().trim();
                if (TextUtils.isEmpty(mensaje)){
                } else {
                    enviarMensaje(mensaje);
                }
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(idUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String name = "" + dataSnapshot.child("name").getValue();
                            String foto = "" + dataSnapshot.child("profileImage").getValue();

                            binding.nombreUsuario.setText(name);
                            if (foto.equals("")){
                                binding.fotoUsuario.setImageResource(R.drawable.icono_perfil_predeterminado);
                            } else {
                                Glide.with(getApplicationContext()).load(foto).into(binding.fotoUsuario);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        
        leerMensajes();
        // mostrarMensajes();

    }

    private void mostrarMensajes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mensajes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Chat modelChat = dataSnapshot.getValue(Model_Chat.class);
                    if (modelChat.getIdUsuarioRecibe().equals(idMyUser) && modelChat.getIdUsuarioEnvia().equals(idUser)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("mensajeVisto" , true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void leerMensajes() {
        chatArrayList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mensajes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Chat modelChat = dataSnapshot.getValue(Model_Chat.class);
                    if (modelChat.getIdUsuarioRecibe().equals(idMyUser) && modelChat.getIdUsuarioEnvia().equals(idUser)
                            || modelChat.getIdUsuarioRecibe().equals(idUser) && modelChat.getIdUsuarioEnvia().equals(idMyUser)){
                        chatArrayList.add(modelChat);
                    }
                    adapterChat = new Adapter_Chat(Activity_Chat.this, chatArrayList);
                    adapterChat.notifyDataSetChanged();
                    binding.recyclerChat.setAdapter(adapterChat);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void enviarMensaje(String mensaje) {

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("idUsuarioEnvia" , idMyUser);
        hashMap.put("idUsuarioRecibe" , idUser);
        hashMap.put("mensaje" , mensaje);
        hashMap.put("timestamp", timestamp);
        hashMap.put("mensajeVisto", false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Mensajes").push()
                .setValue(hashMap);

        binding.mensajeAEnviar.setText("");
        binding.mensajeAEnviar.setHint("Escribe un mensaje");
    }



}