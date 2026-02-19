package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Spinner spn;
    Button btn;
    Double valores[][] = {
            {1.0, 0.92, 7.79, 24.70, 36.80, 528.50, 17.10, 0.79, 149.50, 1.39, 0.88, 7.24},//moendas
            {1.0, 0.001, 100.0, 1000.0, 0.000621371, 39.3701, 3.28084},//longitud
            {1.0, 1000.0, 0.264172},//volumen
            {1.0, 1000.0, 2.20462, 35.274, 0.001},//masa
            {1.0, 0.0009765625, 0.000000953674316, 0.000000000931322, 0.000000000000909495, 8.0}  ,                //almacenamiento
            {1.0, 0.0166667, 0.000277778, 0.000011574, 0.000001653, 0.000000385, 0.0000000317, 1000.0},//tiempo
            {1.0, 0.001, 0.000001, 0.000000001, 0.125, 0.000125, 0.000000125}//transferencia
    };
    String[][] etiquetas = {
            {"Dolar", "Euro", "Quetzal", "Lempira", "Cordoba", "Colon CR", " Peso Mexicano", " Libra Esterlina", "Yen Japonés ", " Dólar Canadiense", "Franco Suizo", "Yuan Chino "}, //monedas
            {"Metro", "Kilómetro", "Centímetro", "Milímetro", "Milla", "Pulgada", "Pie"}, //Longitud
            {"Litro", "Mililitro", "Galón"},  //volumen
            {"Kilogramo", "Gramo", "Libra", "Onza", "Tonelada"}, //masa
            {"Byte", "Kilobyte ", "Megabyte", "Gigabyte", "Terabyte", "Bit"},//almacenamiento
            {"Segundo", "Minuto", "Hora", "Día", "Semana", "Mes", "Año", "Milisegundo"}, //tiempo
            {"bps", "Kbps", "Mbps", "Gbps", "Bps", "KBps ", "MBps "}//transferiencia
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnConvertir);
        btn.setOnClickListener(v -> convertir());

        cambiarEtiqueta(0);//valores predeterminaods

        spn = findViewById(R.id.spnTipo);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cambiarEtiqueta(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void cambiarEtiqueta(int posicion) {
        ArrayAdapter<String> aaEtiquetas = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                etiquetas[posicion]
        );
        aaEtiquetas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn = findViewById(R.id.spnDe);
        spn.setAdapter(aaEtiquetas);

        spn = findViewById(R.id.spnA);
        spn.setAdapter(aaEtiquetas);
    }

    private void convertir() {
        spn = findViewById(R.id.spnTipo);
        int tipo = spn.getSelectedItemPosition();

        spn = findViewById(R.id.spnDe);
        int de = spn.getSelectedItemPosition();

        spn = findViewById(R.id.spnA);
        int a = spn.getSelectedItemPosition();

        tempVal = findViewById(R.id.txtCantidad);
        double cantidad = Double.parseDouble(tempVal.getText().toString());
        double respuesta = conversor(tipo, de, a, cantidad);

        tempVal = findViewById(R.id.lblRespuesta);
        tempVal.setText("Respuesta: " + respuesta);
    }

    double conversor(int tipo, int de, int a, double cantidad) {
        return cantidad * (valores[tipo][a] / valores[tipo][de]);
    }
}


