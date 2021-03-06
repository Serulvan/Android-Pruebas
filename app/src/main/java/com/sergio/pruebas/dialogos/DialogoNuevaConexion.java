package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.entidades.Conexion;
import com.sergio.pruebas.hilos.GestorHilos;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.UnknownHostException;

public class DialogoNuevaConexion extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText ssid, pass;
    private Button btnCancel, btnContin;
    private CheckBox cbAvanzado;
    private Spinner spn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_nueva_conexion);
        ssid=(EditText)findViewById(R.id.dnc_ssid);
        pass=(EditText)findViewById(R.id.dnc_pass);
        ssid.setText(getIntent().getStringExtra("ssid"));
        LinearLayout spll = (LinearLayout) findViewById(R.id.dnc_ll_spl);
        spn = (Spinner)findViewById(R.id.dnc_spn_seguridad);
        spn.setOnItemSelectedListener(this);
        String typePass = getIntent().getStringExtra("pass");
        if (typePass.length()==0){
            pass.setVisibility(View.GONE);
            spll.setVisibility(View.GONE);
        }else if(typePass.equals("$")){
                spn.setSelection(0);
                pass.setVisibility(View.GONE);
        } else if(typePass.toUpperCase().contains("WEP")){
            spn.setSelection(1);
        } else if(typePass.toUpperCase().contains("WPA")){
            spn.setSelection(2);
        } else {
            spn.setSelection(0);
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
            cerrarDNC();
        }else{
            if(ssid.getText().toString().trim().length()==0){
                Toast.makeText(this,R.string.error_ssid_vacio,Toast.LENGTH_SHORT).show();
            }else if (cbAvanzado.isChecked()) {
                Intent i = new Intent(this, DialogoNuevaConexionConfig.class);
                i.putExtra("ssid", ssid.getText().toString().trim());
                i.putExtra("pass", pass.getText().toString().trim());
                i.putExtra("segur",getResources().getStringArray(R.array.passType)[spn.getSelectedItemPosition()]);
                startActivityForResult(i, 0);
            }else{
                String ssID = ssid.getText().toString().trim();
                try {
                    if(pass.getText().toString().trim().length()>0||
                            getResources().getStringArray(R.array.passType)[spn.getSelectedItemPosition()].toLowerCase().contains("abier")) {
                        if (GestionArchivos.buscarRed(ssID, GestionArchivos.getSharedPreferencesListado(this))) {
                            Intent i = new Intent(this, DialogoConfirmarConexionDuplicada.class);
                            i.putExtra("ssid", ssID);
                            startActivityForResult(i, 1);
                        } else {
                            add();
                            cerrarDNC();
                        }
                    } else{
                        Toast.makeText(this,R.string.error_pass_vacia,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            //dialogoNuevaConexionCongfig
            if(resultCode == RESULT_OK){
                cerrarDNC();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }else if (requestCode == 1) {
            //dialogoConfirmarConexionDuplicada
            if(resultCode == RESULT_OK){
                add();
                cerrarDNC();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }

    }

    private void add(){
        try {
            Conexion c = new Conexion(ssid.getText().toString().trim(),
                    pass.getText().toString().trim(),
                    getResources().getStringArray(R.array.passType)[spn.getSelectedItemPosition()]);
            GestionArchivos.añadirRed(c, GestionArchivos.getSharedPreferencesListado(this));
            Toast.makeText(this,R.string.exito_red_añadida,Toast.LENGTH_SHORT).show();
        }
        catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                pass.setVisibility(View.GONE);
                break;
            case 1:
                pass.setVisibility(View.VISIBLE);
                break;
            case 2:
                pass.setVisibility(View.VISIBLE);
                break;
            case 3:
                pass.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cerrarDNC(){
        GestorHilos.reanudarHiloEscaneoWifi();
        finish();
    }
}
