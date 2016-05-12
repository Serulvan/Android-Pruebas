package com.sergio.pruebas.hilos;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.widget.ListView;

import com.sergio.pruebas.hilos.HiloEscaneoWifi;

public abstract class GestorHilos {
    private static HiloEscaneoWifi hew;

    public static void declararHiloEscaneoWifi(ListView lv,WifiManager wm, Activity activity){
        hew = new HiloEscaneoWifi(lv,wm,activity);
    }

    public static void iniciarHiloEscaneoWifi(){
        hew.execute();
    }

    public static void pararHiloEscaneoWifi() {
        if(!hew.isCancelled()){
            hew.parar();
        }
    }

    public static void pausarHiloEscaneoWifi() {
        if(!hew.isCancelled()){
            hew.pausar();
        }
    }

    public static void reanudarHiloEscaneoWifi() {
        if(!hew.isCancelled()){
            hew.reanudar();
        }
    }
}
