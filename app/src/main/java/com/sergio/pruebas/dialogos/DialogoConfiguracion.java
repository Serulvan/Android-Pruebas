package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.memoria.GestionArchivos;
import com.sergio.pruebas.memoria.GestionPreferencias;

public class DialogoConfiguracion extends Activity implements CompoundButton.OnCheckedChangeListener {

    CheckBox cbVib, cbSon;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_configuracion);
        cbVib = (CheckBox) findViewById(R.id.dc_cb_vib);
        cbSon = (CheckBox) findViewById(R.id.dc_cb_son);

        sp = GestionPreferencias.getSharedPreferencesConfig(this);
        cbVib.setChecked(GestionPreferencias.getConfigNotifVibracion(sp));
        cbSon.setChecked(GestionPreferencias.getConfigNotifSonido(sp));

        cbVib.setOnCheckedChangeListener(this);
        cbSon.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId()==cbVib.getId()){
            GestionPreferencias.setConfigNotifVibracion(sp,isChecked);
        }else if (buttonView.getId()==cbSon.getId()){
            GestionPreferencias.setConfigNotifSonido(sp,isChecked);
        }
    }
}
