package com.example.samba;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.samba.databinding.ActivityDestinosPrincipalesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;

public class Activity_Destinos_Principales extends AppCompatActivity {

    ActivityDestinosPrincipalesBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityDestinosPrincipalesBinding.inflate(getLayoutInflater())).getRoot());

        //setSupportActionBar(binding.toolbar);

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();


        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.destinos_principales_editar_perfil) {
                binding.bottomNavView.setVisibility(View.GONE);
                binding.botonIrAgregarProducto.setVisibility(View.GONE);
            } else {
                binding.bottomNavView.setVisibility(View.VISIBLE);
                binding.botonIrAgregarProducto.setVisibility(View.VISIBLE);
            }
        });


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                // Top-level destinations:
                R.id.destinos_Principales_Inicio2, R.id.destinos_Principales_Buscador, R.id.destinos_Principales_Agregar_Producto,
                R.id.destinos_Principales_Chat, R.id.destinos_Principales_Perfil

        )

                .setOpenableLayout(binding.drawerLayout)
                .build();


        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        //NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        //bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);


        binding.botonIrAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Destinos_Principales.this,Activity_Publicar.class));
            }
        });
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Quieres salir de Samba?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}