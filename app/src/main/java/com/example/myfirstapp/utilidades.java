package com.example.myfirstapp;

import android.util.Base64;

public class utilidades {
    public static String url_consulta = "http://10.0.2.2:5984/danielquintanilla/_design/jose%20garcia/_view/jose%20garcia";

    // URL base de la base de datos
    public static String url_mto = "http://10.0.2.2:5984/danielquintanilla";

    private static final String user = "Daniel";
    private static final String passwd = "1030";

    public static String credencialesCodificadas = Base64.encodeToString(
            (user + ":" + passwd).getBytes(), Base64.NO_WRAP);

    public String generarUnicoId() {
        return java.util.UUID.randomUUID().toString();
    }
}