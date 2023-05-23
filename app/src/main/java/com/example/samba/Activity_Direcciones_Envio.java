package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityDestinosPrincipalesBinding;
import com.example.samba.databinding.ActivityDireccionesEnvioBinding;

public class Activity_Direcciones_Envio extends AppCompatActivity {

    private ActivityDireccionesEnvioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityDireccionesEnvioBinding.inflate(getLayoutInflater())).getRoot());


        binding.botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.ddd.getVisibility() == View.VISIBLE){
                    binding.ddd.setVisibility(View.GONE);
                } else binding.ddd.setVisibility(View.VISIBLE);
            }
        });
    }
}