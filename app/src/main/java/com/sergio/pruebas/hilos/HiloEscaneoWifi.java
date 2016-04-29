package com.sergio.pruebas.hilos;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.widget.ListView;

import com.sergio.pruebas.R;
import com.sergio.pruebas.adaptadores.AdaptadorNuevaConexion;
import com.sergio.pruebas.odenadores.OrdenarWifiScanPorLevel;

import java.util.Collections;
import java.util.List;


public class HiloEscaneoWifi extends AsyncTask<Void,Integer,Void> {

    private ListView lv;
    private WifiManager wm;
    private Activity activity;
    private boolean activo, pausa;

    public HiloEscaneoWifi(ListView lv, WifiManager wm, Activity activity) {
        this.lv = lv;
        this.wm = wm;
        this.activity= activity;
        activo = true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        switch (values[0]) {
            case 0:
            if (wm.isWifiEnabled()) {
                wm.startScan();
            }
                break;
            case 1:
                List scanResults = wm.getScanResults();
                Collections.sort(scanResults, new OrdenarWifiScanPorLevel());
                lv.setAdapter(new AdaptadorNuevaConexion(activity, R.layout.wifi_bar, scanResults));
                break;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (activo) {
            try {
                if (!pausa) {
                    publishProgress(0);
                    Thread.sleep(1000);
                    publishProgress(1);
                }
                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void parar() {
        this.activo = false;
    }

    public void pausar() {
        this.pausa=true;
    }

    public  void reanudar(){
        this.pausa=false;
    }
}
