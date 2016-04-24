package com.sergio.pruebas.dialogos;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.sergio.pruebas.R;

public class DialogoNuevaConexionConfig extends Activity implements View.OnFocusChangeListener{

    private EditText ip, mascara, puerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogo_nueva_conexion_config);

        ip= (EditText)findViewById(R.id.dncc_ip);
        mascara= (EditText)findViewById(R.id.dncc_masc);
        puerta= (EditText)findViewById(R.id.dncc_puerta);

        ip.setOnFocusChangeListener(this);
        mascara.setOnFocusChangeListener(this);
        puerta.setOnFocusChangeListener(this);



        String ssid, pass;
        ssid = getIntent().getStringExtra("ssid");
        pass = getIntent().getStringExtra("pass");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.dncc_puerta: case R.id.dncc_ip:
                if (!hasFocus) {
                    boolean mal = false;
                    String sArr[] = ((EditText) v).getText().toString().trim().split("\\.");
                    if (sArr.length == 4) {
                        for (int i = 0; i < sArr.length; i++) {
                            int num = Integer.parseInt(sArr[i]);
                            if (num < 0 || num > 255) {
                                mal = true;
                                break;
                            }
                        }
                    } else {
                        mal = true;
                    }
                    if (mal) {
                        Toast.makeText(this, R.string.error_ip_ipnovalida, Toast.LENGTH_LONG).show();
                        ((EditText) v).setText("");
                    }
                }
                break;
            case R.id.dncc_masc:
                if (!hasFocus) {
                    String sArr[] = ((EditText) v).getText().toString().split("\\.");
                    if (sArr.length == 4) {
                        int temp = 1;
                        boolean continuar = true;
                        for (int i = 0; i < sArr.length; i++) {
                            String segm = Integer.toBinaryString(Integer.parseInt(sArr[i]));
                            for (int j = 0; j < segm.length(); j++) {
                                int actNum = Integer.parseInt(String.valueOf(segm.charAt(j)));
                                if (actNum <= temp) {
                                    temp = actNum;
                                }else {
                                    continuar=false;
                                }
                            }
                            if (!continuar) ;
                            Toast.makeText(this, R.string.error_ip_mascnovalida, Toast.LENGTH_LONG).show();
                            ((EditText) v).setText("");
                            break;
                        }
                    } else {
                        Toast.makeText(this, R.string.error_ip_mascnovalida, Toast.LENGTH_LONG).show();
                        ((EditText) v).setText("");
                    }
                }
                break;
        }
    }
}