package com.sergio.pruebas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sergio.pruebas.conexiones.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.UnknownHostException;

public class EditarConexion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    EditText ssid, pass, ip, masc, puerta;
    LinearLayout passLayout, staticLayout;
    Spinner spnCifrado;
    CheckBox cbDhcp;
    Button  editWitheList, editBlackList, salir, guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_conexion);
        int id = getIntent().getIntExtra("id",-1);

        ssid = (EditText) findViewById(R.id.ec_et_ssid);
        pass = (EditText) findViewById(R.id.ec_et_pass);
        ip = (EditText) findViewById(R.id.ec_et_ip);
        masc = (EditText) findViewById(R.id.ec_et_masc);
        puerta = (EditText) findViewById(R.id.ec_et_puerta);

        passLayout = (LinearLayout) findViewById(R.id.ec_layout_pass);
        staticLayout = (LinearLayout) findViewById(R.id.ec_layout_static);

        spnCifrado = (Spinner) findViewById(R.id.ec_spn);
        spnCifrado.setOnItemSelectedListener(this);

        cbDhcp = (CheckBox) findViewById(R.id.ec_cb);
        cbDhcp.setOnCheckedChangeListener(this);

        editWitheList = (Button) findViewById(R.id.ec_btn_ewl);
        editBlackList = (Button) findViewById(R.id.ec_btn_ebl);
        salir = (Button) findViewById(R.id.ec_btn_salir);
        guardar = (Button) findViewById(R.id.ec_btn_guardar);

        editWitheList.setOnClickListener(this);
        editBlackList.setOnClickListener(this);
        salir.setOnClickListener(this);
        guardar.setOnClickListener(this);
        if (id==-1){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }else{
            try {
                Conexion c = GestionArchivos.obtenerConexionPorId(id,GestionArchivos.getSharedPreferencesListado(this));
                setDatos(c);
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ec_btn_guardar:
                guardar();
                break;
            case R.id.ec_btn_salir:
                finish();
                break;
            case R.id.ec_btn_ewl:
                break;
            case R.id.ec_btn_ebl:
                break;
        }
    }

    private void guardar() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setPass(getResources().getStringArray(R.array.passType)[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setPass(String cifrado){
        switch (cifrado){
            case Conexion.CIFRADO_ABIERTO:
                spnCifrado.setSelection(0);
                passLayout.setVisibility(View.INVISIBLE);
                break;
            case Conexion.CIFRADO_WEP:
                spnCifrado.setSelection(1);
                passLayout.setVisibility(View.VISIBLE);
                break;
            case Conexion.CIFRADO_WPA:
                spnCifrado.setSelection(2);
                passLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setDatos(Conexion c){
        ssid.setHint(c.getSsid());
        setPass(c.getCifrado());
        if(!c.getCifrado().equals(Conexion.CIFRADO_ABIERTO)) {
            pass.setHint(c.getPass());
        }
        cbDhcp.setChecked(!c.getDHCP());
        if(!c.getDHCP()){
            ip.setHint(c.getIp());
            masc.setHint(c.getMasc());
            puerta.setHint(c.getPuerta());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            staticLayout.setVisibility(View.VISIBLE);
        }else{
            staticLayout.setVisibility(View.INVISIBLE);
        }
    }
}
