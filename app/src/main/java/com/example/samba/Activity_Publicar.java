package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.samba.databinding.ActivityPublicarBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Publicar extends AppCompatActivity {

    ActivityPublicarBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri imageUri = null;
    private static final String TAG = "ADD_PRODUCT_TAG";
    String[] tallas = {"S","M","L","XL","XXL"};
    String[] marcas = {"Adidas","Nike","Puma","New Balance","Umbro","Castore","Kappa","Hummel","Joma"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterTallas;
    ArrayAdapter<String> adapterMarcas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPublicarBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Añadiendo Produto");
        progressDialog.setCanceledOnTouchOutside(false);

        autoCompleteTextView = findViewById(R.id.talla_producto);
        adapterTallas = new ArrayAdapter<>(this,R.layout.plantilla_listas,R.id.item, tallas);
        autoCompleteTextView.setAdapter(adapterTallas);
        autoCompleteTextView.setClickable(true);

        autoCompleteTextView = findViewById(R.id.marca_producto);
        adapterMarcas = new ArrayAdapter<>(this,R.layout.plantilla_listas,R.id.item, marcas);
        autoCompleteTextView.setAdapter(adapterMarcas);
        autoCompleteTextView.setClickable(true);

        binding.botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        binding.fotoProducto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttatchMenu();
            }
        });
        
        binding.botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private String titulo="",talla="",marca="",precio="",descripcion="";
    private void validateData() {
        
        titulo = binding.nombreProducto.getText().toString().trim();
        talla = binding.tallaProducto.getText().toString().trim();
        marca = binding.marcaProducto.getText().toString().trim();
        precio = binding.precioProducto.getText().toString().trim();
        descripcion = binding.descripcionProducto.toString().trim();

        if (TextUtils.isEmpty(titulo)){
            Toast.makeText(this,"Introduce un nombre al producto",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(talla)) {
            Toast.makeText(this,"Selecciona una talla",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(marca)){
            Toast.makeText(this,"Selecciona una marca",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(precio)) {
            Toast.makeText(this,"Introduce un precio",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descripcion)) {
            Toast.makeText(this,"Escribe una descripción",Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(this,"Introduce una imagen",Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null){
                agregarProductoFirebase("");
            } else {
                uploadImage("");
            }
        }

    }

    private void agregarProductoFirebase(String s) {
    }

    private void uploadImage(String s) {
        
    }

    private void showImageAttatchMenu() {
    }
}