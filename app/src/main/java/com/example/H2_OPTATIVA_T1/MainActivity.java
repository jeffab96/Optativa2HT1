package com.example.H2_OPTATIVA_T1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

import ec.edu.uce.optativa3.modelo.RegistroLog;
import ec.edu.uce.optativa3.modelo.Usuario;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    String contenido;
    File file;
    EditText texto;

    private SensorManager sensorManager;
    private SensorEventListener lightEventListener;
    private View root;
    private float maxValue;
    private Sensor lightSensor;

    static List<Usuario> usuarioList;

    //Se encarga de crear el archivo y la carpeta al iniciar la aplicación en caso que no existan
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //escribir=findViewById(R.id.btn_prueba);
        //Se encarga de realizar la petición del mensaje de servicio
        getPeticion();
        crearFiles(".xml");
        crearFiles("x.txt");
        crearFiles(".txt");
        //Realiza el login a la app con el archivo de preferencias compartidas.
        preferencesLogin();
        //Se asigna a la matriz local los datos del archivo en caso que existan.
        usuarioList = listarUsuarios();
        //Escribe en el archivo con la matriz de datos local
        escribirFile();

        root = findViewById(R.id.root);
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            Toast.makeText(this, "El dispositivo no tiene sensor", Toast.LENGTH_SHORT).show();
            finish();
        }

        maxValue = lightSensor.getMaximumRange();

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float value = event.values[0];
                getSupportActionBar().setTitle("Luz:" + value + "lx");
                int newValue = (int)(255f + value / maxValue);
                root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public void segundoPlano(final String usu, final String tipo) {
        final Date date = new Date();
        // Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println(usu + "','" + tipo + "','" + date.toString() + "','" + Build.MANUFACTURER + "','" + Build.MODEL + "','" + Build.VERSION.RELEASE);
                ConexionSQLHelper conexion = new ConexionSQLHelper(getApplicationContext(), "bd_usuarios", null, 1);
                SQLiteDatabase db = conexion.getWritableDatabase();
                String insert = "INSERT INTO logs"
                        + "(usuario, tipo, tiempo, nombred, modelod, versiond)"
                        + "VALUES ('" + usu + "','" + tipo + "','" + date.toString() + "','" + Build.MANUFACTURER + "','" + Build.MODEL + "','" + Build.VERSION.RELEASE + "')";
                db.execSQL(insert);

            }
        }, 1000);
    }


    void crearFiles(String formato) {
        File file;
        String archivo = "archivo_datos";
        String carpeta = "/Download/Archivos_OP3/";
        String name = "";
        //Establece el directorio de guardado
        String file_path = (Environment.getExternalStorageDirectory() + carpeta);
        File localFile = new File(file_path);
        //Toast.makeText(this,""+ file_path, Toast.LENGTH_SHORT).show();
        if (!localFile.exists()) {
            //Crea el directorio
            localFile.mkdir();
        }
        name = (archivo + formato);
        file = new File(localFile, name);
        try {
            file.createNewFile();
            // Toast.makeText(this,"Se creo archivo 2", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.file = file;
    }


    //static String matriz[][]=new String[dimx][dimy];
    //Método para escribir en el fichero creado.
    public void escribirFile() {
        //Crea un usuario por defecto si no existe ninguno, sino no hace nada
        if (usuarioList.size() == 0) {
            Usuario u = new Usuario();
            u.setUsuario("admin");
            u.setClave("admin1");
            ConexionSQLHelper conexion = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            String insert = "INSERT INTO usuarios"
                    + "(usuario, clave)"
                    + "VALUES ('" + u.getUsuario() + "','" + u.getClave() + "')";
            db.execSQL(insert);
            //}
        } else {
        }
        //Guarda la matriz con los datos almacenados en el archivo.
        usuarioList = listarUsuarios();
    }

    //Método usado en validacionLogin para guardar la sesión
    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences
                ("credenciales", Context.MODE_PRIVATE);
        EditText eu = findViewById(R.id.txt_entrada_usuario);
        String textU = eu.getText().toString();
        EditText ep = findViewById(R.id.txt_entrada_password);
        String textP = ep.getText().toString();
        String usuario = textU;
        String password = textP;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", usuario);
        editor.putString("pass", password);
        editor.commit();
        System.out.println("sasaasas");
    }

    //Método que se ejecuta al inicio para validar si hay un usuario en sesión
    private void preferencesLogin() {
        SharedPreferences preferences = getSharedPreferences
                ("credenciales", Context.MODE_PRIVATE);
        String u = preferences.getString("user", "");
        String p = preferences.getString("pass", "");

        if (u.length() == 0) {
            System.out.println("000");
        } else {
            System.out.println("999");
            usuarioList = listarUsuarios();
            List<Usuario> a = listarUsuarios();
            String textU = u;
            String textP = p;
            String msg = "Credenciales incorrectas";
            boolean t = false;
            for (int i = 0; i < a.size(); i++) {
                if ((a.get(i).getUsuario().equals(textU)) && (a.get(i).getClave().equals(textP))) {
                    t = true;
                    msg = "Usuario Aceptado";
                }
            }
            if (t == true) {
                Intent listar = new Intent(this, Listar.class);
                startActivity(listar);
            } else {
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }


    //Se ingresa a la ventana de ingresar usuario
    public void vistaRegistrar(View view) {
        Intent vistaRegistrar = new Intent(this, RegistroUsuario.class);
        startActivity(vistaRegistrar);
    }

    //Método para obetener el mensaje de servicio
    public void getPeticion() {
        TextView msgGrupo = findViewById(R.id.txtMsgMain);
        getMensaje(msgGrupo, "G4T7");
    }

    //Se invoca desde todas las vistas para obtener el mensaje de servicio
    public static void getMensaje(TextView textView, String servicio) {
        TextView msgGrupo = textView;

        String link = "https://optativa3-g4-t7.herokuapp.com/" + servicio;
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
            String mensaje = jsonMsg.optString("msg");
            msgGrupo.setText(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }

    DatabaseReference databaseLogs;

    public void subirDatos(View v) {

        databaseLogs = FirebaseDatabase.getInstance().getReference("logs");
        List<Usuario> lista = new ArrayList<>();
        ConexionSQLHelper admin = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();
        Cursor listaLogs = BaseDeDatabase.rawQuery("Select * from logs", null);
        int i=0;
        if (listaLogs.moveToFirst()) {
            do {
                i++;
                String idLog = databaseLogs.push().getKey();
                RegistroLog registroLog = new RegistroLog(idLog, listaLogs.getString(0), listaLogs.getString(1), listaLogs.getString(2), listaLogs.getString(3), listaLogs.getString(4), listaLogs.getString(5));
                databaseLogs.child(idLog).setValue(registroLog);
                //  lista.add(user);
            } while (listaLogs.moveToNext());
        } else {
            Toast.makeText(this, "No se encontraron registros LOGS", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Se han añadido "+i+" registros LOGS", Toast.LENGTH_LONG).show();
        String delete = "DELETE FROM logs";
        BaseDeDatabase.execSQL(delete);
    }

    private boolean buscarUsuario(String usuario, String pass) {
        boolean flag;
        try {
            ConexionSQLHelper admin = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
            SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();
            Cursor usuarios = BaseDeDatabase.rawQuery
                    ("Select * from usuarios where usuario = '" + usuario + "' and clave = '" + pass + "'", null);
            if (usuarios.moveToFirst()) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    public void validarLogin(View view) {
        EditText et = findViewById(R.id.txt_entrada_usuario);
        String usuario = et.getText().toString();
        EditText et_pass = findViewById(R.id.txt_entrada_password);
        String pass = et_pass.getText().toString();
        boolean flag = buscarUsuario(usuario, pass);
        if (flag) {
            Intent validacionLogin = new Intent(this, Listar.class);
            startActivity(validacionLogin);
            guardarPreferencias();
            Toast.makeText(this, "Usuario Aceptado", Toast.LENGTH_SHORT).show();

            segundoPlano(usuario, "entrada");
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        ConexionSQLHelper admin = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();
        Cursor listaUsuarios = BaseDeDatabase.rawQuery("Select * from usuarios", null);
        if (listaUsuarios.moveToFirst()) {
            do {
                Usuario user = new Usuario();
                user.setUsuario(listaUsuarios.getString(0));
                user.setClave(listaUsuarios.getString(1));
                lista.add(user);
            } while (listaUsuarios.moveToNext());
        } else {
            Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
        }
        return lista;
    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }
}
