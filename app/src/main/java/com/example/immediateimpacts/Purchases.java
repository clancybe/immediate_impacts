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

public class Purchases extends AppCompatActivity {
    private TextView textViewPurchases;
    private Button add_purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        add_purchase = (Button) findViewById(R.id.add_purchase);
        add_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPurchase();
            }
        });

        textViewPurchases = findViewById(R.id.json_purchases);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sheet.best/api/sheets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GSheetsApi gSheetsApi = retrofit.create(GSheetsApi.class);

        Call<List<PostPurchase>> call = gSheetsApi.getPurchPosts();

        call.enqueue(new Callback<List<PostPurchase>>() {
            @Override
            public void onResponse(Call<List<PostPurchase>> call, Response<List<PostPurchase>> response) {
                if (!response.isSuccessful()) {
                    textViewPurchases.setText("Code: " + response.code());
                    return;
                }

                List<PostPurchase> posts = response.body();

                Collections.reverse(posts);

                for (PostPurchase post : posts) {
                    String content = "";
                    content += "ID: " + post.getPurchId() + "\n\t";
                    content += "Date: " + post.getPurchDate() + "\n\t";
                    content += "Type: " + post.getPurchType() + "\n\t";
                    content += "Item Price: $" + post.getPurchItemPrice() + "\n\t";
                    content += "Shipping Price: $" + post.getPurchShipPrice() + "\n\t";
                    content += "Tax: $" + post.getPurchTax() + "\n\t";
                    content += "Item: " + post.getPurchItemName() + "\n\t";
                    content += "Quantity: " + post.getPurchQuantity() + "\n\t";
                    content += "Description: " + post.getPurchDescription() + "\n\t";
                    content += "Supplier: " + post.getPurchSupplier() + "\n\n";

                    textViewPurchases.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<PostPurchase>> call, Throwable t) {
                textViewPurchases.setText(t.getMessage());
            }
        });

    }

    public void openAddPurchase() {
        Intent intent = new Intent(this, AddPurchase.class);
        startActivity(intent);
    }
}
