package com.example.pronuncia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class Principal_Page extends AppCompatActivity {

    private static final int REC_CODE_SPEECH_INPPUT = 100;
    private TextView EntradaVoz;
    private ImageButton btnHablar;

    private TextView btnCerrar;
    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient mGoogleSignInClient;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);
        firebaseAuth = FirebaseAuth.getInstance();
        EntradaVoz = findViewById(R.id.textoEntrada);
        btnHablar = findViewById(R.id.mic);
        btnCerrar = findViewById(R.id.cerrarSesion);

        btnCerrar.setOnClickListener(view -> logOut());
        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarEntradaVoz();
            }
        });

    }

    private void logOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(Principal_Page.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }


    private void iniciarEntradaVoz() {
        //Inicializa el reconocimineto de voz
        Intent intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ingrese audio aqui");

        try {
            startActivityForResult(intent, REC_CODE_SPEECH_INPPUT);
        }catch (ActivityNotFoundException e){

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REC_CODE_SPEECH_INPPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String entradaTexto = result.get(0); // Obtén el texto reconocido
                EntradaVoz.setText(entradaTexto); // Muestra el texto en el TextView
            }

// Crea una instancia de LanguageTool para el inglés estadounidense

            }
        }


    private void signOut() {
        // Cerrar sesión en Firebase
        firebaseAuth.signOut();

        // Cerrar sesión en Google si se inició con Google
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                // Acción a realizar cuando se haya cerrado sesión en Google
                // Por ejemplo, redireccionar a la página de inicio de sesión
                Intent intent = new Intent(Principal_Page.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
            });
        } else {
            // Acción a realizar cuando se haya cerrado sesión con correo en Firebase
            // Por ejemplo, redireccionar a la página de inicio de sesión
            Intent intent = new Intent(Principal_Page.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual
        }
    }





}
