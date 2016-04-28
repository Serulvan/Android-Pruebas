package com.sergio.pruebas.hilos;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class HiloCompruebaEstado extends AsyncTask<Void,Void,Void> {
    private Context a;
    private Boolean activo = true;

    public HiloCompruebaEstado(Context a, Boolean activo) {
        this.a = a;
        this.activo = activo;
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (activo) {
            try {
                Thread.sleep(1000 * 60 * 5);
                publishProgress();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Toast.makeText(a, "hola", Toast.LENGTH_SHORT).show();
    }
    //metodo buscar la red wifi mas alta(por encima de la intensidad de la señal actual)
}
