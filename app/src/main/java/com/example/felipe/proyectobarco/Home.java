package com.example.felipe.proyectobarco;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.felipe.proyectobarco.Data.Conection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Set;

public class Home extends AppCompatActivity {

    Button conexion;
    ListView lvList;

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> dispVinculados;

    String usuario;
    String codigo_sesion = "";

    ArrayList<String> lista;
    ArrayAdapter adaptador;

    Conection con;
    SQLiteDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle recibo = getIntent().getExtras();   // Se crea un objeto para recibir lo que se envio por un bundle
        String entrada = recibo.getString("entrada").trim();
        usuario = recibo.getString("usuario").trim();


        if(entrada.matches("login")){
            Toast.makeText(this, "Bienvenido " + usuario + "!", Toast.LENGTH_LONG).show();
        }
        else{
            if(entrada.matches("control")) {
                Toast.makeText(this, "Sesion registrada!", Toast.LENGTH_LONG).show();
            }
        }

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            Toast.makeText(this, "Bluetooth no disponible", Toast.LENGTH_LONG).show();

            //finalizamos la aplicación
            finish();
        } else if (!myBluetooth.isEnabled()) {
            //Solicitud de encendido de bluetooth
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        conexion = (Button) findViewById(R.id.bConectar);
        lvList = (ListView) findViewById(R.id.lvLista);


        con = new Conection(this, "barco", null, 1);

        lista = Consultar();
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);


        lvList.setAdapter(adaptador);

        lvList.setClickable(true);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object sesion = lvList.getItemAtPosition(position);
                String dato = (String) sesion;
                String campo[] = dato.split("\n");
                codigo_sesion = campo[0].substring(8).trim();

                Bundle datos = new Bundle();
                datos.putString("codigo", codigo_sesion);
                datos.putString("usuario", usuario);

                Intent consulta = new Intent(Home.this, Actividad.class);
                consulta.addFlags(consulta.FLAG_ACTIVITY_CLEAR_TOP | consulta.FLAG_ACTIVITY_SINGLE_TOP);

                consulta.putExtras(datos);

                startActivity(consulta);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ArrayList Consultar() {
        db = con.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();

        Cursor c;
        if(usuario.matches("admin")){
            c = db.rawQuery("select codigo, usuario, fecha, hora from sesion;", null, null);
        }
        else{
            c = db.rawQuery("select codigo, usuario, fecha, hora from sesion where usuario='"+usuario+"';", null, null);
        }

        if (c.moveToFirst()) {
            do {
                list.add("Sesion: "+c.getString(0) + "\nUsuario: " + c.getString(1) + "\nFecha: " + c.getString(2) + "\nHora: " + c.getString(3));
            } while (c.moveToNext());
        }
        return list;
    }


    private void listaDispositivosvinculados() {
        dispVinculados = myBluetooth.getBondedDevices();

        if (dispVinculados.size() > 0) {
            for (BluetoothDevice bt : dispVinculados) {
            }
        }
    }

    public void Conectar(View v) {

        listaDispositivosvinculados();

        if (dispVinculados.size() < 1) {
            Toast.makeText(getApplicationContext(), "No se han encontrado dispositivos vinculados", Toast.LENGTH_LONG).show();

        }
        else {
            Intent registro = new Intent(Home.this, Control.class);

            registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle datos = new Bundle();
            datos.putString("usuario", usuario);

            registro.putExtras(datos);

            startActivity(registro);
        }
    }


    public void Regresar(View v) {
        Intent registro = new Intent(Home.this, Login.class);
        registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(registro);
        finish();
    }
}

