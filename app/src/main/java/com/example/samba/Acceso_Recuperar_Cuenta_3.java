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

import com.example.samba.databinding.AccesoRecuperarCuenta2Binding;
import com.example.samba.databinding.AccesoRecuperarCuenta3Binding;


public class Acceso_Recuperar_Cuenta_3 extends Fragment {

    private AccesoRecuperarCuenta3Binding binding;
    NavController navController;
    ImageButton boton_volver;
    ImageButton boton_confirmar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoRecuperarCuenta3Binding.inflate(inflater, container, false)).getRoot();
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
                navController.navigate(R.id.acceso_Recuperar_Cuenta_2);
            }
        });

        boton_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Recuperar_Cuenta_3);
            }
        });

    }
}