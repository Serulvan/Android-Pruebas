package com.sergio.pruebas.memoria;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class GestionPreferencias {
    public static SharedPreferences getSharedPreferencesConfig(Context c){
        return c.getSharedPreferences("$$configuracion", c.MODE_PRIVATE);
    }

    public static void guardarConfiguracion(SharedPreferences sp, boolean vibracion, boolean sonido, boolean servicio, int id){
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean("notificacion_vibracion",vibracion);
        spe.putBoolean("notificacion_sonido",sonido);
        spe.putBoolean("servicio_activo",servicio);
        spe.putInt("actual_conexion_id",id);
        spe.commit();
    }

    public static void setConfigNotifVibracion(SharedPreferences sp, boolean vibracion){
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean("notificacion_vibracion",vibracion);
        spe.commit();
    }

    public static void setConfigNotifSonido(SharedPreferences sp, boolean sonido){
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean("notificacion_sonido",sonido);
        spe.commit();
    }

    public static void setConfigServActivo(SharedPreferences sp, boolean servicio){
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean("servicio_activo",servicio);
        spe.commit();
    }

    public static void setActualConexionId(SharedPreferences sp, int id){
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt("actual_conexion_id",id);
        spe.commit();
    }

    public static int getActualConexionId(SharedPreferences sp){
        return sp.getInt("actual_conexion_id",-1);
    }

    public static boolean getConfigNotifVibracion(SharedPreferences sp){
        return sp.getBoolean("notificacion_vibracion",true);
    }

    public static boolean getConfigNotifSonido(SharedPreferences sp){
        return sp.getBoolean("notificacion_sonido",true);
    }

    public static boolean getConfigServActivo(SharedPreferences sp){
        return sp.getBoolean("servicio_activo",false);
    }
}
