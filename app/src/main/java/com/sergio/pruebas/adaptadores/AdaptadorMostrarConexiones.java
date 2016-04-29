package com.sergio.pruebas.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sergio.pruebas.R;
import com.sergio.pruebas.conexiones.Conexion;

import java.util.ArrayList;

public class AdaptadorMostrarConexiones extends ArrayAdapter {
    private Context c;
    private ArrayList<Conexion> arr;
    private int view;
    public AdaptadorMostrarConexiones(Context context, int resource, ArrayList<Conexion> objects) {
        super(context, resource, objects);
        c=context;
        arr=objects;
        view=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        if (item==null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(view,null);
        }
        TextView ssid = (TextView)item.findViewById(R.id.ssid_list);
        ssid.setText(arr.get(position).getSsid());
        ssid.setTag(String.valueOf(arr.get(position).getId()));

        TextView datos1 = (TextView) item.findViewById(R.id.datos1);
        if (!arr.get(position).getPass().isEmpty()) {
            datos1.setText(arr.get(position).getPass());
        }else datos1.setVisibility(View.INVISIBLE);

        return item;
    }
}
