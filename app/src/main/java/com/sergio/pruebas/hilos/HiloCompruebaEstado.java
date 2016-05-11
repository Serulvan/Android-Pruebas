package com.sergio.pruebas.hilos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.sergio.pruebas.R;


public class HiloCompruebaEstado extends AsyncTask<Void,Void,Void> {
    private Context context;
    private Boolean activo = true;

    public HiloCompruebaEstado(Context context, Boolean activo) {
        this.context = context;
        this.activo = activo;
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (activo) {
            try {
                Thread.sleep(1000 * 60 * 5);
                if (activo) {
                    publishProgress();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }
    //metodo buscar la red wifi mas alta(por encima de la intensidad de la señal actual)

    public void letFinish(){
        activo=false;
    }

    private void notificación(){
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(context);
        ncb.setSmallIcon(R.mipmap.ic_launcher);
        ncb.setContentTitle("conectado a SSID");
        ncb.setContentText("esto es una prueba");
        ncb.setAutoCancel(true);
        //Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.blast_small);
        //ncb.setSound(sound);
        //ncb.setNumber(1);
        //Intent intent = new Intent(context, Inicio.class);
        PendingIntent rpi = PendingIntent.getActivity(context, 0, /*intent*/new Intent(), 0);
        ncb.setContentIntent(rpi);

        int mNotificationId = 0;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, ncb.build());
    }
}
