package com.sergio.pruebas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sergio.pruebas.adaptadores.AdaptadorMostrarConexiones;
import com.sergio.pruebas.entidades.Conexion;
import com.sergio.pruebas.dialogos.DialogoEditarBorrar;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class VerConexiones extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv;
    private Button btnAtras;
    private ArrayList<Conexion> listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_conexiones);
        btnAtras = (Button)findViewById(R.id.vc_btn_atras);
        if (btnAtras != null) {
            btnAtras.setOnClickListener(this);
        }

        lv = (ListView) findViewById(R.id.vc_lv);
        if (lv != null) {
            lv.setOnItemClickListener(this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            listado=GestionArchivos.obtenerLista(GestionArchivos.getSharedPreferencesListado(this));
            if(listado.isEmpty()) {
                Toast.makeText(this,R.string.error_sin_datos,Toast.LENGTH_LONG).show();
            }
            lv.setAdapter(new AdaptadorMostrarConexiones(this, R.layout.layout_bar_wifi, listado));
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this,DialogoEditarBorrar.class);
        i.putExtra("id", (int)view.findViewById(R.id.ssid_list).getTag());
        startActivityForResult(i,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                onStart();
                break;
        }
    }
}
