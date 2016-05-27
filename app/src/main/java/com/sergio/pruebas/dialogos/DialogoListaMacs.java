package com.sergio.pruebas.dialogos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.adaptadores.AdaptadorMostrarMac;
import com.sergio.pruebas.entidades.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;

import org.json.JSONException;

import java.net.UnknownHostException;

public class DialogoListaMacs extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv;
    private int id;
    private String lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_lista_macs);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btnNew = (Button) findViewById(R.id.dlm_btn_nueva);
        if (btnNew != null) {
            btnNew.setOnClickListener(this);
        }

        lv = (ListView)findViewById(R.id.dlm_lv);
        if (lv != null) {
            lv.setOnItemClickListener(this);
        }

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
        if (c!=null) {
            switch (lista) {
                case "w":
                    if (c.getWhiteList().size() != 0) {
                        lv.setAdapter(new AdaptadorMostrarMac(this, R.layout.mac_bar, c.getWhiteList()));
                    } else {
                        Toast.makeText(this, R.string.error_sin_datos, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "b":
                    if (c.getBlackList().size() != 0) {
                        lv.setAdapter(new AdaptadorMostrarMac(this, R.layout.mac_bar, c.getBlackList()));
                    } else {
                        Toast.makeText(this, R.string.error_sin_datos, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }



    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, DialogoNuevaMac.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DialogoEditarBorrar.class);
        i.putExtra("fecha",((TextView)view.findViewById(R.id.mb_fecha)).getText());
        i.putExtra("mac",((TextView)view.findViewById(R.id.mb_mac)).getText());
        startActivityForResult(i,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onStart();
    }
}
