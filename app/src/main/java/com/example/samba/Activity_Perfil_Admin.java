package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.example.samba.databinding.ActivityPerfilAdminBinding;

public class Activity_Perfil_Admin extends AppCompatActivity {

    ActivityPerfilAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPerfilAdminBinding.inflate(getLayoutInflater())).getRoot());

        binding.botonVolverPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}