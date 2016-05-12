package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.conexiones.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
        } else if (v.getId()==continuar.getId()){
            try {
                if (GestionArchivos.buscarRed(ssid, GestionArchivos.getSharedPreferencesListado(this))){
                        Intent i = new Intent(this, DialogoConfirmarConexionDuplicada.class);
                        i.putExtra("ssid", ssid);
                        startActivityForResult(i, 1);
                }else{
                    add();
                }
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                add();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void add(){
        try {
            Conexion c = new Conexion(ssid,pass,
                    InetAddress.getByName(ip.getText().toString()),
                    InetAddress.getByName(mascara.getText().toString()),
                    InetAddress.getByName(puerta.getText().toString()));
            SharedPreferences sp = getSharedPreferences("$$listado", MODE_PRIVATE);
            GestionArchivos.añadirRed(c, sp);
            Toast.makeText(this,R.string.exito_red_añadida,Toast.LENGTH_LONG).show();
        }
        catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}