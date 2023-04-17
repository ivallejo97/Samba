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

import com.example.samba.databinding.AccesoRecuperarCuenta3Binding;
import com.example.samba.databinding.AccesoRecuperarCuenta4Binding;


public class Acceso_Recuperar_Cuenta_4 extends Fragment {

    private AccesoRecuperarCuenta4Binding binding;
    NavController navController;
    Button boton_ir_a_iniciar_sesion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoRecuperarCuenta4Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        boton_ir_a_iniciar_sesion = view.findViewById(R.id.boton_volver_iniciar_sesion);

        boton_ir_a_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Iniciar_Sesion);
            }
        });

    }
}