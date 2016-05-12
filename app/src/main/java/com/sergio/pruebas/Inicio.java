package com.sergio.pruebas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sergio.pruebas.memoria.GestionArchivos;
import com.sergio.pruebas.memoria.GestionPreferencias;
import com.sergio.pruebas.servicio.ServiceWifiManager;

public class Inicio extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button btnAdd, btnGest, del, ver;
    private ToggleButton stateServiceTbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        btnAdd=(Button)findViewById(R.id.btnadd);
        btnGest=(Button)findViewById(R.id.btngest);
        del=(Button)findViewById(R.id.del);
        ver=(Button)findViewById(R.id.ver);

        btnAdd.setOnClickListener(this);
        btnGest.setOnClickListener(this);
        del.setOnClickListener(this);
        ver.setOnClickListener(this);

        stateServiceTbtn = (ToggleButton)findViewById(R.id.tbserv);
        stateServiceTbtn.setChecked(GestionPreferencias.getConfigServActivo(GestionPreferencias.getSharedPreferencesConfig(this)));
        stateServiceTbtn.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnAdd.getId()){
            Intent i = new Intent(this,NuevaConexion.class);
            startActivity(i);
        }else if(v.getId()==btnGest.getId()){
            Intent i = new Intent(this,VerConexiones.class);
            startActivity(i);
        }else if(v.getId()==del.getId()){
            GestionArchivos.getSharedPreferencesListado(this).edit().clear().commit();
        }else if(v.getId()==ver.getId()){
            Toast.makeText(this, getSharedPreferences("$$listado", MODE_PRIVATE).getString("$jList", ""), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            startService(new Intent(this,ServiceWifiManager.class));
        } else {
            stopService(new Intent(this,ServiceWifiManager.class));
        }

    }
}
