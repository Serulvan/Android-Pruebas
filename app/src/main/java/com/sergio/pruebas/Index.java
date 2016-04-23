package com.sergio.pruebas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.net.wifi.WifiManager;
import android.widget.ToggleButton;

import com.sergio.pruebas.hilos.HiloEscaneoWifi;

public class Index extends AppCompatActivity implements View.OnClickListener{
    private TextView ssid, ip, masc, puerta, dns1, dns2;
    private Button btnShow;
    private ToggleButton swButton;
    private TableLayout tl;

    private ListView lvT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        ssid = (TextView)findViewById(R.id.ssid);
        ip = (TextView)findViewById(R.id.ip);
        masc = (TextView)findViewById(R.id.mascara);
        puerta = (TextView)findViewById(R.id.puerta);
        dns1 = (TextView)findViewById(R.id.dn1);
        dns2 = (TextView)findViewById(R.id.dn2);
        btnShow = (Button)findViewById(R.id.btnMostrar);
        swButton = (ToggleButton)findViewById(R.id.swButton);
        tl = (TableLayout)findViewById(R.id.tlDatos);
        lvT =(ListView)findViewById(R.id.lvt);

        btnShow.setOnClickListener(this);
        swButton.setOnClickListener(this);

    }

    public String intToIp(int i) {
        return  ((i & 0xFF) + "." +
                ((i >>>= 8) & 0xFF) + "." +
                ((i >>>= 8) & 0xFF) + "." +
                ((i >>>= 8) & 0xFF));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnMostrar) {
            WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            ip.setText(intToIp(wm.getDhcpInfo().ipAddress));
            masc.setText(intToIp(wm.getDhcpInfo().netmask));
            puerta.setText(intToIp(wm.getDhcpInfo().gateway));
            dns1.setText(intToIp(wm.getDhcpInfo().dns1));
            dns2.setText(intToIp(wm.getDhcpInfo().dns2));
            ssid.setText(wm.getConnectionInfo().getSSID());

            new HiloEscaneoWifi(lvT,wm, this).execute();
        } else{
            if (!swButton.isChecked()){
                tl.setVisibility(View.GONE);
            }else{
                tl.setVisibility(View.VISIBLE);
            }
        }
        //WifiConfiguration wc = new WifiConfiguration();
        //android.provider.Settings.System.putString(getContentResolver(), android.provider.Settings.
        //android.provider.Settings.System.putString(getContentResolver(), Settings.System.WIFI_USE_STATIC_IP,"1");


        //wm.addNetwork()
    }
}
