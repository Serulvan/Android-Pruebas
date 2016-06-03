package com.sergio.pruebas.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sergio.pruebas.R;
import com.sergio.pruebas.entidades.Conexion;

import java.util.ArrayList;
import java.util.Objects;

public class AdaptadorMostrarConexiones extends ArrayAdapter {
    private ArrayList<Conexion> arr;
    private int view;
    public AdaptadorMostrarConexiones(Context context, int resource, ArrayList<Conexion> objects) {
        super(context, resource, objects);
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
        ssid.setTag(arr.get(position).getId());

        TextView datos1 = (TextView) item.findViewById(R.id.datos1);
        if(arr.get(position).getCifrado().equals(Conexion.CIFRADO_ABIERTO)){
            datos1.setVisibility(View.INVISIBLE);
        }else{
            datos1.setText(arr.get(position).getPass());
            datos1.setVisibility(View.VISIBLE);
        }

        //TextView datos3 = (TextView) item.findViewById(R.id.datos3);
        //datos3.setText(arr.get(position).getId());

        RelativeLayout rl = (RelativeLayout) item.findViewById(R.id.lbw_rl);
        rl.setBackgroundResource(R.drawable.list_view_clic_style);
        return item;
    }
}
