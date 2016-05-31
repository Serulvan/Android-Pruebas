package com.sergio.pruebas;

import android.content.Intent;
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

import com.sergio.pruebas.dialogos.DialogoListaMacs;
import com.sergio.pruebas.entidades.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.UnknownHostException;

public class EditarConexion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private Conexion c;
    private EditText ssid, pass, ip, masc, puerta;
    private LinearLayout passLayout, staticLayout;
    private Spinner spnCifrado;
    private CheckBox cbDhcp;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_conexion);

        ssid = (EditText) findViewById(R.id.ec_et_ssid);
        pass = (EditText) findViewById(R.id.ec_et_pass);
        ip = (EditText) findViewById(R.id.ec_et_ip);
        masc = (EditText) findViewById(R.id.ec_et_masc);
        puerta = (EditText) findViewById(R.id.ec_et_puerta);

        passLayout = (LinearLayout) findViewById(R.id.ec_layout_pass);
        staticLayout = (LinearLayout) findViewById(R.id.ec_layout_static);

        spnCifrado = (Spinner) findViewById(R.id.ec_spn);
        if (spnCifrado != null) {
            spnCifrado.setOnItemSelectedListener(this);
        }

        cbDhcp = (CheckBox) findViewById(R.id.ec_cb);
        if (cbDhcp != null) {
            cbDhcp.setOnCheckedChangeListener(this);
        }

        Button editWitheList = (Button) findViewById(R.id.ec_btn_ewl);
        Button editBlackList = (Button) findViewById(R.id.ec_btn_ebl);
        Button salir = (Button) findViewById(R.id.ec_btn_salir);
        Button guardar = (Button) findViewById(R.id.ec_btn_guardar);

        if (editWitheList != null) {
            editWitheList.setOnClickListener(this);
        }
        if (editBlackList != null) {
            editBlackList.setOnClickListener(this);
        }
        if (salir != null) {
            salir.setOnClickListener(this);
        }
        if (guardar != null) {
            guardar.setOnClickListener(this);
        }
        id = getIntent().getIntExtra("id",-1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (id==-1){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }else{
            try {
                c = GestionArchivos.obtenerConexionPorId(id,GestionArchivos.getSharedPreferencesListado(this));
                setDatos();
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ec_btn_guardar:
                if (comprobar()) {
                    guardar();
                    finish();
                }
                break;
            case R.id.ec_btn_salir:
                finish();
                break;
            case R.id.ec_btn_ewl: case R.id.ec_btn_ebl:
                Intent i = new Intent(this, DialogoListaMacs.class);
                i.putExtra("id",c.getId());
                if (v.getId()==R.id.ec_btn_ewl){
                    i.putExtra("lista",Conexion.WHITE);//white
                }else {
                    i.putExtra("lista", Conexion.BlACK);//black
                }
                startActivityForResult(i,0);
                break;
        }
    }

    private boolean comprobar(){
        String stringTemp = getResources().getStringArray(R.array.passType)[spnCifrado.getSelectedItemPosition()];
        if (!stringTemp.equals(Conexion.CIFRADO_ABIERTO)&&
                pass.getHint().toString().length()==0&&
                pass.getText().toString().trim().length()==0) {
            Toast.makeText(this, R.string.error_ec_nopass, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cbDhcp.isChecked()){
            if ((ip.getHint().toString().length()==0
                    && ip.getText().toString().trim().length()==0)
                    || (masc.getHint().toString().length()==0
                    && masc.getText().toString().trim().length()==0)
                    || (puerta.getHint().toString().length()==0
                    && puerta.getText().toString().trim().length()==0)){
                Toast.makeText(this, R.string.error_ec_static, Toast.LENGTH_SHORT).show();
                return false;
            }
            if ((checkFields(ip.getText().toString().trim(),"ip")
                    &&ip.getText().toString().trim().length()!=0)
                    || (checkFields(masc.getText().toString().trim(),"mascara")
                    &&masc.getText().toString().trim().length()!=0)
                    || (checkFields(puerta.getText().toString().trim(),"puerta")
                    &&puerta.getText().toString().trim().length()!=0)){
                Toast.makeText(this, R.string.error_ec_static, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean checkFields(String s, String campo){
        String sArr[] = s.trim().split("\\.");
        switch (campo) {
            case "ip": case "puerta":
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
            case "mascara":
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

    private void guardar() {
        String stringTemp = ssid.getText().toString().trim();
        if (stringTemp.length() != 0) {
            c.setSsid(stringTemp);
        }
        stringTemp = getResources().getStringArray(R.array.passType)[spnCifrado.getSelectedItemPosition()];
        c.setCifrado(stringTemp);
        if (!stringTemp.equals(Conexion.CIFRADO_ABIERTO)) {
            if (pass.getText().toString().trim().length()!=0)c.setPass(pass.getText().toString().trim());
        }else{
            c.setPass(null);
        }
        c.setDHCP(!cbDhcp.isChecked());
        if (cbDhcp.isChecked()){
            if (ip.getText().toString().trim().length()!=0)c.setIp(ip.getText().toString().trim());
            if (masc.getText().toString().trim().length()!=0)c.setMasc(masc.getText().toString().trim());
            if (puerta.getText().toString().trim().length()!=0)c.setPuerta(puerta.getText().toString().trim());


        }else {
            c.setIp(null);
            c.setMasc(null);
            c.setPuerta(null);
        }
        try {
            GestionArchivos.actualizarConexionPorId(c,GestionArchivos.getSharedPreferencesListado(this));
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
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
                passLayout.setVisibility(View.GONE);
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

    private void setDatos(){
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
            staticLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            id = data.getIntExtra("id", -1);
            onStart();
        }
    }
}
