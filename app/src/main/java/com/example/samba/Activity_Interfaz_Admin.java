package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityAgregarCategoriaBinding;
import com.example.samba.databinding.ActivityDestinosPrincipalesBinding;
import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Interfaz_Admin extends AppCompatActivity {

    ActivityInterfazAdminBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityInterfazAdminBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.botonAAdirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Interfaz_Admin.this , Activity_Agregar_Categoria.class));
            }
        });

        binding.botonAAdirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Interfaz_Admin.this , Activity_Publicar_Administrador.class));
            }
        });

        binding.botonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Interfaz_Admin.this , Activity_Perfil_Admin.class));
            }
        });

        binding.botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
            }
        });

    }
}