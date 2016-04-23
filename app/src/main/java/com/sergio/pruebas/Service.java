package com.sergio.pruebas;

import android.content.Intent;
import android.os.IBinder;

import com.sergio.pruebas.hilos.HiloCompruebaEstado;

public class Service extends android.app.Service{

    private HiloCompruebaEstado hce;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
