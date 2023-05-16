package com.example.samba;

import android.widget.Filter;

import java.util.ArrayList;

public class Categoria_Filtrar extends Filter {

    ArrayList<Model_Categoria> filtrarLista;
    Adapter_Categoria adapterCategoria;

    public Categoria_Filtrar(ArrayList<Model_Categoria> filtrarLista, Adapter_Categoria adapterCategoria) {
        this.filtrarLista = filtrarLista;
        this.adapterCategoria = adapterCategoria;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<Model_Categoria> filtrarCategoria = new ArrayList<>();
            for (int i = 0; i < filtrarLista.size(); i++) {
                if (filtrarLista.get(i).getCategoria().toUpperCase().contains(constraint)){
                    filtrarCategoria.add(filtrarLista.get(i));
                }
            }
            results.count = filtrarCategoria.size();
            results.values = filtrarCategoria;
        } else {
            results.count = filtrarLista.size();
            results.values = filtrarLista;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterCategoria.categoriaArrayList = (ArrayList<Model_Categoria>) results.values;

        adapterCategoria.notifyDataSetChanged();
    }
}
