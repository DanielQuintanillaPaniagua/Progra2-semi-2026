package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tempVal;

    Button btn;
    Spinner spn;

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
        spn = findViewById(R.id.cboOpciones);
        switch (spn.getSelectedItemPosition()) {
            case 0: //suma
                respuesta = num1 + num2;
                break;
            case 1: //Resta
                respuesta = num1 - num2;
                break;
            case 2: //Multiplicacion
                respuesta = num1 * num2;
                break;
            case 3: //division
                respuesta = num1 / num2;
                break;
            case 4: //factorial
                respuesta = calcularFactorial(num1.intValue());
                break;
            case 5: //prosentaje
                respuesta = num1 * num2 / 100;
                break;
            case 6://exponente
                respuesta = Math.pow(num1, num2);
                break;
            case 7://raiz
                respuesta = Math.sqrt(num1);
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