package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.example.samba.databinding.ActivityPublicarAdministradorBinding;

public class Activity_Publicar_Administrador extends AppCompatActivity {

    ActivityPublicarAdministradorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPublicarAdministradorBinding.inflate(getLayoutInflater())).getRoot());

        binding.botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}