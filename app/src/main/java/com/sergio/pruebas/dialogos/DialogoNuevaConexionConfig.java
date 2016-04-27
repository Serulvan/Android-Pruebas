package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.conexiones.Conexion;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DialogoNuevaConexionConfig extends Activity implements View.OnFocusChangeListener, View.OnClickListener{

    private EditText ip, mascara, puerta;
    private Button cancelar, continuar;
    private String ssid, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_nueva_conexion_config);

        ip= (EditText)findViewById(R.id.dncc_ip);
        mascara= (EditText)findViewById(R.id.dncc_masc);
        puerta= (EditText)findViewById(R.id.dncc_puerta);

        ip.setOnFocusChangeListener(this);
        mascara.setOnFocusChangeListener(this);
        puerta.setOnFocusChangeListener(this);

        cancelar = (Button)findViewById(R.id.dncc_btncancelar);
        continuar = (Button)findViewById(R.id.dncc_btncontinuar);

        cancelar.setOnClickListener(this);
        continuar.setOnClickListener(this);


        ssid = getIntent().getStringExtra("ssid");
        pass = getIntent().getStringExtra("pass");

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String sArr[];
            switch (v.getId()) {
                case R.id.dncc_puerta: case R.id.dncc_ip:
                    boolean mal = false;
                    sArr = ((EditText) v).getText().toString().trim().split("\\.");
                    if (sArr.length == 4) {
                        for (String aSArr : sArr) {
                            int num = Integer.parseInt(aSArr);
                            if (num < 0 || num > 255) {
                                mal = true;
                                break;
                            }
                        }
                    } else {
                        mal = true;
                    }
                    if (mal) {
                        Toast.makeText(this, R.string.error_ip_ipnovalida, Toast.LENGTH_LONG).show();
                        ((EditText) v).setText("");
                    }
                    break;
                case R.id.dncc_masc:
                    sArr = ((EditText) v).getText().toString().split("\\.");
                    if (sArr.length == 4) {
                        int temp = 1;
                        boolean continuar = true;
                        for (String aSArr : sArr) {
                            String segm = Integer.toBinaryString(Integer.parseInt(aSArr));
                            String tmp = "";
                            for (int k = 0; k < 8 - segm.length(); k++) {
                                tmp += "0";
                            }
                            segm = tmp + segm;
                            for (int j = 0; j < segm.length(); j++) {
                                int actNum = Integer.parseInt(String.valueOf(segm.charAt(j)));
                                if (actNum <= temp) {
                                    temp = actNum;
                                } else {
                                    continuar = false;
                                }
                            }
                            if (!continuar) {
                                Toast.makeText(this, R.string.error_ip_mascnovalida, Toast.LENGTH_LONG).show();
                                ((EditText) v).setText("");
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(this, R.string.error_ip_mascnovalida, Toast.LENGTH_LONG).show();
                        ((EditText) v).setText("");
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cancelar.getId()){
            finish();
        } else{
            try {
                Conexion c = new Conexion(ssid,pass,
                        InetAddress.getByName(ip.getText().toString()),
                        InetAddress.getByName(mascara.getText().toString()),
                        InetAddress.getByName(puerta.getText().toString()));
                SharedPreferences $$listado = getSharedPreferences("$$listado",MODE_PRIVATE);
                String listado = $$listado.getString("$jList", null);
                ArrayList<Conexion> listadoConexiones = new ArrayList();
                JSONArray jArray = new JSONArray(listado);
                for (int i=0;i<jArray.length();i++){
                    listadoConexiones.add((Conexion)jArray.get(i));
                }
                listadoConexiones.add(c);
                Collections.sort(listadoConexiones,new Conexion());
                jArray = new JSONArray(listadoConexiones);
                SharedPreferences.Editor ed = $$listado.edit();
                ed.commit();
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}