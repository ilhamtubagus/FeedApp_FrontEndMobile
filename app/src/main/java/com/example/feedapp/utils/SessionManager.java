package com.example.feedapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.example.feedapp.activity.StartActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SessionManager {
    private EnCryptor encryptor;
    private DeCryptor decryptor;
    private static final String SAMPLE_ALIAS = "TOKEN";
    private static final String TAG = StartActivity.class.getSimpleName();
    private static final String LOGGED_IN_PREF = "LOGGED IN PREF";
    private static final String TOKEN_PREF = "TOKEN";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        /*encryptor = new EnCryptor();
        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }*/
        sp = context.getSharedPreferences(LOGGED_IN_PREF, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean checkStatus(){
        return sp.contains(TOKEN_PREF);
    }

    public void setLoggedIn(Context context, String token){
        editor = sp.edit();
        //editor.putString(TOKEN_PREF, encryptText(token));
        editor.putString(TOKEN_PREF, token);
        editor.apply();
    }

    public String getToken(Context context){
        //String decrypted = sp.getString(TOKEN_PREF, null);
        //return decryptText(decrypted);
        return  "Bearer "+ sp.getString(TOKEN_PREF, null);
    }

    public void setLoggedOut(Context context){
        editor = sp.edit();
        editor.remove(TOKEN_PREF);
        editor.apply();
    }
    //Still error while using encryption
    private String encryptText(final String textToEncrypt) {
         String encryptedString = textToEncrypt;
         try {
            byte[] encryptedText = encryptor
                    .encryptText(SAMPLE_ALIAS, textToEncrypt);
            encryptedString = Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    private String decryptText(String textToDecrypt) {
        String decryptedText = textToDecrypt;
        try {
            byte[] decodedString = Base64.decode(textToDecrypt, Base64.DEFAULT);
            decryptedText = decryptor.decryptData(SAMPLE_ALIAS, decodedString, encryptor.getIv());
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}
