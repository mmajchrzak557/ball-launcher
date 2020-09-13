package com.example.inzynierka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class ManualSteeringActivity extends AppCompatActivity {
    private  SeekBar sbChangeMotor1Value;
    private  SeekBar sbChangeMotor2Value;
    private  SeekBar sbServo;
    private CheckBox cbMotorSync;
    private TextView tvMotor1;
    private TextView tvMotor2;
    private TextView tvServo;
    private boolean power = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_steering);
        Button btnPowerOn = findViewById(R.id.btnPowerOn);
        Button btnPowerOff = findViewById(R.id.btnPowerOff);
        Button btnFire = findViewById(R.id.btnFire);
        cbMotorSync = findViewById(R.id.cbMotorSynchronization);
        tvMotor1 = findViewById(R.id.tvMotor1);
        tvMotor2 = findViewById(R.id.tvMotor2);
        tvServo = findViewById(R.id.tvServo);
        sbServo = findViewById(R.id.sbServo);
        sbChangeMotor1Value = findViewById(R.id.sbChangeMotor1Value);
        sbChangeMotor2Value = findViewById(R.id.sbChangeMotor2Value);
        sbServo.setProgress(90);
        sbChangeMotor1Value.setProgress(0);
        sbChangeMotor2Value.setProgress(0);
        btnPowerOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                power = true;
                sendMessage(1);
            }
        });
        btnPowerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(0);
                power = false;
            }
        });
        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(2);
            }
        });
        sbChangeMotor1Value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    if (cbMotorSync.isChecked())
                        sbChangeMotor2Value.setProgress(progress);
                    sendMessage(1);
                }
                int percentValue = Math.round((float) (progress/2.55));
                tvMotor1.setText("Silnik 1: " + percentValue + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        sbChangeMotor2Value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    if (cbMotorSync.isChecked())
                        sbChangeMotor1Value.setProgress(progress);
                    sendMessage(1);
                }
                int percentValue = Math.round((float) (progress/2.55));
                tvMotor2.setText("Silnik 2: " + percentValue + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
            ((BaseApplication) getBaseContext().getApplicationContext()).sendData(0, 0, 90, 0);
            if(!((BaseApplication) getBaseContext().getApplicationContext()).mmMyBluetoothService.connected) {
            Toast.makeText(this, "Nie podłączona urządzenia", Toast.LENGTH_LONG).show();
        }
        sbServo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendMessage(1);
                tvServo.setText("Serwo: " + sbServo.getProgress() + (char)176);
            }
        });
        sbServo.setProgress(90);
    }

    private int[] getProgressValues(){
        int motor1 = sbChangeMotor1Value.getProgress();
        int motor2 = sbChangeMotor2Value.getProgress();
        int servo  = sbServo.getProgress();
        return new int[]{motor1, motor2, servo};
    }

    private void sendMessage(int state){  // 0 - turn off, 1 - setup 2 - fire
        if(!power) return;
        int[] data = getProgressValues();
        BaseApplication c =  (BaseApplication) getBaseContext().getApplicationContext();
        switch(state){
            case 0:
                c.sendData(0, 0, 90, 0);
                break;
            case 1:
                c.sendData(data[0], data[1], data[2], 0);
                break;
            case 2:
                c.sendData(data[0], data[1], data[2], -1);
                break;
        }

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.cbMotorSynchronization) {
            if (checked) {
                SeekBar sb1 = findViewById(R.id.sbChangeMotor1Value);
                SeekBar sb2 = findViewById(R.id.sbChangeMotor2Value);
                sb2.setProgress(sb1.getProgress());
            }
        }
    }
}
