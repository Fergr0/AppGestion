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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    private ArrayList<Restaurante> restaurantes;
    private MiApatador miAdaptador;
    private DatabaseHelper databaseHelper;

    private static final int REQUEST_CODE_EDITAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos
        databaseHelper = new DatabaseHelper(this);

        // Cargar los restaurantes desde la base de datos
        restaurantes = databaseHelper.obtenerRestaurantes();

        // Configurar el ListView y el adaptador
        lista = findViewById(R.id.lista);
        miAdaptador = new MiApatador(this, restaurantes);
        lista.setAdapter(miAdaptador);

        // Registrar el menú contextual
        registerForContextMenu(lista);

        // Configuración del botón flotante
        FloatingActionButton btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AcercaDeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Restaurante restauranteNuevo = (Restaurante) data.getSerializableExtra("restaurante");

            if (restauranteNuevo != null) {
                if (requestCode == 1) {  // Nuevo restaurante
                    // Insertar el restaurante en la base de datos y actualizar la lista
                    databaseHelper.insertarRestaurante(restauranteNuevo);
                    restaurantes.add(restauranteNuevo);
                } else if (requestCode == REQUEST_CODE_EDITAR) {  // Editar restaurante
                    int posicion = data.getIntExtra("posicion", -1);
                    if (posicion != -1) {
                        // Actualizar el restaurante en la base de datos y en la lista
                        databaseHelper.actualizarRestaurante(posicion, restauranteNuevo);
                        restaurantes.set(posicion, restauranteNuevo);
                    }
                }
                miAdaptador.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Restaurante restauranteSeleccionado = restaurantes.get(info.position);

        if (item.getItemId() == R.id.context_edit_restaurante) {
            Intent intent = new Intent(MainActivity.this, EditRestauranteActivity.class);
            intent.putExtra("restaurante", restauranteSeleccionado); // Pasar el objeto restaurante
            intent.putExtra("id", restauranteSeleccionado.getId()); // Asegúrate de que Restaurante tiene un ID
            startActivityForResult(intent, REQUEST_CODE_EDITAR);
            return true;
        } else if (item.getItemId() == R.id.context_delete_restaurante) {
            // Eliminar el restaurante de la base de datos usando su ID
            databaseHelper.eliminarRestaurante(restauranteSeleccionado.getId());

            // Eliminar el restaurante de la lista en memoria
            restaurantes.remove(info.position);
            miAdaptador.notifyDataSetChanged();

            // Mostrar un Toast personalizado
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
        }

        return super.onContextItemSelected(item);
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

    @Override
    protected void onResume() {
        super.onResume();

        // Recargar los datos desde la base de datos
        restaurantes.clear(); // Limpiar la lista actual
        restaurantes.addAll(databaseHelper.obtenerRestaurantes()); // Añadir los nuevos datos
        miAdaptador.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }


}
