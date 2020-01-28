package com.example.H2_OPTATIVA_T1;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.H2_OPTATIVA_T1.R;

import ec.edu.uce.optativa3.modelo.Estudiante;

public class Editar extends AppCompatActivity {
  //  List<Estudiante> matrizAux;
    String estudianteE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("creadoooedit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        //Se encarga de realizar la petición del mensaje de servicio
        getPeticion();
        //Toma la matriz
        // Listar.listaEstud = listarUsuarios();
        estudianteE = Listar.estudiante;
    }

    public void editarUsuario(View view) {
    /*    System.out.println("creatoUsuarioEdit");

        String usu[][] = new String[1][matrizAux[0].length];
        String datos[][];
        datos = new String[matrizAux.length][matrizAux[0].length];
*/
        Estudiante estudiante = new Estudiante();
        boolean valida = true;
        EditText nombre = findViewById(R.id.txt_registro_nombreE);
        EditText apellido = findViewById(R.id.txt_registro_apellidoE);
        EditText email = findViewById(R.id.txt_registro_emailE);
        EditText celular = findViewById(R.id.txt_registro_celularE);
        String sexo = obtenerSexo();
        EditText fecha = findViewById(R.id.txt_registro_fechaNacimientoE);
        String asignaturas = obtenerAsignatura();
        String beca = obtenerBeca();

        estudiante.setNombre(nombre.getText().toString());
        estudiante.setApellido(apellido.getText().toString());
        estudiante.setEmail(email.getText().toString());
        estudiante.setCelular(celular.getText().toString());
        estudiante.setFoto("FOTO EDITADA");
        estudiante.setGenero(sexo);
        estudiante.setFechaN(fecha.getText().toString());
        estudiante.setAsignaturas( asignaturas);
        estudiante.setBecado(beca);

        //Comprueba si campo el usuario no está vacío
        if (nombre.getText().length() == 0) {
            valida = false;
            String msg = "Usuario vacio";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        if (valida == true) {
            ConexionSQLHelper conexion = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            String update = "UPDATE estudiantes SET " +
                    "nombre='"+estudiante.getNombre()+"',"+
                    "apellido='"+estudiante.getApellido()+"',"+
                    "email='"+estudiante.getEmail()+"',"+
                    "celular='"+estudiante.getCelular()+"',"+
                    "foto='"+estudiante.getFoto()+"',"+
                    "genero='"+estudiante.getGenero()+"',"+
                    "fechaN='"+estudiante.getFechaN()+"',"+
                    "asignaturas='"+estudiante.getAsignaturas()+"',"+
                    "becado='"+estudiante.getBecado()+"'"+
                    "WHERE nombre='"+estudianteE+"'";
            db.execSQL(update);
            Toast.makeText(this, Detalle.getServicio("editar"), Toast.LENGTH_SHORT).show();
            Intent listar = new Intent(this, Listar.class);
            startActivity(listar);
        }
    }


    String obtenerSexo() {
        String resultado = "";
        RadioButton rh = findViewById(R.id.radio_registro_hombreE);
        RadioButton rm = findViewById(R.id.radio_registro_mujerE);

        if (rh.isChecked() == true) {
            resultado += "masculino";
        } else {
            resultado += "femenino";
        }
        return resultado;
    }

    String obtenerAsignatura() {
        String resultado = "";
        CheckBox c1 = findViewById(R.id.chk_registro_fisicaE);
        CheckBox c2 = findViewById(R.id.chk_registro_inglesE);
        CheckBox c3 = findViewById(R.id.chk_registro_lenguajeE);
        CheckBox c4 = findViewById(R.id.chk_registro_matematicaE);
        CheckBox c5 = findViewById(R.id.chk_registro_quimicaE);

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
        Switch sw = findViewById(R.id.swicht_registro_becadoE);
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


