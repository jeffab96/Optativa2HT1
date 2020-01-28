package com.example.H2_OPTATIVA_T1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import ec.edu.uce.optativa3.modelo.Usuario;

public class Registro extends AppCompatActivity {
/*
    String archivo = "archivo_datos";
    String archivox = "archivo_datos";
    String archivoxh = "archivo_datosx";
    String carpeta = "/Download/Archivos_OP3/";
    String contenido;
    File file;
    String file_path = "";
    File filex;
    String file_pathx = "";
    File filexh;
    String file_pathxh = "";
    EditText texto;
    String name = "";
    static int dimx;
    static int dimy;
    static String matriz[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //escribir=findViewById(R.id.btn_prueba);
        getPeticion();
    }

    public void escribirFile() {
        boolean valida = true;
        List<Usuario> datos;
        if (dimx == 0) {
            datos = new String[1][11];
            datos[0][0] = "admin";
            datos[0][1] = "admin1";
            datos[0][2] = "nom";
            datos[0][3] = "apell";
            datos[0][4] = "admin@uce.com";
            datos[0][5] = "09990";
            datos[0][6] = "foto";
            datos[0][7] = "femenino";
            datos[0][8] = "21/08";
            datos[0][9] = "matematica, fisica";
            datos[0][10] = "no";
        } else {
            //        String datos1[][]={{"admin","admin"}};
            datos = new String[dimx + 1][dimy];
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    datos[i][j] = matriz[i][j];
                }
            }

            EditText usuario = findViewById(R.id.txt_registro_usuario);
            EditText pass = findViewById(R.id.txt_registro_password);
            EditText passcon = findViewById(R.id.txt_registro_confirmaPassword);
            EditText nombre = findViewById(R.id.txt_registro_nombre);
            EditText apellido = findViewById(R.id.txt_registro_apellido);
            EditText email = findViewById(R.id.txt_registro_email);
            EditText celular = findViewById(R.id.txt_registro_celular);
            String sexo = obtenerSexo();
            EditText fecha = findViewById(R.id.txt_registro_fechaNacimiento);
            String asignaturas = obtenerAsignatura();
            String beca = obtenerBeca();

            datos[datos.length - 1][0] = usuario.getText().toString();
            datos[datos.length - 1][1] = pass.getText().toString();
            // datos[datos.length - 1][5] = passcon.getText().toString();
            datos[datos.length - 1][2] = nombre.getText().toString();
            datos[datos.length - 1][3] = apellido.getText().toString();
            datos[datos.length - 1][4] = email.getText().toString();
            datos[datos.length - 1][5] = celular.getText().toString();
            datos[datos.length - 1][6] = "FOTO";
            datos[datos.length - 1][7] = sexo;
            datos[datos.length - 1][8] = fecha.getText().toString();
            datos[datos.length - 1][9] = asignaturas;
            datos[datos.length - 1][10] = beca;


            //Comprueba si campo el usuario no está vacío
            if (usuario.getText().length() == 0) {
                valida = false;
                String msg = "Usuario vacio";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            //Comprueba si los campos de contraseñas coinciden
            if (!pass.getText().toString().equals(passcon.getText().toString())) {
                valida = false;
                String msg = "Las contraseñas no coinciden";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }
        //String f[][]=leerToArray();
        // String datos[][]={{"jeffer","alex"},{"carlos","albert"},{"tania","rea"}};
        if (valida == true) {

            FileWriter fichero = null;
            PrintWriter pw = null;
            try {
                fichero = new FileWriter(file);
                pw = new PrintWriter(fichero);
                // EditText text=findViewById(R.id.txt_entrada_usuario);
                for (int i = 0; i < datos.length; i++) {
                    pw.println("");
                    for (int j = 0; j < datos[i].length; j++) {
                        pw.print(datos[i][j] + ",");
                    }
                }
                String msg = "Usuario creado";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                pw.flush();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fichero)
                        fichero.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        }
        MainActivity.usuarioList = datos;
        System.out.println(valida);
        Intent listar = new Intent(this, MainActivity.class);
        startActivity(listar);
    }


    String obtenerSexo() {
        String resultado = "";
        RadioButton rh = findViewById(R.id.radio_registro_hombre);
        RadioButton rm = findViewById(R.id.radio_registro_mujer);

        if (rh.isChecked() == true) {
            resultado += "masculino";
        } else {
            resultado += "femenino";
        }
        return resultado;
    }

    String obtenerAsignatura() {
        String resultado = "";
        CheckBox c1 = findViewById(R.id.chk_registro_fisica);
        CheckBox c2 = findViewById(R.id.chk_registro_ingles);
        CheckBox c3 = findViewById(R.id.chk_registro_lenguaje);
        CheckBox c4 = findViewById(R.id.chk_registro_matematica);
        CheckBox c5 = findViewById(R.id.chk_registro_quimica);

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
        Switch sw = findViewById(R.id.swicht_registro_becado);
        if (sw.isChecked() == true) {
            resultado += "Sí";
        } else {
            resultado += "No";
        }
        return resultado;
    }

    public void dim() {
        //int a[]=new int[2];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader archivo = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(archivo);
        int i = 0;
        int j = 0;
        int ascii;
        try {
            while ((ascii = br.read()) != -1) {
                if (ascii == 10) {
                    i++;
                    j = 0;
                } else {
                    if (ascii == 44) {
                        j++;
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dimx = i;
        dimy = j;
        System.out.println("dimx" + i);
        System.out.println("dimy" + j);
    }

    public void escribir(View view) {
        escribirFile();
        metodoXML(matriz);
    }

    public void leer(View view) {
        dim();
        leerToArray();
    }

    public String[][] leerToArray() {
        dim();
        String matrix[][] = new String[dimx][dimy];
        EditText text = findViewById(R.id.txt_registro_usuario);
        String contenido = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader archivo = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(archivo);
        int ascii;

        try {

            int i = -1;
            int j = 0;
            while ((ascii = br.read()) != -1) {
                char caracter = (char) ascii;
                if (ascii == 10) {
                    i++;
                    j = 0;
                } else {
                    if (ascii == 44) {
                        System.out.println("azxzx" + matrix.length + "mmm" + matrix[0].length);
                        matrix[i][j] = contenido;
                        contenido = "";
                        j++;
                    } else {
                        contenido += caracter;
                    }
                }
            }
            text.setText(contenido);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public String metodoXML(String[][] datos) {
///////////////////////////////////////////"metodo" para crear un string en foemato xml de una matriz
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String result = "aa";
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UFT-8", true);
            serializer.startTag("", "usuarios");
            for (int i = 0; i < datos.length; i++) {
                serializer.startTag("", "usuario");

                serializer.startTag("", "nickname");
                serializer.text(String.valueOf(datos[i][0]));
                serializer.endTag("", "nickname");

                serializer.startTag("", "pass");
                serializer.text(String.valueOf(datos[i][1]));
                serializer.endTag("", "pass");

                serializer.startTag("", "nombre");
                serializer.text(String.valueOf(datos[i][2]));
                serializer.endTag("", "nombre");

                serializer.startTag("", "apellido");
                serializer.text(String.valueOf(datos[i][3]));
                serializer.endTag("", "apellido");

                serializer.startTag("", "email");
                serializer.text(String.valueOf(datos[i][4]));
                serializer.endTag("", "email");

                serializer.startTag("", "celular");
                serializer.text(String.valueOf(datos[i][5]));
                serializer.endTag("", "celular");

                serializer.startTag("", "genero");
                serializer.text(String.valueOf(datos[i][7]));
                serializer.endTag("", "genero");

                serializer.startTag("", "fecha");
                serializer.text(String.valueOf(datos[i][8]));
                serializer.endTag("", "fecha");

                serializer.startTag("", "asignatura");
                serializer.text(String.valueOf(datos[i][9]));
                serializer.endTag("", "asignatura");

                serializer.startTag("", "becado");
                serializer.text(String.valueOf(datos[i][10]));
                serializer.endTag("", "becado");


                serializer.endTag("", "usuario");
            }
            serializer.endTag("", "usuarios");
            serializer.endDocument();
            result = writer.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(filex);
            pw = new PrintWriter(fichero);
            // EditText text=findViewById(R.id.txt_entrada_usuario);

            pw.print(result);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        try {
            fichero = new FileWriter(filexh);
            pw = new PrintWriter(fichero);
            // EditText text=findViewById(R.id.txt_entrada_usuario);

            pw.print(result);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        matriz = leerToArray();

        System.out.println("ssssss" + result);
        return result;
    }

    public void getPeticion() {
        TextView msgGrupo = findViewById(R.id.txtMsgMain);
        MainActivity.getMensaje(msgGrupo,"G4T7");
    }
*/
}
