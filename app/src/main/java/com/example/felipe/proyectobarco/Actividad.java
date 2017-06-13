package com.example.felipe.proyectobarco;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.proyectobarco.Data.Conection;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Actividad extends AppCompatActivity {


    int CodSesion;
    String usuario;

    ListView lvList;

    ArrayList<String> lista;
    ArrayAdapter adaptador;

    Conection con;
    SQLiteDatabase db;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);

        con = new Conection(this, "barco", null, 1);

        Bundle recibo = getIntent().getExtras();   // Se crea un objeto para recibir lo que se envio por un bundle
        String cod = recibo.getString("codigo").trim();
        usuario = recibo.getString("usuario").trim();
        CodSesion = Integer.parseInt(cod);


        lvList = (ListView)findViewById(R.id.lvLista);

        lista = Consultar();
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1,lista);

        lvList.setAdapter(adaptador);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ArrayList Consultar(){
        db = con.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();

        Cursor c = db.rawQuery("select codigo, control from actividad where codigo_sesion = "+CodSesion+";",null,null);
        if(c.moveToFirst()){
            do {
                list.add(c.getString(0)+" "+c.getString(1));
            }while(c.moveToNext());
        }
        return list;
    }


    public void Regresar(View v){

        Intent salir = new Intent(Actividad.this, Home.class);

        Bundle datos = new Bundle();
        datos.putString("entrada", "actividad");
        datos.putString("usuario", usuario);

        salir.putExtras(datos);
        startActivity(salir);
        finish();
    }
}
