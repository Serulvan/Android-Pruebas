package com.sergio.pruebas.servicio;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sergio.pruebas.hilos.HiloCompruebaEstado;

public class Servicio extends Service {

    private HiloCompruebaEstado hce;

    @Override
    public void onCreate() {
        super.onCreate();
        hce = new HiloCompruebaEstado(getBaseContext(),true);
        hce.execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
