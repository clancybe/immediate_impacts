package com.example.immediateimpacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_screen);

        Intent intent = getIntent();
        String text = intent.getStringExtra(AddPurchase.EXTRA_TEXT);

        TextView error_text = (TextView) findViewById(R.id.error_text);
        error_text.setText(text);
    }
}