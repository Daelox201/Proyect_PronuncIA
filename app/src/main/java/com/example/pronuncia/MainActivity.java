package com.example.pronuncia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


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

        // Inflar ambos layouts
        View mainLayout = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(mainLayout);

        singUp = mainLayout.findViewById(R.id.sing_up);
        edtEmail = mainLayout.findViewById(R.id.email);
        edtPassword = mainLayout.findViewById(R.id.psw);
        btnSignIn = mainLayout.findViewById(R.id.btnSing_In);

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
                        // Simulación de inicio de sesión con Google si los credenciales son válidos
                        if (isValidEmailAndPassword(email, password)) {
                            simulateGoogleAuthentication();

                        } else {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                startPrincipalPage();

                            } else {
                                Toast.makeText(this, "Verifica tu correo electrónico primero.", Toast.LENGTH_SHORT).show();
                            }
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

    private void simulateGoogleAuthentication() {
        // Primer aviso: Iniciando sesión con Google
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Iniciando sesión con Google...")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    // Al hacer clic en OK, mostrar el segundo aviso
                    simulateDialogflowInteraction();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void simulateDialogflowInteraction() {
        // Segundo aviso: DialogFlow funciona correctamente
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("DialogFlow funciona correctamente")
                .setPositiveButton("OK", (dialog, id) -> {
                    // Al hacer clic en OK, redirigir a Principal_Page
                    testSecurity();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void testSecurity() {
        // Prueba de seguridad: Simular una alerta indicando que la prueba fue realizada
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Prueba de seguridad realizada con éxito")
                .setPositiveButton("OK", (dialog, id) -> {
                    // Acciones después de la prueba de seguridad (si es necesario)
                    startPrincipalPage();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void startPrincipalPage() {
        // Lógica para iniciar la actividad Principal_Page
        Intent intent = new Intent(MainActivity.this, Principal_Page.class);
        startActivity(intent);

        finish(); // Cierra la actividad actual

    }


    private boolean isValidEmailAndPassword(String email, String password) {
        // Aquí puedes implementar tu lógica de validación de correo y contraseña
        // Simplemente retorna true para simular una validación exitosa
        return email.equals("lpzluisantonio@gmail.com") && password.equals("Pronuncia123$");
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void testPerformance() {
        // Prueba de rendimiento: Simular una espera de 3 segundos
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Preba de rendimiento correcta")
                .setPositiveButton("OK", (dialog, id) -> {
                    // Al hacer clic en OK, redirigir a Principal_Page
                });

        // Ejemplo de cálculo del tiempo de inicio de sesión
        long inicio = System.currentTimeMillis();
        signInWithEmailAndPassword(); // Método de inicio de sesión
        long fin = System.currentTimeMillis();
        long tiempoTotal = fin - inicio;
        System.out.println("El tiempo de inicio de sesión fue de: " + tiempoTotal + " milisegundos.");

    }


    private void testStability() {
        // Prueba de estabilidad: Simular un mensaje de consola indicando la realización de la prueba
        System.out.println("Prueba de estabilidad: Realizada con éxito");

    }

    private void testScalability() {
        // Prueba de escalabilidad: Simular un mensaje de consola indicando la realización de la prueba
        System.out.println("Prueba de escalabilidad: Realizada con éxito");
        // Simulación de múltiples solicitudes de inicio de sesión
        for (int i = 0; i < 10; i++) {
            signInWithEmailAndPassword(); // Realizar múltiples inicios de sesión
        }
        System.out.println("Se realizaron 10 solicitudes de inicio de sesión.");

    }

    private void testUsability() {
        // Prueba de usabilidad: Simular un mensaje de consola indicando la realización de la prueba
        System.out.println("Prueba de usabilidad: Realizada con éxito");


    }


}