package com.sergio.pruebas.hilos;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.widget.ListView;

import com.sergio.pruebas.R;
import com.sergio.pruebas.adaptadores.AdaptadorNuevaConexion;


public class HiloEscaneoWifi extends AsyncTask<Void,Void,Void> {

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
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        if (wm.isWifiEnabled()) {
            wm.startScan();
        }
        lv.setAdapter(new AdaptadorNuevaConexion(activity, R.layout.wifi_bar, wm.getScanResults()));
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (activo) {
            if (!pausa) {
                publishProgress();
            }
            try {
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
