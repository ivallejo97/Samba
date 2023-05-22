package com.example.samba;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.samba.databinding.ActivityPublicarAdministradorBinding;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Activity_Publicar_Administrador extends AppCompatActivity {

    private ActivityPublicarAdministradorBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<String> categoriaArrayList, categoriaIdArrayList;
    private Uri imageUri = null;
    private static final String TAG = "ADD_PRODUCT_TAG";
    String[] marcas = {"Adidas","Nike","Puma","New Balance","Umbro","Castore","Kappa","Hummel","Joma"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterTallas;
    ArrayAdapter<String> adapterMarcas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityPublicarAdministradorBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarCategorias();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Añadiendo Producto");
        progressDialog.setCanceledOnTouchOutside(false);

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

        binding.botonAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerCategoria();
            }
        });
    }

    private String titulo="",talla ="",marca="",precio="",descripcion="";
    private void validateData() {

        titulo = binding.nombreProducto.getText().toString().trim();
        marca = binding.marcaProducto.getText().toString().trim();
        precio = binding.precioProducto.getText().toString().trim();
        descripcion = binding.descripcionProducto.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)){
            Toast.makeText(this,"Introduce un nombre al producto",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(marca)){
            Toast.makeText(this,"Selecciona una marca",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(precio)) {
            Toast.makeText(this,"Introduce un precio",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(categoriaSeleccionada)) {
            Toast.makeText(this,"Escoge una categoria",Toast.LENGTH_SHORT).show();
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

    private void uploadImage(String s) {
        Log.d(TAG,"agregarProducto: Añadiendo a Storage...");
        progressDialog.setMessage("Añadiendo el producto...");
        progressDialog.show();

        String rutaProductos = "Camisetas/"+firebaseAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(rutaProductos);

        String imagenID = UUID.randomUUID().toString();

        storageReference.child(imagenID).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"onSucces: Camiseta añadida a Storage...");
                        Log.d(TAG,"onSucces: ruta a la camiseta");

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
                        Toast.makeText(Activity_Publicar_Administrador.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void agregarProductoFirebase(String agregarProductoUrl) {
        Log.d(TAG,"agregarProducto: Añadiendo a DB...");
        progressDialog.setMessage("Añadiendo Producto a DB");

        long timestamp = System.currentTimeMillis();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("titulo",""+titulo);
        hashMap.put("marca",""+marca);
        hashMap.put("precio",""+precio);
        hashMap.put("categoriaId",""+categoriaIdSeleccionada);
        hashMap.put("descripcion",""+descripcion);
        hashMap.put("numeroVisitas",0);
        hashMap.put("url",""+agregarProductoUrl);
        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("id",""+timestamp);
        hashMap.put("timestamp",timestamp);
        if (imageUri != null){
            hashMap.put("fotoCategoria", "" +agregarProductoUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Camisetas");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onSucces: Actulizado Correctamente");
                        Toast.makeText(Activity_Publicar_Administrador.this,"Producto Añadido",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure: Failed to update db" + e.getMessage());
                        Toast.makeText(Activity_Publicar_Administrador.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarCategorias() {
        Log.d(TAG,"cargarCategorias: Cargando Categorias...");
        categoriaArrayList = new ArrayList<>();
        categoriaIdArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categorias");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriaArrayList.clear();
                categoriaIdArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String categoriaId = ""+ dataSnapshot.child("id").getValue();
                    String categoria = ""+ dataSnapshot.child("categoria").getValue();

                    categoriaArrayList.add(categoria);
                    categoriaIdArrayList.add(categoriaId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String categoriaSeleccionada, categoriaIdSeleccionada;
    private void escogerCategoria() {
        Log.d(TAG,"escogerCategoria: Mostrando el dialogo para escoger");

        String[] arrayCategorias = new String[categoriaArrayList.size()];
        for (int i = 0; i < categoriaArrayList.size(); i++) {
            arrayCategorias[i] = categoriaArrayList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escoge una categoria")
                .setItems(arrayCategorias, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categoriaSeleccionada = categoriaArrayList.get(which);
                        categoriaIdSeleccionada = categoriaIdArrayList.get(which);
                        binding.categoria.setText(categoriaSeleccionada);
                    }
                }).show();

    }

    private void showImageAttatchMenu() {
        PopupMenu popupMenu = new PopupMenu(this,binding.fotoProducto2);
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
                        Toast.makeText(Activity_Publicar_Administrador.this,"Cancelado",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Activity_Publicar_Administrador.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );



}