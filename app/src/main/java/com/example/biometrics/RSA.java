package com.example.biometrics;

import android.content.Context;
import android.util.Log;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

    public PrivateKey PrivateKey = null;
    public PublicKey PublicKey = null;

    public Context context;

    public RSA()
    {


    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPrivateKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] encodedPrivateKey = stringToBytes(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        this.PrivateKey = privateKey;
    }

    public void setPublicKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException{

        byte[] encodedPublicKey = stringToBytes(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        this.PublicKey = publicKey;
    }

    public String getPrivateKeyString(){
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(this.PrivateKey.getEncoded());
        return bytesToString(pkcs8EncodedKeySpec.getEncoded());
    }

    public String getPublicKeyString(){
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(this.PublicKey.getEncoded());
        return bytesToString(x509EncodedKeySpec.getEncoded());
    }


    public void genKeyPair(int size) throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException  {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(size);
        KeyPair kp = kpg.genKeyPair();

        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        this.PrivateKey = privateKey;
        this.PublicKey = publicKey;
    }

    public String Encrypt(String plain) throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException,IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException {

        byte[] encryptedBytes;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.PublicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());

        return bytesToString(encryptedBytes);

    }

    public String Decrypt(String result) throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] decryptedBytes;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.PrivateKey);
        decryptedBytes = cipher.doFinal(stringToBytes(result));
        return new String(decryptedBytes);
    }

    public String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }


    public void saveToDiskPrivateKey(String path){
        try {
            FileOutputStream outputStream = null;
            outputStream =  this.context.openFileOutput(path, Context.MODE_PRIVATE);
            outputStream.write(this.getPrivateKeyString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.d("RSA:","Error write PrivateKey");
        }
    }

    public void saveToDiskPublicKey(String path) {
        try {
            FileOutputStream outputStream = null;
            outputStream =  this.context.openFileOutput(path, Context.MODE_PRIVATE);
            outputStream.write(this.getPublicKeyString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.d("RSA:","Error write Public");
        }
    }

    public void openFromDiskPublicKey(String path) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String content = this.readFileAsString(path);
        this.setPublicKeyString(content);
    }

    public void openFromDiskPrivateKey(String path) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String content = this.readFileAsString(path);
        this.setPrivateKeyString(content);
    }


    private String readFileAsString(String filePath) throws IOException {

        BufferedReader fin = new BufferedReader(new InputStreamReader(context.openFileInput(filePath)));
        String txt = fin.readLine();
        fin.close();
        return txt;

    }

}