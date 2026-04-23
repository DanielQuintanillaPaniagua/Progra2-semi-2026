package com.example.myfirstapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaActivity extends AppCompatActivity {

    DB db;
    ListView lista;
    EditText txtBuscar;
    ArrayList<HashMap<String, String>> todosLosDatos;
    ProductoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        db        = new DB(this);
        lista     = findViewById(R.id.lvProductos);
        txtBuscar = findViewById(R.id.txtBuscar);

        cargarLista();

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
        });

        lista.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);

            String[] opciones = {"✏️ Modificar", "🗑️ Eliminar", "➕ Agregar nuevo"};

            new AlertDialog.Builder(this)
                    .setTitle(item.get("descripcion"))
                    .setItems(opciones, (dialog, which) -> {
                        switch (which) {
                            case 0: // Modificar
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra("idProducto",   item.get("idProducto"));
                                intent.putExtra("codigo",       item.get("codigo"));
                                intent.putExtra("descripcion",  item.get("descripcion"));
                                intent.putExtra("marca",        item.get("marca"));
                                intent.putExtra("presentacion", item.get("presentacion"));
                                intent.putExtra("precio",       item.get("precio"));
                                intent.putExtra("costo",        item.get("costo"));
                                intent.putExtra("stock",        item.get("stock"));
                                intent.putExtra("urlFoto",      item.get("urlFoto"));
                                startActivity(intent);
                                break;

                            case 1: // Eliminar
                                confirmarEliminacion(item);
                                break;

                            case 2: // Nuevo
                                startActivity(new Intent(this, MainActivity.class));
                                break;
                        }
                    })
                    .show();
        });

        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }

    private void cargarLista() {
        todosLosDatos = new ArrayList<>();
        Cursor c = db.lista_productos();


        if (c.moveToFirst()) {
            do {
                HashMap<String, String> fila = new HashMap<>();
                fila.put("idProducto",   c.getString(0));
                fila.put("codigo",       c.getString(1));
                fila.put("descripcion",  c.getString(2));
                fila.put("marca",        c.getString(3));
                fila.put("presentacion", c.getString(4));
                fila.put("precio",       c.getString(5));
                fila.put("costo",        c.getString(6));
                fila.put("stock",        c.getString(7));
                fila.put("urlFoto",      c.getString(8));
                todosLosDatos.add(fila);
            } while (c.moveToNext());
        }
        c.close();

        adapter = new ProductoAdapter(this, todosLosDatos);
        lista.setAdapter(adapter);
    }

    private void filtrar(String texto) {
        ArrayList<HashMap<String, String>> filtrados = new ArrayList<>();
        for (HashMap<String, String> fila : todosLosDatos) {
            if (fila.get("descripcion").toLowerCase().contains(texto.toLowerCase()) ||
                    fila.get("codigo").toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(fila);
            }
        }
        adapter = new ProductoAdapter(this, filtrados);
        lista.setAdapter(adapter);
    }

    private void confirmarEliminacion(HashMap<String, String> item) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setMessage("¿Eliminar " + item.get("descripcion") + "?")
                .setPositiveButton("Sí", (d, w) -> {
                    String[] datos = {item.get("idProducto")};
                    if (db.administrar_Productos("eliminar", datos).equals("ok")) {
                        Toast.makeText(this, "Eliminado localmente", Toast.LENGTH_SHORT).show();
                        cargarLista();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }
}