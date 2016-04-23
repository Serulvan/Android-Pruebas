package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.sergio.pruebas.R;

public class DialogoNuevaConexion extends Activity implements View.OnClickListener{

    private EditText ssid, pass;
    private Button btnCancel, btnContin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_nueva_conexion);
        ssid=(EditText)findViewById(R.id.dnc_ssid);
        pass=(EditText)findViewById(R.id.dnc_pass);
        ssid.setText(getIntent().getStringExtra("ssid"));
        btnContin = (Button)findViewById(R.id.dnc_btncontinuar);
        btnCancel = (Button)findViewById(R.id.dnc_btncancelar);

        btnContin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==btnCancel.getId()){
            finish();
        }else{

        }
    }
}
