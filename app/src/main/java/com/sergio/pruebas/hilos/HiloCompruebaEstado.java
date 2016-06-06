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
import com.sergio.pruebas.entidades.Conexion;
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
    private ArrayList<Conexion> misRedes;
    private SharedPreferences sp;
    private boolean activo;
    private int time;

    public HiloCompruebaEstado(Context context) {
        this.context = context;
        activo=true;
        time = 1000 * 60 * 5;
        sp = GestionArchivos.getSharedPreferencesListado(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (activo) {
            try {
                publishProgress();
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            misRedes = GestionArchivos.obtenerLista(sp);
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> wmc = wm.getScanResults();
            Collections.sort(wmc, new OrdenarWifiScanPorLevel());
            int posArr[] = new int[] {-1,-1};
            if (checkLink(wm)) {//wifi on?
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (hasActiveNetConextion(cm)) {//hay conexion?
                    //si hay conexion
                    if (!hasActiveInternetConnection()) {//hay internet?
                        //no hay internet
                        hayaRedes(posArr,-200,wmc,wm);
                    } else {
                        //si hay internet
                        hayaRedes(posArr,wm.getConnectionInfo().getRssi(),wmc,wm);
                    }

                } else {
                    //no hay conexion
                    hayaRedes(posArr,-200,wmc,wm);
                }
            }
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void hayaRedes(int[] posArr, int currentSignal, List<ScanResult> wmc, WifiManager wm) throws JSONException, UnknownHostException {
        String mac;
        Conexion c;
        while ((posArr = comparacion(wmc, currentSignal, posArr[0]))[0] != -1) {
            //mientras halla redes disponibles
            mac = wmc.get(posArr[0]).BSSID;
            c = misRedes.get(posArr[1]);
            if (!GestionArchivos.isOnBlackList(mac, c)) {
                //si no esta en la lista negra
                changeConection(c, wm);
                if (hasActiveInternetConnection()) {//hay internet
                    //si internet
                    if (!GestionArchivos.isOnWhiteList(mac, c)) {
                        //si no esta en la lista blanca
                        c.addWhiteListMac(mac);
                        GestionArchivos.actualizarConexionPorId(c, sp);
                    }
                    return;
                }
                else {
                    //no internet
                    if (!GestionArchivos.isOnWhiteList(mac, c)&&!GestionArchivos.isOnBlackList(mac, c)){
                        //si no esta en la lista blanca NI en la negra
                        c.addBlackListMac(mac);
                        GestionArchivos.actualizarConexionPorId(c,sp);
                    }
                }
            }
        }
    }

    //metodo buscar la red wifi mas alta(por encima de la intensidad de la señal actual)
    //debuelve un array de enteros: 0 para la posicion del scan y 1 para la de mis redes
    private int[] comparacion(List<ScanResult> wmc, int currentSignal, int posicion){
        for (int i = posicion+1; i < wmc.size(); i++) {
            if (wmc.get(i).level>currentSignal){
                for (int j = 0; j < misRedes.size(); j++) {
                    if (wmc.get(i).SSID.equals(misRedes.get(j).getSsid())){
                        return new int[]{i,j};
                    }
                }
            }
        }
        return new int[]{-1,-1};
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
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, c.getPuerta());
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, c.getMasc());
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, c.getIp());
        }
        GestionPreferencias.setActualConexionId(GestionPreferencias.getSharedPreferencesConfig(context),c.getId());
        wm.disconnect();
        wm.enableNetwork(netId, true);
        wm.reconnect();
        notificación(c);
    }

    private void notificación(Conexion c){
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.drawable.ic_notify);
        ncb.setContentTitle(context.getString(R.string.notificacion_titulo));
        ncb.setContentText(context.getString(R.string.notificacion_cuerpo) + " " + c.getSsid());
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

    private boolean checkLink(WifiManager wm){
        if (wm.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            wm.startScan();
            return true;
        }
        return false;
    }

    private boolean hasActiveNetConextion(ConnectivityManager cm){
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private boolean hasActiveInternetConnection() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://216.58.210.174").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException ignored) {}
        return false;
    }

    public void letFinish() {
        activo=false;
        time=0;
    }
}
