package com.sergio.pruebas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sergio.pruebas.adaptadores.AdaptadorMostrarConexiones;
import com.sergio.pruebas.adaptadores.AdaptadorNuevaConexion;
import com.sergio.pruebas.conexiones.Conexion;
import com.sergio.pruebas.dialogos.DialogoEditarBorrar;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class VerConexiones extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv;
    private Button btnAtras;
    private String listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_conexiones);
        btnAtras = (Button)findViewById(R.id.vc_btn_atras);
        btnAtras.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.vc_lv);
        lv.setOnItemClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            SharedPreferences sp = getSharedPreferences("$$listado", MODE_PRIVATE);
            listado = sp.getString("$jList", "");
            if(!listado.isEmpty()) {
                lv.setAdapter(new AdaptadorMostrarConexiones(this, R.layout.wifi_bar, GestionArchivos.obtenerLista(sp,this)));
            }else {
                Toast.makeText(this,R.string.error_sin_datos,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
        //Toast.makeText(this,listado,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this,DialogoEditarBorrar.class));
    }
}
