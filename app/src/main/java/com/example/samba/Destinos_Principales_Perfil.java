package com.example.samba;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.samba.databinding.DestinosPrincipalesPerfilBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Destinos_Principales_Perfil extends Fragment {

    DestinosPrincipalesPerfilBinding binding;
    NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    private static final String TAG = "PROFILE_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = DestinosPrincipalesPerfilBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Verificar cuenta");
        progressDialog.setCanceledOnTouchOutside(false);
        loadUserInfo();


        binding.iconoVerificarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser.isEmailVerified()){
                    Toast.makeText(getContext(),"Usuario ya verififcado", Toast.LENGTH_SHORT).show();
                } else {
                    dialogVerificarEmail();
                }
            }
        });

        binding.botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.destinos_principales_editar_perfil);
            }
        });

        binding.iconoAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cerrar Sesión")
                        .setMessage("¿Estas seguro de que quieres cerrar sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                startActivity(new Intent(getContext(), Activity_Acceso.class));
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        binding.vistaCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Carrito.class));
            }
        });

        binding.vistaFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Favoritos.class));
            }
        });

        binding.vistaPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Pedidos.class));
            }
        });

        binding.vistaMisProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Mis_Productos.class));
            }
        });


        binding.vistaDirecciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Direcciones_Envio.class));
            }
        });

        binding.vistaMetodosDePago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Metodos_Pago.class));
            }
        });

        binding.vistaQuienesSomos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Activity_Quienes_Somos.class));
            }
        });
    }

    private void dialogVerificarEmail() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Verificar cuenta")
                .setMessage("Enviaremos un mensaje a " + firebaseUser.getEmail() + " para que puedas verificar tu cuenta. Sigue las instrucciones para verificar la cuenta.")
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enviarMensajeVerificacion();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void enviarMensajeVerificacion() {

        progressDialog.setMessage("Enviando el correo para verificar cuenta a " + firebaseUser.getEmail());
        progressDialog.show();

        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Instrucciones enviadas a " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al enviar las instrucciones a " + firebaseUser.getEmail() ,Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loadUserInfo() {
        Log.d(TAG,"loadUserInfo: Loading User..." + firebaseAuth.getUid());

        if (firebaseUser.isEmailVerified()){
            binding.iconoVerificarCuenta.setImageResource(R.drawable.icono_verificado);
        } else {
            binding.iconoVerificarCuenta.setImageResource(R.drawable.icono_verificacion_necesaria);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String name = "" + snapshot.child("name").getValue();
                        String username = "" + snapshot.child("username").getValue();
                        String pais = "" + snapshot.child("pais").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String telefono = "" + snapshot.child("telefono").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String userType = "" + snapshot.child("userType").getValue();

                        binding.correoElectronico.setText(email);
                        binding.nombreUsuario.setText(username);

                        if (isAdded()){
                            Glide.with(getContext())
                                    .load(profileImage)
                                    .placeholder(R.drawable.icono_perfil_predeterminado)
                                    .into(binding.fotoPerfil);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}