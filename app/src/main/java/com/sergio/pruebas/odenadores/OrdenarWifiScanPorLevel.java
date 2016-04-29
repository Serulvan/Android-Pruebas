package com.sergio.pruebas.odenadores;

import android.net.wifi.ScanResult;

import java.util.Comparator;

public class OrdenarWifiScanPorLevel implements Comparator {

    @Override
    public int compare(Object lhs, Object rhs) {
        return ((ScanResult)rhs).level -(((ScanResult) lhs).level);
        //return 0;
    }
}
