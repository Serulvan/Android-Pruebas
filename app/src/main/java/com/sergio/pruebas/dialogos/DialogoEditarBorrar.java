package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.test;

public class DialogoEditarBorrar extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_editar_borrar);
        Button btnEdit = (Button)findViewById(R.id.deb_btn_edit);
        Button btnCanc = (Button)findViewById(R.id.deb_btn_canc);

        btnEdit.setOnClickListener(this);
        btnCanc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.deb_btn_edit){
            Intent i = new Intent(this, test.class);
            startActivity(i);
        }else{
            //borrar la red de la lista
            //finish();
            Toast.makeText(this, getIntent().getIntExtra("id", -1), Toast.LENGTH_SHORT).show();
        }
    }
}
