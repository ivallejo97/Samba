package com.example.samba;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.AccesoCrearCuentaBinding;
import com.example.samba.databinding.DestinosPrincipalesPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Destinos_Principales_Perfil extends Fragment {

    DestinosPrincipalesPerfilBinding binding;
    NavController navController;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "PROFILE_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = DestinosPrincipalesPerfilBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.destinos_principales_editar_perfil);
            }
        });


    }

    private void loadUserInfo() {
        Log.d(TAG,"loadUserInfo: Loading User..." + firebaseAuth.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String name = "" + snapshot.child("name").getValue();
                        String pais = "" + snapshot.child("pais").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String telefono = "" + snapshot.child("telefono").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String userType = "" + snapshot.child("userType").getValue();

                        binding.correoElectronico.setText(email);
                        binding.nombreUsuario.setText(name);
                        binding.pais.setText(pais);
                        binding.telefono.setText(telefono);
                        binding.user.setText(userType);

                        Glide.with(getContext())
                                .load(profileImage)
                                .placeholder(R.drawable.icono_aceptar)
                                .into(binding.fotoPerfil);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}