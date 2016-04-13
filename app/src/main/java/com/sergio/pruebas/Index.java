package com.sergio.pruebas;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.net.*;
import android.net.wifi.WifiManager;

public class Index extends AppCompatActivity implements View.OnClickListener{
    private TextView ssid, ip, masc, puerta, dns1, dns2;
    private Button btnShow;
    private TableLayout tl;
    WifiManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        wm= (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wm.getDhcpInfo();
        ssid = (TextView)findViewById(R.id.ssid);
        ip = (TextView)findViewById(R.id.ip);
        masc = (TextView)findViewById(R.id.mascara);
        puerta = (TextView)findViewById(R.id.puerta);
        dns1 = (TextView)findViewById(R.id.dn1);
        dns2 = (TextView)findViewById(R.id.dn2);
        btnShow = (Button)findViewById(R.id.btnMostrar);
        tl = (TableLayout)findViewById(R.id.tlDatos);

        btnShow.setOnClickListener(this);

    }

    public String intToIp(int i) {
        return  ((i & 0xFF) + "." +
                ((i >>>= 8) & 0xFF) + "." +
                ((i >>>= 8) & 0xFF) + "." +
                ((i >>>= 8) & 0xFF));
    }

    @Override
    public void onClick(View v) {
        ip.setText(intToIp(wm.getDhcpInfo().ipAddress));
        masc.setText(intToIp(wm.getDhcpInfo().netmask));
        puerta.setText(intToIp(wm.getDhcpInfo().gateway));
        dns1.setText(intToIp(wm.getDhcpInfo().dns1));
        dns2.setText(intToIp(wm.getDhcpInfo().dns2));
        ssid.setText(wm.getConnectionInfo().getSSID());
        tl.setVisibility(View.VISIBLE);

        WifiConfiguration wc = new WifiConfiguration();
        //android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.
        //android.provider.Settings.System.putString(getContentResolver(), Settings.System.WIFI_USE_STATIC_IP,"1");


        //wm.addNetwork()
    }
}
