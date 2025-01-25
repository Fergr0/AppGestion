package com.example.gestionfernando;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        TextView textViewInfo = findViewById(R.id.textViewInfo);

        // Leer el archivo de texto desde res/raw
        StringBuilder texto = new StringBuilder();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.informacion);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            while ((linea = reader.readLine()) != null) {
                texto.append(linea).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            texto.append("Error al leer el archivo.");
        }

        // Mostrar el contenido del archivo en el TextView
        textViewInfo.setText(texto.toString());
    }
}
