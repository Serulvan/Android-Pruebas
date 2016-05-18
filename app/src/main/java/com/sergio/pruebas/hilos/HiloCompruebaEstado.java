package com.sergio.pruebas.hilos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.sergio.pruebas.R;
import com.sergio.pruebas.conexiones.Conexion;
import com.sergio.pruebas.memoria.GestionArchivos;
import com.sergio.pruebas.memoria.GestionPreferencias;
import com.sergio.pruebas.odenadores.OrdenarWifiScanPorLevel;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
                if (activo) {
                    publishProgress();
                }
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        try {
            WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            if (checkLink(wm)) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (hasActiveNetConextion(cm)) {
                    //search int currentSignal = wm.getConnectionInfo().getRssi();
                    String[] sArr = search(wm, -1, wm.getConnectionInfo().getRssi());
                    int pos = Integer.valueOf(sArr[1]);
                    while (pos!= -1) {//busqueda de conexiones en rango validas
                        //change
                        Conexion c = GestionArchivos.obtenerLista(GestionArchivos.getSharedPreferencesListado(context)).get(pos);
                        changeConection(c,wm);
                        if (!hasActiveInternetConnection()) {
                            //no hay internet
                            if (!GestionArchivos.isOnWhiteList(sArr,c)){
                                c.addBlackListMac(sArr[0]);
                            }
                            sArr = search(wm, pos, wm.getConnectionInfo().getRssi());
                            pos = Integer.valueOf(sArr[1]);
                        }else{
                            //si hay internet
                            break;
                        }
                    }
                } else {
                    String sArr[] = search(wm, -1, -200);
                    int pos = Integer.valueOf(sArr[1]);
                    if (pos!=-1){
                        //change
                        changeConection(GestionArchivos.obtenerLista(GestionArchivos.getSharedPreferencesListado(context)).get(pos),wm);
                    }
                }

            }
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    //metodo buscar la red wifi mas alta(por encima de la intensidad de la señal actual)

    public void letFinish(){
        activo=false;
    }

    private boolean checkLink(WifiManager wm){
        if (wm.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            wm.startScan();
            return true;
        }
        return false;
    }

    private String[] search(WifiManager wm, int startAt, int currentSignal) throws JSONException, UnknownHostException {
        List<ScanResult> redes = wm.getScanResults();
        Collections.sort(redes, new OrdenarWifiScanPorLevel());
        SharedPreferences sp = GestionArchivos.getSharedPreferencesListado(context);
        ArrayList<Conexion> misRedes = GestionArchivos.obtenerLista(sp);
        String z[] = {"","-1"};
        for (int i = startAt+1; i < redes.size(); i++) {
            if (redes.get(i).level<currentSignal){
                break;
            }
            for (int j = 0; j < misRedes.size(); j++) {
                if (redes.get(i).SSID.equals(misRedes.get(j).getSsid())){
                    z[0]=redes.get(i).BSSID;
                    z[1]=String.valueOf(j);
                    return z;
                }
            }
        }
        return z;
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
        if (c.getCifrado().toLowerCase().contains("wep")) {
            wc.wepKeys[0] = "\"" + c.getPass() + "\"";
            wc.wepTxKeyIndex = 0;
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        }else if(c.getCifrado().toLowerCase().contains("wpa")){
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
        GestionPreferencias.setActualConexionId(GestionPreferencias.getSharedPreferencesConfig(context),c.getId());
        wm.disconnect();
        wm.enableNetwork(netId, true);
        wm.reconnect();
        notificación(c);
    }

    private void notificación(Conexion c){
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.mipmap.ic_launcher);
        ncb.setContentTitle(context.getString(R.string.notificacion_titulo));
        ncb.setContentText(context.getString(R.string.notificacion_cuerpo) + c.getSsid());
        ncb.setAutoCancel(true);
        if (GestionPreferencias.getConfigNotifVibracion(GestionPreferencias.getSharedPreferencesConfig(context))) {
            long[] pat = {0, 500, 200};
            ncb.setVibrate(pat);
        }
        if (GestionPreferencias.getConfigNotifSonido(GestionPreferencias.getSharedPreferencesConfig(context))) {
            //Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.blast_small);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ncb.setSound(sound);
        }
        //ncb.setNumber(1);
        //Intent intent = new Intent(context, Inicio.class);
        PendingIntent rpi = PendingIntent.getActivity(context, 0, /*intent*/new Intent(), 0);
        ncb.setContentIntent(rpi);

        int mNotificationId =  c.getId();
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, ncb.build());
    }

    private boolean hasActiveNetConextion(ConnectivityManager cm){
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean hasActiveInternetConnection() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {}
        return false;
    }
}
