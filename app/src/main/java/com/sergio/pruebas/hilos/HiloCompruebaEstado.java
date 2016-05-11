package com.sergio.pruebas.hilos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.sergio.pruebas.R;
import com.sergio.pruebas.conexiones.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;
import com.sergio.pruebas.odenadores.OrdenarWifiScanPorLevel;

import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HiloCompruebaEstado extends AsyncTask<Void,Void,Void> {
    private Context context;
    private Boolean activo = true;

    public HiloCompruebaEstado(Context context, Boolean activo) {
        this.context = context;
        this.activo = activo;
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (activo) {
            try {
                Thread.sleep(1000 * 60 * 5);
                if (activo) {
                    publishProgress();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if (checkLink(wm)){

        }
    }
    //metodo buscar la red wifi mas alta(por encima de la intensidad de la señal actual)

    public void letFinish(){
        activo=false;
    }

    private boolean checkLink(WifiManager wm){
        wm.startScan();
        if (wm.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            return true;
        }
        return false;
    }

    private int getPosReConection(WifiManager wm) throws JSONException, UnknownHostException {
        List<ScanResult> redes = wm.getScanResults();
        Collections.sort(redes, new OrdenarWifiScanPorLevel());
        SharedPreferences sp = GestionArchivos.getSharedPreferencesListado(context);
        ArrayList<Conexion> misRedes = GestionArchivos.obtenerLista(sp);
        int currentSignal = wm.getConnectionInfo().getRssi();
        int z = -1,i = 0,j;
        boolean stop=false;
        while (i<redes.size()&&redes.get(i).level>currentSignal){
            j=0;
            while(j<misRedes.size()){
                if (!redes.get(i).SSID.equals(misRedes.get(j).getSsid())) {
                    j++;
                }else{
                    z=j;
                    stop=true;
                    break;
                }
            }
            if (stop){
                break;
            }
        }
        return z;
    }

    private void changeConection(Conexion c){
        //get conection 0 or create new;
        //set SSID;
        //set pass;

        //Set to 1 for true and 0 for false.
        android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, String.valueOf(c.getIDHCP()));
        if (!c.getDHCP()){
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, c.getPuerta().getHostAddress());
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, c.getMasc().getHostAddress());
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, c.getIp().getHostAddress());
        }

    }

    private void notificación(){
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.mipmap.ic_launcher);
        ncb.setContentTitle("conectado a SSID");
        ncb.setContentText("esto es una prueba");
        ncb.setAutoCancel(true);
        long[] pat = {0,200,200};
        ncb.setVibrate(pat);
        //Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.blast_small);
        //ncb.setSound(sound);
        ncb.setNumber(1);
        //Intent intent = new Intent(context, Inicio.class);
        PendingIntent rpi = PendingIntent.getActivity(context, 0, /*intent*/new Intent(), 0);
        ncb.setContentIntent(rpi);

        int mNotificationId = 0;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, ncb.build());
    }
}
