package com.sergio.pruebas;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sergio.pruebas.conexiones.Conexion;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class test extends AppCompatActivity implements View.OnClickListener {

    TextView ssid1, pass1, ip1, masc1, puerta1, ssid2, pass2, ip2, masc2, puerta2;
    Button btser, bttest;
    Conexion c1, c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ssid1 = (TextView)findViewById(R.id.textView);
        pass1 = (TextView)findViewById(R.id.textView2);
        ip1 = (TextView)findViewById(R.id.textView3);
        masc1 = (TextView)findViewById(R.id.textView4);
        puerta1 = (TextView)findViewById(R.id.textView5);
        ssid2 = (TextView)findViewById(R.id.textView6);
        pass2 = (TextView)findViewById(R.id.textView7);
        ip2 = (TextView)findViewById(R.id.textView8);
        masc2 = (TextView)findViewById(R.id.textView9);
        puerta2 = (TextView)findViewById(R.id.textView10);

        String ssid, pass, ip, masc, puerta;

        ssid = ssid1.getText().toString();
        pass = pass1.getText().toString();
        ip = ip1.getText().toString();
        masc = masc1.getText().toString();
        puerta = puerta1.getText().toString();

        try {
            c1 = new Conexion(ssid,pass, InetAddress.getByName(ip),InetAddress.getByName(masc),InetAddress.getByName(puerta));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ssid = ssid2.getText().toString();
        pass = pass2.getText().toString();
        ip = ip2.getText().toString();
        masc = masc2.getText().toString();
        puerta = puerta2.getText().toString();

        try {
            c2 = new Conexion(ssid,pass, InetAddress.getByName(ip),InetAddress.getByName(masc),InetAddress.getByName(puerta));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        btser = (Button)findViewById(R.id.btnser);
        bttest = (Button)findViewById(R.id.btntest);
        btser.setOnClickListener(this);
        bttest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        WifiManager wm = (WifiManager) getSystemService(this.WIFI_SERVICE);
        WifiConfiguration wc = new WifiConfiguration();
        if (v.getId() == btser.getId()) {
            try {
                MyConfig.setIpAssignment(c1.getDHCP(),wc);
                MyConfig.setIpAddress(c1.getIp(),c1.getPrefijoRed(),wc);
                MyConfig.setGateway(c1.getPuerta(),wc);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else{
            try {
                MyConfig.setIpAssignment(c2.getDHCP(),wc);
                MyConfig.setIpAddress(c2.getIp(),c2.getPrefijoRed(),wc);
                MyConfig.setGateway(c2.getPuerta(),wc);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        wm.updateNetwork(wc); // apply the setting
        wm.saveConfiguration(); // Save it
    }
}
