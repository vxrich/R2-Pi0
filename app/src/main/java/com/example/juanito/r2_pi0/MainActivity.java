package com.example.juanito.r2_pi0;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private BluetoothCommunication blueComm;

    private VocalTranslator translator;

    private JoystickTranslator trans = new JoystickTrigonometricTranslator();

    private final int SPEECH_RECOGNITION_CODE = 1;

    public String message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getApplicationContext();
        final Activity activity = this;

        blueComm = new BluetoothCommunication(context, activity);
        translator  = new VocalTranslator(blueComm);

        setContentView(R.layout.activity_main);

        // Creazione dei pulsanti e elementi visivi

        JoyStickView joy = (JoyStickView) findViewById(R.id.joy);
        ImageButton shutdown = (ImageButton) findViewById(R.id.shutdown);
        ImageButton music = (ImageButton) findViewById(R.id.music);
        ImageButton bluetooth = (ImageButton) findViewById(R.id.bluetooth);
        ImageButton vocal_control = (ImageButton) findViewById(R.id.vocal_control);

        joy.setOnJoystickMoveListener(new JoyStickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                blueComm.move(trans.getSpeed(power, angle));
                blueComm.rotate(trans.getRotation(power, angle));
            }
        }, 100);


        shutdown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   blueComm.shutdown();
                }
            });

        music.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   blueComm.makeSound();
                }
            });

        bluetooth.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //Apre l'activity con la lista dei device associati e trovati
                    blueComm.connection();
                }
            });

        vocal_control.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    startSpeechToText();

                }
            });
    }

    //Metodo per l'apertura degli intent dei comandi vocali
    private void startSpeechToText()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Padron Luke sono in ascolto...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Mi dispiace! Il riconoscimento vocale non e' supportato dal tuo dispositivo.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo per il riconoscimento del parlato e conversione in stringa
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    message = result.get(0);
                    translator.translate(message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


}
