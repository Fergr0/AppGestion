package com.example.gestionfernando;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

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

        // Usa un ViewHolder para optimizar la lista
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
            viewHolder.fechaUltimaVisita = convertView.findViewById(R.id.edit_fecha_ultima_visita); // Nueva referencia

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Obtén el restaurante actual
        Restaurante restauranteActual = restaurantes.get(posicion);

        // Asignar valores a las vistas
        viewHolder.nombre.setText(restauranteActual.getNombre());
        viewHolder.descripcion.setText(restauranteActual.getDescripcion());
        viewHolder.imagen.setImageResource(restauranteActual.getImagenUrl());
        viewHolder.direccionWeb.setText(restauranteActual.getDireccionWeb());
        viewHolder.telefono.setText(restauranteActual.getTelefono());
        viewHolder.radio.setChecked(restauranteActual.isEsFavorito());
        viewHolder.barra.setRating(restauranteActual.getPuntuacion());

        // Formatear la fecha de última visita
        if (restauranteActual.getFechaUltimaVisita() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()); // Formato de fecha
            String fechaFormateada = sdf.format(restauranteActual.getFechaUltimaVisita());
            viewHolder.fechaUltimaVisita.setText(fechaFormateada);
        } else {
            viewHolder.fechaUltimaVisita.setText("Fecha no disponible");
        }

        // Configurar el botón para abrir el menú contextual
        viewHolder.btnMenuContextual.setOnClickListener(v -> {
            // Llamar al menú contextual desde el botón
            v.setTag(posicion); // Asignar la posición del restaurante como tag
            ((MainActivity) context).lista.showContextMenuForChild(v); // Mostrar el menú contextual
        });

        return convertView;
    }

    // Clase ViewHolder para optimización
    static class ViewHolder {
        TextView nombre;
        TextView descripcion;
        ImageView imagen;
        TextView direccionWeb;
        TextView telefono;
        RadioButton radio;
        RatingBar barra;
        TextView fechaUltimaVisita; // Nueva vista para la fecha
        Button btnMenuContextual;
    }
}
