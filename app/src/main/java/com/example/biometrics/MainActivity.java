package com.example.biometrics;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Executor executor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;

        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                    Log.d("Hola","Cancelado");
                    Toast.makeText(getApplicationContext(),"Cancelado",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(getApplicationContext(), SecurityActivity.class);
                startActivity(intent);
                //TODO: Called when a biometric is recognized.
                Log.d("Hola","Datos reconocidos");
                Toast.makeText(getApplicationContext(),"Datos reconocidos",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //TODO: Called when a biometric is valid but not recognized.
                Log.d("Hola","Datos no reconocidos");
                Toast.makeText(getApplicationContext(),"Datos no reconocidos",Toast.LENGTH_LONG).show();

            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Validacion con datos biometricos")
                .setSubtitle("Autenticacion en proceso.")
                .setDescription("Muestre sus datos biometricos para poder continuar")
                .setNegativeButtonText("Cancelar")
                .build();

        findViewById(R.id.authenticateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

    }
}
