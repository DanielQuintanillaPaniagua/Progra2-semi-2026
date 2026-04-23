package com.example.myfirstapp;

import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class enviarDatosServidor{
    public interface Callback {
        void onRespuesta(String respuesta);
    }

    public static void enviar(String jsonDatos, String metodo, String _url, Callback callback) {
        new Thread(() -> {
            String jsonResponse = "";
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod(metodo);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                // Uso de las credenciales de la clase utilidades
                httpURLConnection.setRequestProperty("Authorization", "Basic " + utilidades.credencialesCodificadas);

                Writer writer = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                writer.write(jsonDatos);
                writer.flush();
                writer.close();

                int responseCode = httpURLConnection.getResponseCode();
                InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                        ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();

                if (inputStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String linea;
                    while ((linea = br.readLine()) != null) sb.append(linea);
                    jsonResponse = sb.toString();
                    br.close();
                }
            } catch (Exception e) {
                jsonResponse = "Error: " + e.getMessage();
            } finally {
                if (httpURLConnection != null) httpURLConnection.disconnect();
            }

            String finalResponse = jsonResponse;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (callback != null) callback.onRespuesta(finalResponse);
            });
        }).start();
    }
}