package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.samba.databinding.ActivityCrearCuentaBinding;
import com.example.samba.databinding.ActivityOpcionesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Activity_Crear_Cuenta extends AppCompatActivity {

    private ActivityCrearCuentaBinding binding;
    String[] paises = {"España","Italia","Portugal","Inglaterra","Alemania","Francia","Suiza","Croacia","Austria"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterPaises;
    private FirebaseAuth firebaseAuth;
    Dialog_Crear_Cuenta dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCrearCuentaBinding.inflate(getLayoutInflater())).getRoot());

        autoCompleteTextView = findViewById(R.id.nacionalidad);
        adapterPaises = new ArrayAdapter<>(this,R.layout.plantilla_listas,R.id.item, paises);
        autoCompleteTextView.setAdapter(adapterPaises);
        autoCompleteTextView.setClickable(true);
        dialog = new Dialog_Crear_Cuenta(this);
        firebaseAuth = FirebaseAuth.getInstance();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Crear_Cuenta.this, Activity_Opciones.class));
            }
        });

        binding.botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(Activity_Crear_Cuenta.this,"" + item, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String nombre = "", nombre_usuario = "", email = "", telefono= "", pais = "", password = "";
    // Validar el formato de los datos (comprobar que es el correcto)
    private void validateData() {
        //Obtener el texto del edittext, y convertirlo en un string eliminando espacios innecesarios
        nombre = binding.nombre.getText().toString().trim();
        nombre_usuario = binding.nombreUsuario.getText().toString().trim();
        email = binding.correoElectronico.getText().toString().trim();
        telefono = binding.numeroTelefono.getText().toString().trim();
        pais = binding.nacionalidad.getText().toString().trim();
        password = binding.contraseA.getText().toString().trim();
        String cPassword = binding.repetirContraseA.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this,"Introduce tu nombre",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nombre_usuario)) {
            Toast.makeText(this,"Introduce un nombre de usuario",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,"Dirección de correo electronico inválida",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(telefono).matches()) {
            Toast.makeText(this,"Número de telefono incorrecto",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pais)){
            Toast.makeText(this,"Selecciona un país",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Escribe una contraseña",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(this,"Repite la contraseña",Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        } else {
            createAccount();
        }

    }
    // Pasarle los valores de los edittext email y password al metodo createUserWithEmailAndPassword de la instancia de firebaseAuth
    // para crear la cuenta
    private void createAccount() {
        dialog.showDialog();
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
                        Toast.makeText(Activity_Crear_Cuenta.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Una vez se crea la cuenta, usamos este metodo para crear un map en el cual guardaremos las claves y valores de los distintos
    // apartados que tendrá un usuario
    private void updateUserInfo() {
        long timestamp = System.currentTimeMillis();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Crear el map y añadir las claves y valores
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", nombre);
        hashMap.put("username", nombre_usuario);
        hashMap.put("telefono", telefono);
        hashMap.put("pais", pais);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);
        // Crear la referencia de usuarios en la base de datos(si no esta creada aún) y añadir el valor del usuario, si ya esta creada
        // tan solo se añaden los apartados con las claves y valores del usuario que se crea.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        try {
                            Thread.sleep(3000);
                            dialog.hideDialog();
                            startActivity(new Intent(Activity_Crear_Cuenta.this, Activity_Cuenta_Creada.class));

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Crear_Cuenta.this,"Cuenta no Creada",Toast.LENGTH_SHORT).show();

                    }
                });

    }
}