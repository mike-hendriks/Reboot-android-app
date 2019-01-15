package com.example.jaron.accelerometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResultActivity extends AppCompatActivity {

    TextView Totaal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Totaal = (TextView)findViewById(R.id.pushup);
        ReadMessage();
    }

    public void ReadMessage(){

        try {
            String Message;
            FileInputStream fileInputStream = openFileInput("hello_file");
            InputStreamReader inputSteamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputSteamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while((Message=bufferedReader.readLine()) !=null){
                stringBuffer.append(Message);
            }
            Totaal.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
