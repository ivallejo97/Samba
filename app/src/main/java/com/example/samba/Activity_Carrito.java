package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityCarritoBinding;
import com.example.samba.databinding.ActivityInterfazAdminBinding;

public class Activity_Carrito extends AppCompatActivity {

    ActivityCarritoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCarritoBinding.inflate(getLayoutInflater())).getRoot());

        binding.botonCerrarVentana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}