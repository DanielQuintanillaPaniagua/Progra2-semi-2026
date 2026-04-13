package com.example.myfirstapp;

import java.util.Base64;

public class utilidades {
    static String url_consulta = "http://192.168.0.8:5984/amigos/_design/amigos/_view/amigos";
    static String url_mto = "http://192.168.0.8:5984/amigos";
    static String user = "admin";
    static String passwd = "admin";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user +":"+ passwd).getBytes());
    public String generarUnicoId(){
        return java.util.UUID.randomUUID().toString();
    }
}
