package com.example.felipe.proyectobarco.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Conection  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "barco";

    String queryCuenta = " create table cuenta(usuario TEXT PRIMARY KEY,password TEXT); ";
    String querySesion = "create table sesion(codigo INTEGER PRIMARY KEY,usuario TEXT,fecha TEXT, hora TEXT)";
    String queryActividad = "create table actividad(codigo INTEGER PRIMARY KEY,codigo_sesion TEXT,control TEXT)";

    public Conection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryCuenta);
        db.execSQL(querySesion);
        db.execSQL(queryActividad);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void BD_backup() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        final String inFileName = "/data/data/com.example.felipe.proyectobarco/databases/"+DATABASE_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = null;

        fis = new FileInputStream(dbFile);

        //String directorio = obtenerDirectorioCopias();
        String directorio = "/storage/sdcard0/Download/";
        File d = new File(directorio);
        if (!d.exists()) {
            d.mkdir();
        }
        String outFileName = directorio + "/"+DATABASE_NAME + "_"+timeStamp;

        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        fis.close();
    }


}
