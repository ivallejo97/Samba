package com.example.samba;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.ActivityEditarCamisetaAdministradorBinding;
import com.example.samba.databinding.ActivityEditarCamisetaUsuarioBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Activity_Editar_Camiseta_Administrador extends AppCompatActivity {

    private ActivityEditarCamisetaAdministradorBinding binding;

    private FirebaseAuth firebaseAuth;
    private Uri imageUri = null;

    private String precioCamiseta = "", nombreCamiseta = "", marcaCamiseta = "", descripcionCamiseta = "";
    String idCamiseta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityEditarCamisetaAdministradorBinding.inflate(getLayoutInflater())).getRoot());

        Intent intent = getIntent();
        idCamiseta = intent.getStringExtra("id");
        firebaseAuth = FirebaseAuth.getInstance();
        cargarInformacionCamiseta();

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonGuardarInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.botonOpcionesImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttatchMenu();
            }
        });


    }

    private void cargarInformacionCamiseta() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.child(idCamiseta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_camiseta = "" + snapshot.child("titulo").getValue();
                        String talla_camiseta = "" + snapshot.child("talla").getValue();
                        String marca_camiseta = "" + snapshot.child("marca").getValue();
                        String precio_camiseta = "" + snapshot.child("precio").getValue();
                        String descripcion_camiseta = "" + snapshot.child("descripcion").getValue();
                        String foto_camiseta = "" + snapshot.child("url").getValue();
                        String visitas_camiseta = "" + snapshot.child("numeroVisitas").getValue();

                        binding.nombreCamiseta.setText(nombre_camiseta);
                        binding.marcaCamiseta.setText(marca_camiseta);
                        binding.precioCamiseta.setText(precio_camiseta);
                        binding.descripcionCamiseta.setText(descripcion_camiseta);
                        binding.numeroVisitasCamiseta.setText(visitas_camiseta.replace("null","N/A"));
                        if (!isDestroyed()){
                            Glide.with(Activity_Editar_Camiseta_Administrador.this).load(foto_camiseta).into(binding.fotoCamiseta);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void validateData() {
        precioCamiseta = binding.precioCamiseta.getText().toString().trim();
        nombreCamiseta = binding.nombreCamiseta.getText().toString().trim();
        marcaCamiseta = binding.marcaCamiseta.getText().toString().trim();
        descripcionCamiseta = binding.descripcionCamiseta.getText().toString().trim();

        if (TextUtils.isEmpty(precioCamiseta)){
            Toast.makeText(this,"Introduce un nombre",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nombreCamiseta)) {
            Toast.makeText(this,"Introduce un nombre",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(marcaCamiseta)) {
            Toast.makeText(this,"Introduce una marca",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descripcionCamiseta)) {
            Toast.makeText(this,"Introduce una descripcion",Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null){
                actualizarCamiseta("");
            } else {
                uploadImage("");
            }
        }
    }


    private void uploadImage(String s) {
        String file = "Camisetas/"+firebaseAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(file);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadImage = ""+ uriTask.getResult();
                        actualizarCamiseta(uploadImage);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Editar_Camiseta_Administrador.this,"Fallo en la actualización de la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void actualizarCamiseta(String imageUrl) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("titulo",""+nombreCamiseta);
        hashMap.put("precio",""+precioCamiseta);
        hashMap.put("marca",""+marcaCamiseta);
        hashMap.put("descripcion",""+descripcionCamiseta);

        if (imageUri != null){
            hashMap.put("url", ""+imageUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.child(idCamiseta)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Activity_Editar_Camiseta_Administrador.this,"Camiseta actualizada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Editar_Camiseta_Administrador.this,"Fallo al actulizar la camiseta en la DB", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImageAttatchMenu() {
        PopupMenu popupMenu = new PopupMenu(Activity_Editar_Camiseta_Administrador.this,binding.botonOpcionesImagen);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Camara");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Galeria");

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int which = item.getItemId();
                if (which == 0){
                    pickImageCamera();
                } else if (which ==  1) {
                    pickImageGallery();
                }

                return false;
            }
        });
    }

    private void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"NEW PICK");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"SAMPLE IMAGE DESCRIPTION");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        binding.fotoCamiseta.setImageURI(imageUri);
                    }else {
                        Toast.makeText(Activity_Editar_Camiseta_Administrador.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        binding.fotoCamiseta.setImageURI(imageUri);
                    } else {
                        Toast.makeText(Activity_Editar_Camiseta_Administrador.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}