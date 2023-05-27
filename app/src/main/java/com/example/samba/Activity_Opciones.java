package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityAgregarCategoriaBinding;
import com.example.samba.databinding.ActivityOpcionesBinding;

public class Activity_Opciones extends AppCompatActivity {

    private ActivityOpcionesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityOpcionesBinding.inflate(getLayoutInflater())).getRoot());


        binding.botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Opciones.this, Activity_Acceso.class));
            }
        });

        binding.botonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Opciones.this, Activity_Crear_Cuenta.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}