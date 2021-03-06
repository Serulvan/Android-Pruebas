package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.Intent;
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

public class DialogoListaMacs extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv;
    private int id;
    private String lista;
    private Conexion c=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_lista_macs);
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
        try {
            c = GestionArchivos.obtenerConexionPorId(id,GestionArchivos.getSharedPreferencesListado(this));
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
        if (c!=null) {
            switch (lista) {
                case Conexion.WHITE:
                    if (c.getWhiteList().size() == 0) {
                        Toast.makeText(this, R.string.error_sin_datos, Toast.LENGTH_SHORT).show();
                    }
                    lv.setAdapter(new AdaptadorMostrarMac(this, R.layout.layout_bar_mac, c.getWhiteList()));
                    break;
                case Conexion.BlACK:
                    if (c.getBlackList().size() == 0) {
                        Toast.makeText(this, R.string.error_sin_datos, Toast.LENGTH_SHORT).show();
                    }
                    lv.setAdapter(new AdaptadorMostrarMac(this, R.layout.layout_bar_mac, c.getBlackList()));
                    break;
            }
        }
    }



    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, DialogoNuevaMac.class);
        i.putExtra("id",id);
        i.putExtra("lista",lista);
        startActivityForResult(i,0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DialogoEditarBorrarMac.class);
        i.putExtra("fecha",((TextView)view.findViewById(R.id.mb_fecha)).getText());
        i.putExtra("mac",((TextView)view.findViewById(R.id.mb_mac)).getText());
        i.putExtra("id",this.id);
        i.putExtra("lista",lista);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            id = data.getIntExtra("id", -1);
            lista = data.getStringExtra("lista");
            switch (requestCode) {
                case 0:
                    //nueva mac
                    switch (lista) {
                        case Conexion.WHITE:
                            c.addWhiteListMac(data.getStringExtra("mac"));
                            break;
                        case Conexion.BlACK:
                            c.addBlackListMac(data.getStringExtra("mac"));
                            break;
                    }
                    break;
                case 1:
                    //editar borrar mac
                    switch (data.getIntExtra("accion", -1)) {
                        case 0:
                            //borrar
                            c.borrarMacPorFecha(data.getStringExtra("fecha"), lista);
                            break;
                        case 1:
                            //modificar
                            c.modificarMacPorFecha(data.getStringExtra("fecha"), data.getStringExtra("mac"), lista);
                            break;
                    }
                    break;
            }
            //guardar cambios
            try {
                GestionArchivos.actualizarConexionPorId(c, GestionArchivos.getSharedPreferencesListado(this));
            } catch (JSONException | UnknownHostException e) {
                e.printStackTrace();
            }
            onStart();
        }
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
        Intent i = new Intent();
        i.putExtra("id",id);
        i.putExtra("lista",lista);
        setResult(RESULT_OK,i);
        finish();

    }
}
