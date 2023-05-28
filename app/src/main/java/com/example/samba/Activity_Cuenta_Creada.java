package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityCrearCuentaBinding;
import com.example.samba.databinding.ActivityCuentaCreadaBinding;

public class Activity_Cuenta_Creada extends AppCompatActivity {

    private ActivityCuentaCreadaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCuentaCreadaBinding.inflate(getLayoutInflater())).getRoot());

        binding.botonIrInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Cuenta_Creada.this, Activity_Acceso.class));
            }
        });
    }
}