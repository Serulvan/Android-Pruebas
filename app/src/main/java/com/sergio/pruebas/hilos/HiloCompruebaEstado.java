package com.sergio.pruebas.hilos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
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
    private int i = 0;

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

    private void changeConection(Conexion c, WifiManager wm){
        //get conection 0 or create new;
        List<WifiConfiguration> lcn=wm.getConfiguredNetworks();
        WifiConfiguration wc;
        if (lcn.size()>0){
            wc = lcn.get(0);
        } else{
            wc = new WifiConfiguration();
        }
        //set SSID;
        wc.SSID="\""+c.getSsid()+"\"";
        //set pass;
        if ("wep".equals("wep")) {
            wc.wepKeys[0] = "\"" + c.getPass() + "\"";
            wc.wepTxKeyIndex = 0;
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        }else if("wpa".equals("wpa")){
            wc.preSharedKey = "\""+ c.getPass() +"\"";
        }else{
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        //remove all connections  and add the new connection
        for( WifiConfiguration i : lcn ) {
            wm.removeNetwork(i.networkId);
            wm.saveConfiguration();
        }
        int netId = wm.addNetwork(wc);
        //Set to 1 for true and 0 for false.
        android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, String.valueOf(c.getIDHCP()));
        if (!c.getDHCP()){
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, c.getPuerta().getHostAddress());
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, c.getMasc().getHostAddress());
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, c.getIp().getHostAddress());
        }
        wm.disconnect();
        wm.enableNetwork(netId, true);
        wm.reconnect();
        notificación(c);
    }

    private void notificación(Conexion c){
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.mipmap.ic_launcher);
        ncb.setContentTitle("notificacion_titulo");
        ncb.setContentText("notificacion_cuerpo" + c.getSsid());
        ncb.setAutoCancel(true);
        long[] pat = {0,200,200};
        ncb.setVibrate(pat);
        //Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.blast_small);
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ncb.setSound(sound);
        ncb.setNumber(1);
        //Intent intent = new Intent(context, Inicio.class);
        PendingIntent rpi = PendingIntent.getActivity(context, 0, /*intent*/new Intent(), 0);
        ncb.setContentIntent(rpi);

        int mNotificationId = i++;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, ncb.build());
    }
}
