package com.sergio.pruebas.dialogos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.sergio.pruebas.R;

public class DialogoNuevaMac extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_nueva_mac);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
