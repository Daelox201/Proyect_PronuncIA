package com.example.pronuncia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail,edtPassword;
    private TextView singUp;

    private Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singUp = findViewById(R.id.sing_up);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.psw);
        btnSignIn = findViewById(R.id.btnSing_In);

        firebaseAuth = FirebaseAuth.getInstance();

        singUp.setOnClickListener(View -> cargarRegister());
        btnSignIn.setOnClickListener(view -> signInWithEmailAndPassword());


    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            Intent i = new Intent(MainActivity.this,Principal_Page.class);
            startActivity(i);
            finish();
            // Si el usuario ya ha iniciado sesión, redirige a la actividad del contenido principal.

        }
    }

    public void cargarRegister(){
        //Toast.makeText(MainActivity.this,"Usuario creado con exito",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this,RegisterPage.class);
        startActivity(i);
        finish();
    }



    private void signInWithEmailAndPassword() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (!isValidEmail(email)) {
            edtEmail.setError("Correo electrónico no válido");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Ingresa tu contraseña");
            edtPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Intent intent = new Intent(MainActivity.this, Principal_Page.class);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual
                        } else {
                            Toast.makeText(this, "Verifica tu correo electrónico primero.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Verificar si el error es debido a una contraseña incorrecta
                        if (task.getException() != null &&
                                task.getException().getMessage() != null &&
                                task.getException().getMessage().contains("password")) {
                            Toast.makeText(this, "Contraseña incorrecta. Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Inicio de sesión fallido. Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}