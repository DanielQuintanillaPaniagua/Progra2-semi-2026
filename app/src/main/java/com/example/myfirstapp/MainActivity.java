package com.example.myfirstapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    DB db;
    Button btn;
    String accion = "nuevo", idProducto = "", urlFoto = "";
    ArrayList<String> listaFotos = new ArrayList<>();
    FloatingActionButton fab;
    ImageButton img;
    TextView tvContadorFotos;

    double ganancia = 0, porcentaje = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        img = findViewById(R.id.imgFotoProducto);
        tvContadorFotos = findViewById(R.id.tvContadorFotos);
        btn = findViewById(R.id.btnGuardarProducto);
        fab = findViewById(R.id.fabListaProductos);

        img.setOnClickListener(v -> tomarFoto());

        btn.setOnClickListener(v -> guardarProducto());

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, ListaActivity.class)));
    }

    private void guardarProducto() {
        String codigo = ((EditText)findViewById(R.id.txtCodigo)).getText().toString();
        String descripcion = ((EditText)findViewById(R.id.txtDescripcion)).getText().toString();
        String marca = ((EditText)findViewById(R.id.txtMarca)).getText().toString();
        String presentacion = ((EditText)findViewById(R.id.txtPresentacion)).getText().toString();
        String precio = ((EditText)findViewById(R.id.txtPrecio)).getText().toString();
        String costo = ((EditText)findViewById(R.id.txtCosto)).getText().toString();
        String stock = ((EditText)findViewById(R.id.txtStock)).getText().toString();

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()
                || costo.isEmpty() || stock.isEmpty()) {
            mostrarMsg("Todos los campos son obligatorios");
            return;
        }

        double precioD = Double.parseDouble(precio);
        double costoD = Double.parseDouble(costo);
        ganancia = precioD - costoD;
        porcentaje = (ganancia / costoD) * 100;

        String fotos = String.join(",", listaFotos);
        String[] datos = {idProducto, codigo, descripcion, marca,
                presentacion, precio, costo, stock, fotos};

        String resultado = db.administrar_Productos(accion, datos);

        if (resultado.equals("ok")) {
            mostrarMsg("Guardado ✓ Ganancia: " + ganancia +
                    " | %: " + String.format("%.2f", porcentaje));

            Cursor c = db.lista_productos();
            String idReal = "";
            if (c.moveToLast()) idReal = c.getString(0);
            c.close();

            sincronizarConCouchDB(idReal, codigo, descripcion,
                    marca, presentacion, precio, costo, stock, fotos);

            limpiarCampos();
        } else {
            mostrarMsg("Error: " + resultado);
        }
    }

    private void sincronizarConCouchDB(String id, String codigo, String descripcion,
                                       String marca, String presentacion,
                                       String precio, String costo, String stock,
                                       String urlFoto) {

        String docUrl = utilidades.url_mto + "/producto_" + id;

        // Calcular ganancia
        String gananciaStr = "0.00%";
        try {
            double p = Double.parseDouble(precio);
            double c = Double.parseDouble(costo);
            if (c > 0) gananciaStr = String.format("%.2f%%", ((p - c) / c) * 100);
        } catch (Exception e) { }

        String gananciaFinal = gananciaStr;

        // Primero verificar si ya existe para obtener _rev
        obtenerDatosServidor.obtener(docUrl, respuesta -> {
            String rev = "";
            if (respuesta != null && respuesta.contains("_rev")) {
                try {
                    int i = respuesta.indexOf("\"_rev\":\"") + 8;
                    int j = respuesta.indexOf("\"", i);
                    rev = respuesta.substring(i, j);
                } catch (Exception e) { }
            }

            String json = "{" +
                    (rev.isEmpty() ? "" : "\"_rev\":\"" + rev + "\",") +
                    "\"codigo\":\"" + codigo + "\"," +
                    "\"descripcion\":\"" + descripcion + "\"," +
                    "\"marca\":\"" + marca + "\"," +
                    "\"presentacion\":\"" + presentacion + "\"," +
                    "\"precio\":\"" + precio + "\"," +
                    "\"costo\":\"" + costo + "\"," +
                    "\"stock\":" + stock + "," +
                    "\"ganancia\":\"" + gananciaFinal + "\"," +   // ← ganancia
                    "\"urlFoto\":\"" + urlFoto + "\"" +
                    "}";

            enviarDatosServidor.enviar(json, "PUT", docUrl, r -> {
                if (r != null && (r.contains("\"ok\":true") || r.contains("\"id\""))) {
                    mostrarMsg("Sincronizado con CouchDB ✓");
                } else {
                    mostrarMsg("Error CouchDB: " + r);
                }
            });
        });
    }
    private void tomarFoto() {
        try {
            File foto = crearImgProducto();
            Uri uri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", foto);

            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(i, 1);
        } catch (Exception e) {
            mostrarMsg("Error al abrir cámara");
        }
    }

    private File crearImgProducto() throws Exception {
        String nombre = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File img = File.createTempFile(nombre, ".jpg", dir);
        urlFoto = img.getAbsolutePath();
        return img;
    }

    @Override
    protected void onActivityResult(int r, int res, @Nullable Intent d) {
        super.onActivityResult(r, res, d);
        if (r == 1 && res == RESULT_OK) {
            listaFotos.add(urlFoto);
            img.setImageURI(Uri.fromFile(new File(urlFoto)));
        }
    }

    private void limpiarCampos() {
        ((EditText)findViewById(R.id.txtCodigo)).setText("");
        ((EditText)findViewById(R.id.txtDescripcion)).setText("");
        ((EditText)findViewById(R.id.txtMarca)).setText("");
        ((EditText)findViewById(R.id.txtPresentacion)).setText("");
        ((EditText)findViewById(R.id.txtPrecio)).setText("");
        ((EditText)findViewById(R.id.txtCosto)).setText("");
        ((EditText)findViewById(R.id.txtStock)).setText("");
        listaFotos.clear();
        img.setImageResource(android.R.drawable.ic_menu_camera);
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}