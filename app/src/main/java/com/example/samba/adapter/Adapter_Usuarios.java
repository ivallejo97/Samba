package com.example.samba.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.Activity_Chat;
import com.example.samba.filtros.Categoria_Filtrar;
import com.example.samba.model.Model_Usuarios;
import com.example.samba.databinding.EstiloChatBinding;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Usuarios extends RecyclerView.Adapter<Adapter_Usuarios.HolderUsuarios> {

    private Context context;
    public ArrayList<Model_Usuarios> arrayList;
    private EstiloChatBinding binding;
    private Categoria_Filtrar filtrarCategoria;

    public Adapter_Usuarios(Context context, ArrayList<Model_Usuarios> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter_Usuarios.HolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloChatBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Usuarios.HolderUsuarios(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Usuarios.HolderUsuarios holder, int position) {
        Model_Usuarios model = arrayList.get(position);
        String uid = model.getUid();
        String nombreCompleto = model.getName();
        String nombreUsuario = model.getUsername();
        String fotoUsuario = model.getProfileImage();

        holder.name.setText(nombreCompleto);
        holder.username.setText("@" + nombreUsuario);
        Glide.with(context).load(fotoUsuario).into(binding.fotoUsuario);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Chat.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class HolderUsuarios extends RecyclerView.ViewHolder{

        TextView name,username;
        CircleImageView fotoUsuario;
        public HolderUsuarios(@NonNull View itemView) {
            super(itemView);

            name = binding.nombreCompletoUsuario;
            username = binding.nombreUsuario;
            fotoUsuario = binding.fotoUsuario;
        }
    }


}
