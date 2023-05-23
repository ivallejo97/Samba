package com.example.samba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samba.R;
import com.example.samba.databinding.EstiloChatBinding;
import com.example.samba.databinding.EstiloChatDerechaBinding;
import com.example.samba.databinding.EstiloChatIzquierdaBinding;
import com.example.samba.model.Model_Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.HolderChat> {


    private static final int MSG_IZQUIERDO = 0;
    private static final int MSG_DERECHO = 1;
    Context context;
    ArrayList<Model_Chat> chatArrayList;
    FirebaseUser firebaseUser;



    public Adapter_Chat(Context context, ArrayList<Model_Chat> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_DERECHO){
            View view = LayoutInflater.from(context).inflate(R.layout.estilo_chat_derecha, parent, false);
            return new HolderChat(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.estilo_chat_izquierda, parent, false);
            return new HolderChat(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderChat holder, int position) {

        String mensaje = chatArrayList.get(position).getMensaje();
        String idEnvia = chatArrayList.get(position).getIdUsuarioEnvia();
        String idRecibe = chatArrayList.get(position).getIdUsuarioRecibe();
        String timestamp = chatArrayList.get(position).getTimestamp();

        holder.mensaje.setText(mensaje);
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatArrayList.get(position).getIdUsuarioEnvia().equals(firebaseUser.getUid())){
            return MSG_DERECHO;
        } else {
            return MSG_IZQUIERDO;
        }
    }

    class HolderChat extends RecyclerView.ViewHolder{

        TextView mensaje;
        public HolderChat(@NonNull View itemView) {
            super(itemView);

            mensaje = itemView.findViewById(R.id.mensaje);
        }
    }

}
