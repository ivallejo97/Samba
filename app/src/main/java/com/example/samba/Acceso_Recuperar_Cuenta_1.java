package com.example.samba;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.samba.databinding.AccesoRecuperarCuenta1Binding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class Acceso_Recuperar_Cuenta_1 extends Fragment {

    private AccesoRecuperarCuenta1Binding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    NavController navController;
    ImageButton boton_volver;
    ImageButton boton_continuar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = AccesoRecuperarCuenta1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        boton_volver = view.findViewById(R.id.boton_volver);
        boton_continuar = view.findViewById(R.id.boton_continuar);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Recuperar Contraseña");
        progressDialog.setCanceledOnTouchOutside(false);


        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Iniciar_Sesion);
            }
        });

        boton_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navController.navigate(R.id.action_acceso_Recuperar_Cuenta_1_to_acceso_Recuperar_Cuenta_2);
                validateData();
            }
        });

    }

    private String email = "";
    private void validateData() {

        email = binding.correoElectronico.getText().toString().trim();
        if (email.isEmpty()){
            Toast.makeText(getContext(), "Introduce el correo eléctronico de la cuenta", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Correo eléctronico no válido", Toast.LENGTH_SHORT).show();
        } else {
            recoverPassword();
        }

    }

    private void recoverPassword() {
        progressDialog.setMessage("Enviando las instrucciones a " + email);
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Sigue las instrucciones que hemos enviado a " + email + " para restablecer la contraseña de la cuenta." , Toast.LENGTH_SHORT);
                    }
                })  .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error al enviar las instrucciones a  " + email + " " + e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }
}