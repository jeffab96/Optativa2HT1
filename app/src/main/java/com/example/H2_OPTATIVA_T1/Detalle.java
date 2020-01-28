package com.example.H2_OPTATIVA_T1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.H2_OPTATIVA_T1.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ec.edu.uce.optativa3.modelo.Estudiante;

public class Detalle extends AppCompatActivity {

    String archivo = "archivo_datos";
    String carpeta = "/Download/Archivos_OP3/";
    File file;
    String file_path = "";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.file_path = (Environment.getExternalStorageDirectory() + this.carpeta);
        File localFile = new File(this.file_path);
        //Toast.makeText(this,""+ file_path, Toast.LENGTH_SHORT).show();
        if (!localFile.exists()) {
            localFile.mkdir();
        }
        this.name = (this.archivo + ".txt");
        this.file = new File(localFile, this.name);
        try {
            this.file.createNewFile();
            // Toast.makeText(this,"Se creo archivo 2", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Estudiante> matrix = Listar.listaEstud;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        String estudiante = Listar.estudiante;
        Estudiante estud = new Estudiante();
        for (int i = 0; i < matrix.size(); i++) {
            if (estudiante.equals(matrix.get(i).getNombre())) {
                estud = matrix.get(i);
            }
        }
        TextView txt_user = findViewById(R.id.txt_detalle_nombre);
        txt_user.setText(estud.getNombre());
        //PASSWORD
        TextView txt_ape = findViewById(R.id.txt_detalle_apellido);
        txt_ape.setText(estud.getApellido());
        TextView txt_email = findViewById(R.id.txt_detalle_email);
        txt_email.setText(estud.getEmail());
        TextView txt_celu = findViewById(R.id.txt_detalle_celular);
        txt_celu.setText(estud.getCelular());
        //FOTO
        TextView txt_sexo = findViewById(R.id.txt_detalle_sexo);
        txt_sexo.setText(estud.getGenero());
        TextView txt_fechaN = findViewById(R.id.txt_detalle_fechaNacimiento);
        txt_fechaN.setText(estud.getFechaN());
        TextView txt_asign = findViewById(R.id.txt_detalle_asignaturas);
        txt_asign.setText(estud.getAsignaturas());
        TextView txt_beca = findViewById(R.id.txt_detalle_becado);
        txt_beca.setText(estud.getBecado());
        getPeticion();
    }

    public void getPeticion() {
        TextView msgGrupo = findViewById(R.id.txtMsgMain);
        MainActivity.getMensaje(msgGrupo, "G4T7");
    }

    public void regresar(View v) {
        Intent listar = new Intent(this, Listar.class);
        startActivity(listar);
    }

    public void eliminar(View view) {
        String estudianteE = Listar.estudiante;

        ConexionSQLHelper conexion = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        String delete = "DELETE FROM estudiantes " +
                "WHERE nombre='" + estudianteE + "'";
        db.execSQL(delete);

        Toast.makeText(this, getServicio("eliminar"), Toast.LENGTH_SHORT).show();
        Intent listar = new Intent(this, Listar.class);
        startActivity(listar);

    }

    public static String getServicio(String servicio) {
        String mensaje = "";
        // String link = "https://optativa3-g4-t7.herokuapp.com/G4T7";
        String link = "https://optativa3-g4-t7.herokuapp.com/operacion/" + servicio;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conexion;

        try {
            url = new URL(link);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            json = response.toString();
            JSONObject jsonMsg = new JSONObject(json);
            mensaje = jsonMsg.optString("msg");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mensaje;
    }

    public void vistaEditar(View view) {
        Intent listar = new Intent(this, Editar.class);
        startActivity(listar);
    }
}





