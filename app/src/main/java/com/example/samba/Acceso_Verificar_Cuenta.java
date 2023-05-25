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
import android.widget.ImageButton;

import com.example.samba.databinding.AccesoVerificarCuentaBinding;


public class Acceso_Verificar_Cuenta extends Fragment {

    private AccesoVerificarCuentaBinding binding;
    NavController navController;
    ImageButton boton_volver;
    ImageButton boton_confirmar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoVerificarCuentaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        boton_volver = view.findViewById(R.id.boton_volver);
        boton_confirmar = view.findViewById(R.id.boton_confirmar);

        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_acceso_Verificar_Cuenta_to_acceso_Crear_Cuenta);
            }
        });

        boton_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_acceso_Verificar_Cuenta_to_acceso_Cuenta_Verificada);
            }
        });

    }
}