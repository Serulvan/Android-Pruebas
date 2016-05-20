package com.sergio.pruebas.conexiones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;

public class Conexion implements Comparator {
    private String ssid,pass, cifrado;
    private InetAddress ip,masc,puerta;
    private ArrayList<String> whiteList, blackList;
    private boolean auto;
    private int id;
    private static int currentMaxId=-1;

    public Conexion() {
        //solo se usa para ordenar con el sort
    }

    public Conexion(JSONObject jo) throws JSONException, UnknownHostException {
        ssid = jo.getString("ssid");
        cifrado=jo.getString("cifrado");
        if (!cifrado.equals("abierto")){
            pass = jo.getString("pass");
        }
        auto = jo.getBoolean("auto");
        id = jo.getInt("id");
        if (currentMaxId<0){
            currentMaxId=jo.getInt("currentMaxId");
        }
        if (jo.has("ip")) {
            auto=false;
            ip = InetAddress.getByName(jo.getString("ip"));
            masc = InetAddress.getByName(jo.getString("masc"));
            puerta = InetAddress.getByName(jo.getString("puerta"));
        } else auto=true;
        whiteList = new ArrayList<>();
        if (jo.get("whiteList").toString().length()>2) {
            rellenarList(whiteList, jo.getJSONArray("whiteList"));
        }
        blackList = new ArrayList<>();
        if (jo.get("blackList").toString().length()>2) {
            rellenarList(blackList,jo.getJSONArray("blackList"));
        }
    }

    public Conexion(String ssid, String pass, String cifrado) {
        this.ssid = ssid;
        this.cifrado = cifrado;
        if (!cifrado.equals("abierto")) {
            this.pass = pass;
        }
        auto=true;
        id=getCurrentMaxId();
        whiteList = new ArrayList<>();
        blackList = new ArrayList<>();
    }

    public Conexion(String ssid, String pass, String cifrado, InetAddress ip, InetAddress masc, InetAddress puerta) {
        this.ssid = ssid;
        this.cifrado = cifrado;
        if (!cifrado.equals("abierto")) {
            this.pass = pass;
        }
        this.ip = ip;
        this.masc = masc;
        this.puerta = puerta;
        auto=false;
        id=getCurrentMaxId();
        whiteList = new ArrayList<>();
        blackList = new ArrayList<>();
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

    public String getCifrado() {
        return cifrado;
    }

    public void setCifrado(String cifrado) {
        this.cifrado = cifrado;
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

    public ArrayList<String> getWhiteList() {
        return whiteList;
    }

    public void addWhiteListMac(String mac) {
        if (whiteList==null){
            whiteList = new ArrayList();
        }
        whiteList.add(mac);
    }

    public ArrayList<String> getBlackList() {
        return blackList;
    }

    public void addBlackListMac(String mac) {
        if (blackList==null){
            blackList = new ArrayList();
        }
        blackList.add(mac);
    }

    public String getSDHCP(){
        if (auto) {
            return "DHCP";
        }
        else {
            return "STATIC";
        }
    }

    public boolean getDHCP(){
        return auto;
    }

    public int getIDHCP(){
        if (auto){
            return 1;
        }else {
            return 0;
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
        jo.put("cifrado",cifrado);
        if (!cifrado.equals("abierto")){
            jo.put("pass",pass);
        }
        jo.put("auto",auto);
        jo.put("id",id);
        jo.put("currentMaxId",currentMaxId);
        if (!auto) {
            jo.put("ip", ip.getHostAddress());
            jo.put("masc", masc.getHostAddress());
            jo.put("puerta", puerta.getHostAddress());
        }
        jo.put("whiteList",whiteList);
        jo.put("blackList",blackList);
        return jo;
    }

    private int getCurrentMaxId(){
        return ++currentMaxId;
    }

    private void rellenarList(ArrayList<String> list, JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            list.add(ja.getString(i));
        }
    }
}
