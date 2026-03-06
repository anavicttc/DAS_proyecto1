package com.das.das_proyecto1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosHelper extends SQLiteOpenHelper {

    // Información de la base de datos
    private static final String DATABASE_NAME = "MenuSemanal.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    public static final String TABLE_MENU = "menu";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DIA = "dia";
    public static final String COLUMN_DESAYUNO = "desayuno";
    public static final String COLUMN_ALMUERZO = "almuerzo";
    public static final String COLUMN_COMIDA = "comida";
    public static final String COLUMN_MERIENDA = "merienda";
    public static final String COLUMN_CENA = "cena";

    // Sentencia SQL para crear la tabla
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_MENU + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DIA + " TEXT, " +
                    COLUMN_DESAYUNO + " TEXT, " +
                    COLUMN_ALMUERZO + " TEXT, " +
                    COLUMN_COMIDA + " TEXT, " +
                    COLUMN_MERIENDA + " TEXT, " +
                    COLUMN_CENA + " TEXT" + ");";

    public BaseDatosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL(TABLE_CREATE); // Crear tabla
        insertarDias(bd); // Pre-cargar los 7 días
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }

    // Método para insertar los 7 días por defecto al instalar la app
    private void insertarDias(SQLiteDatabase db) {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (String dia : dias) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DIA, dia);
            values.put(COLUMN_DESAYUNO, "");
            values.put(COLUMN_ALMUERZO, "");
            values.put(COLUMN_COMIDA, "");
            values.put(COLUMN_MERIENDA, "");
            values.put(COLUMN_CENA, "");
            db.insert(TABLE_MENU, null, values);
        }
    }

    // MÉTODO OBLIGATORIO: Modificar (Update)
    public int actualizarDia(Dia dia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESAYUNO, dia.getDesayuno());
        values.put(COLUMN_ALMUERZO, dia.getAlmuerzo());
        values.put(COLUMN_COMIDA, dia.getComida());
        values.put(COLUMN_MERIENDA, dia.getMerienda());
        values.put(COLUMN_CENA, dia.getCena());

        // Actualizar fila donde el ID coincida
        return db.update(TABLE_MENU, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(dia.getId())});
    }

    public List<Dia> obtenerMenu() {
        List<Dia> listaDias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null);

        if (cursor.moveToFirst()) {
            do {
                Dia dia = new Dia();
                dia.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                dia.setNombreDia(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIA)));
                dia.setDesayuno(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESAYUNO)));
                dia.setAlmuerzo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALMUERZO)));
                dia.setComida(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMIDA)));
                dia.setMerienda(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MERIENDA)));
                dia.setCena(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CENA)));
                listaDias.add(dia);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaDias;
    }
}