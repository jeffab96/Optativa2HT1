package com.example.H2_OPTATIVA_T1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_ESTUDIANTES = "CREATE TABLE estudiantes (nombre text, apellido text, email text, celular text, foto text, genero text, fechaN text, asignaturas text, becado text)";
    final String CREAR_TABLA_USUARIOS = "CREATE TABLE usuarios (usuario text, clave text)";
    final String CREAR_TABLA_LOGS = "CREATE TABLE logs (usuario text, tipo text, tiempo text, nombred text, modelod text, versiond text)";
    public ConexionSQLHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_ESTUDIANTES);
        db.execSQL(CREAR_TABLA_USUARIOS);
        db.execSQL(CREAR_TABLA_LOGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS estudiantes");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS logs");
        onCreate(db);
    }
}
