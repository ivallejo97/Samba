package com.example.samba;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.samba.databinding.ActivityAgregarCategoriaBinding;
import com.example.samba.databinding.ActivityInterfazAdminBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class Activity_Agregar_Categoria extends AppCompatActivity {

    ActivityAgregarCategoriaBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri imageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityAgregarCategoriaBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Añadiendo Categoria");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttatchMenu();
            }
        });

        binding.botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String nombreCategoria = "", descripcionCategoria = "";
    // Validar que el formato de los datos coincida con el formato necesario para añadir la categoria a la base de datos.
    private void validateData() {

        nombreCategoria = binding.nombreCategoria.getText().toString().trim();
        descripcionCategoria = binding.descripcionCategoria.getText().toString().trim();

        if (TextUtils.isEmpty(nombreCategoria)){
            Toast.makeText(this,"Introduce el nombre de la categoria", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descripcionCategoria)){
            Toast.makeText(this,"Añade una descripción de la categoria", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null){
            Toast.makeText(this,"Añade una imagen", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null){
                addCategoryFirebase("");
            } else {
                uploadImage("");
            }
        }
    }

    // Metodo para añadir e actualizar una imagen en la base de datos, la imagen en este caso se guarda en el apartado de storage de
    // firebase, este apartado sirve para guardar todas las imagenes que se añaden a la app. Dependiendo del nombre del string file
    // la imagen se guardara en una carpeta distinta
    private void uploadImage(String s) {
        String file = "CategoryImages/"+firebaseAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(file);
        String imagenID = UUID.randomUUID().toString();
        storageReference.child(imagenID).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadImage = ""+ uriTask.getResult();
                        addCategoryFirebase(uploadImage);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Agregar_Categoria.this,"Fallo en la actualización de la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Con los datos validados en el metodo validateData utilizamos un Map para guardar los datos con una estructura Clave, Valor
    // una vez tenemos todos los datos guardados en el map, creamos una nueva referencia en la base de datos con los datos del map
    private void addCategoryFirebase(String imageUrl) {

        progressDialog.setMessage("Añadiendo Categoria...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id","" + timestamp);
        hashMap.put("categoria","" + nombreCategoria);
        hashMap.put("descripcionCategoria","" + descripcionCategoria);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid","" + firebaseAuth.getUid());

        if (imageUri != null){
            hashMap.put("fotoCategoria", "" +imageUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Agregar_Categoria.this,"Categoria Añadida", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Agregar_Categoria.this,"" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

    // Metodo para mostrar un menu con dos opciones distintas para añadir una imagen
    private void showImageAttatchMenu() {
        PopupMenu popupMenu = new PopupMenu(this,binding.botonCamara);
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
                        binding.fotoFondoCategoria.setImageURI(imageUri);
                    }else {
                        Toast.makeText(Activity_Agregar_Categoria.this,"Cancelado",Toast.LENGTH_SHORT).show();
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
                        binding.fotoFondoCategoria.setImageURI(imageUri);
                    } else {
                        Toast.makeText(Activity_Agregar_Categoria.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}