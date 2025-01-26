package com.example.gestionfernando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_LAST_USERNAME = "last_username";

    private List<Usuario> listaUsuarios;
    private EditText usernameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar la lista de usuarios
        listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new Usuario("admin", "1234"));
        listaUsuarios.add(new Usuario("usuario1", "abcd"));
        listaUsuarios.add(new Usuario("fernando", "clave123"));

        // Referencias a los elementos del layout
        usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        // Cargar el último nombre de usuario desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastUsername = preferences.getString(KEY_LAST_USERNAME, "");
        if (!lastUsername.isEmpty()) {
            usernameField.setText(lastUsername);
        }

        // Acción del botón
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Validar usuario
                if (validarUsuario(username, password)) {
                    // Guardar el nombre de usuario en SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(KEY_LAST_USERNAME, username);
                    editor.apply();

                    // Si es válido, avanzar a la siguiente pantalla
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Si no es válido, mostrar mensaje de error
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para validar el usuario
    private boolean validarUsuario(String nombre, String clave) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getNombre().equals(nombre) && usuario.getClave().equals(clave)) {
                return true;
            }
        }
        return false;
    }
}
