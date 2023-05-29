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
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.samba.databinding.ActivityPublicarBinding;
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
        
        binding.botonCamara.setOnClickListener(new View.OnClickListener() {
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
    // Validar que los datos de los edittex son los correctos
    private void validateData() {
        
        titulo = binding.nombreProducto.getText().toString().trim();
        talla = binding.tallaProducto.getText().toString().trim();
        marca = binding.marcaProducto.getText().toString().trim();
        precio = binding.precioProducto.getText().toString().trim();
        descripcion = binding.descripcionProducto.getText() .toString().trim();

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
    // Guardar la imagen que se añade a la aplicación en el storage de firebase
    private void uploadImage(String s) {
        progressDialog.setMessage("Añadiendo el producto...");
        progressDialog.show();

        String rutaProductos = "CamisetasUsuarios/"+firebaseAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(rutaProductos);

        // Crear una id unica para cada imagen, para que al añadir mas de un producto la imagen no se sobreescriba
        String imagenID = UUID.randomUUID().toString();

        storageReference.child(imagenID).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadImage = "" + uriTask.getResult();
                        agregarProductoFirebase(uploadImage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure: Fail");
                        Toast.makeText(Activity_Publicar.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
    // Crear un map para guardar todos los valores a la refrencia CamisetasUsuarios, para guardar cada una de las camisetas que se
    // añadan desde esta activity
    private void agregarProductoFirebase(String fotoProducto) {
        progressDialog.setMessage("Añadiendo el producto a la DB");

        long timestamp = System.currentTimeMillis();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("titulo","" + titulo);
        hashMap.put("talla","" + talla);
        hashMap.put("marca","" + marca);
        hashMap.put("precio","" + precio);
        hashMap.put("descripcion","" + descripcion);
        hashMap.put("numeroVisitas",0);
        if (imageUri != null){
            hashMap.put("fotoProducto","" + fotoProducto);
        }
        hashMap.put("uid","" + firebaseAuth.getUid());
        hashMap.put("id","" + timestamp);
        hashMap.put("timestamp", timestamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onSucces: Actulizado Correctamente");
                        Toast.makeText(Activity_Publicar.this,"Producto Añadido",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure: Failed to update db" + e.getMessage());
                        Toast.makeText(Activity_Publicar.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Mostrar el menu de opciones para añadir una imagen a a la app
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
                        binding.fotoProducto2.setImageURI(imageUri);
                    }else {
                        Toast.makeText(Activity_Publicar.this,"Cancelado",Toast.LENGTH_SHORT).show();
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
                        binding.fotoProducto2.setImageURI(imageUri);
                    } else {
                        Toast.makeText(Activity_Publicar.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}