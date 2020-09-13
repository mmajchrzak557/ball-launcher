package com.example.inzynierka;

import android.app.Application;


public class BaseApplication extends Application {
    public MyBluetoothService mmMyBluetoothService;
    @Override
    public void onCreate()
    {
        super.onCreate();
        mmMyBluetoothService = new MyBluetoothService();
    }
    public void sendData(int motor1, int motor2, int servo, int time){
        if (this.mmMyBluetoothService.connected) {
            byte pwm1 = (byte) motor1;
            byte pwm2 = (byte) motor2;
            byte pos = (byte) servo;
            byte t = (byte) time;
            byte[] message = {pwm1, pwm2, pos, t};
            this.mmMyBluetoothService.myThread.write(message);
        }
    }

}
