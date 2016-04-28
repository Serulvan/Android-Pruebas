package com.sergio.pruebas.memoria;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.sergio.pruebas.conexiones.Conexion;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GestionArchivos {

    public static void añadirRed(Conexion c, SharedPreferences sp, Activity a) throws JSONException, UnknownHostException {
        ArrayList<Conexion> arrLista = obtenerLista(sp, a);
        arrLista.add(c);
        guardarLista(arrLista, sp, a);
    }

    public static boolean buscarRed(String ssid, SharedPreferences sp, Activity a) throws JSONException, UnknownHostException {
        ArrayList<Conexion> arrLista = obtenerLista(sp, a);
        for (int i=0; i<arrLista.size();i++ ){
            if (ssid.equals(arrLista.get(i).getSsid())){
                return true;
            } else return false;
        }
        return false;
    }

    public static ArrayList<Conexion> obtenerLista(SharedPreferences sp, Activity a) throws JSONException, UnknownHostException {
        String listado = sp.getString("$jList", "");
        Toast.makeText(a, listado+" obtencion lista", Toast.LENGTH_SHORT).show();
        if (listado.isEmpty()) return new ArrayList<>();
        else {
            JSONArray ja = new JSONArray(listado);
            ArrayList<Conexion> arrList = new ArrayList<>();
            for (int i = 0; i<ja.length();i++){
                String ssid = ja.getJSONObject(i).getString("ssid");
                String pass;
                Conexion c;
                if (ja.getJSONObject(i).getString("pass").isEmpty()){
                    pass = "";
                }else {
                    pass = ja.getJSONObject(i).getString("pass");
                }
                if (Boolean.valueOf(ja.getJSONObject(i).getString("auto"))) {
                    c = new Conexion(ssid, pass);
                } else{
                    InetAddress ip = InetAddress.getByName(ja.getJSONObject(i).getString("ip"));
                    InetAddress masc = InetAddress.getByName(ja.getJSONObject(i).getString("masc"));
                    InetAddress puerta = InetAddress.getByName(ja.getJSONObject(i).getString("puerta"));
                    c = new Conexion(ssid, pass, ip, masc, puerta);
                }
                arrList.add(c);
            }
            return arrList;
        }

    }

    private static void guardarLista(ArrayList<Conexion> arrLista, SharedPreferences sp, Activity a) throws JSONException {
        SharedPreferences.Editor ed = sp.edit();
        JSONArray ja = new JSONArray();
        for (int i = 0; i<arrLista.size(); i++){
            ja.put(arrLista.get(i).toJsonObject());
        }
        Toast.makeText(a, ja.toString()+" guardar lista", Toast.LENGTH_LONG).show();
        ed.putString("$jList", ja.toString());
        ed.commit();
    }
}
