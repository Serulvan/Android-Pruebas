package com.sergio.pruebas.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;

public class Conexion implements Comparator {
    private String ssid,pass, cifrado,ip,masc,puerta;
    private ArrayList<Mac> whiteList, blackList;
    private boolean auto;
    private int id;
    private static int currentMaxId=-1;
    public static final String CIFRADO_ABIERTO = "Abierto";
    public static final String CIFRADO_WEP = "WEP";
    public static final String CIFRADO_WPA = "WPA/WPA2 PSK";
    //public static final String CIFRADO_IEEE80XX = "";
    public static final String WHITE = "w";
    public static final String BlACK = "b";

    public Conexion() {
        //solo se usa para ordenar con el sort
    }

    public Conexion(JSONObject jo) throws JSONException, UnknownHostException {
        ssid = jo.getString("ssid");
        cifrado=jo.getString("cifrado");
        if (!cifrado.equals(CIFRADO_ABIERTO)){
            pass = jo.getString("pass");
        }
        auto = jo.getBoolean("auto");
        id = jo.getInt("id");
        if (currentMaxId<0){
            currentMaxId=jo.getInt("currentMaxId");
        }
        if (jo.has("ip")) {
            auto=false;
            ip = jo.getString("ip");
            masc = jo.getString("masc");
            puerta = jo.getString("puerta");
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
        if (!cifrado.equals(CIFRADO_ABIERTO)){
            this.pass = pass;
        }
        auto=true;
        id=getCurrentMaxId();
        whiteList = new ArrayList<>();
        blackList = new ArrayList<>();
    }

    public Conexion(String ssid, String pass, String cifrado, String ip, String masc, String puerta) {
        this.ssid = ssid;
        this.cifrado = cifrado;
        if (!cifrado.equals(CIFRADO_ABIERTO)){
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMasc() {
        return masc;
    }

    public void setMasc(String masc) {
        this.masc = masc;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public ArrayList<Mac> getWhiteList() {
        return whiteList;
    }

    public void addWhiteListMac(String mac) {
        if (whiteList==null){
            whiteList = new ArrayList<>();
        }
        whiteList.add(new Mac(mac));
    }

    public ArrayList<Mac> getBlackList() {
        return blackList;
    }

    public void addBlackListMac(String mac) {
        if (blackList==null){
            blackList = new ArrayList<>();
        }
        blackList.add(new Mac(mac));
    }

    public void borrarMacPorFecha (String fecha, String lista){
        switch (lista){
            case WHITE:
                for (Mac mac : whiteList) {
                    if (mac.getFecha().equals(fecha)){
                        whiteList.remove(mac);
                        return;
                    }
                }
                break;
            case BlACK:
                for (Mac mac : blackList) {
                    if (mac.getFecha().equals(fecha)){
                        blackList.remove(mac);
                        return;
                    }
                }
                break;
        }
    }

    public void modificarMacPorFecha (String fecha, String sMac, String lista){
        switch (lista){
            case WHITE:
                for (Mac mac : whiteList) {
                    if (mac.getFecha().equals(fecha)){
                        mac.setMac(sMac);
                        return;
                    }
                }
                break;
            case BlACK:
                for (Mac mac : blackList) {
                    if (mac.getFecha().equals(fecha)){
                        mac.setMac(sMac);
                        return;
                    }
                }
                break;
        }
    }

    @Deprecated
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

    public void setDHCP(boolean b){
        auto=b;
    }

    public int getIDHCP(){
        if (auto){
            return 1;
        }else {
            return 0;
        }
    }
    @Deprecated
    public int getPrefijoRed(){
        String sa[] = masc.split("\\.");
        String s = Integer.toBinaryString(Integer.parseInt(sa[0]))+
                Integer.toBinaryString(Integer.parseInt(sa[1]))+
                Integer.toBinaryString(Integer.parseInt(sa[2]))+
                Integer.toBinaryString(Integer.parseInt(sa[3]))+'0';
        int i=0;
        while (s.charAt(i++)=='1'&&i<s.length());
        return --i;
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("ssid",ssid);
        jo.put("cifrado",cifrado);
        if (!cifrado.equals(CIFRADO_ABIERTO)){
            jo.put("pass",pass);
        }
        jo.put("auto",auto);
        jo.put("id",id);
        jo.put("currentMaxId",currentMaxId);
        if (!auto) {
            jo.put("ip", ip);
            jo.put("masc", masc);
            jo.put("puerta", puerta);
        }
        JSONArray ja = new JSONArray();
        for (int i = 0; i < whiteList.size(); i++) {
            ja.put(whiteList.get(i).toJsonObjet());
        }
        jo.put("whiteList",ja);
        ja = new JSONArray();
        for (int i = 0; i < blackList.size(); i++) {
            ja.put(blackList.get(i).toJsonObjet());
        }
        jo.put("blackList",ja);
        return jo;
    }

    private int getCurrentMaxId(){
        return ++currentMaxId;
    }

    private void rellenarList(ArrayList<Mac> list, JSONArray ja) throws JSONException {
        for (int i = 0; i < ja.length(); i++) {
            list.add(new Mac(ja.getJSONObject(i)));
        }
    }

    public void update(Conexion c){
        ssid = c.getSsid();
        cifrado = c.getCifrado();
        if (!cifrado.equals(CIFRADO_ABIERTO)){
            pass = c.getPass();
        }
        auto=c.getDHCP();
        if(!auto) {
            ip = c.getIp();
            masc = c.getMasc();
            puerta = c.getPuerta();
        }
        whiteList = c.getWhiteList();
        blackList = c.getBlackList();
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        //ordenar por ssid
        return ((Conexion)lhs).getSsid().compareTo(((Conexion) rhs).getSsid());
    }


}
