package com.sergio.pruebas.hilos;

import android.os.AsyncTask;

/**
 * Created by Serulvan on 23/04/2016.
 */
public class HiloCompruebaEstado extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(1000*60*5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //metodo buscar la red wifi mas alta(por encima de la intensidad de la señal actual)
}
