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
import java.util.Locale;

public class EditRestauranteActivity extends AppCompatActivity {

    private EditText nombre, descripcion, direccionWeb, telefono, editFechaUltimaVisita;
    private RadioButton esFavorito;
    private RatingBar puntuacion;
    private Button btnGuardar;
    private ImageView imagen;
    private Restaurante restaurante;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurante);

        // Inicializar vistas y base de datos
        databaseHelper = new DatabaseHelper(this);

        nombre = findViewById(R.id.editNombre);
        descripcion = findViewById(R.id.editDescripcion);
        direccionWeb = findViewById(R.id.editDireccionWeb);
        telefono = findViewById(R.id.editTelefono);
        editFechaUltimaVisita = findViewById(R.id.edit_fecha_ultima_visita);
        esFavorito = findViewById(R.id.editFavorito);
        puntuacion = findViewById(R.id.editPuntuacion);
        btnGuardar = findViewById(R.id.btnGuardar);
        imagen = findViewById(R.id.editImagen);

        // Obtener el restaurante desde el intent
        restaurante = (Restaurante) getIntent().getSerializableExtra("restaurante");

        if (restaurante != null) {
            // Rellenar los campos del restaurante
            nombre.setText(restaurante.getNombre());
            descripcion.setText(restaurante.getDescripcion());
            direccionWeb.setText(restaurante.getDireccionWeb());
            telefono.setText(restaurante.getTelefono());
            imagen.setImageResource(restaurante.getImagenUrl());
            esFavorito.setChecked(restaurante.isEsFavorito());
            puntuacion.setRating(restaurante.getPuntuacion());

            // Manejar la fecha de última visita (evitar NullPointerException)
            if (restaurante.getFechaUltimaVisita() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaFormateada = sdf.format(restaurante.getFechaUltimaVisita());
                editFechaUltimaVisita.setText(fechaFormateada);
            } else {
                editFechaUltimaVisita.setText(""); // Dejar vacío si no hay fecha
            }
        }

        // Configurar el DatePicker para la fecha
        editFechaUltimaVisita.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(EditRestauranteActivity.this, (view, year, month, dayOfMonth) -> {
                String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                editFechaUltimaVisita.setText(fechaSeleccionada);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Listener para guardar los cambios
        btnGuardar.setOnClickListener(v -> {
            restaurante.setNombre(nombre.getText().toString());
            restaurante.setDescripcion(descripcion.getText().toString());
            restaurante.setDireccionWeb(direccionWeb.getText().toString());
            restaurante.setTelefono(telefono.getText().toString());
            restaurante.setEsFavorito(esFavorito.isChecked());
            restaurante.setPuntuacion(puntuacion.getRating());

            try {
                // Convertir la fecha de texto a Date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fechaUltimaVisita = sdf.parse(editFechaUltimaVisita.getText().toString());
                restaurante.setFechaUltimaVisita(fechaUltimaVisita);

                // Actualizar el restaurante en la base de datos
                databaseHelper.actualizarRestaurante(restaurante.getId(), restaurante);
                Toast.makeText(this, "Restaurante actualizado correctamente", Toast.LENGTH_SHORT).show();

                // Finalizar la actividad
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
