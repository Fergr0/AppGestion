package com.example.gestionfernando;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditRestauranteActivity extends AppCompatActivity {

    private EditText nombre, descripcion, direccionWeb, telefono, editFechaUltimaVisita;
    private RadioButton esFavorito;
    private RatingBar puntuacion;
    private Button btnGuardar;
    private ImageView imagen;
    private Restaurante restaurante;
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurante);

        // Inicializar vistas
        nombre = findViewById(R.id.editNombre);
        descripcion = findViewById(R.id.editDescripcion);
        direccionWeb = findViewById(R.id.editDireccionWeb);
        telefono = findViewById(R.id.editTelefono);
        editFechaUltimaVisita = findViewById(R.id.edit_fecha_ultima_visita);
        esFavorito = findViewById(R.id.editFavorito);
        puntuacion = findViewById(R.id.editPuntuacion);
        btnGuardar = findViewById(R.id.btnGuardar);
        imagen = findViewById(R.id.editImagen);

        // Obtener el restaurante y su posición
        restaurante = (Restaurante) getIntent().getSerializableExtra("restaurante");
        posicion = getIntent().getIntExtra("posicion", -1);

        if (restaurante != null) {
            // Rellenar los campos con los datos del restaurante
            nombre.setText(restaurante.getNombre());
            descripcion.setText(restaurante.getDescripcion());
            direccionWeb.setText(restaurante.getDireccionWeb());
            telefono.setText(restaurante.getTelefono());
            imagen.setImageResource(restaurante.getImagenUrl());
            esFavorito.setChecked(restaurante.isEsFavorito());
            puntuacion.setRating(restaurante.getPuntuacion());

            // Formatear la fecha antes de mostrarla
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String fechaFormateada = sdf.format(restaurante.getFechaUltimaVisita());
            editFechaUltimaVisita.setText(fechaFormateada);
        }

        // Configurar el DatePicker para la fecha
        editFechaUltimaVisita.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditRestauranteActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Establecer la fecha seleccionada en el EditText
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                        String fechaSeleccionada = sdf.format(calendar.getTime());
                        editFechaUltimaVisita.setText(fechaSeleccionada);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Configurar el botón guardar
        btnGuardar.setOnClickListener(v -> {
            // Actualizar los datos del restaurante
            restaurante.setNombre(nombre.getText().toString());
            restaurante.setDescripcion(descripcion.getText().toString());
            restaurante.setDireccionWeb(direccionWeb.getText().toString());
            restaurante.setTelefono(telefono.getText().toString());
            restaurante.setEsFavorito(esFavorito.isChecked());
            restaurante.setPuntuacion(puntuacion.getRating());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                Date fechaUltimaVisita = sdf.parse(editFechaUltimaVisita.getText().toString());
                restaurante.setFechaUltimaVisita(fechaUltimaVisita);
            } catch (Exception e) {
                Toast.makeText(EditRestauranteActivity.this, "Fecha inválida", Toast.LENGTH_SHORT).show();
            }

            // Devolver los datos a la actividad principal
            Intent resultIntent = new Intent();
            resultIntent.putExtra("restaurante", restaurante);
            resultIntent.putExtra("posicion", posicion);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
