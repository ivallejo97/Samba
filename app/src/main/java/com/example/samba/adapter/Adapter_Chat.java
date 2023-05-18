package com.example.samba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.samba.filtros.Categoria_Filtrar;
import com.example.samba.model.Model_Chat;
import com.example.samba.databinding.EstiloChatBinding;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.HolderChat> {

    private Context context;
    public ArrayList<Model_Chat> arrayList;
    private EstiloChatBinding binding;
    private Categoria_Filtrar filtrarCategoria;

    public Adapter_Chat(Context context, ArrayList<Model_Chat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter_Chat.HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = EstiloChatBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Adapter_Chat.HolderChat(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Chat.HolderChat holder, int position) {
        Model_Chat model = arrayList.get(position);
        String id = model.getUid();
        String nombreCompleto = model.getName();
        String nombreUsuario = model.getUsername();
        String fotoUsuario = model.getProfileImage();

        holder.name.setText(nombreCompleto);
        holder.username.setText(nombreUsuario);
        Glide.with(context).load(fotoUsuario).into(binding.fotoUsuario);




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class HolderChat extends RecyclerView.ViewHolder{

        TextView name,username;
        CircleImageView fotoUsuario;
        public HolderChat(@NonNull View itemView) {
            super(itemView);

            name = binding.nombreCompletoUsuario;
            username = binding.nombreUsuario;
            fotoUsuario = binding.fotoUsuario;
        }
    }


}
