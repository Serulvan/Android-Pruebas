package com.sergio.pruebas;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sergio.pruebas.dialogos.DialogoNuevaConexion;
import com.sergio.pruebas.hilos.GestorHilos;

public class NuevaConexion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_conexion);
        ListView lv = (ListView) findViewById(R.id.lv);
        Button nc_btn_avanzado = (Button) findViewById(R.id.nc_btn_avanzado);
        if (lv != null) {
            lv.setOnItemClickListener(this);
        }
        if (nc_btn_avanzado != null) {
            nc_btn_avanzado.setOnClickListener(this);
        }
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        GestorHilos.declararHiloEscaneoWifi(lv, wm, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //lanzar hilo
        GestorHilos.iniciarHiloEscaneoWifi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cerrar Hilo
        GestorHilos.pararHiloEscaneoWifi();
    }

    @Override
    public void onClick(View v) {
        //pausar Hilo
        GestorHilos.pausarHiloEscaneoWifi();
        Intent i = new Intent(this, DialogoNuevaConexion.class);
        i.putExtra("pass", "$");
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //pausar Hilo
        GestorHilos.pausarHiloEscaneoWifi();

        Intent i = new Intent(this, DialogoNuevaConexion.class);
        TextView tv = (TextView)view.findViewById(R.id.ssid_list);
        i.putExtra("ssid", tv.getText().toString());

        tv=(TextView)view.findViewById(R.id.datos1);
        i.putExtra("pass",tv.getText().toString());
        startActivity(i);
    }
}
