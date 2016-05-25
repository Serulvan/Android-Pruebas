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
    private String ssid, pass, segur;

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
        segur = getIntent().getStringExtra("segur");

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.dncc_puerta: case R.id.dncc_ip:
                    if (checkFields(v)) {
                        Toast.makeText(this, R.string.error_ip_ipnovalida, Toast.LENGTH_SHORT).show();
                        ((EditText) v).setText("");
                    }
                    break;
                case R.id.dncc_masc:
                    if (checkFields(v)) {
                        Toast.makeText(this, R.string.error_ip_mascnovalida, Toast.LENGTH_SHORT).show();
                        ((EditText) v).setText("");
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==cancelar.getId()){
            cerrarDNCC(RESULT_CANCELED);
        } else if (v.getId()==continuar.getId()){
            try {
                if (ip.getText().toString().trim().length()>0&&
                        mascara.getText().toString().trim().length()>0&&
                        puerta.getText().toString().trim().length()>0&&
                        !checkFields(ip)&&!checkFields(mascara)&&!checkFields(puerta)){
                    if (GestionArchivos.buscarRed(ssid, GestionArchivos.getSharedPreferencesListado(this))) {
                        Intent j = new Intent(this, DialogoConfirmarConexionDuplicada.class);
                        j.putExtra("ssid", ssid);
                        startActivityForResult(j, 0);
                    }else {
                        add();
                        cerrarDNCC(RESULT_OK);
                    }
                }else{
                    Toast.makeText(this,R.string.error_campos_vacios,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if(resultCode == RESULT_OK){
                add();
                cerrarDNCC(RESULT_OK);
            }
            if (resultCode == RESULT_CANCELED) {
                cerrarDNCC(RESULT_CANCELED);
            }
        }
    }

    private void add(){
        try {
            Conexion c = new Conexion(ssid,pass,segur,
                    ip.getText().toString(),
                    mascara.getText().toString(),
                    puerta.getText().toString());
            SharedPreferences sp = getSharedPreferences("$$listado", MODE_PRIVATE);
            GestionArchivos.añadirRed(c, sp);
            Toast.makeText(this,R.string.exito_red_añadida,Toast.LENGTH_SHORT).show();
        }
        catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void cerrarDNCC(int result){
        setResult(result, new Intent());
        finish();
    }

    public boolean checkFields(View v){
        String sArr[] = ((EditText) v).getText().toString().trim().split("\\.");
        switch (v.getId()) {
            case R.id.dncc_puerta: case R.id.dncc_ip:
                if (sArr.length == 4) {
                    for (String aSArr : sArr) {
                        int num = Integer.parseInt(aSArr);
                        if (num < 0 || num > 255) {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
                break;
            case R.id.dncc_masc:
                if (sArr.length == 4) {
                    int temp = 1;
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
                                return true;
                            }
                        }
                    }
                } else {
                    return true;
                }
                break;
        }
        return false;
    }
}