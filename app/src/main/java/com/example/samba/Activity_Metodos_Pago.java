package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.samba.adapter.Adapter_Direcciones_Envio;
import com.example.samba.adapter.Adapter_Metodos_Pago;
import com.example.samba.databinding.ActivityCamisetaTiendaBinding;
import com.example.samba.databinding.ActivityMetodosPagoBinding;
import com.example.samba.model.Model_Direcciones_Envio;
import com.example.samba.model.Model_Metodos_Pago;
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

public class Activity_Metodos_Pago extends AppCompatActivity {

    private ActivityMetodosPagoBinding binding;
    private ArrayList<Model_Metodos_Pago> modelMetodosPagosList;
    private Adapter_Metodos_Pago adapterMetodosPago;
    private FirebaseAuth firebaseAuth;
    String[] fecha_caducidad = {"12/23","12/24","12/25","12/26","12/27","12/28","12/29","12/30","12/31"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMetodosPagoBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarMetodosPago();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.textInputTitular.getVisibility() == View.VISIBLE){
                    binding.textInputTitular.setVisibility(View.GONE);
                } else binding.textInputTitular.setVisibility(View.VISIBLE);

                if (binding.textInputNumero.getVisibility() == View.VISIBLE){
                    binding.textInputNumero.setVisibility(View.GONE);
                } else binding.textInputNumero.setVisibility(View.VISIBLE);

                if (binding.textInputCaducidad.getVisibility() == View.VISIBLE){
                    binding.textInputCaducidad.setVisibility(View.GONE);
                } else binding.textInputCaducidad.setVisibility(View.VISIBLE);

                if (binding.textInputCVV.getVisibility() == View.VISIBLE){
                    binding.textInputCVV.setVisibility(View.GONE);
                } else binding.textInputCVV.setVisibility(View.VISIBLE);

                if (binding.botonAgregarMetodoPago.getVisibility() == View.VISIBLE){
                    binding.botonAgregarMetodoPago.setVisibility(View.GONE);
                } else binding.botonAgregarMetodoPago.setVisibility(View.VISIBLE);
            }
        });

        binding.botonAgregarMetodoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.textInputCaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFechaCaducidad();
            }
        });

    }

    private void cargarMetodosPago() {
        modelMetodosPagosList = new ArrayList<>();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MetodosPago");
        databaseReference.orderByChild("uid").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelMetodosPagosList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Model_Metodos_Pago modelMetodosPago = dataSnapshot.getValue(Model_Metodos_Pago.class);
                            modelMetodosPagosList.add(modelMetodosPago);
                        }
                        adapterMetodosPago = new Adapter_Metodos_Pago(Activity_Metodos_Pago.this , modelMetodosPagosList);
                        binding.recyclerMetodosPago.setAdapter(adapterMetodosPago);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String titular="",numero="", fechaCaducidad="",CVV="";
    private void validateData() {

        titular = binding.editTextTitular.getText().toString().trim();
        numero = binding.editTextNumero.getText().toString().trim();
        fechaCaducidad = binding.textInputCaducidad.getText().toString().trim();
        CVV = binding.editTextCVV.getText().toString().trim();


        if (TextUtils.isEmpty(titular)){
            Toast.makeText(this,"Introduce el nombre del titular",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(numero)) {
            Toast.makeText(this,"Introduce el numero de la tarjeta",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fechaCaducidad)){
            Toast.makeText(this,"Introduce el numero la fecha de caducidad",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(CVV)) {
            Toast.makeText(this,"Introduce el CVV de la tarjeta",Toast.LENGTH_SHORT).show();
        } else {
            agregarMetodoPago();
        }
    }

    private void agregarMetodoPago() {

        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("titular", "" + titular);
        hashMap.put("numero", "" + numero);
        hashMap.put("caducidad", "" + fechaCaducidad);
        hashMap.put("cvv", "" + CVV);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("id", "" + timestamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MetodosPago");
        databaseReference.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Activity_Metodos_Pago.this,"Metodo de pago añadido",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Metodos_Pago.this,"Error al añadir el metodo de pago",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarFechaCaducidad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce la fecha de caducidad")
                .setItems(fecha_caducidad, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fechaCaducidad = fecha_caducidad[which];
                        binding.textInputCaducidad.setText(fechaCaducidad);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}