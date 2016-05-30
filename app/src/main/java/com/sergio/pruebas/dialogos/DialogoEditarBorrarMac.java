package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.sergio.pruebas.R;

public class DialogoEditarBorrarMac extends Activity implements View.OnClickListener {

    private Button btnDel, btnEdit;
    private int id;
    private String lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_editar_borrar_mac);

        btnDel=(Button)findViewById(R.id.debm_btn_borrar);
        if (btnDel != null) {
            btnDel.setOnClickListener(this);
        }

        btnEdit=(Button)findViewById(R.id.debm_btn_editar);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(this);
        }
        id=getIntent().getIntExtra("id",-1);
        lista= getIntent().getStringExtra("lista");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnEdit.getId()){
            Intent i = new Intent(this,DialogoNuevaMac.class);
            i.putExtra("mac",getIntent().getStringExtra("mac"));
            i.putExtra("fecha",getIntent().getStringExtra("fecha"));
            i.putExtra("id",id);
            i.putExtra("lista",lista);
            startActivityForResult(i,0);
        }else{
            Intent i = new Intent();
            i.putExtra("id",getIntent().getIntExtra("id",-1));
            i.putExtra("fecha",getIntent().getStringExtra("fecha"));
            i.putExtra("accion",0);
            i.putExtra("id",id);
            i.putExtra("lista",lista);
            cerrarDEBM(RESULT_OK,i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            Intent i = new Intent();
            i.putExtra("mac", data.getStringExtra("mac"));
            i.putExtra("fecha", data.getStringExtra("fecha"));
            i.putExtra("accion", 1);
            id = data.getIntExtra("id", -1);
            lista = data.getStringExtra("lista");
            i.putExtra("id", id);
            i.putExtra("lista", lista);
            cerrarDEBM(RESULT_OK, i);
        }
    }

    public void cerrarDEBM(int result, Intent i){
        setResult(result, i);
        finish();
    }
}
