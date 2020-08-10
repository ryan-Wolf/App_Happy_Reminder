package com.example.apphappyreminderz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    //CONSTRUCTOR

    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //INSERTAR

    public void insertData(String apellido, String nombre, String telefono, String fechaNac, String edad, String mensaje, String fechaNot, String tiempo, byte[] image) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO RECORDV2 VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";//10
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, apellido);
        statement.bindString(2, nombre);
        statement.bindString(3, telefono);
        statement.bindString(4, fechaNac);
        statement.bindString(5, edad);
        statement.bindString(6, mensaje);
        statement.bindString(7, fechaNot);
        statement.bindString(8, tiempo);
        statement.bindBlob(9, image);

        statement.executeInsert();
    }

    //ACTUALIZAR

    public void updateData(String apellido, String nombre, String telefono, String fechaNac, String edad, String mensaje, String fechaNot, String tiempo, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE RECORDV2 SET apellido=?, nombre=?, telefono=?, fechaNac=?, edad=?, mensaje=?, fechaNot=?, tiempo=?, image=? WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, apellido);
        statement.bindString(2, nombre);
        statement.bindString(3, telefono);
        statement.bindString(4, fechaNac);
        statement.bindString(5, edad);
        statement.bindString(6, mensaje);
        statement.bindString(7, fechaNot);
        statement.bindString(8, tiempo);
        statement.bindBlob(9, image);
        statement.bindDouble(10, id);

        statement.execute();
        database.close();
    }

    //ELIMINAR

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM RECORDV2 WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double) id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
