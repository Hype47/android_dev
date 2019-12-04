package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonMapOverlay = findViewById(R.id.buttonMapOverlay);
        buttonMapOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapOverlayActivity.class);
                intent.putExtra("lat", -6.2640495);
                intent.putExtra("lon", 107.0835378);
                intent.putExtra("title","Sabtu Pagi, Jalur Arteri Karawang Ramai Lancar");
                startActivity(intent);
            }
        });

        Button buttonMapDesign2 = findViewById(R.id.buttonMapDesign2);
        buttonMapDesign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapDesign2.class);
                intent.putExtra("lat", -6.2640495);
                intent.putExtra("lon", 107.0835378);
                intent.putExtra("title", "Bottleneck di Kilometer 38 Jadi Penyebab Kepadatan di Tol Cikampek");
                intent.putExtra("snips","\" KARAWANG, KOMPAS.com \\u2014 Kepadatan arus lalu lintas di Kilometer (Km) 35 hingga Km 47 Tol Jakarta-Cikampek pada Sabtu " +
                        "(9/6/2018) terjadi akibat\\u00a0bottleneck atau penyempitan lebar jalan dari kondisi normal. Penyempitan terjadi di Km 38.\\\"Dari empat jalur, menjadi " +
                        "tiga jalur,\\\" kata Kasatlantas  Karawang AKP Arman Sahti.Karena itu, sejak pukul 09.00 WIB, Korlantas Polri memberlakukan contraflow di\\u00a0Km 35 " +
                        "hingga Km 47 tol Jakarta-Cikampek dari arah Jakarta menuju Bandung.Pihak kepolisian mengimbau seluruh pengguna jalan untuk selalu berhati-hati dalam berkendara, " +
                        "mematuhi rambu-rambu, dan mengikuti arahan petugas di lapangan.Sementara itu, dari pantauan di Km 57 tol Jakarta-Cikampek arah Jakarta menuju Bandung, arus lalu " +
                        "lintas tampak ramai lancar. Rest area Km 57 masih tampak lengang. Kendaraan didominasi minibus berpelat nomor B dan bus antarkota antarprovinsi (AKP).\"");
                startActivity(intent);
            }
        });
    }

    public void goToNext(View view) {

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);

        // To send values
        intent.putExtra("lat",12.9045003);
        intent.putExtra("lon",100.345);

        startActivity(intent);

    }


    public void goToMap (View view) {

        // To go to next activity
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        // To send values
        intent.putExtra("lat",51.5287718);
        intent.putExtra("lon",-0.2416811);

        // Activate the intent
        startActivity(intent);
    }





}


