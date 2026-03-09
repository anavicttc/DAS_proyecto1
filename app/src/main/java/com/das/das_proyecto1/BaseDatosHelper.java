package com.das.das_proyecto1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MenuSemanal.db";
    private static final int DATABASE_VERSION = 3;

    //tabla menú semanal:
    public static final String TABLE_MENU = "menu";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DIA = "dia";
    public static final String COLUMN_DESAYUNO = "desayuno";
    public static final String COLUMN_ALMUERZO = "almuerzo";
    public static final String COLUMN_COMIDA = "comida";
    public static final String COLUMN_MERIENDA = "merienda";
    public static final String COLUMN_CENA = "cena";
    //tabla lista de la compra
    public static final String TABLE_COMPRA = "lista_compra";
    public static final String COLUMN_COMPRA_ID = "id_compra";
    public static final String COLUMN_COMPRA_NOMBRE = "nombre_producto";
    //tabla plato
    public static final String TABLE_PLATOS = "platos";
    public static final String COLUMN_PLATO_ID = "id_plato";
    public static final String COLUMN_PLATO_NOMBRE = "nombre_plato";
    public static final String COLUMN_PLATO_DESC = "descripcion_plato";
    public static final String COLUMN_PLATO_FOTO = "foto_plato";

    public BaseDatosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        //creamos las tres tablas:
        //menú
        bd.execSQL("CREATE TABLE " + TABLE_MENU + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DIA + " TEXT, " + COLUMN_DESAYUNO + " TEXT, " +
                COLUMN_ALMUERZO + " TEXT, " + COLUMN_COMIDA + " TEXT, " +
                COLUMN_MERIENDA + " TEXT, " + COLUMN_CENA + " TEXT);");
        //lista compra
        bd.execSQL("CREATE TABLE " + TABLE_COMPRA + " (" +
                COLUMN_COMPRA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COMPRA_NOMBRE + " TEXT);");
        //platos
        bd.execSQL("CREATE TABLE " + TABLE_PLATOS + " (" +
                COLUMN_PLATO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLATO_NOMBRE + " TEXT, " +
                COLUMN_PLATO_DESC + " TEXT, " +
                COLUMN_PLATO_FOTO + " TEXT);");
        insertarDias(bd); //precarga
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_COMPRA);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PLATOS);
        onCreate(db);
    }

    //insertar 7 días por defecto
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

    public int actualizarDia(Dia dia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESAYUNO, dia.getDesayuno());
        values.put(COLUMN_ALMUERZO, dia.getAlmuerzo());
        values.put(COLUMN_COMIDA, dia.getComida());
        values.put(COLUMN_MERIENDA, dia.getMerienda());
        values.put(COLUMN_CENA, dia.getCena());

        //actualizar fila donde el id coincida
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
    //insertar producto a la lista de la compra:
    public void insertarProducto(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPRA_NOMBRE, nombre);
        db.insert(TABLE_COMPRA, null, values);
    }
    public List<Producto> obtenerCompra() {
        List<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPRA, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Producto(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public void borrarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Borramos donde el ID coincida
        db.delete(TABLE_COMPRA, COLUMN_COMPRA_ID + " = ?", new String[]{String.valueOf(id)});
    }
    //PLATOS:
    public void insertarPlato(String nombre, String descripcion, String fotoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLATO_NOMBRE, nombre);
        values.put(COLUMN_PLATO_DESC, descripcion);
        values.put(COLUMN_PLATO_FOTO, fotoUri);
        db.insert(TABLE_PLATOS, null, values);
    }
    public List<Plato> obtenerPlatos() {
        List<Plato> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLATOS, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Plato(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}