package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.conexiones.Conexion;
import com.sergio.pruebas.hilos.GestorHilos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

public class DialogoNuevaConexion extends Activity implements View.OnClickListener{

    private EditText ssid, pass;
    private Button btnCancel, btnContin;
    private CheckBox cbAvanzado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_nueva_conexion);
        ssid=(EditText)findViewById(R.id.dnc_ssid);
        pass=(EditText)findViewById(R.id.dnc_pass);
        ssid.setText(getIntent().getStringExtra("ssid"));
        if (getIntent().getIntExtra("pass",10)==0){
            pass.setVisibility(View.GONE);
        }
        btnContin = (Button)findViewById(R.id.dnc_btncontinuar);
        btnCancel = (Button)findViewById(R.id.dnc_btncancelar);

        btnContin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        cbAvanzado=(CheckBox)findViewById(R.id.dnc_cb_avanzado);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==btnCancel.getId()){
            GestorHilos.reanudarHiloEscaneoWifi();
            finish();
        }else{
            if(ssid.getText().toString().trim().length()==0){
                Toast.makeText(this,R.string.error_ssid_vacio,Toast.LENGTH_SHORT).show();
            }else if (cbAvanzado.isChecked()) {
                Intent i = new Intent(this, DialogoNuevaConexionConfig.class);
                i.putExtra("ssid", ssid.getText().toString().trim());
                i.putExtra("pass", pass.getText().toString().trim());
                startActivity(i);
            }else{
                try {
                    Conexion c = new Conexion(ssid.getText().toString().trim(),pass.getText().toString().trim());
                    SharedPreferences $$listado = getSharedPreferences("$$listado", MODE_PRIVATE);
                    String listado = $$listado.getString("$jList", "");
                    ArrayList<Conexion> listadoConexiones = new ArrayList();
                    JSONArray jArray;
                    JSONObject jo;
                    if (!listado.isEmpty()) {
                        jo = new JSONObject(listado);
                        jArray = jo.getJSONArray("conexionList");
                        for (int i = 0; i < jArray.length(); i++) {
                            listadoConexiones.add((Conexion)jArray.get(i));
                        }
                    }
                    listadoConexiones.add(c);
                    Collections.sort(listadoConexiones, new Conexion());
                    jArray = new JSONArray(listadoConexiones);
                    SharedPreferences.Editor ed = $$listado.edit();
                    jo = new JSONObject();
                    jo.put("conexionList",jArray);
                    Toast.makeText(this,jo.toString(),Toast.LENGTH_LONG).show();
                    ed.putString("$jList",jo.toString());
                    ed.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }
}
