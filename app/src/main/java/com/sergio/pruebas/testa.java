package com.sergio.pruebas;

import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sergio.pruebas.conexiones.Conexion;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class testa extends AppCompatActivity implements View.OnClickListener {

    TextView ssid1, pass1, ip1, masc1, puerta1, ssid2, pass2, ip2, masc2, puerta2;
    Button btser, bttest;
    Conexion c1, c2;

    //http://stackoverflow.com/questions/4106502/set-android-ip-dns-gateway-setting-programatically

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

        c1 = new Conexion(ssid,pass,"wpa", ip,masc,puerta);

        ssid = ssid2.getText().toString();
        pass = pass2.getText().toString();
        ip = ip2.getText().toString();
        masc = masc2.getText().toString();
        puerta = puerta2.getText().toString();

        c2 = new Conexion(ssid,pass,"wep", ip,masc,puerta);

        btser = (Button)findViewById(R.id.btnser);
        bttest = (Button)findViewById(R.id.btntest);
        btser.setOnClickListener(this);
        bttest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btser.getId()) {
            //Set to 1 for true and 0 for false.
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "1");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, c1.getPuerta());
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, c1.getMasc());
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, c1.getIp());
        }else{
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "0");
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_GATEWAY, c2.getPuerta());
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_NETMASK, c2.getMasc());
            android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.System.WIFI_STATIC_IP, c2.getIp());
        }
    }
}
