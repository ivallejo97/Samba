package com.example.samba;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MetodosApp extends Application {




    public static void addCamisetaFavoritos(Context context, String idCamiseta){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        long timestamp = System.currentTimeMillis();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("idCamiseta", "" + idCamiseta);
        hashMap.put("timestamp", "" + timestamp);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Favoritos").child(idCamiseta)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void eliminarFavoritos(Context context, String idCamiseta){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Favoritos").child(idCamiseta)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
