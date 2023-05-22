package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samba.databinding.ActivityEditarCamisetaAdministradorBinding;
import com.example.samba.databinding.ActivityEditarCamisetaUsuarioBinding;

public class Activity_Editar_Camiseta_Administrador extends AppCompatActivity {

    private ActivityEditarCamisetaAdministradorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityEditarCamisetaAdministradorBinding.inflate(getLayoutInflater())).getRoot());
    }
}