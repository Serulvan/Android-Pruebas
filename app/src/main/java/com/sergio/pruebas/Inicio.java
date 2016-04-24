package com.sergio.pruebas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd, btnGest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        btnAdd=(Button)findViewById(R.id.btnadd);
        btnGest=(Button)findViewById(R.id.btngest);

        btnAdd.setOnClickListener(this);
        btnGest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnAdd.getId()){
            Intent i = new Intent(this,NuevaConexion.class);
            startActivity(i);
        }else if(v.getId()==btnGest.getId()){
            //Intent i = new Intent(this,);
            //startActivity(i);
        }
    }
}
