package com.sergio.pruebas.memoria;

import android.content.Context;
import android.content.SharedPreferences;

import com.sergio.pruebas.entidades.Conexion;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class GestionArchivos {

    public static SharedPreferences getSharedPreferencesListado(Context c){
        return c.getSharedPreferences("$$listado", c.MODE_PRIVATE);
    }

    public static void añadirRed(Conexion c, SharedPreferences sp) throws JSONException, UnknownHostException {
        ArrayList<Conexion> arrLista = obtenerLista(sp);
        int max = 0;
        for (int i = 0; i < arrLista.size(); i++) {
            if (max<arrLista.get(i).getId()) max = arrLista.get(i).getId();
        }
        c.setId(max+1);
        arrLista.add(c);
        guardarLista(arrLista, sp);
    }

    public static boolean buscarRed(String ssid, SharedPreferences sp) throws JSONException, UnknownHostException {
        ArrayList<Conexion> arrLista = obtenerLista(sp);
        for (int i=0; i<arrLista.size();i++ ){
            if (ssid.equals(arrLista.get(i).getSsid())){
                return true;
            }
        }
        return false;
    }

    public static void borrarRedPorId(int id, SharedPreferences sp) throws JSONException, UnknownHostException {
        ArrayList<Conexion> arrLista = obtenerLista(sp);
        for (int i = 0; i < arrLista.size(); i++) {
            if (id==arrLista.get(i).getId()){
                arrLista.remove(i);
                guardarLista(arrLista,sp);
                return;
            }
        }
    }

    public static ArrayList<Conexion> obtenerLista(SharedPreferences sp) throws JSONException, UnknownHostException {
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

    public static Conexion obtenerConexionPorId( int id, SharedPreferences sp) throws JSONException, UnknownHostException {
        ArrayList<Conexion> lista = obtenerLista(sp);
        for (int i = 0; i < lista.size(); i++) {
            if (id==lista.get(i).getId()){
                return lista.get(i);
            }
        }
        return null;
    }

    public static void actualizarConexionPorId(Conexion c ,SharedPreferences sp) throws JSONException, UnknownHostException {
        ArrayList<Conexion> lista = obtenerLista(sp);
        for (int i = 0; i < lista.size(); i++) {
            if (c.getId()==lista.get(i).getId()){
                lista.get(i).update(c);
                guardarLista(lista,sp);
                return;
            }
        }
    }

    public static boolean isOnWhiteList(String s, Conexion c){
        for (int i = 0; i < c.getWhiteList().size(); i++) {
            if (s.equals(c.getWhiteList().get(i).getMac())){
                return true;
            }
        }
        return false;
    }

    public static boolean isOnBlackList(String s, Conexion c){
        for (int i = 0; i < c.getBlackList().size(); i++) {
            if (s.equals(c.getBlackList().get(i).getMac())){
                return true;
            }
        }
        return false;
    }

    private static void guardarLista(ArrayList<Conexion> arrLista, SharedPreferences sp) throws JSONException {
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
