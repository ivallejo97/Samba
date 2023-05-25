package com.example.samba.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samba.R;
import com.example.samba.databinding.EstiloDireccionesEnvioBinding;
import com.example.samba.databinding.EstiloMisCamisetasUsuarioBinding;
import com.example.samba.model.Model_Direcciones_Envio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter_Direcciones_Envio extends RecyclerView.Adapter<Adapter_Direcciones_Envio.HolderDireccionesEnvio> {

    private Context context;
    ArrayList<Model_Direcciones_Envio> modelDireccionesEnviosList;
    EstiloDireccionesEnvioBinding binding;

    public Adapter_Direcciones_Envio(Context context, ArrayList<Model_Direcciones_Envio> modelDireccionesEnviosList) {
        this.context = context;
        this.modelDireccionesEnviosList = modelDireccionesEnviosList;
    }

    @NonNull
    @Override
    public HolderDireccionesEnvio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloDireccionesEnvioBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderDireccionesEnvio(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDireccionesEnvio holder, int position) {
        Model_Direcciones_Envio modelDireccionesEnvio = modelDireccionesEnviosList.get(position);

        String calle = "" + modelDireccionesEnvio.getCalle();
        String numero = "" + modelDireccionesEnvio.getNumero();
        String piso = "" + modelDireccionesEnvio.getPiso();
        String puerta = "" + modelDireccionesEnvio.getPuerta();
        String pais = "" + modelDireccionesEnvio.getPais();
        String provincia = "" + modelDireccionesEnvio.getProvincia();
        String ciudad = "" + modelDireccionesEnvio.getLocalidad();

        holder.ubicacion.setText("" + (position + 1));
        holder.calle.setText(calle);
        holder.numero.setText(numero);
        holder.piso.setText(piso);
        holder.puerta.setText(puerta);
        holder.pais.setText(pais);
        holder.provincia.setText(provincia);
        holder.localidad.setText(ciudad);

        holder.botonDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textoCalle.getVisibility() == View.VISIBLE){
                    holder.botonDesplegar.setImageResource(R.drawable.icono_desplegar);
                    holder.textoCalle.setVisibility(View.GONE);
                } else {
                    holder.botonDesplegar.setImageResource(R.drawable.icono_replegar);
                    holder.textoCalle.setVisibility(View.VISIBLE);
                }

                if (holder.textoNumero.getVisibility() == View.VISIBLE){
                    holder.textoNumero.setVisibility(View.GONE);
                } else holder.textoNumero.setVisibility(View.VISIBLE);

                if (holder.textoPiso.getVisibility() == View.VISIBLE){
                    holder.textoPiso.setVisibility(View.GONE);
                } else holder.textoPiso.setVisibility(View.VISIBLE);

                if (holder.textoPuerta.getVisibility() == View.VISIBLE){
                    holder.textoPuerta.setVisibility(View.GONE);
                } else holder.textoPuerta.setVisibility(View.VISIBLE);

                if (holder.calle.getVisibility() == View.VISIBLE){
                    holder.calle.setVisibility(View.GONE);
                } else holder.calle.setVisibility(View.VISIBLE);

                if (holder.numero.getVisibility() == View.VISIBLE){
                    holder.numero.setVisibility(View.GONE);
                } else holder.numero.setVisibility(View.VISIBLE);

                if (holder.piso.getVisibility() == View.VISIBLE){
                    holder.piso.setVisibility(View.GONE);
                } else holder.piso.setVisibility(View.VISIBLE);

                if (holder.puerta.getVisibility() == View.VISIBLE){
                    holder.puerta.setVisibility(View.GONE);
                } else holder.puerta.setVisibility(View.VISIBLE);

                if (holder.localidad.getVisibility() == View.VISIBLE){
                    holder.localidad.setVisibility(View.GONE);
                } else holder.localidad.setVisibility(View.VISIBLE);

                if (holder.provincia.getVisibility() == View.VISIBLE){
                    holder.provincia.setVisibility(View.GONE);
                } else holder.provincia.setVisibility(View.VISIBLE);

                if (holder.pais.getVisibility() == View.VISIBLE){
                    holder.pais.setVisibility(View.GONE);
                } else holder.pais.setVisibility(View.VISIBLE);

            }
        });


        holder.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Dirección")
                        .setMessage("¿Estas seguro de que quieres eliminar esta dirección?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarDireccion(modelDireccionesEnvio,holder);
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

    private void eliminarDireccion(Model_Direcciones_Envio modelDireccionesEnvio, HolderDireccionesEnvio holder) {

        String id = modelDireccionesEnvio.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DireccionesEnvio");
        databaseReference.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Dirección eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Fallo al eliminar la dirección", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return modelDireccionesEnviosList.size();
    }


    class HolderDireccionesEnvio extends RecyclerView.ViewHolder{

        TextView ubicacion, calle, numero, puerta, piso, pais, provincia, localidad, textoCalle, textoNumero, textoPiso, textoPuerta;
        ImageView botonEliminar, botonDesplegar;

        public HolderDireccionesEnvio(@NonNull View itemView) {
            super(itemView);

            textoCalle = binding.textoCalle;
            textoNumero = binding.textoNumero;
            textoPiso = binding.textoPiso;
            textoPuerta = binding.textoPuerta;
            ubicacion = binding.numeroUbicacion;
            calle = binding.calle;
            numero = binding.numero;
            puerta = binding.puerta;
            piso = binding.piso;
            pais = binding.pais;
            provincia = binding.provincia;
            localidad = binding.ciudad;
            botonEliminar = binding.botonEliminar;
            botonDesplegar = binding.botonDesplegar;

        }
    }
}
