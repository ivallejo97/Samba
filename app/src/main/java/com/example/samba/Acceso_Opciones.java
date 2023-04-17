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

import com.example.samba.databinding.AccesoOpcionesBinding;


public class Acceso_Opciones extends Fragment {

    private AccesoOpcionesBinding binding;
    NavController navController;
    Button botonIniciarSesion;
    Button botonCrearCuenta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoOpcionesBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        botonIniciarSesion = view.findViewById(R.id.boton_iniciar_sesion);
        botonCrearCuenta = view.findViewById(R.id.boton_crear_cuenta);

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_acceso_Opciones_to_acceso_Iniciar_Sesion);
            }
        });

        botonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_acceso_Opciones_to_acceso_Crear_Cuenta);
            }
        });

    }
}