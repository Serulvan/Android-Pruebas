package com.sergio.pruebas.adaptadores;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sergio.pruebas.R;

import java.util.List;



public class AdaptadorNuevaConexion extends ArrayAdapter {
    private List<ScanResult> arr;
    private int view;
    public AdaptadorNuevaConexion(Context context, int resource, List<ScanResult> objects) {
        super(context, resource, objects);
        arr=objects;
        view=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        if (item==null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(view,null);
        }
        TextView ssid = (TextView)item.findViewById(R.id.ssid_list);
        ssid.setText(arr.get(position).SSID);
        TextView datos1 = (TextView)item.findViewById(R.id.datos1);
        boolean hasPass;
        if (arr.get(position).capabilities.contains("WPA")) {
            datos1.setText("WPA/WPA2 PSK");
            hasPass=true;
        }else if (arr.get(position).capabilities.contains("WEP")){
            datos1.setText("WEP");
            hasPass=true;
        }else if (arr.get(position).capabilities.contains("IBSS")){
            datos1.setText("");
            hasPass=false;
        }else if (arr.get(position).capabilities.contains("ESS")){
            datos1.setText("");
            hasPass=false;
        }else{
            datos1.setText(arr.get(position).capabilities);
            hasPass = arr.get(position).capabilities.length() > 0;
        }

        //TextView datos2 = (TextView)item.findViewById(R.id.datos2);
        ImageView iv = (ImageView)item.findViewById(R.id.wb_imv_wifisignal);
        //50/3=16,6666666 => 17
        int nSe;

        if (arr.get(position).level>-50){
            nSe=4;
        }else if (arr.get(position).level>-67){
            nSe=3;
        }else if (arr.get(position).level>-84){
            nSe=2;
        }else if (arr.get(position).level>-100){
            nSe=1;
        }else{
            nSe=0;
        }
        //datos2.setText(String.valueOf(nSe));
        //datos2.setVisibility(View.VISIBLE);


        setImage(iv,nSe,hasPass);

        RelativeLayout rl = (RelativeLayout) item.findViewById(R.id.lbw_rl);
        rl.setBackgroundResource(R.drawable.list_view_clic_style);
        return item;
    }

    private void setImage(ImageView iv, int level,  boolean hasPass){
        if(hasPass){
            switch (level){
                case 0:
                    iv.setImageResource(R.drawable.wifi_c_0);
                    break;
                case 1:
                    iv.setImageResource(R.drawable.wifi_c_25);
                    break;
                case 2:
                    iv.setImageResource(R.drawable.wifi_c_50);
                    break;
                case 3:
                    iv.setImageResource(R.drawable.wifi_c_75);
                    break;
                case 4:
                    iv.setImageResource(R.drawable.wifi_c_100);
                    break;
            }
        }else{
            switch (level){
                case 0:
                    iv.setImageResource(R.drawable.wifi_o_0);
                    break;
                case 1:
                    iv.setImageResource(R.drawable.wifi_o_25);
                    break;
                case 2:
                    iv.setImageResource(R.drawable.wifi_o_50);
                    break;
                case 3:
                    iv.setImageResource(R.drawable.wifi_o_75);
                    break;
                case 4:
                    iv.setImageResource(R.drawable.wifi_o_100);
                    break;
            }
        }
        iv.setVisibility(View.VISIBLE);
    }
}
