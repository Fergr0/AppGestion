package com.example.gestionfernando;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurantes.db";
    private static final int DATABASE_VERSION = 2; // ¡Aumenta la versión para activar onUpgrade()!

    // Nombre de la tabla y columnas
    private static final String TABLE_RESTAURANTES = "restaurantes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_IMAGEN = "imagen"; // Ahora es STRING
    private static final String COLUMN_DIRECCION_WEB = "direccion_web";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_ES_FAVORITO = "es_favorito";
    private static final String COLUMN_PUNTUACION = "puntuacion";
    private static final String COLUMN_FECHA_VISITA = "fecha_visita";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla con COLUMN_IMAGEN como TEXT
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RESTAURANTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_DESCRIPCION + " TEXT,"
                + COLUMN_IMAGEN + " TEXT," // Cambiado de INTEGER a TEXT
                + COLUMN_DIRECCION_WEB + " TEXT,"
                + COLUMN_TELEFONO + " TEXT,"
                + COLUMN_ES_FAVORITO + " INTEGER,"
                + COLUMN_PUNTUACION + " REAL,"
                + COLUMN_FECHA_VISITA + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Verificar si la columna ya existe antes de intentar agregarla
            if (!columnExists(db, TABLE_RESTAURANTES, COLUMN_IMAGEN)) {
                db.execSQL("ALTER TABLE " + TABLE_RESTAURANTES + " ADD COLUMN " + COLUMN_IMAGEN + " TEXT;");
            }
        }
    }

    /**
     * Metodo para verificar si una columna ya existe en la base de datos.
     */
    private boolean columnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean exists = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            int nameIndex = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                if (name.equals(columnName)) {
                    exists = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exists;
    }


    // Insertar restaurante
    public void insertarRestaurante(Restaurante restaurante) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, restaurante.getNombre());
        values.put(COLUMN_DESCRIPCION, restaurante.getDescripcion());
        values.put(COLUMN_IMAGEN, restaurante.getImagenUrl()); // Ahora almacena como STRING
        values.put(COLUMN_DIRECCION_WEB, restaurante.getDireccionWeb());
        values.put(COLUMN_TELEFONO, restaurante.getTelefono());
        values.put(COLUMN_ES_FAVORITO, restaurante.isEsFavorito() ? 1 : 0);
        values.put(COLUMN_PUNTUACION, restaurante.getPuntuacion());
        values.put(COLUMN_FECHA_VISITA, restaurante.getFechaUltimaVisita() != null
                ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(restaurante.getFechaUltimaVisita())
                : null);

        db.insert(TABLE_RESTAURANTES, null, values);
        db.close();
    }

    // Obtener todos los restaurantes
    public ArrayList<Restaurante> obtenerRestaurantes() {
        ArrayList<Restaurante> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESTAURANTES, null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (cursor.moveToFirst()) {
            do {
                Date fecha = null;
                try {
                    String fechaTexto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_VISITA));
                    if (fechaTexto != null && !fechaTexto.isEmpty()) {
                        fecha = dateFormat.parse(fechaTexto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Restaurante restaurante = new Restaurante(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN)), // Ahora es String
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION_WEB)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ES_FAVORITO)) == 1,
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PUNTUACION)),
                        fecha
                );

                lista.add(restaurante);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Eliminar restaurante
    public void eliminarRestaurante(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Actualizar restaurante
    public void actualizarRestaurante(int id, Restaurante restaurante) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOMBRE, restaurante.getNombre());
        values.put(COLUMN_DESCRIPCION, restaurante.getDescripcion());
        values.put(COLUMN_IMAGEN, restaurante.getImagenUrl()); // Ahora almacena como STRING
        values.put(COLUMN_DIRECCION_WEB, restaurante.getDireccionWeb());
        values.put(COLUMN_TELEFONO, restaurante.getTelefono());
        values.put(COLUMN_ES_FAVORITO, restaurante.isEsFavorito() ? 1 : 0);
        values.put(COLUMN_PUNTUACION, restaurante.getPuntuacion());
        values.put(COLUMN_FECHA_VISITA, restaurante.getFechaUltimaVisita() != null
                ? new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(restaurante.getFechaUltimaVisita())
                : null);

        db.update(TABLE_RESTAURANTES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
