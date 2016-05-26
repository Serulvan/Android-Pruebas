package com.sergio.pruebas.dialogos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sergio.pruebas.R;
import com.sergio.pruebas.adaptadores.AdaptadorMostrarMac;
import com.sergio.pruebas.entidades.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.UnknownHostException;

public class DialogoListaMacs extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnNueva;
    private ListView lv;
    private int id;
    private String lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_lista_macs);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        btnNueva = (Button)findViewById(R.id.dlm_btn_nueva);
        btnNueva.setOnClickListener(this);

        lv = (ListView)findViewById(R.id.dlm_lv);
        lv.setOnItemClickListener(this);

        id=getIntent().getIntExtra("id",-1);
        lista = getIntent().getStringExtra("lista");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Conexion c=null;
        try {
            c = GestionArchivos.obtenerConexionPorId(id,GestionArchivos.getSharedPreferencesListado(this));
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
        switch (lista){
            case "w":
                lv.setAdapter(new AdaptadorMostrarMac(this,R.layout.mac_bar,c.getWhiteList()));
                break;
            case "b":
                lv.setAdapter(new AdaptadorMostrarMac(this,R.layout.mac_bar,c.getBlackList()));
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
