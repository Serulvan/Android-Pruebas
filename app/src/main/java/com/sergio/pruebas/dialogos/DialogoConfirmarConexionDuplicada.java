package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sergio.pruebas.R;

public class DialogoConfirmarConexionDuplicada extends Activity implements View.OnClickListener {

    private TextView mensaje;
    private Button acep, canc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_confirmar_conexion_duplicada);

        acep = (Button)findViewById(R.id.dccd_btn_aceptar);
        canc = (Button)findViewById(R.id.dccd_btn_cancel);
        mensaje = (TextView)findViewById(R.id.dccd_tv);
        String text = getIntent().getStringExtra("ssid")+ " " +getResources().getString(R.string.alerta_red_existente);
        mensaje.setText(text);
        acep.setOnClickListener(this);
        canc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        if (v.getId()==acep.getId()){
            setResult(Activity.RESULT_OK, i);
        }else if(v.getId()==canc.getId()){
            setResult(Activity.RESULT_CANCELED, i);
        }
        finish();
    }
}
