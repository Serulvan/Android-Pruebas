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

/**
 * Created by Serulvan on 28/04/2016.
 */
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
        if (!arr.get(position).getPass().isEmpty()) {
            TextView datos1 = (TextView) item.findViewById(R.id.datos1);
            datos1.setText(arr.get(position).getPass());
        }/*
        TextView datos2 = (TextView)item.findViewById(R.id.datos2);

        //50/3=16,6666666 => 17
        int nSe;
        if (arr.get(position).level>-50){
            nSe=4;
        }else if (arr.get(position).level>-67){
            nSe=3;
        }else if (arr.get(position).level>-84){
            nSe=2;
        }else{
            nSe=1;
        }
        datos2.setText(String.valueOf(arr.get(position).level));
        datos2.setVisibility(View.VISIBLE);*/

        return item;
    }
}
