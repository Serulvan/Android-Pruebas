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
import java.util.Collections;

public class GestionArchivos {

    public static void añadirRed(Conexion c, SharedPreferences sp, Activity a) throws JSONException, UnknownHostException {
        ArrayList<Conexion> arrLista = obtenerLista(sp, a);
        int max = 0;
        for (int i = 0; i < arrLista.size(); i++) {
            if (max<arrLista.get(i).getId()) max = arrLista.get(i).getId();
        }
        c.setId(max+1);
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
        if (listado.isEmpty()) return new ArrayList<>();
        else {
            JSONArray ja = new JSONArray(listado);
            ArrayList<Conexion> arrList = new ArrayList<>();
            for (int i = 0; i<ja.length();i++){
                arrList.add(new Conexion(ja.getJSONObject(i)));
            }
            return arrList;
        }

    }

    private static void guardarLista(ArrayList<Conexion> arrLista, SharedPreferences sp, Activity a) throws JSONException {
        SharedPreferences.Editor ed = sp.edit();
        Collections.sort(arrLista, new Conexion());
        JSONArray ja = new JSONArray();
        for (int i = 0; i<arrLista.size(); i++){
            ja.put(arrLista.get(i).toJsonObject());
        }
        ed.putString("$jList", ja.toString());
        ed.commit();
    }
}
