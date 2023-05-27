package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Activity_Carga_Pantalla_Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_pantalla_inicio);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onDestroy();
                startActivity(new Intent(Activity_Carga_Pantalla_Inicio.this, Activity_Opciones.class));
            }
        }, 3000 );
    }
}