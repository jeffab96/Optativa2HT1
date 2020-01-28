package com.example.H2_OPTATIVA_T1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroEstudiante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_estudiante);
        getPeticion();
    }

    public void escribir(View view) {
        registrarUsuario();
    }

    private void registrarUsuario() {
        boolean valida = true;

        EditText nombre = findViewById(R.id.txt_registro_nomE);
        EditText apellido = findViewById(R.id.txt_registro_apellE);
        EditText email = findViewById(R.id.txt_registro_emailE);
        EditText celular = findViewById(R.id.txt_registro_celulE);
        String sexo = obtenerSexo();
        EditText fecha = findViewById(R.id.txt_registro_fechaNE);
        String asignaturas = obtenerAsignatura();
        String beca = obtenerBeca();
        //Foto /9no elemento

        //Comprueba si campo el nombre no está vacío
        if (nombre.getText().length() == 0) {
            valida = false;
            String msg = "Nombre vacio";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }


        if (valida == true) {
            ConexionSQLHelper conexion = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            String insert = "INSERT INTO estudiantes"
                    + "(nombre, apellido,email, celular, foto, genero, fechaN, asignaturas, becado)"
                    + "VALUES ('" + nombre.getText().toString() + "','" + apellido.getText().toString() +"','" +
                    email.getText().toString() +"','" + celular.getText().toString()+"','','"
                    +  sexo +"','"+  fecha.getText().toString()+"','" +  asignaturas+"','" +  beca+"')";
            db.execSQL(insert);
            String msg = "Estudiante Creado";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            Intent listar = new Intent(this, MainActivity.class);
            startActivity(listar);
        }
    }

    String obtenerSexo() {
        String resultado = "";
        RadioButton rh = findViewById(R.id.radio_registro_masE);
        RadioButton rm = findViewById(R.id.radio_registro_femE);

        if (rh.isChecked() == true) {
            resultado += "masculino";
        } else {
            resultado += "femenino";
        }
        return resultado;
    }

    String obtenerAsignatura() {
        String resultado = "";
        CheckBox c1 = findViewById(R.id.chk_registro_fisE);
        CheckBox c2 = findViewById(R.id.chk_registro_ingleE);
        CheckBox c3 = findViewById(R.id.chk_registro_lengE);
        CheckBox c4 = findViewById(R.id.chk_registro_mateE);
        CheckBox c5 = findViewById(R.id.chk_registro_quimicE);

        if (c1.isChecked() == true) {
            resultado += "Física ";
        }
        if (c2.isChecked() == true) {
            resultado += "Inglés ";
        }
        if (c3.isChecked() == true) {
            resultado += "Lenguaje ";
        }
        if (c4.isChecked() == true) {
            resultado += "Matemática ";
        }
        if (c5.isChecked() == true) {
            resultado += "Química ";
        }
        return resultado;
    }

    String obtenerBeca() {
        String resultado = "";
        Switch sw = findViewById(R.id.swicht_registro_becaE);
        if (sw.isChecked() == true) {
            resultado += "Sí";
        } else {
            resultado += "No";
        }
        return resultado;
    }

    public void getPeticion() {
        TextView msgGrupo = findViewById(R.id.txtMsgMain);
        MainActivity.getMensaje(msgGrupo, "G4T7");
    }
}
