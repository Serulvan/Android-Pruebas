package com.sergio.pruebas.dialogos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.sergio.pruebas.R;

public class DialogoEditarBorrarMac extends AppCompatActivity implements View.OnClickListener {

    Button btnDel, btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_editar_borrar_mac);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        btnDel=(Button)findViewById(R.id.debm_btn_borrar);
        if (btnDel != null) {
            btnDel.setOnClickListener(this);
        }

        btnEdit=(Button)findViewById(R.id.debm_btn_editar);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnEdit.getId()){
            Intent i = new Intent(this,DialogoNuevaMac.class);
            i.putExtra("mac",getIntent().getStringExtra("mac"));
            i.putExtra("fecha",getIntent().getStringExtra("fecha"));
            startActivityForResult(i,0);
        }else{

        }
    }
}
