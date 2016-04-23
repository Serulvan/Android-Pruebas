package com.sergio.pruebas;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class test_int_ip extends AppCompatActivity {
    private TextView ip, ipint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_int_ip);
        ip = (TextView) findViewById(R.id.getip);
        ipint = (TextView) findViewById(R.id.getintip);
    }
}
