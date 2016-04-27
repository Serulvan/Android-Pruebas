package com.sergio.pruebas.conexiones;

import android.content.SharedPreferences;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;

public class Conexion implements Comparator {
    private String ssid,pass;
    private InetAddress ip,masc,puerta;
    private boolean auto;

    public Conexion() {
    }

    public Conexion(String ssid, String pass) {
        this.ssid = ssid;
        this.pass = pass;
        auto=true;
    }

    public Conexion(String ssid, String pass, InetAddress ip, InetAddress masc, InetAddress puerta) {
        this.ssid = ssid;
        this.pass = pass;
        this.ip = ip;
        this.masc = masc;
        this.puerta = puerta;
        auto=false;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public InetAddress getMasc() {
        return masc;
    }

    public void setMasc(InetAddress masc) {
        this.masc = masc;
    }

    public InetAddress getPuerta() {
        return puerta;
    }

    public void setPuerta(InetAddress puerta) {
        this.puerta = puerta;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        return ((Conexion)lhs).getSsid().compareTo(((Conexion) rhs).getSsid());
    }
}
