package com.example.inzynierka;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Connection extends AppCompatActivity {
    public final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    EditText etAddress;
    BaseApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        context = (BaseApplication) getBaseContext().getApplicationContext();
        etAddress = findViewById(R.id.etAddress);
        etAddress.setText(context.mmMyBluetoothService.MODULE_MAC);
        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!adapter.isEnabled()) {
                        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
                    } else {
                        if (checkMAC(etAddress.getText().toString())) {
                            context.mmMyBluetoothService.
                                    initiateBluetoothProcess(adapter, etAddress.getText().toString());
                            checkConnection(context);
                        } else{
                            Toast.makeText(context, "Błędny adres MAC", Toast.LENGTH_LONG).show();
                        }
                    }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            Toast.makeText(this, "Włączono Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean checkMAC (String MAC) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        if(MAC.length() != 17) return false;
        for(int i = 0; i < MAC.length(); i++) {
            char c = MAC.charAt(i);
            if ((i+1)%3 != 0 && alphabet.indexOf(c) < 0) return false;
            if((i+1)%3 == 0 && c != 58) return false;
        }
        return true;
    }

    public void checkConnection(final Context context){
        if (((BaseApplication) context.getApplicationContext()).mmMyBluetoothService.connected) {
            Toast.makeText(context, "Nawiązano połączenie", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Nie nawiązano połączenia", Toast.LENGTH_LONG).show();

        }
    }
}
