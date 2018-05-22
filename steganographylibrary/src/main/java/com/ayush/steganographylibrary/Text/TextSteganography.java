package com.ayush.steganographylibrary.Text;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.ayush.steganographylibrary.Utils.Crypto;
import com.ayush.steganographylibrary.Utils.Utility;
import com.ayush.steganographylibrary.Utils.Zipping;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * This main class of the text steganography
 */
public class TextSteganography {

    //Tag for Log
    private static String TAG = TextSteganography.class.getName();

    String message;
    String secret_key;
    String encrypted_message;
    Bitmap image;
    Bitmap encrypted_image;
    byte[] encrypted_zip;
    Boolean encoded;
    Boolean decoded;
    Boolean secretKeyWrong;

    public TextSteganography() {
    }

    public TextSteganography(String message, String secret_key, Bitmap image) {

        this.message = message;
        this.secret_key = convertKeyTo128bit(secret_key);
        this.image = image;
        try {
            this.encrypted_zip = Zipping.compress(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.encrypted_message = encryptMessage(new String(getEncrypted_zip(), "ISO-8859-1"), this.secret_key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        this.encoded = false;
        this.decoded = false;
        this.secretKeyWrong = true;

    }

    public TextSteganography(String secret_key, Bitmap image) {
        this.secret_key = convertKeyTo128bit(secret_key);
        this.image = image;
        decoded = false;
    }

    public Bitmap getEncrypted_image() {
        return encrypted_image;
    }

    public void setEncrypted_image(Bitmap encrypted_image) {
        this.encrypted_image = encrypted_image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public byte[] getEncrypted_zip() {
        return encrypted_zip;
    }

    public void setEncrypted_zip(byte[] encrypted_zip) {
        this.encrypted_zip = encrypted_zip;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

    public void setEncrypted_message(String encrypted_message) {
        this.encrypted_message = encrypted_message;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Boolean isEncoded() {
        return encoded;
    }

    public void setEncoded(Boolean encoded) {
        this.encoded = encoded;
    }

    public Boolean isDecoded() {
        return decoded;
    }

    public void setDecoded(Boolean decoded) {
        this.decoded = decoded;
    }

    public Boolean isSecretKeyWrong() {
        return secretKeyWrong;
    }

    public void setSecretKeyWrong(Boolean secretKeyWrong) {
        this.secretKeyWrong = secretKeyWrong;
    }

    public static String encryptMessage(String message, String secret_key){

        Log.d(TAG, "Message : " + message );

        String encrypted_message = null;
        if (message != null){
            if (!Utility.isStringEmpty(secret_key)){
                try {
                    encrypted_message = Crypto.encryptMessage(message, secret_key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                encrypted_message = message;
            }
        }

        Log.d(TAG, "Encrypted_message : " + encrypted_message );

        return encrypted_message;
    }

    public static String decryptMessage(String message, String secret_key){
        String decrypted_message = null;
        if (message != null){
            if (!Utility.isStringEmpty(secret_key)){
                try {
                    decrypted_message = Crypto.decryptMessage(message, secret_key);
                } catch (Exception e) {
                    Log.d(TAG, "Error : " + e.getMessage() + " , may be due to wrong key.");
                }
            }
            else {
                decrypted_message = message;
            }
        }

        Log.d(TAG , "Decrypted Message : " + decrypted_message);

        return decrypted_message;
    }

    public static String convertKeyTo128bit(String secret_key){

        String result = secret_key;

        if (secret_key.length() <= 16){
            for (int i = 0; i < (16 - secret_key.length()); i++){
                result += "#";
            }
        }
        else {
            result = result.substring(0, 15);
        }

        Log.d(TAG, "Secret Key Length : " + result.getBytes().length);

        return result;
    }
}
