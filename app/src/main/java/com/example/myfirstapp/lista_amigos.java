
package com.example.myfirstapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_amigos extends Activity {
    Bundle parametros = new Bundle();
    DB db;
    FloatingActionButton fab;
    ListView ltsAmigos;
    Cursor cAmigos;
    final ArrayList<amigo> alAmigo = new ArrayList<>();
    final ArrayList<amigo> alAmigoCopia = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    int posicion = 0;
    amigo misAmigos;
    detectarinternet di;
    obtenerdatosdelservidor datosServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_amigos);

        parametros.putString("accion", "nuevo");
        db = new DB(this);

        ltsAmigos = findViewById(R.id.ltsAmigos);

        fab = findViewById(R.id.fabAgregarAmigos);
        fab.setOnClickListener(v -> abrirActivity());
        di = new detectarinternet(this);
        obtenerAmigos();
        buscarAmigos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
        } catch (Exception e) {
            mostrarMsg("Error al desplegar menu: " + e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxAgregar) {
                abrirActivity();
            } else if (item.getItemId() == R.id.mnxModificar) {
                parametros.putString("accion", "modificar");
                parametros.putString("amigos", jsonArray.getJSONObject(posicion).getJSONObject("value").toString());
                abrirActivity();
            } else if (item.getItemId() == R.id.mnxEliminar) {
                borrarAmigo();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error al seleccionar un item del menu: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private void borrarAmigo() {
        try {
            String nombre = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("nombre");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("¿Está seguro de borrar a?");
            confirmacion.setMessage(nombre);
            confirmacion.setPositiveButton("SI", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_amigos("eliminar",
                            new String[]{jsonArray.getJSONObject(posicion).getJSONObject("value").getString("idAmigo")});

                    if( respuesta.equals("ok") && di.hayConexionInternet() ){
                        JSONObject datosAmigos = new JSONObject();
                        String _id = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("_id");
                        String _rev = jsonArray.getJSONObject(posicion).getJSONObject("value").getString("_rev");
                        String url = utilidades.url_mto +"/"+_id +"?rev="+ _rev;

                        enviarDatosServidor objEnviarDatosServidor = new enviarDatosServidor(this);
                        respuesta = objEnviarDatosServidor.execute(datosAmigos.toString(),"DELETE", url).get();
                        JSONObject respuestaJSON = new JSONObject(respuesta);

                        if(!respuestaJSON.getBoolean("ok")){
                            mostrarMsg("Error al eliminar en el servidor: "+ respuesta);
                        }
                        mostrarMsg("Registro eliminado con exito.");
                    }else if(respuesta.equals("ok")){
                        mostrarMsg("Amigo borrado con exito solo en local.");
                    }
                    obtenerAmigos();
                } catch (Exception e) {
                    mostrarMsg(e.getMessage());
                }
            });
            confirmacion.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            confirmacion.create().show();
        } catch (Exception e) {
            mostrarMsg("Error al borrar el amigo: " + e.getMessage());
        }
    }

    private void buscarAmigos() {
        TextView tempVal = findViewById(R.id.txtBuscarAmigos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alAmigo.clear();
                String buscar = s.toString().trim().toLowerCase();

                if (buscar.isEmpty()) {
                    alAmigo.addAll(alAmigoCopia);
                } else {
                    for (amigo item : alAmigoCopia) {
                        if (item.getNombre().toLowerCase().contains(buscar) ||
                                item.getDui().contains(buscar) ||
                                item.getEmail().toLowerCase().contains(buscar)) {
                            alAmigo.add(item);
                        }
                    }
                }
                ltsAmigos.setAdapter(new AdaptadorAmigos(getApplicationContext(), alAmigo));
            }
        });
    }

    private void abrirActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void obtenerAmigos() {
        try {
            if(di.hayConexionInternet()){//si hay conexion a internet
                datosServidor = new obtenerdatosdelservidor();
                String respuesta = datosServidor.execute().get();
                jsonObject = new JSONObject(respuesta);
                jsonArray = jsonObject.getJSONArray("rows");
                mostrarAmigos();
            }else {//no hay conexion a internet
                cAmigos = db.lista_amigos();
                if (cAmigos.moveToFirst()) {
                    jsonArray = new JSONArray();
                    do {
                        jsonObject = new JSONObject();
                        jsonObject.put("idAmigo", cAmigos.getString(0));
                        jsonObject.put("nombre", cAmigos.getString(1));
                        jsonObject.put("direccion", cAmigos.getString(2));
                        jsonObject.put("telefono", cAmigos.getString(3));
                        jsonObject.put("email", cAmigos.getString(4));
                        jsonObject.put("dui", cAmigos.getString(5));
                        jsonObject.put("foto", cAmigos.getString(6));
                        jsonArray.put(jsonObject);
                    } while (cAmigos.moveToNext());
                    mostrarAmigos();
                } else {
                    mostrarMsg("No hay amigos que mostrar");
                    abrirActivity();
                }
            }
        } catch (Exception e) {
            mostrarMsg(e.getMessage());
        } finally {
            if (cAmigos != null && !cAmigos.isClosed()) {
                cAmigos.close();
            }
        }
    }

    private void mostrarAmigos() {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                alAmigo.clear();
                alAmigoCopia.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i).getJSONObject("value");
                    misAmigos = new amigo(
                            jsonObject.getString("idAmigo"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("direccion"),
                            jsonObject.getString("telefono"),
                            jsonObject.getString("email"),
                            jsonObject.getString("dui"),
                            jsonObject.getString("foto")
                    );
                    alAmigo.add(misAmigos);
                }
                alAmigoCopia.addAll(alAmigo);
                ltsAmigos.setAdapter(new AdaptadorAmigos(this, alAmigo));
                registerForContextMenu(ltsAmigos);
            } else {
                mostrarMsg("No hay amigos que mostrar...");
                abrirActivity();
            }
        } catch (Exception e) {
            mostrarMsg(e.getMessage());
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}