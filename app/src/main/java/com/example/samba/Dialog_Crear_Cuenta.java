package com.example.samba;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;
import android.widget.Toast;

public class Dialog_Crear_Cuenta {
    Context context;
    Dialog dialog;

    TextView textView;

    public Dialog_Crear_Cuenta(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_crear_cuenta);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textView = dialog.findViewById(R.id.texo_crear_cuenta);
        dialog.create();
        dialog.show();
    }

    public void modifyDialog(String titulo){
        textView = dialog.findViewById(R.id.texo_crear_cuenta);
        textView.setText(titulo);
    }

    public void hideDialog(){
        dialog.dismiss();
    }


}
