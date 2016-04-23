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
import android.widget.Toast;

import com.sergio.pruebas.dialogos.DialogoNuevaConexion;
import com.sergio.pruebas.hilos.HiloEscaneoWifi;

public class NuevaConexion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv;
    private WifiManager wm;
    private HiloEscaneoWifi he;
    private Button nc_btn_avanzado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nueva_conexion);
        lv=(ListView)findViewById(R.id.lv);
        nc_btn_avanzado=(Button)findViewById(R.id.nc_btn_avanzado);
        lv.setOnItemClickListener(this);
        nc_btn_avanzado.setOnClickListener(this);
        wm = (WifiManager) getSystemService(this.WIFI_SERVICE);
        he = new HiloEscaneoWifi(lv,wm,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //lanzar hilo
        he.execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //lanzar hilo
        he.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        he.pausar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cerrar Hilo
        he.parar();
    }

    @Override
    public void onClick(View v) {
        //cerrar Hilo
        onPause();
        Intent i = new Intent(this, DialogoNuevaConexion.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //cerrar Hilo
        onPause();
        Intent i = new Intent(this, DialogoNuevaConexion.class);
        TextView tv = (TextView)view.findViewById(R.id.ssid_list);
        i.putExtra("ssid", tv.getText().toString());
        startActivity(i);
    }
}
