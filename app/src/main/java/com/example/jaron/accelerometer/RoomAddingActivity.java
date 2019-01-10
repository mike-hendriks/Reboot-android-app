package com.example.jaron.accelerometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomAddingActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonCancel, buttonDelete;
    TextView digit1, digit2, digit3, digit4;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_adding);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("workout");

        button0 = (Button)findViewById(R.id.button0);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);
        button6 = (Button)findViewById(R.id.button6);
        button7 = (Button)findViewById(R.id.button7);
        button8 = (Button)findViewById(R.id.button8);
        button9 = (Button)findViewById(R.id.button9);
        buttonDelete = (Button)findViewById(R.id.buttonDelete);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);


        digit1 = (TextView)findViewById(R.id.digit1);
        digit2 = (TextView)findViewById(R.id.digit2);
        digit3 = (TextView)findViewById(R.id.digit3);
        digit4 = (TextView)findViewById(R.id.digit4);

        button0.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(0);
            }
        });

        button1.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(1);
            }
        });

        button2.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(2);
            }
        });

        button3.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(3);
            }
        });

        button4.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(4);
            }
        });

        button5.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(5);
            }
        });

        button6.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(6);
            }
        });

        button7.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(7);
            }
        });

        button8.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(8);
            }
        });

        button9.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillDigits(9);
            }
        });

        buttonDelete.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDigits();
            }
        });

        buttonCancel.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomAddingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void FillDigits(int digit)
    {
        if(digit1.getText().equals("__"))
        {
            digit1.setText("" + digit);
            code += digit;
            return;
        }
        else if(digit2.getText().equals("__"))
        {
            digit2.setText("" + digit);
            code += digit;
            return;
        }
        else if(digit3.getText().equals("__"))
        {
            digit3.setText("" + digit);
            code += digit;
            return;
        }
        else if(digit4.getText().equals("__"))
        {
            digit4.setText("" + digit);
            code += digit;
        }
        //vergelijken met de database of de code klopt. Vervolgens doorgaan naar workout
        /*if(code == #Databasecode)
        {
            Intent intent = new Intent(this, )
        }*/
        else
        {
            digit1.setText("__");
            digit2.setText("__");
            digit3.setText("__");
            digit4.setText("__");
            code = "";
        }
    }

    public void DeleteDigits()
    {
        digit1.setText("__");
        digit2.setText("__");
        digit3.setText("__");
        digit4.setText("__");
        code = "";
    }
}


