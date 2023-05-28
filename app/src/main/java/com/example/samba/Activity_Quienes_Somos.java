package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityMetodosPagoBinding;
import com.example.samba.databinding.ActivityQuienesSomosBinding;

public class Activity_Quienes_Somos extends AppCompatActivity {

    private ActivityQuienesSomosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityQuienesSomosBinding.inflate(getLayoutInflater())).getRoot());

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}