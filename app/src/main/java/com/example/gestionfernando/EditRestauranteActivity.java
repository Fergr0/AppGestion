package com.example.gestionfernando;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditRestauranteActivity extends AppCompatActivity {

    private EditText nombre, descripcion, direccionWeb, telefono, editFechaUltimaVisita;
    private RadioButton esFavorito;
    private RatingBar puntuacion;
    private Button btnGuardar, btnTomarFoto;
    private ImageView imagen;
    private Restaurante restaurante;
    private DatabaseHelper databaseHelper;
    private String currentPhotoPath;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurante);

        databaseHelper = new DatabaseHelper(this);
        nombre = findViewById(R.id.editNombre);
        descripcion = findViewById(R.id.editDescripcion);
        direccionWeb = findViewById(R.id.editDireccionWeb);
        telefono = findViewById(R.id.editTelefono);
        editFechaUltimaVisita = findViewById(R.id.edit_fecha_ultima_visita);
        esFavorito = findViewById(R.id.editFavorito);
        puntuacion = findViewById(R.id.editPuntuacion);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        imagen = findViewById(R.id.editImagen);

        restaurante = (Restaurante) getIntent().getSerializableExtra("restaurante");

        if (restaurante != null) {
            nombre.setText(restaurante.getNombre());
            descripcion.setText(restaurante.getDescripcion());
            direccionWeb.setText(restaurante.getDireccionWeb());
            telefono.setText(restaurante.getTelefono());
            esFavorito.setChecked(restaurante.isEsFavorito());
            puntuacion.setRating(restaurante.getPuntuacion());

            cargarImagen(restaurante.getImagenUrl());

            // Cargar fecha correctamente
            if (restaurante.getFechaUltimaVisita() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                editFechaUltimaVisita.setText(sdf.format(restaurante.getFechaUltimaVisita()));
            }
        }

        btnTomarFoto.setOnClickListener(v -> {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "No hay cámara disponible", Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarImagen(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Uri imageUri;
            if (imagePath.matches("\\d+")) {
                imagen.setImageResource(Integer.parseInt(imagePath));
            } else {
                imageUri = Uri.parse(imagePath);
                imagen.setImageURI(imageUri);
            }
        } else {
            imagen.setImageResource(R.drawable.placeholder_image);
        }
    }

    private void guardarCambios() {
        restaurante.setNombre(nombre.getText().toString());
        restaurante.setDescripcion(descripcion.getText().toString());
        restaurante.setDireccionWeb(direccionWeb.getText().toString());
        restaurante.setTelefono(telefono.getText().toString());
        restaurante.setEsFavorito(esFavorito.isChecked());
        restaurante.setPuntuacion(puntuacion.getRating());

        if (currentPhotoPath != null) {
            restaurante.setImagenUrl(currentPhotoPath);
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fechaUltimaVisita = sdf.parse(editFechaUltimaVisita.getText().toString());
            restaurante.setFechaUltimaVisita(fechaUltimaVisita);

            databaseHelper.actualizarRestaurante(restaurante.getId(), restaurante);
            Toast.makeText(this, "Restaurante actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show();
                return;
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.gestionfernando.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imageFile = new File(currentPhotoPath);
            if (imageFile.exists()) {
                imagen.setImageURI(Uri.fromFile(imageFile));
            } else {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
