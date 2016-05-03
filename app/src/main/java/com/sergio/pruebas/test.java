package com.sergio.pruebas;

import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
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
        /*NotificationCompat.Builder ncb = new NotificationCompat.Builder(this);
        ncb.setSmallIcon(R.mipmap.ic_launcher);
        ncb.setContentTitle("My notification");
        ncb.setContentText("Hello World!");
        ncb.setTicker("haaaaaaaaa");
        NotificationManager nm;
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = 001;
        nm.notify(id,ncb.build());*/
        Log.i("Start", "notification");

   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("You've received new message.");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(0);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = new String("This is first line....");
        events[1] = new String("This is second line...");
        events[2] = new String("This is third line...");
        events[3] = new String("This is 4th line...");
        events[4] = new String("This is 5th line...");
        events[5] = new String("This is 6th line...");

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(1, mBuilder.build());
        /*WifiManager wm = (WifiManager) getSystemService(this.WIFI_SERVICE);
        WifiConfiguration wc = null;
        List<WifiConfiguration> cn = wm.getConfiguredNetworks();
        wc = cn.get(0);
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
*/
    }
}
