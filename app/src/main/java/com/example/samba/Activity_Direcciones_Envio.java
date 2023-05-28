package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.samba.adapter.Adapter_Direcciones_Envio;
import com.example.samba.databinding.ActivityDestinosPrincipalesBinding;
import com.example.samba.databinding.ActivityDireccionesEnvioBinding;
import com.example.samba.model.Model_Direcciones_Envio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Direcciones_Envio extends AppCompatActivity {

    private ActivityDireccionesEnvioBinding binding;
    private ArrayList<Model_Direcciones_Envio> modelDireccionesEnviosList;
    private Adapter_Direcciones_Envio adapterDireccionesEnvio;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityDireccionesEnvioBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarDirecciones();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonAgregarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.textInputCalle.getVisibility() == View.VISIBLE){
                    binding.textInputCalle.setVisibility(View.GONE);
                } else binding.textInputCalle.setVisibility(View.VISIBLE);

                if (binding.textInputNumero.getVisibility() == View.VISIBLE){
                    binding.textInputNumero.setVisibility(View.GONE);
                } else binding.textInputNumero.setVisibility(View.VISIBLE);

                if (binding.textInputPiso.getVisibility() == View.VISIBLE){
                    binding.textInputPiso.setVisibility(View.GONE);
                } else binding.textInputPiso.setVisibility(View.VISIBLE);

                if (binding.textInputPuerta.getVisibility() == View.VISIBLE){
                    binding.textInputPuerta.setVisibility(View.GONE);
                } else binding.textInputPuerta.setVisibility(View.VISIBLE);

                if (binding.textInputPais.getVisibility() == View.VISIBLE){
                    binding.textInputPais.setVisibility(View.GONE);
                } else binding.textInputPais.setVisibility(View.VISIBLE);

                if (binding.textInputProvincia.getVisibility() == View.VISIBLE){
                    binding.textInputProvincia.setVisibility(View.GONE);
                } else binding.textInputProvincia.setVisibility(View.VISIBLE);

                if (binding.textInputLocalidad.getVisibility() == View.VISIBLE){
                    binding.textInputLocalidad.setVisibility(View.GONE);
                } else binding.textInputLocalidad.setVisibility(View.VISIBLE);

                if (binding.textInputCp.getVisibility() == View.VISIBLE){
                    binding.textInputCp.setVisibility(View.GONE);
                } else binding.textInputCp.setVisibility(View.VISIBLE);

                if (binding.botonAgregarDireccion.getVisibility() == View.VISIBLE){
                    binding.botonAgregarDireccion.setVisibility(View.GONE);
                } else binding.botonAgregarDireccion.setVisibility(View.VISIBLE);
            }
        });

    }

    private void cargarDirecciones() {
        modelDireccionesEnviosList = new ArrayList<>();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DireccionesEnvio");
        databaseReference.orderByChild("uid").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelDireccionesEnviosList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Model_Direcciones_Envio modelDireccionesEnvio = dataSnapshot.getValue(Model_Direcciones_Envio.class);
                            modelDireccionesEnviosList.add(modelDireccionesEnvio);
                        }
                        adapterDireccionesEnvio = new Adapter_Direcciones_Envio(Activity_Direcciones_Envio.this , modelDireccionesEnviosList);
                        binding.recyclerDireccionesEnvio.setAdapter(adapterDireccionesEnvio);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String calle="",numero="", piso="",puerta="",pais="",provincia="",localidad="", codigoPostal="";
    private void validateData() {

        calle = binding.editTextCalle.getText().toString().trim();
        numero = binding.editTextNumero.getText().toString().trim();
        piso = binding.editTextPiso.getText().toString().trim();
        puerta = binding.editTextPuerta.getText().toString().trim();
        pais = binding.editTextPais.getText().toString().trim();
        provincia = binding.editTextProvincia.getText().toString().trim();
        localidad = binding.editTextLocalidad.getText().toString().trim();

        if (TextUtils.isEmpty(calle)){
            Toast.makeText(this,"Introduce tu calle",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(numero)) {
            Toast.makeText(this,"Introduce el numero de portal",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(piso)){
            Toast.makeText(this,"Introduce el numero de tu piso",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(puerta)) {
            Toast.makeText(this,"Introduce el numero de tu puerta",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pais)) {
            Toast.makeText(this,"Introduce el pais",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(provincia)) {
            Toast.makeText(this,"Indica la provincia",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(localidad)) {
            Toast.makeText(this,"Indica la localidad",Toast.LENGTH_SHORT).show();
        } else {
            agregarDireccionEnvio();
        }

    }

    private void agregarDireccionEnvio() {

        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("calle", "" + calle);
        hashMap.put("numero", "" + numero);
        hashMap.put("piso", "" + piso);
        hashMap.put("puerta", "" + puerta);
        hashMap.put("pais", "" + pais);
        hashMap.put("provincia", "" + provincia);
        hashMap.put("localidad", "" + localidad);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("id", "" + timestamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DireccionesEnvio");
        databaseReference.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Activity_Direcciones_Envio.this,"Dirección Añadida",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Direcciones_Envio.this,"Error al añadir la dirección",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}