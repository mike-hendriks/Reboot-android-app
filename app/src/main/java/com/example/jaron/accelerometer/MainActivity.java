package com.example.jaron.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("testData");

    private TextView xText, yText, zText, iText, jText, mTextResult;
    private Sensor mySensor;
    private SensorManager SM;
    int i, j = 0;
    private boolean still_in_range, start_pushup = false;
    private Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        xText = (TextView)findViewById(R.id.idX);
        zText = (TextView)findViewById(R.id.idZ);
        yText = (TextView)findViewById(R.id.idY);
        iText = (TextView)findViewById(R.id.idI);
        jText = (TextView)findViewById(R.id.idJ);
        mTextResult = (TextView)findViewById(R.id.Database);
        btnSend = (Button)findViewById(R.id.btnSendSitUps);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);

        if(event.values[1] > 9){

            if(still_in_range == false) {
                i++;
                iText.setText(String.valueOf(i));
                still_in_range = true;
            }
        }else {
            still_in_range = false;
        }

        //werkt nog niet goed

        if(event.values[2] > 9.9 || start_pushup)
        {
            if(event.values[2] < 9)
            {
                j++;
                jText.setText(String.valueOf(j));
                start_pushup = false;
            }
            else
            {
                start_pushup = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //  CODE voor DATABASE.
    @Override
    protected void onStart() {
        super.onStart();

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTextResult.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String send = iText.getText().toString();

                mConditionRef.setValue(send);
                Log.d(send, "onClick: , ");
            }
        });
    }
}
