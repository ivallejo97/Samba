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
import android.widget.ImageButton;

import com.example.samba.databinding.AccesoRecuperarCuenta1Binding;
import com.example.samba.databinding.AccesoRecuperarCuenta2Binding;


public class Acceso_Recuperar_Cuenta_2 extends Fragment {

    private AccesoRecuperarCuenta2Binding binding;
    NavController navController;
    ImageButton boton_volver;
    ImageButton boton_continuar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoRecuperarCuenta2Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        boton_continuar = view.findViewById(R.id.boton_continuar);


        boton_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Iniciar_Sesion);
            }
        });

    }
}