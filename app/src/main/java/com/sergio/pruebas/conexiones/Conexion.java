package com.sergio.pruebas.conexiones;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;

public class Conexion implements Comparator {
    private String ssid,pass;
    private InetAddress ip,masc,puerta;
    private boolean auto;
    private int id;

    public Conexion() {
        //solo se usa para ordenar con el sort
    }

    public Conexion(JSONObject jo) throws JSONException, UnknownHostException {
        ssid = jo.getString("ssid");
        pass = jo.getString("pass");
        auto = jo.getBoolean("auto");
        id = jo.getInt("id");
        if (jo.has("ip")) {
            auto=false;
            ip = InetAddress.getByName(jo.getString("ip"));
            masc = InetAddress.getByName(jo.getString("masc"));
            puerta = InetAddress.getByName(jo.getString("puerta"));
        } else auto=true;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDHCP(){
        if (auto) {
            return "STATIC";
        }
        else {
            return "DHCP";
        }
    }

    public int getPrefijoRed(){
        String sa[] = masc.getHostAddress().split("\\.");
        String s = Integer.toBinaryString(Integer.parseInt(sa[0]))+
                Integer.toBinaryString(Integer.parseInt(sa[1]))+
                Integer.toBinaryString(Integer.parseInt(sa[2]))+
                Integer.toBinaryString(Integer.parseInt(sa[3]))+'0';
        int i=0;
        while (s.charAt(i++)=='1'&&i<s.length());
        return --i;
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        //ordenar por ssid
        return ((Conexion)lhs).getSsid().compareTo(((Conexion) rhs).getSsid());
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("ssid",ssid);
        jo.put("pass",pass);
        jo.put("auto",auto);
        jo.put("id",id);
        if (!auto) {
            jo.put("ip", ip.getHostAddress());
            jo.put("masc", masc.getHostAddress());
            jo.put("puerta", puerta.getHostAddress());
        }
        return jo;
    }
}
