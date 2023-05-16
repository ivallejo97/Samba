package com.example.samba;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.samba.databinding.ActivityEditarPerfilAdminBinding;
import com.example.samba.databinding.ActivityPerfilAdminBinding;
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

import org.checkerframework.checker.units.qual.A;

import java.net.URI;
import java.util.HashMap;

public class Activity_Editar_Perfil_Admin extends AppCompatActivity {

    ActivityEditarPerfilAdminBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EDIT_ADMIN_PROFILE";
    private Uri imageUri = null;
    private String nombre = "", nombreUsuario = "", telefono = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityEditarPerfilAdminBinding.inflate(getLayoutInflater())).getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.nuevaFotoPerfilAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAttatchMenu();
            }
        });

        binding.botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        binding.botonCerrarEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadUserInfo() {
        Log.d(TAG,"loadUserInfo: Loading User..." + firebaseAuth.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombre_completo = "" + snapshot.child("name").getValue();
                        String nombre_usuario = "" + snapshot.child("username").getValue();
                        String email = "" + snapshot.child("email").getValue();
                        String telefono = "" + snapshot.child("telefono").getValue();
                        String nacionalidad = "" + snapshot.child("pais").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String userType = "" + snapshot.child("userType").getValue();

                        binding.nombreCompletoAdministrador.setText(nombre_completo);
                        binding.nombreUsuarioAdministrador.setText(nombre_usuario);
                        binding.correoElectronicoAdministrador.setText(email);
                        binding.numeroTelefonoAdministrador.setText(telefono);
                        binding.nacionalidadAdministrador.setText(nacionalidad);

                        if (!Activity_Editar_Perfil_Admin.this.isDestroyed()){
                            Glide.with(Activity_Editar_Perfil_Admin.this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.icono_aceptar)
                                    .into(binding.nuevaFotoPerfilAdministrador);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    private void validateData() {
        nombre = binding.nombreCompletoAdministrador.getText().toString().trim();
        nombreUsuario = binding.nombreUsuarioAdministrador.getText().toString().trim();
        telefono = binding.numeroTelefonoAdministrador.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this,"Introduce un nombre",Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null){
                updateProfile("");
            } else {
                uploadImage("");
            }
        }
    }

    private void uploadImage(String s) {
        String file = "ProfileImages/"+firebaseAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(file);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadImage = ""+ uriTask.getResult();
                        Log.d(TAG, "Imagen actualizada" + uploadImage);
                        updateProfile(uploadImage);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Editar_Perfil_Admin.this,"Fallo en la actualización de la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfile(String imageUrl) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name",""+nombre);
        hashMap.put("username",""+nombreUsuario);
        hashMap.put("telefono",""+telefono);

        if (imageUri != null){
            hashMap.put("profileImage", ""+imageUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Activity_Editar_Perfil_Admin.this,"Perfil actualizado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_Editar_Perfil_Admin.this,"Fallo al actulizar la imagen en la DB", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    private void showImageAttatchMenu() {
        PopupMenu popupMenu = new PopupMenu(this,binding.nuevaFotoPerfilAdministrador);
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
                        binding.nuevaFotoPerfilAdministrador.setImageURI(imageUri);
                    }else {
                        Toast.makeText(Activity_Editar_Perfil_Admin.this,"Cancelado",Toast.LENGTH_SHORT).show();
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
                        binding.nuevaFotoPerfilAdministrador.setImageURI(imageUri);
                    } else {
                        Toast.makeText(Activity_Editar_Perfil_Admin.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    public void onBackPressed(){
        if (binding.botonCerrarEditarPerfil.isPressed()){
            super.onBackPressed();
        } else {

        }
    }


}