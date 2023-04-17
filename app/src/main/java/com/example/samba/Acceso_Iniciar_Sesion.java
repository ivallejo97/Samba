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
import android.widget.TextView;
import android.widget.Toast;

import com.example.samba.databinding.AccesoIniciarSesionBinding;
import com.example.samba.databinding.AccesoOpcionesBinding;


public class Acceso_Iniciar_Sesion extends Fragment {

    private AccesoIniciarSesionBinding binding;
    NavController navController;
    Button botonIniciarSesion;
    Button botonVolverInicio;
    TextView recuperarContraseña;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoIniciarSesionBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        botonIniciarSesion = view.findViewById(R.id.boton_iniciar_sesion);
        botonVolverInicio = view.findViewById(R.id.boton_volver_a_inicio);
        recuperarContraseña = view.findViewById(R.id.recuperar_contraseña);

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Sesión Iniciada", Toast.LENGTH_SHORT).show();
            }
        });

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_acceso_Iniciar_Sesion_to_acceso_Opciones);
            }
        });

        recuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Recuperar_Cuenta_1);
            }
        });

    }
}