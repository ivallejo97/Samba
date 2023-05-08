package com.example.samba;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.samba.databinding.AccesoCrearCuentaBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Acceso_Crear_Cuenta extends Fragment {

    private AccesoCrearCuentaBinding binding;
    NavController navController;
    ImageButton boton_volver;
    ImageButton boton_continuar;

    String[] paises = {"España","Italia","Portugal","Inglaterra","Alemania","Francia","Suiza","Croacia","Austria"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterPaises;
    private FirebaseAuth firebaseAuth;
    Dialog_Crear_Cuenta dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = AccesoCrearCuentaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        boton_volver = view.findViewById(R.id.boton_volver);
        boton_continuar = view.findViewById(R.id.boton_continuar);
        autoCompleteTextView = view.findViewById(R.id.nacionalidad);
        adapterPaises = new ArrayAdapter<>(getContext(),R.layout.plantilla_listas,R.id.item, paises);
        autoCompleteTextView.setAdapter(adapterPaises);
        autoCompleteTextView.setClickable(true);
        dialog = new Dialog_Crear_Cuenta(getContext());
        firebaseAuth = FirebaseAuth.getInstance();


        boton_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_acceso_Crear_Cuenta_to_acceso_Opciones);
            }
        });

        boton_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
                dialog.showDialog();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(),"" + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String name = "", username = "", email = "", telefono= "", pais = "", password = "";
    private void validateData() {
        name = binding.nombre.getText().toString().trim();
        username = binding.nombreUsuario.getText().toString().trim();
        email = binding.correoElectronico.getText().toString().trim();
        telefono = binding.numeroTelefono.getText().toString().trim();
        pais = binding.nacionalidad.getText().toString().trim();
        password = binding.contraseA.getText().toString().trim();
        String cPassword = binding.repetirContraseA.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(getContext(),"Introduce tu nombre",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(),"Introduce un nombre de usuario",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(),"Dirección de correo electronico inválida",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(telefono).matches()) {
            Toast.makeText(getContext(),"Número de telefono incorrecto",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pais)){
            Toast.makeText(getContext(),"Selecciona un país",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(),"Escribe una contraseña",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(getContext(),"Repite la contraseña",Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {
            Toast.makeText(getContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        } else {
            createAccount();
        }

    }

    private void createAccount() {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        long timestamp = System.currentTimeMillis();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("username", username);
        hashMap.put("telefono", telefono);
        hashMap.put("pais", pais);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        try {
                            dialog.modifyDialog("Cuenta Creada");
                            Thread.sleep(3000);
                            dialog.hideDialog();
                            navController.navigate(R.id.action_acceso_Crear_Cuenta_to_acceso_Verificar_Cuenta);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        onDestroyView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Cuenta no Creada",Toast.LENGTH_SHORT).show();

                    }
                });

    }
}