package com.example.immediateimpacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDistributions();
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPurchases();
            }
        });

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
    }

    public void openDistributions() {
        Intent intent = new Intent(this, Distributions.class);
        startActivity(intent);
    }

    public void openPurchases() {
        Intent intent = new Intent(this, Purchases.class);
        startActivity(intent);
    }

    public void openMap() {
        Toast toast = Toast.makeText(getApplicationContext(), "Mapping function not yet implemented", Toast.LENGTH_SHORT);
        toast.show();
    }
}
