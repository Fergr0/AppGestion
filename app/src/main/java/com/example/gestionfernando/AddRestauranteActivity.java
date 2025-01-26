package com.example.gestionfernando;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRestauranteActivity extends AppCompatActivity {

    private EditText editNombre, editDescripcion, editDireccionWeb, editTelefono, editFechaUltimaVisita;
    private RatingBar editRating;
    private ImageView imagePreview;
    private int selectedImage = R.drawable.restaurante1; // Imagen predeterminada
    private SimpleDateFormat dateFormat;
    private DatabaseHelper databaseHelper; // Base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurante);

        // Inicializar el formato de fecha y la base de datos
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        databaseHelper = new DatabaseHelper(this);

        // Referencias a los elementos del diseño
        editNombre = findViewById(R.id.edit_nombre);
        editDescripcion = findViewById(R.id.edit_descripcion);
        editDireccionWeb = findViewById(R.id.edit_direccion_web);
        editTelefono = findViewById(R.id.edit_telefono);
        editRating = findViewById(R.id.edit_rating);
        imagePreview = findViewById(R.id.image_preview);
        editFechaUltimaVisita = findViewById(R.id.edit_fecha_ultima_visita);

        Button btnSelectImage = findViewById(R.id.btn_select_image);
        Button btnSave = findViewById(R.id.btn_save);

        // Listener para seleccionar una imagen
        btnSelectImage.setOnClickListener(v -> {
            selectedImage = R.drawable.restaurante1; // Cambiar imagen predeterminada
            imagePreview.setImageResource(selectedImage);
            Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
        });

        // Listener para seleccionar la fecha de última visita
        editFechaUltimaVisita.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddRestauranteActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                        editFechaUltimaVisita.setText(fecha);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Listener para guardar el restaurante
        btnSave.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String descripcion = editDescripcion.getText().toString().trim();
            String direccionWeb = editDireccionWeb.getText().toString().trim();
            String telefono = editTelefono.getText().toString().trim();
            float puntuacion = editRating.getRating();
            String fechaUltimaVisita = editFechaUltimaVisita.getText().toString().trim();

            // Validación de campos vacíos
            if (nombre.isEmpty() || descripcion.isEmpty() || direccionWeb.isEmpty() || telefono.isEmpty() || fechaUltimaVisita.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar el formato de la fecha
            if (!fechaUltimaVisita.matches("\\d{2}/\\d{2}/\\d{4}")) {
                Toast.makeText(this, "Formato de fecha inválido. Usa dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insertar en la base de datos
            try {
                Restaurante nuevoRestaurante = new Restaurante(
                        nombre,
                        descripcion,
                        selectedImage,
                        direccionWeb,
                        telefono,
                        false, // Inicialmente no favorito
                        puntuacion,
                        dateFormat.parse(fechaUltimaVisita) // Fecha como Date
                );

                databaseHelper.insertarRestaurante(nuevoRestaurante); // Guardar en SQLite
                Toast.makeText(this, "Restaurante añadido correctamente", Toast.LENGTH_SHORT).show();
                finish(); // Finalizar actividad

            } catch (ParseException e) {
                Toast.makeText(this, "Error al parsear la fecha", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

