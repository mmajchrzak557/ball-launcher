package com.example.inzynierka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView tvInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.tvInfo);
        Button btnManualSteering = findViewById(R.id.btnManualSteering);
        btnManualSteering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManualSteeringActivity.class);
                startActivity(intent);
            }
        });
        Button btnAreaSelection = findViewById(R.id.btnAreaSelection);
        btnAreaSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AreaSelection.class);
                startActivity(intent);
            }
        });
        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Connection.class);
                startActivity(intent);
            }
        });
        if (((BaseApplication) getBaseContext().getApplicationContext()).mmMyBluetoothService.connected) {
            tvInfo.setText("Nawiązano połączenie");

        } else {
            tvInfo.setText("Nie udało się nawiązać połączenia");
        }

    }
}

