package com.sergio.pruebas.servicio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.sergio.pruebas.R;
import com.sergio.pruebas.hilos.HiloCompruebaEstado;

public class ServiceWifiManager extends Service {
    private boolean started = false;
    private HiloCompruebaEstado hce;

    public ServiceWifiManager() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!started) {
            Toast.makeText(this, R.string.servicio_start, Toast.LENGTH_SHORT).show();
            started=true;
            if (hce==null) {
                hce = new HiloCompruebaEstado(this);
                hce.execute();
            }else {
                hce.activeAgain();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hce.letFinish();
        hce=null;
        Toast.makeText(this, R.string.servicio_stop, Toast.LENGTH_SHORT).show();
        started = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
