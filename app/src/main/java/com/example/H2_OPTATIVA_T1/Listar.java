package com.example.H2_OPTATIVA_T1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.edu.uce.optativa3.modelo.Estudiante;

public class Listar extends AppCompatActivity {
    static String estudiante;
    static  List<Estudiante> listaEstud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        fillTable();
        getPeticion();
    }

    public void agregarEstudiante(View view){
        Intent registroEstudiante = new Intent(this, RegistroEstudiante.class);
        startActivity(registroEstudiante);
    }

    public List<Estudiante> listarEstudiantes() {
       List <Estudiante> listaEstudiant = new ArrayList<>();
        ConexionSQLHelper admin = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();
        Cursor listaEstudiantes = BaseDeDatabase.rawQuery("Select * from estudiantes", null);
        if (listaEstudiantes.moveToFirst()) {
            do {
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(listaEstudiantes.getString(0));
                estudiante.setApellido(listaEstudiantes.getString(1));
                estudiante.setEmail(listaEstudiantes.getString(2));
                estudiante.setCelular(listaEstudiantes.getString(3));
                estudiante.setFoto("fotoP");
                estudiante.setGenero(listaEstudiantes.getString(5));
                estudiante.setFechaN(listaEstudiantes.getString(6));
                estudiante.setAsignaturas(listaEstudiantes.getString(7));
                estudiante.setBecado(listaEstudiantes.getString(8));
                listaEstudiant.add(estudiante);
            } while (listaEstudiantes.moveToNext());
        } else {
            Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
        }
        return listaEstudiant;
    }

    //Método para rellenar la Tabla con un arreglo y seleccionar un usuario para mostrar su detalle
    private void fillTable() {
        List<Estudiante> matrix = listarEstudiantes();
        listaEstud=listarEstudiantes();
        int n = matrix.size();
        TableLayout table = findViewById(R.id.tableLayout1);
        table.removeAllViews();
        table.setBackgroundColor(Color.GREEN);
        TableRow rowCab = new TableRow(Listar.this);
        //Cabecera para la tabla dinámica
        rowCab.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        EditText cabeceraNum = new EditText(Listar.this);
        cabeceraNum.setText("N°");
        rowCab.addView(cabeceraNum);
        EditText cabeceraNombre = new EditText(Listar.this);
        cabeceraNombre.setText("NOMBRE");
        rowCab.addView(cabeceraNombre);
        //EditText cabeceraClave = new EditText(Listar.this);
        //cabeceraClave.setText("CLAVE");
        //rowCab.addView(cabeceraClave);
        rowCab.setBackgroundColor(Color.BLUE);
        table.addView(rowCab);
        //Se procede a la asignación del número y del nombre
        for (int i = 0; i < n; i++) {
            TableRow row = new TableRow(Listar.this);
            //Alternación de colores
            if (i % 2 == 0) {
                row.setBackgroundColor(Color.YELLOW);
            }
            //row.setBackgroundColor(Color.BLUE);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            EditText editNum = new EditText(Listar.this);
            editNum.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            editNum.setText(i + 1 + "");
            row.addView(editNum);
            //for (int j = 0; j < 1; j++) {
            EditText edit = new EditText(Listar.this);
            edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            edit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            //edit.setText(matrix[i][j]);
            edit.setText(matrix.get(i).getNombre());
            final String estudianteAuxiliar = matrix.get(i).getNombre();
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    estudiante = estudianteAuxiliar;
                    Intent i = new Intent(Listar.this, Detalle.class);
                    startActivity(i);
                }
            });
            edit.setKeyListener(null);
            row.addView(edit);
            //}
            table.addView(row);
        }
    }

    public void cerrarSesion(View v) {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String userText = preferences.getString("user", "");
        preferences.edit().clear().commit();
        segundoPlano(userText,"salida");
        try {
            JSONArray mJSONArray = new JSONArray();
            for (int i = 0; i < MainActivity.usuarioList.size(); i++) {
                JSONObject jObjd = new JSONObject();
                jObjd.put("usuario", MainActivity.usuarioList.get(i).getUsuario());
                jObjd.put("clave", MainActivity.usuarioList.get(i).getClave());
                mJSONArray.put(jObjd);
                System.out.println(mJSONArray.toString());
                String link = "http://localhost/upl/" + mJSONArray.toString();
                System.out.println(link);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL url = null;
                HttpURLConnection conexion;
                url = new URL(link);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent principal = new Intent(this, MainActivity.class);
        startActivity(principal);
    }

    @SuppressLint("NewApi")
    public void cerrarAplicacion(View v) {
        finishAffinity();
    }

    public void irOpciones(View view) {
        Intent validacionLogin = new Intent(this, Opciones.class);
        startActivity(validacionLogin);
    }

    public void getPeticion() {
        TextView msgGrupo = findViewById(R.id.txtMsgMain);
        MainActivity.getMensaje(msgGrupo, "G4T7");
    }

    public void segundoPlano(final String usu, final String tipo){
        final Date date= new Date();
        // Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println( usu + "','" + tipo + "','" +date.toString()+ "','" + Build.MANUFACTURER+ "','" +Build.MODEL+ "','" +Build.VERSION.RELEASE);
                ConexionSQLHelper conexion = new ConexionSQLHelper(getApplicationContext(), "bd_usuarios", null, 1);
                SQLiteDatabase db = conexion.getWritableDatabase();
                String insert = "INSERT INTO logs"
                        + "(usuario, tipo, tiempo, nombred, modelod, versiond)"
                        + "VALUES ('" + usu + "','" + tipo + "','" +date.toString()+ "','" +Build.MANUFACTURER+ "','" +Build.MODEL+ "','" +Build.VERSION.RELEASE+"')";
                db.execSQL(insert);
            }
        }, 1000);
    }

}
