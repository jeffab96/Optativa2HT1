package com.example.H2_OPTATIVA_T1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        getPeticion();
    }

    public void escribir(View view) {
        registrarUsuario();
    }

    private void registrarUsuario() {
        boolean valida = true;

        EditText usuario = findViewById(R.id.txt_registro_user);
        EditText clave = findViewById(R.id.txt_registro_upass);
        EditText clavec = findViewById(R.id.txt_registro_passc);

        //Comprueba si campo el usuario no está vacío
        if (usuario.getText().length() == 0) {
            valida = false;
            String msg = "Usuario vacio";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        //Comprueba si los campos de contraseñas coinciden
        if (!clave.getText().toString().equals(clavec.getText().toString())) {
            valida = false;
            String msg = "Las contraseñas no coinciden";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        //insert into  usuarios (usuario, clave) values('admin','admin1');

        if (valida == true) {
            ConexionSQLHelper conexion = new ConexionSQLHelper(this, "bd_usuarios", null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            String insert = "INSERT INTO usuarios"
                    + "(usuario, clave)"
                    + "VALUES ('" + usuario.getText().toString() + "','" + clave.getText().toString() + "')";
            db.execSQL(insert);
            String msg = "Usuariol Creado";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            Intent listar = new Intent(this, MainActivity.class);
            startActivity(listar);
        }
    }

    public void getPeticion() {
        TextView msgGrupo = findViewById(R.id.txtMsgMain);
        MainActivity.getMensaje(msgGrupo, "G4T7");
    }
}
