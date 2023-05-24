package com.example.samba.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.Activity_Editar_Camiseta_Usuario;
import com.example.samba.databinding.EstiloCamisetasUsuarioBinding;
import com.example.samba.databinding.EstiloMisCamisetasUsuarioBinding;
import com.example.samba.model.Model_Camisetas_Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Adapter_Mis_Camisetas_Usuario extends RecyclerView.Adapter<Adapter_Mis_Camisetas_Usuario.HolderMisCamisetasUsuarios> {

    private Context context;
    ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList;
    EstiloMisCamisetasUsuarioBinding binding;

    public Adapter_Mis_Camisetas_Usuario(Context context, ArrayList<Model_Camisetas_Usuario> camisetasUsuariosList) {
        this.context = context;
        this.camisetasUsuariosList = camisetasUsuariosList;
    }

    @NonNull
    @Override
    public HolderMisCamisetasUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloMisCamisetasUsuarioBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Mis_Camisetas_Usuario.HolderMisCamisetasUsuarios(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMisCamisetasUsuarios holder, int position) {
        Model_Camisetas_Usuario modelCamisetasUsuario = camisetasUsuariosList.get(position);

        String nombreCamiseta = "" + modelCamisetasUsuario.getTitulo();
        String precioCamiseta = "" + modelCamisetasUsuario.getPrecio();
        String tallaCamiseta = "" + modelCamisetasUsuario.getTalla();
        String fotoCamiseta = "" + modelCamisetasUsuario.getFotoProducto();
        long timestamp = modelCamisetasUsuario.getTimestamp();

        String fechaPublicacion = formatoTimestamp(timestamp);

        holder.nombreCamiseta.setText(nombreCamiseta);
        holder.precioCamiseta.setText(precioCamiseta);
        holder.tallaCamiseta.setText("Talla: " + tallaCamiseta);
        holder.fechaPublicacion.setText("Publicado el: " + fechaPublicacion);
        Glide.with(context).load(fotoCamiseta).into(holder.fotoCamiseta);

        holder.botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Editar_Camiseta_Usuario.class);
                intent.putExtra("id" , modelCamisetasUsuario.getId());
                context.startActivity(intent);
            }
        });

        holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Producto")
                        .setMessage("¿Estas seguro de que quieres eliminar este producto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarProducto(modelCamisetasUsuario,holder);
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
    }

    private void eliminarProducto(Model_Camisetas_Usuario modelCamisetasUsuario, HolderMisCamisetasUsuarios holder) {

        String id = modelCamisetasUsuario.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CamisetasUsuarios");
        databaseReference.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Articulo Eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return camisetasUsuariosList.size();
    }

    public static final String formatoTimestamp(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd/MM/yyyy", calendar).toString();

        return date;
    }

    class HolderMisCamisetasUsuarios extends RecyclerView.ViewHolder{

        ShapeableImageView fotoCamiseta;
        TextView precioCamiseta, nombreCamiseta, tallaCamiseta, fechaPublicacion;
        ImageView botonEliminar, botonEditar;

        public HolderMisCamisetasUsuarios(@NonNull View itemView) {
            super(itemView);

            fotoCamiseta = binding.fotoProducto;
            precioCamiseta = binding.precioProducto;
            nombreCamiseta = binding.nombreProducto;
            tallaCamiseta = binding.tallaProducto;
            fechaPublicacion = binding.fechaPublicacion;
            botonEliminar = binding.botonEliminar;
            botonEditar = binding.botonEditar;

        }
    }
}
