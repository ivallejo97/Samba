package com.example.samba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samba.databinding.AccesoIniciarSesionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Acceso_Iniciar_Sesion extends Fragment {

    private AccesoIniciarSesionBinding binding;
    NavController navController;
    Button botonIniciarSesion;
    Button botonVolverInicio;
    TextView recuperarContraseña;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;


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
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Comprobando datos");

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Activity_Opciones.class));
            }
        });

        recuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.acceso_Recuperar_Cuenta_1);
            }
        });

    }

    private String email = "", password = "";

    private void validateData() {
        email = binding.usuario.getText().toString().trim();
        password = binding.contraseA.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Dirección de correo electronico inválida", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Verificando usuario...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "Comprobando Datos", Toast.LENGTH_SHORT).show();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {
        try {
            Thread.sleep(1000);
            progressDialog.setMessage("Iniciando sesión");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        String userType = "" + snapshot.child("userType").getValue();
                        if (userType.equals("user")){

                            startActivity(new Intent(getContext(), Activity_Destinos_Principales.class));
                            onDestroyView();

                        } else if (userType.equals("admin")){
                            startActivity(new Intent(getContext(), Activity_Interfaz_Admin.class));
                            onDestroyView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });


    }
}