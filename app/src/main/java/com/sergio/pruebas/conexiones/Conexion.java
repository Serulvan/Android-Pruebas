package com.sergio.pruebas.conexiones;

import java.net.InetAddress;

public class Conexion {
    private String ssid,pass;
    private InetAddress ip,masc,puerta;

    public Conexion(String ssid) {
        this.ssid = ssid;
    }

    public Conexion(String ssid, String pass) {
        this.ssid = ssid;
        this.pass = pass;
    }

    public Conexion(InetAddress ip, InetAddress masc, InetAddress puerta, String ssid) {
        this.ip = ip;
        this.masc = masc;
        this.puerta = puerta;
        this.ssid = ssid;
    }

    public Conexion(String ssid, String pass, InetAddress ip, InetAddress masc, InetAddress puerta) {
        this.ssid = ssid;
        this.pass = pass;
        this.ip = ip;
        this.masc = masc;
        this.puerta = puerta;
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
}
