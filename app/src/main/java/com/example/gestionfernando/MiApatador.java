package com.example.gestionfernando;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MiApatador extends ArrayAdapter<Restaurante> {

    private ArrayList<Restaurante> restaurantes;
    private Context context;

    public MiApatador(Context context, ArrayList<Restaurante> restaurantes) {
        super(context, R.layout.restaurante, restaurantes);
        this.context = context;
        this.restaurantes = restaurantes;
    }

    @NonNull
    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.restaurante, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nombre = convertView.findViewById(R.id.nombre);
            viewHolder.descripcion = convertView.findViewById(R.id.descripcion);
            viewHolder.imagen = convertView.findViewById(R.id.imagen);
            viewHolder.direccionWeb = convertView.findViewById(R.id.direccionweb);
            viewHolder.telefono = convertView.findViewById(R.id.telefono);
            viewHolder.radio = convertView.findViewById(R.id.radio);
            viewHolder.barra = convertView.findViewById(R.id.barra);
            viewHolder.btnMenuContextual = convertView.findViewById(R.id.btnMenuContextual);
            viewHolder.fechaUltimaVisita = convertView.findViewById(R.id.edit_fecha_ultima_visita);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Restaurante restauranteActual = restaurantes.get(posicion);

        viewHolder.nombre.setText(restauranteActual.getNombre());
        viewHolder.descripcion.setText(restauranteActual.getDescripcion());
        viewHolder.direccionWeb.setText(restauranteActual.getDireccionWeb());
        viewHolder.telefono.setText(restauranteActual.getTelefono());
        viewHolder.radio.setChecked(restauranteActual.isEsFavorito());
        viewHolder.barra.setRating(restauranteActual.getPuntuacion());

        // **Manejo correcto de la imagen (Recurso o Ruta)**
        String imagenUrl = restauranteActual.getImagenUrl();
        if (imagenUrl != null) {
            if (imagenUrl.matches("\\d+")) { // Si es un número, es un recurso
                viewHolder.imagen.setImageResource(Integer.parseInt(imagenUrl));
            } else { // Si es una ruta, carga desde el almacenamiento
                viewHolder.imagen.setImageURI(Uri.parse(imagenUrl));
            }
        } else {
            viewHolder.imagen.setImageResource(R.drawable.placeholder_image); // Imagen por defecto
        }

        // **Formato de fecha**
        if (restauranteActual.getFechaUltimaVisita() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            viewHolder.fechaUltimaVisita.setText(sdf.format(restauranteActual.getFechaUltimaVisita()));
        } else {
            viewHolder.fechaUltimaVisita.setText("Fecha no disponible");
        }

        // **Configuración del botón de menú contextual**
        viewHolder.btnMenuContextual.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                v.setTag(posicion);
                ((MainActivity) context).lista.showContextMenuForChild(v);
            } else {
                Toast.makeText(context, "Error al abrir el menú", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    // **ViewHolder para mejorar rendimiento**
    static class ViewHolder {
        TextView nombre;
        TextView descripcion;
        ImageView imagen;
        TextView direccionWeb;
        TextView telefono;
        RadioButton radio;
        RatingBar barra;
        TextView fechaUltimaVisita;
        Button btnMenuContextual;
    }
}
