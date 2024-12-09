package com.example.gestionfernando;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public ListView lista;
    private ArrayList<Restaurante> restaurantes;
    private MiApatador miAdaptador;

    // Constante para la solicitud de edición
    private static final int REQUEST_CODE_EDITAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cambiar la configuración del formato de fecha para español
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES")); // Utilizando Locale en español

        // Inicializar el ArrayList de restaurantes
        restaurantes = new ArrayList<>();
        try {
            restaurantes.add(new Restaurante("Restaurante 1", "Este es el primer restaurante", R.drawable.restaurante1, "restau.com", "999 99 99 99", true, 1, sdf.parse("12 Mayo 2024")));
            restaurantes.add(new Restaurante("Restaurante 2", "Este es el segundo restaurante", R.drawable.restaurante1, "restau2.com", "777 77 77 77", false, 2, sdf.parse("5 Junio 2024")));
            restaurantes.add(new Restaurante("Restaurante 3", "Este es el tercer restaurante", R.drawable.restaurante1, "restau3.com", "123 45 67 89", true, 7, sdf.parse("21 Octubre 2024")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Obtener el ListView desde el layout
        lista = findViewById(R.id.lista);

        // Crear el adaptador para la lista, pasando el ArrayList convertido a array
        miAdaptador = new MiApatador(this, restaurantes);
        lista.setAdapter(miAdaptador);

        // Registrar el ListView para el menú contextual
        registerForContextMenu(lista);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contextual, menu); // Inflamos el menú
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Restaurante restauranteSeleccionado = restaurantes.get(info.position);

        // Acción para la opción de editar
        if (item.getItemId() == R.id.context_edit_restaurante) {
            Intent intent = new Intent(MainActivity.this, EditRestauranteActivity.class);
            intent.putExtra("restaurante", restauranteSeleccionado); // Pasar el restaurante seleccionado
            intent.putExtra("posicion", info.position); // Pasar la posición para actualizarla luego
            startActivityForResult(intent, REQUEST_CODE_EDITAR); // Usar un requestCode diferente para edición
            return true;
        }
        // Acción para la opción de eliminar
        else if (item.getItemId() == R.id.context_delete_restaurante) {
            restauranteSeleccionado = restaurantes.get(info.position);
            restaurantes.remove(info.position);
            miAdaptador.notifyDataSetChanged();

            // Crear y mostrar el Toast personalizado
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_mio, findViewById(R.id.toast_root));

            ImageView toastImage = layout.findViewById(R.id.toast_image);
            TextView toastText = layout.findViewById(R.id.toast_text);

            toastImage.setImageResource(R.drawable.ic_delete);
            toastText.setText("Restaurante eliminado: " + restauranteSeleccionado.getNombre());

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Asegúrate de que el resultado sea OK y que los datos no sean nulos
        if (resultCode == RESULT_OK && data != null) {
            // Obtener el restaurante actualizado desde el Intent
            Restaurante restauranteNuevo = (Restaurante) data.getSerializableExtra("restaurante");

            if (restauranteNuevo != null) {
                // Agregar el nuevo restaurante a la lista
                restaurantes.add(restauranteNuevo);  // Añadir el restaurante a la lista
                miAdaptador.notifyDataSetChanged();  // Notificar al adaptador para actualizar la vista

                // Mostrar un mensaje confirmando la adición
                Toast.makeText(this, "Restaurante añadido", Toast.LENGTH_SHORT).show();
            }else{
                Log.d("MainActivity", "No se recibió ningún restaurante");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_restaurante) {
            Intent intent = new Intent(MainActivity.this, AddRestauranteActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
