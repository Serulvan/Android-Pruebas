package com.sergio.pruebas;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class test_int_ip extends AppCompatActivity {
    private TextView ip, ipint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_int_ip);
        ip = (TextView) findViewById(R.id.getip);
        ipint = (TextView) findViewById(R.id.getintip);
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        ip.setText(String.valueOf(ipToInt(intToIp(wm.getDhcpInfo().ipAddress))));
        //ip.setText(String.valueOf(0xDA00A8C0));
        //ip.setText(String.valueOf(a));

        ipint.setText(String.valueOf(wm.getDhcpInfo().ipAddress));

    }

    public String intToIp(int i) {
        return  ((i & 0xFF) + "." +
                ((i >>>= 8) & 0xFF) + "." +
                ((i >>>= 8) & 0xFF) + "." +
                ((i >>>= 8) & 0xFF));
    }

    public long ipToInt(String s){
        String sArr[]= s.split("\\."),toDecode="0x";
        for (int i=0; i<sArr.length;i++){
            if(Integer.toHexString(Integer.parseInt(sArr[i])).length()==2) {
                toDecode += Integer.toHexString(Integer.parseInt(sArr[i]));
            }else{
                toDecode += "0"+Integer.toHexString(Integer.parseInt(sArr[i]));
            }
        }
        return Long.parseLong("DA00A8C0",16);
    }
}
