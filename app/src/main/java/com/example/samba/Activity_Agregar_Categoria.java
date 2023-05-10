package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityAgregarCategoriaBinding;
import com.example.samba.databinding.ActivityInterfazAdminBinding;

public class Activity_Agregar_Categoria extends AppCompatActivity {

    ActivityAgregarCategoriaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityAgregarCategoriaBinding.inflate(getLayoutInflater())).getRoot());

        binding.botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}