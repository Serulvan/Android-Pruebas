package com.sergio.pruebas.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sergio.pruebas.memoria.GestionPreferencias;
import com.sergio.pruebas.servicio.ServiceWifiManager;

public class WifiManagerReceiver extends BroadcastReceiver {
    public WifiManagerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (GestionPreferencias.getConfigServActivo(GestionPreferencias.getSharedPreferencesConfig(context))){
            context.startService(new Intent(context,ServiceWifiManager.class));
        }
    }
}
