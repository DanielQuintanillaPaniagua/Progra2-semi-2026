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
            {1.0, 0.92, 7.79, 24.70, 36.80, 528.50, 17.10, 0.79, 149.50, 1.39, 0.88, 7.24}, //moendas
            {1.0, 1000.0, 0.01, 0.001, 1609.34, 0.0254, 0.3048}, //longitud
            {1.0, 0.001, 3.78541}, //volumen
            { 1.0, 0.001, 0.453592, 0.0283495, 1000.0},//masa
            {1.0, 1024.0, 1048576.0, 1073741824.0, 1099511627776.0, 0.125} ,//almacenamiento
            {1.0, 60.0, 3600.0, 86400.0, 604800.0, 2592000.0, 31536000.0, 0.001},//tiempo
            {1.0, 1000.0, 1000000.0, 1000000000.0, 8.0, 8000.0, 8000000.0 } //transferencia
    };
    String[][] etiquetas = {
            {"Dolar", "Euro", "Quetzal", "Lempira", "Cordoba", "Colon CR"," Peso Mexicano"," Libra Esterlina","Yen Japonés "," Dólar Canadiense","Franco Suizo","Yuan Chino "}, //monedas
            {"Metro","Kilómetro","Centímetro","Milímetro","Milla","Pulgada","Pie"}, //Longitud
            {"Litro","Mililitro","Galón"},  //volumen
            {"Kilogramo","Gramo","Libra","Onza","Tonelada"}, //masa
            {"Byte","Kilobyte ","Megabyte","Gigabyte","Terabyte","Bit"},//almacenamiento
            {"Segundo","Minuto","Hora","Día","Semana","Mes","Año","Milisegundo"}, //tiempo
            {"bps","Kbps","Mbps","Gbps","Bps","KBps ","MBps "}//transferiencia
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnConvertir);
        btn.setOnClickListener(v->convertir());

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
    private void cambiarEtiqueta(int posicion){
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
    private void convertir(){
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
        tempVal.setText("Respuesta: "+ respuesta);
    }
    double conversor(int tipo, int de, int a, double cantidad){
        return valores[tipo][de]/valores[tipo][a] * cantidad;
    }
    }
