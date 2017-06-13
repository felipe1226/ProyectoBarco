package com.example.felipe.proyectobarco;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.felipe.proyectobarco.Data.Conection;
import com.example.felipe.proyectobarco.Home;
import com.example.felipe.proyectobarco.R;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class Login extends ActionBarActivity
{

    boolean nuevo = false;
    EditText usuario;
    EditText password;

    Conection con;
    SQLiteDatabase db;

    ContentValues datosCuenta = new ContentValues();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        con = new Conection(this, "barco", null, 1);
        db = con.getWritableDatabase();

        usuario = (EditText)findViewById(R.id.etUsuario);
        password = (EditText)findViewById(R.id.etPass);

        validarDispositivo();
        if(nuevo) {
            String passwdMd5 = "";
            try {
                passwdMd5 = this.toMd5("1234");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            datosCuenta.put("usuario", "admin");
            datosCuenta.put("password", passwdMd5);
            db.insert("cuenta", null, datosCuenta);

            try {
                passwdMd5 = this.toMd5("1116269948");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            datosCuenta.put("usuario", "felipe");
            datosCuenta.put("password", passwdMd5);
            db.insert("cuenta", null, datosCuenta);

            try {
                passwdMd5 = this.toMd5("12345");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            datosCuenta.put("usuario", "jorge");
            datosCuenta.put("password", passwdMd5);
            db.insert("cuenta", null, datosCuenta);

            try {
                passwdMd5 = this.toMd5("ChiquitaBrava");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            datosCuenta.put("usuario", "jennifer");
            datosCuenta.put("password", passwdMd5);
            db.insert("cuenta", null, datosCuenta);

            try {
                passwdMd5 = this.toMd5("lenis");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            datosCuenta.put("usuario", "lenis");
            datosCuenta.put("password", passwdMd5);
            db.insert("cuenta", null, datosCuenta);

            try {
                con.BD_backup();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Ingresar(View v) throws NoSuchAlgorithmException {
        usuario = (EditText)findViewById(R.id.etUsuario);
        String usu = usuario.getText().toString().trim();

        password = (EditText)findViewById(R.id.etPass);
        String pass = password.getText().toString().trim();

        String passwdMd5 = this.toMd5(pass);

        String userId = "";

        Cursor c = db.rawQuery("select usuario from cuenta where usuario='"+usu+"' and password='"+passwdMd5+"';",null,null);
        if (c.moveToFirst()) {//es por que hay fila resultante
            userId = c.getString(0);
        }
        if(userId.matches("")){
            Toast.makeText(this, "Usuario erroneo o no registrado", Toast.LENGTH_LONG).show();
        }
        else{

            usuario.setText("");
            password.setText("");

            Intent registro = new Intent(Login.this, Home.class);
            registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);

            Bundle datos = new Bundle();
            datos.putString("entrada", "login");
            datos.putString("usuario", usu);

            registro.putExtras(datos);
            startActivity(registro);
        }
        c.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void validarDispositivo(){
        String userId = "";

        Cursor c = db.rawQuery("select usuario from cuenta where usuario='admin';",null,null);
        if (c.moveToFirst()) {//es por que hay fila resultante
            userId = c.getString(0);
        }
        if(userId.matches("")){
            nuevo = true;
        }

    }

    public String toMd5(String texto) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(texto.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        //ahora sb.toString(); es la contraseña cifrada
        return  sb.toString();
    }
}
