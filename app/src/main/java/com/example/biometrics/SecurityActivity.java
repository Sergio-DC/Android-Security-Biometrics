package com.example.biometrics;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecurityActivity extends AppCompatActivity {

    private EditText etTexto;
    private TextView tvTexto;
    private Button btEncriptar, btDesEncriptar;
    private String textoSalida;
    private String llave = "123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        etTexto = findViewById(R.id.mainActivityEtTexto);
        tvTexto = findViewById(R.id.mainActivityTvTexto);
        btEncriptar = findViewById(R.id.mainActivityBtEncriptar);
        btEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    textoSalida = encriptar(etTexto.getText().toString(), llave);
                    tvTexto.setText(textoSalida);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btDesEncriptar = findViewById(R.id.mainActivityBtDesencriptar);
        btDesEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    textoSalida = desencriptar(textoSalida, llave);
                    tvTexto.setText(textoSalida);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private String desencriptar(String datos, String password) throws Exception{
        SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
        String datosDesencriptadosString = new String(datosDesencriptadosByte);
        return datosDesencriptadosString;
    }

    private String encriptar(String datos, String password) throws Exception{
        SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }
}

