package com.example.gestionfernando;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRestauranteActivity extends AppCompatActivity {

    private EditText editNombre, editDescripcion, editDireccionWeb, editTelefono, editFechaUltimaVisita;
    private RatingBar editRating;
    private ImageView imagePreview;
    private String selectedImage; // Cambiado a String para admitir imágenes de galería
    private SimpleDateFormat dateFormat;
    private DatabaseHelper databaseHelper;

    // Selector de imagen de la galería
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    selectedImage = imageUri.toString();
                    imagePreview.setImageURI(imageUri);
                }
            });

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

        // Imagen predeterminada
        selectedImage = String.valueOf(R.drawable.restaurante1);
        imagePreview.setImageResource(R.drawable.restaurante1);

        // Seleccionar imagen desde la galería
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Seleccionar la fecha de última visita
        editFechaUltimaVisita.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddRestauranteActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        editFechaUltimaVisita.setText(fecha);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Guardar el restaurante
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
                        selectedImage, // Ahora es String, admite imágenes de galería o recursos
                        direccionWeb,
                        telefono,
                        false, // Inicialmente no favorito
                        puntuacion,
                        dateFormat.parse(fechaUltimaVisita)
                );

                databaseHelper.insertarRestaurante(nuevoRestaurante);
                Toast.makeText(this, "Restaurante añadido correctamente", Toast.LENGTH_SHORT).show();
                finish();

            } catch (ParseException e) {
                Toast.makeText(this, "Error al parsear la fecha", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
