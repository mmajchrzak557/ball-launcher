package com.example.inzynierka;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class AreaSelection extends AppCompatActivity {
    private CustomCanvas cc;
    private TextView tv;
    private TextView tvRotation;
    private TextView tvTime;
    private SeekBar sbRotation;
    private SeekBar sbTime;
    private CheckBox cbRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_selection);
        cc = findViewById(R.id.customCanvas);
        tv = findViewById(R.id.textView);
        tvRotation = findViewById(R.id.tvRotation);
        tvTime = findViewById(R.id.tvTime);
        sbRotation = findViewById(R.id.sbRotation);
        sbTime = findViewById(R.id.sbTime);
        cbRandom = findViewById(R.id.cbRandomShot);
        sbRotation.setProgress(sbRotation.getMax()/2);
        sbTime.setProgress(sbTime.getMax()/2);
        tvRotation.setText(sbRotation.getProgress() - sbRotation.getMax()/2 + " %");
        float time = ((float)sbTime.getProgress() + 10 )/ 10;
        tvTime.setText(time + " s");
        int dutyCycle = map((int) cc.distance, 1178, 1736, 70, 100);
        tv.setText(dutyCycle + "% , " + cc.angle + (char)176);
        cc.enable = true;
        if (!((BaseApplication) getBaseContext().getApplicationContext()).mmMyBluetoothService.connected){
            Toast.makeText(this, "Nie podłączono urządzenia", Toast.LENGTH_LONG).show();
        }
        final Button btnStartStop = findViewById(R.id.btnStart);
        btnStartStop.setBackgroundColor(Color.rgb(0, 210, 50));
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication context = (BaseApplication) getBaseContext().getApplicationContext();
                if (String.valueOf(btnStartStop.getText()).equals("Stop")){
                    btnStartStop.setText("Start");
                    btnStartStop.setBackgroundColor(Color.rgb(0, 210, 50));
                    context.sendData(0, 0, 90, 0);
                }
                else {
                    btnStartStop.setText("Stop");
                    btnStartStop.setBackgroundColor(Color.RED);
                    if(cbRandom.isChecked()){
                        int p1 = randomInt(70, 100);
                        int p2 = randomInt(70, 100);
                        int pos = randomInt(0, 180);
                        context.sendData(p1, p2, pos, 0);
                    }else {
                        int rot = getRotation(sbRotation);
                        int time = sbTime.getProgress() + 10;
                        int[] PWM = calculatePWM((int)cc.distance, rot);
                        int angle = map(cc.angle, 69, 111, 0, 180);
                        context.sendData(PWM[0], PWM[1], angle, time);
                    }
                }
            }
        });
        sbRotation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvRotation.setText(progress - sbRotation.getMax()/2 + " %");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float time = ((float) sbTime.getProgress() + 10 )/ 10;
                tvTime.setText(time + " s");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int dutyCycle = map((int) cc.distance, 1178, 1736, 70, 100);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tv.setText(dutyCycle + "% , " + cc.angle + (char)176);
                return true;
            case MotionEvent.ACTION_MOVE:
                tv.setText(dutyCycle + "% , " + cc.angle + (char)176);
                return false;
        }
        return false;
    }

    protected int[] calculatePWM(int distance, int rotation){
        int[] pwm = new int[2];
        int basePWM = map(distance, 1178, 1736, 70, 100);
        if(rotation < 0) {
            pwm[0] = basePWM - Math.abs(rotation)/2;
            pwm[1] = basePWM + Math.abs(rotation)/2;
        } else{
            pwm[0] = basePWM + Math.abs(rotation)/2;
            pwm[1] = basePWM - Math.abs(rotation)/2;
        }
        return pwm;
    }

    protected int map(int value, int inMin, int inMax, int outMin, int outMax){
        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    private int randomInt(int min, int max){
        return (int) (Math.random() * (max - min) + min);
    }

    protected int getRotation(SeekBar rotation){return rotation.getProgress() - rotation.getMax() / 2;}
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.cbRandomShot) {
            if (checked) {
                cc.enable = false;
                cc.circleX = cc.width / 2;
                cc.circleY = (int) (0.5 * cc.height);
                cc.invalidate();
            } else {
                cc.enable = true;
                cc.invalidate();
            }
        }
    }
}
