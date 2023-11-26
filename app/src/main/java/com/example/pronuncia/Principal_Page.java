package com.example.pronuncia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




public class Principal_Page extends AppCompatActivity {
    private SessionsClient sessionsClient;
    private SessionName session;

    private static final int REC_CODE_SPEECH_INPPUT = 100;
    private TextView EntradaVoz;
    private ImageButton btnHablar;

    private TextView btnCerrar;
    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient mGoogleSignInClient;

    private LinearLayout chatLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);
        firebaseAuth = FirebaseAuth.getInstance();
//        EntradaVoz = findViewById(R.id.textoEntrada);
        chatLayout = findViewById(R.id.chatLayout);
        btnHablar = findViewById(R.id.mic);
        btnCerrar = findViewById(R.id.cerrarSesion);

        btnCerrar.setOnClickListener(view -> logOut());
        btnHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarEntradaVoz();
            }
        });

        try {
            InputStream stream = getResources().openRawResource(R.raw.dialogflow_credentials); // Reemplaza con el nombre de tu archivo JSON de credenciales
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();
            if (projectId != null){
                sessionsClient = SessionsClient.create(SessionsSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build());
                session = SessionName.of(projectId, UUID.randomUUID().toString());
            }else {
                Log.e("Error", "ID");
            }

        } catch (Exception e) {
            Log.e("Error","Error project id null");
            e.printStackTrace();
        }


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
        String entradaTexto = null;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REC_CODE_SPEECH_INPPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                entradaTexto = result.get(0); // Obtén el texto reconocido
//                EntradaVoz.setText(entradaTexto); // Muestra el texto en el TextView



            }
// Crea una instancia de LanguageTool para el inglés estadounidense

            }

        addMessage(entradaTexto, true);
        detectarIntent(entradaTexto);
        }

    private void addMessage(String message, boolean isUser) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int paddingPixel = 16;
        float density = getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        textView.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

        params.setMargins(16, 16, 16, 16);

        if (isUser) {
            textView.setBackgroundResource(R.drawable.user_message_background);
            textView.setGravity(Gravity.END);
        } else {
            textView.setBackgroundResource(R.drawable.bot_message_background);
            textView.setGravity(Gravity.START);
        }

        textView.setLayoutParams(params);
        textView.setText(message);

        chatLayout.addView(textView); // Añadir el TextView al layout del chat
    }


    private void detectarIntent(String texto) {



        if(session != null){



            TextInput.Builder textInput = TextInput.newBuilder().setText(texto).setLanguageCode("es"); // Cambia el código de idioma según corresponda
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentRequest.Builder builder = DetectIntentRequest.newBuilder();
            builder.setSession(session.toString());
            builder.setQueryInput(queryInput);
            DetectIntentRequest request = builder
                    .build();

            new Thread(() -> {
                try {
                    DetectIntentResponse response = sessionsClient.detectIntent(request);
                    // Procesar la respuesta de Dialogflow
                    String respuesta = response.getQueryResult().getFulfillmentText();
                    runOnUiThread(() -> addMessage(respuesta, false));
                } catch (Exception e) {
                    Log.e("Error","Error client");
                    e.printStackTrace();
                }
            }).start();
        }else{
            Log.e("Error", "Error de client Null");
        }

    }

    private void mostrarRespuesta(final String respuesta) {
        runOnUiThread(() -> EntradaVoz.setText(respuesta));
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
