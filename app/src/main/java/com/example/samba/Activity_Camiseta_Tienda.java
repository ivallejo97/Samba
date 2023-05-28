package com.example.samba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.samba.adapter.Adapter_Camisetas_Tienda;
import com.example.samba.databinding.ActivityCamisetaTiendaBinding;
import com.example.samba.model.Model_Camisetas_Tienda;
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

public class Activity_Camiseta_Tienda extends AppCompatActivity {

    ActivityCamisetaTiendaBinding binding;
    String idCamiseta, categoriaId;
    boolean favorito = false;
    boolean carrito = false;
    private FirebaseAuth firebaseAuth;
    String[] tallas = {"S","M","L","XL","XXL"};
    String[] cantidad = {"1","2","3","4","5"};

    String cantidadTotal, tallaDefinitiva, personalizacionCamiseta, precioTotal;
    String precioCamiseta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCamisetaTiendaBinding.inflate(getLayoutInflater())).getRoot());

        Intent intent = getIntent();
        idCamiseta = intent.getStringExtra("id");
        categoriaId = intent.getStringExtra("categoriaId");

        firebaseAuth = FirebaseAuth.getInstance();

        cantidadTotal = binding.cantidadCamisetas.getText().toString().trim();
        tallaDefinitiva = binding.tallaCamiseta.getText().toString().trim();
        personalizacionCamiseta = binding.datosCamiseta.getText().toString().trim();

        comprobarFavorito();
        comprobarCarrito();
        aumentarNumeroDeVisitas(idCamiseta);
        cargarInformacionCamiseta();
        cargarCategoriaCamiseta();


        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonComprarCamiseta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Camiseta_Tienda.this, Activity_Carrito.class));
            }
        });

        binding.botonVerCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Camiseta_Tienda.this, Activity_Carrito.class));
            }
        });

        binding.cantidadCamisetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCantidad();
            }
        });

        binding.tallaCamiseta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTallas();
            }
        });

        binding.botonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorito){
                    MetodosApp.eliminarFavoritos(getApplicationContext(),idCamiseta);
                } else {
                    MetodosApp.addCamisetaFavoritos(getApplicationContext(),idCamiseta);
                }
            }
        });

        binding.botonCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrito){
                    MetodosApp.eliminarCarrito(getApplicationContext(),idCamiseta, cantidadTotal, tallaDefinitiva, personalizacionCamiseta, precioTotal);
                } else {
                    if (TextUtils.isEmpty(binding.datosCamiseta.getText().toString().trim())) {
                        personalizacionCamiseta = "No";
                    }
                    String cantidadSeleccionada = binding.cantidadCamisetas.getText().toString().trim();
                    if (cantidadSeleccionada.equals("-")) {
                        Toast.makeText(Activity_Camiseta_Tienda.this, "Selecciona una cantidad válida", Toast.LENGTH_SHORT).show();
                    } else {
                        MetodosApp.addCamisetaCarrito(getApplicationContext(),idCamiseta, cantidadTotal, tallaDefinitiva, personalizacionCamiseta, precioTotal);
                    }
                }
            }
        });

        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.datosCamiseta.setVisibility(View.VISIBLE);
                } else {
                    binding.datosCamiseta.setVisibility(View.GONE);
                }
            }
        });

        binding.datosCamiseta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar en este caso
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personalizacionCamiseta = s.toString().trim();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementar en este caso
            }
        });

    }

    private void cargarCategoriaCamiseta() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.child(categoriaId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_categoria = "" + snapshot.child("categoria").getValue();

                        binding.categoria.setText(nombre_categoria);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void aumentarNumeroDeVisitas(String idCamiseta) {
        if (idCamiseta != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
            databaseReference.child(idCamiseta)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String numeroVisitas = "" + snapshot.child("numeroVisitas").getValue();
                            if (numeroVisitas.equals("") || numeroVisitas.equals("null")){
                                numeroVisitas = "0";
                            }

                            long nuevoNumeroVisitas = Long.parseLong(numeroVisitas) + 1;
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("numeroVisitas",nuevoNumeroVisitas);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Camisetas");
                            reference.child(idCamiseta)
                                    .updateChildren(hashMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void cargarInformacionCamiseta() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.child(idCamiseta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_camiseta = "" + snapshot.child("titulo").getValue();
                        String marca_camiseta = "" + snapshot.child("marca").getValue();
                        String precio_camiseta = "" + snapshot.child("precio").getValue();
                        String descripcion_producto = "" + snapshot.child("descripcion").getValue();
                        String foto_camiseta = "" + snapshot.child("url").getValue();
                        String visitas_camisetas = "" + snapshot.child("numeroVisitas").getValue();

                        binding.nombreCamiseta.setText(nombre_camiseta);
                        binding.marcaCamiseta.setText(marca_camiseta);
                        binding.precioCamiseta.setText(precio_camiseta + "€");
                        binding.descripcionCamiseta.setText(descripcion_producto);
                        Glide.with(getApplicationContext()).load(foto_camiseta).into(binding.fotoCamiseta);
                        binding.numeroVisitasCamiseta.setText(visitas_camisetas.replace("null","N/A"));
                        precioCamiseta = precio_camiseta;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void mostrarTallas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una talla")
                .setItems(tallas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tallaSeleccionada = tallas[which];
                        binding.tallaCamiseta.setText(tallaSeleccionada);

                        tallaDefinitiva = tallaSeleccionada;
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void mostrarCantidad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona la cantidad")
                .setItems(cantidad, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cantidadSeleccionada = cantidad[which];
                        binding.cantidadCamisetas.setText(cantidadSeleccionada);

                        cantidadTotal = cantidadSeleccionada;

                        if (cantidadSeleccionada.equals("1")){
                            precioTotal = String.valueOf(Double.parseDouble(precioCamiseta));

                        } else {
                            precioTotal = String.valueOf(Double.parseDouble(precioCamiseta) * Integer.parseInt(cantidadTotal));
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void comprobarFavorito(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Favoritos").child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favorito = snapshot.exists();
                        if (favorito){
                            binding.botonFavoritos.setImageResource(R.drawable.icono_favorito_marcado);
                        } else {
                            binding.botonFavoritos.setImageResource(R.drawable.icono_favoritos_perfil);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void comprobarCarrito(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Carrito").child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        carrito = snapshot.exists();
                        if (carrito){
                            binding.botonCarrito.setImageResource(R.drawable.icono_eliminar_carrito);
                        } else {
                            binding.botonCarrito.setImageResource(R.drawable.icono_agregar_carrito);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    
}