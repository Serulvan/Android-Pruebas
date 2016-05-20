package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.sergio.pruebas.EditarConexion;
import com.sergio.pruebas.R;
import com.sergio.pruebas.memoria.GestionArchivos;
import com.sergio.pruebas.test;

import org.json.JSONException;

import java.net.UnknownHostException;

public class DialogoEditarBorrar extends Activity implements View.OnClickListener {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_editar_borrar);
        Button btnEdit = (Button)findViewById(R.id.deb_btn_edit);
        Button btnCanc = (Button)findViewById(R.id.deb_btn_canc);

        btnEdit.setOnClickListener(this);
        btnCanc.setOnClickListener(this);

        id = getIntent().getIntExtra("id", -1);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.deb_btn_edit){
            Intent i = new Intent(this, EditarConexion.class);
            i.putExtra("id",id);
            startActivity(i);
            finish();
        }else{
            try {
                GestionArchivos.borrarRedPorId(id,GestionArchivos.getSharedPreferencesListado(this));
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }
            //METER METODO DE CONFIRMACION REPITIENDO EL SSID POR EJEMPLO
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }
}
