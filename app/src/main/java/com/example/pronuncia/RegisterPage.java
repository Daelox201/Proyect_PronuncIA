package com.example.pronuncia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    private TextView singIn;
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail, edtPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        singIn = findViewById(R.id.sing_up);
        edtEmail = findViewById(R.id.txtEmail);
        edtPassword = findViewById(R.id.psw);
        btnSignUp = findViewById(R.id.btnSing_In);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(view -> registerUser());

        singIn.setOnClickListener(view -> cargarLogin());

        // Llamamos al método para ejecutar las pruebas
        runRegistrationTests();
    }

    private void cargarLogin() {
        Intent i = new Intent(RegisterPage.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void registerUser() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (!isValidEmail(email)) {
            edtEmail.setError("Correo electrónico no válido");
            edtEmail.requestFocus();
            return;
        }

        if (!isStrongPassword(password)) {
            return;
        }

        checkIfEmailExists(email, password);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    } else {
                        handleRegistrationFailure(task);
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Se ha enviado un correo de verificación.", Toast.LENGTH_SHORT).show();
                            finish(); // Regresa a la actividad anterior
                        } else {
                            Toast.makeText(this, "No se pudo enviar el correo de verificación.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void handleRegistrationFailure(Task<AuthResult> task) {
        try {
            throw task.getException();
        } catch (FirebaseAuthException e) {
            // Manejo de excepciones específicas de FirebaseAuth
            String errorCode = e.getErrorCode();
            // Agregar manejo para cada tipo de error aquí
        } catch (Exception e) {
            // Manejo de otras excepciones
            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void checkIfEmailExists(String email, String password) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (!isNewUser) {
                            Toast.makeText(this, "Esta dirección de correo electrónico ya existe. Intenta iniciar sesión.", Toast.LENGTH_LONG).show();
                            cargarLogin();
                        } else {
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            sendEmailVerification();
                                        } else {
                                            handleRegistrationFailure(task1);
                                        }
                                    });
                        }
                    }
                });
    }

    private boolean isStrongPassword(String password) {
        // Definir los criterios para una contraseña segura
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_])(?=\\S+$).{8,}$";

        // Verificar si la contraseña cumple con los criterios
        if (password.matches(regex)) {
            return true;
        } else {
            edtPassword.setError("La contraseña debe contener al menos 8 caracteres, incluyendo al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.");
            edtPassword.requestFocus();
            return false;
        }
    }

    private void runRegistrationTests() {
        // Prueba 1: Registro exitoso con credenciales válidas
        testRegistration("correo@ejemplo.com", "ContraseñaSegura123");

        // Prueba 2: Registro fallido con correo electrónico inválido
        testRegistration("correo_invalido", "ContraseñaSegura123");

        // Prueba 3: Registro fallido con contraseña débil
        testRegistration("correo@ejemplo.com", "123");

        // ... (puedes agregar más pruebas aquí con distintos escenarios)
    }

    private void testRegistration(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        sendEmailVerification();
                    } else {
                        // Manejar registro fallido
                        handleRegistrationFailure(task);
                    }
                });
    }
}

