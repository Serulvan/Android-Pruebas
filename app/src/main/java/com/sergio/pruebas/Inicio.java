package com.sergio.pruebas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sergio.pruebas.dialogos.DialogoConfiguracion;
import com.sergio.pruebas.memoria.GestionArchivos;
import com.sergio.pruebas.memoria.GestionPreferencias;
import com.sergio.pruebas.servicio.ServiceWifiManager;

public class Inicio extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button btnAdd, btnGest, btnConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        btnAdd=(Button)findViewById(R.id.btnadd);
        btnGest=(Button)findViewById(R.id.btngest);
        btnConfig=(Button)findViewById(R.id.btnconf);

        btnAdd.setOnClickListener(this);
        btnGest.setOnClickListener(this);
        btnConfig.setOnClickListener(this);

        ToggleButton stateServiceTbtn = (ToggleButton) findViewById(R.id.tbserv);
        if (stateServiceTbtn != null) {
            stateServiceTbtn.setChecked(GestionPreferencias.getConfigServActivo(GestionPreferencias.getSharedPreferencesConfig(this)));
            stateServiceTbtn.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnAdd.getId()){
            Intent i = new Intent(this,NuevaConexion.class);
            startActivity(i);
        }else if(v.getId()==btnGest.getId()) {
            Intent i = new Intent(this, VerConexiones.class);
            startActivity(i);
        }else if(v.getId()==btnConfig.getId()){
            Intent i = new Intent(this, DialogoConfiguracion.class);
            startActivity(i);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            GestionPreferencias.setConfigServActivo(GestionPreferencias.getSharedPreferencesConfig(this),true);
            startService(new Intent(this,ServiceWifiManager.class));
        } else {
            GestionPreferencias.setConfigServActivo(GestionPreferencias.getSharedPreferencesConfig(this),false);
            stopService(new Intent(this,ServiceWifiManager.class));
        }

    }
}
