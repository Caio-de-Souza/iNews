package com.souza.caio.inews;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class Utils {

    public static String readApiKey(Context context) {
        AssetManager assetManager = context.getAssets();
        String apiKey = null;

        try {
            InputStream inputStream = assetManager.open("api_key.txt");
            apiKey = new Scanner(inputStream).useDelimiter("\\A").next();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    public static String loadCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
