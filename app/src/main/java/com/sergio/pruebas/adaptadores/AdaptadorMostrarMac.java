package com.sergio.pruebas.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sergio.pruebas.R;
import com.sergio.pruebas.entidades.Mac;

import java.util.ArrayList;

public class AdaptadorMostrarMac extends ArrayAdapter {

    private int resource;
    private ArrayList<Mac> mac;

    public AdaptadorMostrarMac(Context context, int resource, ArrayList<Mac> mac) {
        super(context, resource, mac);
        this.resource=resource;
        this.mac=mac;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        if (item==null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(resource,null);
        }

        TextView tvMac = (TextView)item.findViewById(R.id.mb_mac);
        tvMac.setText(mac.get(position).getMac());
        TextView tvfecha = (TextView)item.findViewById(R.id.mb_fecha);
        tvfecha.setText(mac.get(position).getFecha());

        return item;
    }
}
