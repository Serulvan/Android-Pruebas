package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sergio.pruebas.R;

import java.util.regex.Pattern;

public class DialogoNuevaMac extends Activity implements View.OnClickListener {

    private EditText etMac;
    private String fecha;
    private int id;
    private String lista, mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_nueva_mac);
        mac = getIntent().getStringExtra("mac");
        fecha = getIntent().getStringExtra("fecha");

        etMac= (EditText) findViewById(R.id.dnm_et_mac);
        if (etMac != null) {
            etMac.setHint(mac);
        }

        Button btnOk = (Button) findViewById(R.id.dnm_btn_aceptar);
        if (btnOk != null) {
            btnOk.setOnClickListener(this);
        }
        id=getIntent().getIntExtra("id",-1);
        lista= getIntent().getStringExtra("lista");
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        i.putExtra("id",this.id);
        i.putExtra("lista",lista);
        if (comprobarMac(etMac.getText().toString().trim())){
            i.putExtra("mac",formatearMac(etMac.getText().toString().trim()));
            i.putExtra("fecha",fecha);
            cerrarDNM(RESULT_OK,i);
        }else if (etMac.getText().toString().trim().isEmpty() && etMac.getHint()!=null){
            i.putExtra("mac",etMac.getHint().toString());
            i.putExtra("fecha",fecha);
            cerrarDNM(RESULT_OK,i);
        }else{
            Toast.makeText(this, R.string.error_mac, Toast.LENGTH_SHORT).show();
        }
    }

    private void cerrarDNM(int result, Intent i) {
        setResult(result,i);
        finish();
    }

    private boolean comprobarMac(String mac){
        Pattern p = Pattern.compile("^(([0-9A-Fa-f]{2})(:|-)?)*([0-9A-Fa-f]{2})$");
        return p.matcher(mac).matches();
    }

    private String formatearMac(String mac){
        mac = mac.replace(":","");
        mac = mac.replace("-","");
        String macs[] = mac.split("(?=(?:..)*$)");
        mac="";
        for (int i = 1; i < macs.length; i++) {
            mac+=":"+macs[i];
        }
        mac=mac.replaceFirst("^:","");
        return mac;
    }
}
