package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.example.samba.databinding.ActivityPerfilAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class Activity_Perfil_Admin extends AppCompatActivity {

    ActivityPerfilAdminBinding binding;
    NavController navController;
    private FirebaseAuth firebaseAuth;
    public static final String TAG = "ADMIN_PROFILE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPerfilAdminBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.botonVolverPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Perfil_Admin.this, Activity_Editar_Perfil_Admin.class));
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
                        String nombre_completo = "" + snapshot.child("name").getValue();
                        String nombre_usuario = "" + snapshot.child("username").getValue();
                        String email = "" + snapshot.child("email").getValue();
                        String telefono = "" + snapshot.child("telefono").getValue();
                        String nacionalidad = "" + snapshot.child("pais").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String userType = "" + snapshot.child("userType").getValue();

                        binding.nombreCompletoAdministrador.setText(nombre_completo);
                        binding.nombreUsuarioAdministrador.setText(nombre_usuario);
                        binding.correoElectronicoAdministrador.setText(email);
                        binding.numeroTelefonoAdministrador.setText(telefono);
                        binding.nacionalidadAdministrador.setText(nacionalidad);

                        Glide.with(Activity_Perfil_Admin.this)
                                .load(profileImage)
                                .placeholder(R.drawable.icono_aceptar)
                                .into(binding.fotoPerfilAdministrador);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}