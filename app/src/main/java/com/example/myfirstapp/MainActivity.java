package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    TextView tempVal;
    Spinner spn;
    Button btn;
    Double valores[] = new Double[] {1.0, 0.92, 7.79, 24.70, 36.80, 528.50, 17.10, 0.79, 149.50, 1.39, 0.88, 7.24};
    Double valoresLongitud[] = new Double[] {1.0, 1000.0, 0.01, 0.001, 1609.34, 0.0254, 0.3048};
    Double valoresVolumen[] = {1.0, 0.001, 3.78541};
    Double valoresmasa[] = new Double[] { 1.0, 0.001, 0.453592, 0.0283495, 1000.0};
    Double valoresAlmacenamiento[] = {1.0, 1024.0, 1048576.0, 1073741824.0, 1099511627776.0, 0.125};
    Double valorTiempo[] = new  Double[] {1.0, 60.0, 3600.0,86400.0,604800.0, 2592000.0, 31536000.0, 0.001  };
    Double valortransferencia[]=new Double[] {1.0, 1000.0, 1000000.0, 1000000000.0, 8.0, 8000.0, 8000000.0 };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tabMonedas).setIndicator("",getDrawable(R.drawable.money_6997728)));
        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tabLongitud).setIndicator("", getDrawable(R.drawable.height_12317278)));
        tbh.addTab(tbh.newTabSpec("Volumen").setContent(R.id.tabVolumen).setIndicator("",getDrawable(R.drawable.production_3516759)));
        tbh.addTab(tbh.newTabSpec("Masa").setContent(R.id.tabMasa).setIndicator("", getDrawable(R.drawable.scale_15168338)));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tabAlmacenamiento).setIndicator("", getDrawable(R.drawable.hosting_542646)));
        tbh.addTab(tbh.newTabSpec("Tiempo").setContent(R.id.tabTiempo).setIndicator("",getDrawable(R.drawable.clock_259699) ));
        tbh.addTab(tbh.newTabSpec("transferencia").setContent(R.id.tabtransferencia).setIndicator("", getDrawable(R.drawable.file_15250160)));




        btn = findViewById(R.id.btnMonedasConvertir);
        btn.setOnClickListener(v->convertirMonedas());
        btn =findViewById(R.id.btnLongitudConvertir);
        btn.setOnClickListener(v->convertirLongitud());
        btn = findViewById(R.id.btnVolumenConvertir);
        btn.setOnClickListener(v->convertirVolumen());
        btn =findViewById(R.id.btnMasaConvertir);
        btn.setOnClickListener(v->convertirMasa());
        btn =findViewById(R.id.btnAlmacenamientoConvertir);
        btn.setOnClickListener(v->convertirAlmacenamiento());
        btn =findViewById(R.id.btnTiempoConvertir);
        btn.setOnClickListener(v->convertirTiempo());
        btn=findViewById(R.id.btntransferenciaConvertir);
        btn.setOnClickListener(v->convertirtransferencia());











    }
    private void convertirMonedas(){
        spn = findViewById(R.id.spnMonedasDe);
        int de = spn.getSelectedItemPosition();

        spn = findViewById(R.id.spnMonedasA);
        int a = spn.getSelectedItemPosition();

        tempVal = findViewById(R.id.txtMonedasCantidad);
        double cantidad = Double.parseDouble(tempVal.getText().toString());
        double respuesta = conversor(de, a, cantidad, valores);
        tempVal = findViewById(R.id.lblMonedasRespuesta);
        tempVal.setText("Respuesta: "+ respuesta);
    }


    private void convertirLongitud(){
        spn = findViewById(R.id.spnLongitudDe);
        int de = spn.getSelectedItemPosition();

        spn = findViewById(R.id.spnLongitudA);
        int a = spn.getSelectedItemPosition();

        tempVal = findViewById(R.id.txtLongitudCantidad);
        double cantidad = Double.parseDouble(tempVal.getText().toString());
        double respuesta = conversor(de, a, cantidad, valoresLongitud);
        tempVal = findViewById(R.id.lblLongitudRespuesta);
        tempVal.setText("Respuesta: "+ respuesta);
    }
    private void convertirVolumen(){
        spn =findViewById(R.id.spnVolumenDe);
                int de =spn.getSelectedItemPosition();
        spn =findViewById(R.id.spnVolumenA);
        int a = spn.getSelectedItemPosition();
        tempVal=findViewById(R.id.txtVolumenCantidad);
        double cantidad = Double.parseDouble(tempVal.getText().toString());
        double respuesta = conversor(de, a, cantidad, valoresVolumen);
        tempVal = findViewById(R.id.lblVolumenRespuesta);
        tempVal.setText("Respuesta:"+respuesta);
    }
    private void convertirMasa(){
    spn =findViewById(R.id.spnMasaDe);
         int de = spn.getSelectedItemPosition();
         spn =findViewById(R.id.spnMasaA);
         int a = spn.getSelectedItemPosition();
         tempVal=findViewById(R.id.txtMasaCantidad);
         double cantidad =Double.parseDouble(tempVal.getText().toString());
         double respuesta = conversor(de,a,cantidad, valoresmasa);
         tempVal =findViewById(R.id.lblMasaRespuesta);
         tempVal.setText("Respuesta:"+respuesta);
    }
    private void convertirAlmacenamiento(){
        spn =findViewById(R.id.spnAlmacenamientoDe);
        int de =spn.getSelectedItemPosition();
        spn =findViewById(R.id.spnAlmacenamientoA);
        int a =spn.getSelectedItemPosition();
        tempVal=findViewById(R.id.txtAlmacenamientoCantidad);
        double cantidad =Double.parseDouble(tempVal.getText().toString());
        double respuesta = conversor(de,a,cantidad,valoresAlmacenamiento);
        tempVal =findViewById(R.id.lblAlmacenamientoRespuesta);
        tempVal.setText("Respuesta"+respuesta);
    }
    private void convertirTiempo(){
        spn = findViewById(R.id.spnTiempoDe);
        int de = spn.getSelectedItemPosition();
        spn = findViewById(R.id.spnTiempoA);
        int a = spn.getSelectedItemPosition();
        tempVal=findViewById(R.id.txtTiempoCantidad);
        double cantidad =Double.parseDouble(tempVal.getText().toString());
        double respuesta = conversor(de,a,cantidad,valorTiempo );
        tempVal=findViewById(R.id.lblTiempoRespuesta);
        tempVal.setText("Respuesta"+respuesta);
    }
    private void convertirtransferencia(){
    spn=findViewById(R.id.spntransferenciaDe);
    int de=spn.getSelectedItemPosition();
    spn=findViewById(R.id.spntransferenciaA);
    int a=spn.getSelectedItemPosition();
    tempVal=findViewById(R.id.txttransferenciaCantidad);
    double cantidad=Double.parseDouble(tempVal.getText().toString());
    double respuesta=conversor(de,a,cantidad,valortransferencia);
    tempVal=findViewById(R.id.lbltransferenciaRespuesta);
    tempVal.setText("Respuesta"+respuesta);
    }

    double conversor(int de, int a, double cantidad, Double[] valoresArray){
        return cantidad * valoresArray[de] / valoresArray[a];
    }



}
