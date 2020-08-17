package com.example.immediateimpacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Distributions extends AppCompatActivity {
    private TextView textViewDistributions;
    private Button add_distribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributions);

        add_distribution = (Button) findViewById(R.id.add_distribution);
        add_distribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDistribution();
            }
        });

        textViewDistributions = findViewById(R.id.json_distributions);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sheet.best/api/sheets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GSheetsApi gSheetsApi = retrofit.create(GSheetsApi.class);

        Call<List<PostDistribution>> call = gSheetsApi.getDistPosts();

        call.enqueue(new Callback<List<PostDistribution>>() {
            @Override
            public void onResponse(Call<List<PostDistribution>> call, Response<List<PostDistribution>> response) {
                if (!response.isSuccessful()) {
                    textViewDistributions.setText("Code: " + response.code());
                    return;
                }

                List<PostDistribution> posts = response.body();

                Collections.reverse(posts);

                for (PostDistribution post : posts) {
                    String content = "";
                    content += "ID: " + post.getDistId() + "\n\t";
                    content += "Date: " + post.getDistDate() + "\n\t";
                    content += "Time: " + post.getDistTime() + "\n\t";
                    content += "Donee: " + post.getDistDonee() + "\n\t";
                    content += "Street: " + post.getDistStreet() + "\n\t";
                    content += "City: " + post.getDistCity() + "\n\t";
                    content += "State: " + post.getDistState() + "\n\t";
                    content += "Item Name: " + post.getDistItemName() + "\n\t";
                    content += "Quantity: " + post.getDistQuantity() + "\n\t";
                    content += "Description: " + post.getDistDescription() + "\n\n";

                    textViewDistributions.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<PostDistribution>> call, Throwable t) {
                textViewDistributions.setText(t.getMessage());
            }
        });
    }

    public void openAddDistribution() {
        Intent intent = new Intent(this, AddDistribution.class);
        startActivity(intent);
    }
}
