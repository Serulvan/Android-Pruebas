package com.sergio.pruebas.entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Mac {
    private String mac, fecha;

    public Mac(JSONObject jo) throws JSONException {
        mac = jo.getString("mac");
        fecha = jo.getString("fecha");
    }

    public Mac(String mac) {
        this.mac = mac;
        Calendar c = Calendar.getInstance();
        fecha = c.getTime().toString();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getFecha() {
        return fecha;
    }

    public JSONObject toJsonObjet() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("mac",mac);
        jo.put("fecha",fecha);
        return jo;
    }
}
