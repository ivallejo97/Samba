package com.example.samba;

import android.widget.Filter;

import java.util.ArrayList;

public class Filtro_Camisetas extends Filter {

    ArrayList<Model_Camisetas_Tienda> filtrarCamisetas;
    Adapter_Camisetas_Tienda adapterCamisetasTienda;

    public Filtro_Camisetas(ArrayList<Model_Camisetas_Tienda> filtrarCamisetas, Adapter_Camisetas_Tienda adapterCamisetasTienda) {
        this.filtrarCamisetas = filtrarCamisetas;
        this.adapterCamisetasTienda = adapterCamisetasTienda;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<Model_Camisetas_Tienda> filtroCamisetas = new ArrayList<>();
            for (int i = 0; i < filtrarCamisetas.size(); i++) {
                if (filtrarCamisetas.get(i).getTitulo().toUpperCase().contains(constraint)){
                    filtroCamisetas.add(filtrarCamisetas.get(i));
                }
            }
            results.count = filtroCamisetas.size();
            results.values = filtroCamisetas;
        } else {
            results.count = filtrarCamisetas.size();
            results.values = filtrarCamisetas;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterCamisetasTienda.camisetasTiendaArrayList = (ArrayList<Model_Camisetas_Tienda>) results.values;

        adapterCamisetasTienda.notifyDataSetChanged();
    }
}
