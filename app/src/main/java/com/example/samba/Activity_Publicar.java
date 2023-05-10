package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.samba.databinding.ActivityPublicarBinding;

public class Activity_Publicar extends AppCompatActivity {

    ActivityPublicarBinding binding;

    String[] tallas = {"S","M","L","XL","XXL"};
    String[] marcas = {"Adidas","Nike","Puma","New Balance","Umbro","Castore","Kappa","Hummel"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterTallas;
    ArrayAdapter<String> adapterMarcas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPublicarBinding.inflate(getLayoutInflater())).getRoot());


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

    }
}