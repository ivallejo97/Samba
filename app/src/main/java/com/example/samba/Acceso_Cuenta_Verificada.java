package com.example.samba;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samba.databinding.AccesoCuentaVerificadaBinding;
import com.example.samba.databinding.AccesoVerificarCuentaBinding;


public class Acceso_Cuenta_Verificada extends Fragment {

    private AccesoCuentaVerificadaBinding binding;
    NavController navController;
    Button boton_ir_inicio_sesion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoCuentaVerificadaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        binding.botonVolverIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Iniciar_Sesion);
            }
        });
    }
}