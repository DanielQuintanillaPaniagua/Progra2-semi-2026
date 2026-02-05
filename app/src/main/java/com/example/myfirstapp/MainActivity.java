package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tempVal;

    Button btn;
    RadioButton opt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(v -> calcular());
    }

    private void calcular() {
        tempVal = findViewById(R.id.txtNum1);
        Double num1 = Double.parseDouble(tempVal.getText().toString());

        tempVal = findViewById(R.id.textNume2);
        Double num2 = Double.parseDouble(tempVal.getText().toString());
        double respuesta = 0;

        opt = findViewById(R.id.optsuma);
        if (opt.isChecked()) {
            respuesta = num1 + num2;
        }

        opt = findViewById(R.id.optResta);
        if (opt.isChecked()) {
            respuesta = num1 - num2;
        }

        opt = findViewById(R.id.optMultiplicar);
        if (opt.isChecked()) {
            respuesta = num1 * num2;
        }

        opt = findViewById(R.id.optDividir);
        if (opt.isChecked()) {
            respuesta = num1 / num2;
        }

        opt = findViewById(R.id.optFactorial);
        if (opt.isChecked()) {
            respuesta = calcularFactorial(num1.intValue());
        }
        opt = findViewById(R.id.optPorcentaje);
        if (opt.isChecked()) {
            respuesta = num1 * num2 / 100;
        }

        opt = findViewById(R.id.optExponente);
        if (opt.isChecked()) {
            respuesta = Math.pow(num1, num2);
        }
      opt=findViewById(R.id.optRaiz);
        if (opt.isChecked()) {
            respuesta=Math.sqrt(num1);
        }

        tempVal = findViewById(R.id.lblRespuesta);
        tempVal.setText("Respuesta: " + respuesta);
    }


    private long calcularFactorial(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 0 || n == 1) {
            return 1;
        }

        long factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
}