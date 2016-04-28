package com.sergio.pruebas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Inicio extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd, btnGest, del, ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        btnAdd=(Button)findViewById(R.id.btnadd);
        btnGest=(Button)findViewById(R.id.btngest);
        del=(Button)findViewById(R.id.del);
        ver=(Button)findViewById(R.id.ver);

        btnAdd.setOnClickListener(this);
        btnGest.setOnClickListener(this);
        del.setOnClickListener(this);
        ver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnAdd.getId()){
            Intent i = new Intent(this,NuevaConexion.class);
            startActivity(i);
        }else if(v.getId()==btnGest.getId()){
            Intent i = new Intent(this,VerConexiones.class);
            startActivity(i);
        }else if(v.getId()==del.getId()){
            getSharedPreferences("$$listado",MODE_PRIVATE).edit().clear().commit();
            Toast.makeText(this, getSharedPreferences("$$listado", MODE_PRIVATE).getString("$jList", ""), Toast.LENGTH_SHORT).show();
        }else if(v.getId()==ver.getId()){
            Toast.makeText(this, getSharedPreferences("$$listado", MODE_PRIVATE).getString("$jList", ""), Toast.LENGTH_SHORT).show();
        }
    }
}
