package com.example.myfirstapp;

import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class obtenerDatosServidor {

    public interface Callback {
        void onRespuesta(String respuesta);
    }

    public static void obtener(Callback callback) {
        obtener(utilidades.url_consulta, callback);
    }

    public static void obtener(String _url, Callback callback) {
        new Thread(() -> {
            String jsonResponse = "";
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept", "application/json"); // Indicamos que esperamos JSON
                httpURLConnection.setRequestProperty("Authorization",
                        "Basic " + utilidades.credencialesCodificadas);

                // Verificamos el código de respuesta (200 es OK)
                int responseCode = httpURLConnection.getResponseCode();
                InputStream inputStream;

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String linea;
                    while ((linea = bufferedReader.readLine()) != null) {
                        sb.append(linea);
                    }
                    jsonResponse = sb.toString();
                    bufferedReader.close();
                }

            } catch (Exception e) {
                jsonResponse = "Error: " + e.getMessage();
            } finally {
                if (httpURLConnection != null) httpURLConnection.disconnect();
            }

            final String finalResponse = jsonResponse;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (callback != null) {
                    callback.onRespuesta(finalResponse);
                }
            });
        }).start();
    }
}