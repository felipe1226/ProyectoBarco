package com.example.felipe.proyectobarco;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.widget.ToggleButton;

import com.example.felipe.proyectobarco.Data.Conection;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class Control extends ActionBarActivity {

    Button frente, atras, izquierda, derecha, parar;
    ToggleButton encendido;

    String codigo_sesion;
    String usuario;
    String fecha;
    String hora;

    boolean enc;

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Conection con;
    SQLiteDatabase db;

    ContentValues datosSesion = new ContentValues();
    ContentValues datosActividad = new ContentValues();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        con = new Conection(this, "barco", null, 1);
        db = con.getWritableDatabase();

        address = "20:16:04:11:28:23";

        Bundle recibo = getIntent().getExtras();
        usuario = recibo.getString("usuario").trim();

        setContentView(R.layout.activity_control);

        encendido = (ToggleButton) findViewById(R.id.bEncendido);

        frente = (Button) findViewById(R.id.bFrente);
        atras = (Button) findViewById(R.id.bAtras);
        izquierda = (Button) findViewById(R.id.bIzquierda);
        derecha = (Button) findViewById(R.id.bDerecha);
        parar = (Button) findViewById(R.id.bParar);

        new ConnectBT().execute(); //Call the class to connect

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        fecha = (new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year)).toString();
        Date horaActual=new Date();

        hora=horaActual.getHours()+":"+horaActual.getMinutes()+":" +horaActual.getSeconds();

        datosSesion.put("usuario", usuario);
        datosSesion.put("fecha", fecha);
        datosSesion.put("hora", hora);
        db.insert("sesion", null, datosSesion);

        try {
            con.BD_backup();

        } catch (IOException e) {
            e.printStackTrace();
        }
        getCodigo_sesion();

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void getCodigo_sesion(){
        Cursor c = db.rawQuery("select codigo from sesion where usuario='"+usuario+"' and fecha='"+fecha+"' and hora='"+hora+"';",null,null);
        if (c.moveToFirst()) {//es por que hay fila resultante
            codigo_sesion = c.getString(0);
        }
    }

    public void msgOff() {
        Toast.makeText(getApplicationContext(), "Encienda el barco", Toast.LENGTH_LONG).show();
    }

    public void Encendido(View v) {
        if (enc) {
            enc = false;
            Parar(v);
            Toast.makeText(this, "Barco apagado!", Toast.LENGTH_SHORT).show();
        } else {
            enc = true;
            Toast.makeText(this, "Barco encendido!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("7".toString().getBytes());
                Thread.sleep(1000);
                btSocket.close();
            } catch (IOException e) {
                msg("Error");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finish();

    }

    public void Frente(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("1".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Frente");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }


    public void Atras(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("6".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Atras");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }


    public void FrenteIzquierda(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("3".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Frente Izquierda");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }


    public void Izquierda(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("5".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Izquierda");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }

    public void FrenteDerecha(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("2".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Frente Derecha");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }

    public void Derecha(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("4".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Derecha");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }


    public void Parar(View v) {
        if (enc) {
            if (btSocket != null) {
                try {
                    btSocket.getOutputStream().write("7".toString().getBytes());
                } catch (IOException e) {
                    msg("Error");
                }
            }
            datosActividad.put("codigo_sesion",codigo_sesion);
            datosActividad.put("control", "Parar");
            db.insert("actividad", null, datosActividad);
        } else {
            msgOff();
        }
    }

    public void Salir(View v) throws InterruptedException {
        if (enc) {
            Toast.makeText(this, "Barco encendido aun!", Toast.LENGTH_LONG).show();
        } else {
            Disconnect();
            try {
                con.BD_backup();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent salir = new Intent(Control.this, Home.class);

            Bundle datos = new Bundle();
            datos.putString("entrada", "control");
            datos.putString("usuario", usuario);

            salir.putExtras(datos);
            startActivity(salir);
            finish();
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Control.this, "Conectando barco...", "Espere un momento!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//conectamos al dispositivo y verificamos si esta disponible
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Conexión Fallida");
                finish();
            } else {
                msg("Barco conectado!");
                isBtConnected = true;
                enc = true;
            }
            progress.dismiss();
        }
    }
}