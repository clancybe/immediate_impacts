package com.example.immediateimpacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class AddPurchase extends AppCompatActivity {
    private Button submit_purchase;
    public static final String EXTRA_TEXT = "com.example.immediateimpacts.EXTRA_TEXT";
    private String radioType = " ";
    private String newId;

    private Spinner spinner_year;
    private Spinner spinner_month;
    private Spinner spinner_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        submit_purchase = (Button) findViewById(R.id.submit_purchase);
        submit_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPurchase();
            }
        });

        spinner_year = (Spinner)findViewById(R.id.spinner_year);
        spinner_month = (Spinner)findViewById(R.id.spinner_month);
        spinner_day = (Spinner)findViewById(R.id.spinner_day);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sheet.best/api/sheets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GSheetsApi gSheetsApi = retrofit.create(GSheetsApi.class);

        Call<List<PostPurchase>> dataSet = gSheetsApi.getPurchPosts();
        dataSet.enqueue(new Callback<List<PostPurchase>>() {
            @Override
            public void onResponse(Call<List<PostPurchase>> call, Response<List<PostPurchase>> response) {
                if (!response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                    //intent.putExtra(EXTRA_TEXT, response.code());
                    intent.putExtra(EXTRA_TEXT, "Non Successful Get (finding id)");
                    startActivity(intent);
                    return;
                }

                List<PostPurchase> posts = response.body();

                PostPurchase recentPost = posts.get(posts.size()-1);
                int prevId = Integer.parseInt(recentPost.getPurchId().substring(2));
                newId = "P." + (prevId+1);
                //newId = recentPost.getPurchId();
            }

            @Override
            public void onFailure(Call<List<PostPurchase>> call, Throwable t) {
                Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                //intent.putExtra(EXTRA_TEXT, response.code());
                intent.putExtra(EXTRA_TEXT, "Failure on Get ID Call");
                startActivity(intent);
                return;
            }
        });
    }

    private void submitPurchase() {

        boolean hasType = true;
        boolean hasName = true;

        String purchMonth = String.valueOf(spinner_month.getSelectedItem());
        String purchDay = String.valueOf(spinner_day.getSelectedItem());
        String purchYear = String.valueOf(spinner_year.getSelectedItem());

        String datePat = "MM-dd-yyyy";
        DateFormat df = new SimpleDateFormat(datePat);
        String today = df.format(Calendar.getInstance().getTime());
        if (purchMonth.equals("Month")) purchMonth = today.substring(0,2);
        if (purchDay.equals("Day")) purchDay = today.substring(3,5);
        if (purchYear.equals("Year")) purchYear = today.substring(6,10);

        String purchId = newId;
        String purchDate = purchMonth + "-" + purchDay + "-" + purchYear;
        String purchType = radioType;
        String purchItemPrice = ((EditText) findViewById(R.id.input_purchItemPrice)).getText().toString();
        String purchShipPrice = ((EditText) findViewById(R.id.input_purchShipPrice)).getText().toString();
        String purchTax = ((EditText) findViewById(R.id.input_purchTax)).getText().toString();
        String purchItemName = ((EditText) findViewById(R.id.input_purchItemName)).getText().toString();
        String purchQuantity = ((EditText) findViewById(R.id.input_purchQuantity)).getText().toString();
        String purchDescription = ((EditText) findViewById(R.id.input_purchDescription)).getText().toString();
        String purchSupplier = ((EditText) findViewById(R.id.input_purchSupplier)).getText().toString();

        if (!(purchType.length()>0)) hasType = false;
        if (!(purchItemPrice.length()>0)) purchItemPrice = "0";
        if (!(purchShipPrice.length()>0)) purchShipPrice = "0";
        if (!(purchTax.length()>0)) purchTax = "0";
        if (!(purchItemName.length()>0)) hasName = false;
        if (!(purchQuantity.length()>0)) purchQuantity = "0";
        if (!(purchDescription.length()>0)) purchDescription = "(none)";
        if (!(purchSupplier.length()>0)) purchSupplier = "(none)";


        if (!hasType) {
            Toast toast = Toast.makeText(getApplicationContext(), "A purchase type must be selected", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (!hasName) {
            Toast toast = Toast.makeText(getApplicationContext(), "Item Name cannot be left blank", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {

            PostPurchase post = new PostPurchase(purchId, purchDate, purchType, purchItemPrice, purchShipPrice, purchTax, purchItemName, purchQuantity, purchDescription, purchSupplier);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://sheet.best/api/sheets/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GSheetsApi gSheetsApi = retrofit.create(GSheetsApi.class);

            if (hasType && hasName) {
                Call<PostPurchase> call = gSheetsApi.submitPurchase(post);
                call.enqueue(new Callback<PostPurchase>() {
                    @Override
                    public void onResponse(Call<PostPurchase> call, Response<PostPurchase> response) {
                        if (!response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                            //intent.putExtra(EXTRA_TEXT, response.code());
                            intent.putExtra(EXTRA_TEXT, "Non Successful Post");
                            startActivity(intent);
                            return;
                        }

                        Intent intent = new Intent(getApplicationContext(), Purchases.class);
                        startActivity(intent);

                    }

                    //THIS IS BAD CODE THAT YOU NEED TO FIX
                    //currently, the code is successfully posting, but it thinks it is failing
                    //as a TEMPORARY fix, on failure, it sends you back to the purchases page, as if it was successful
                    @Override
                    public void onFailure(Call<PostPurchase> call, Throwable t) {
                        Intent intent = new Intent(getApplicationContext(), Purchases.class);
                        //intent.putExtra(EXTRA_TEXT, t.getMessage());
                        //intent.putExtra(EXTRA_TEXT, "2");
                        startActivity(intent);
                    }
                });
            }
        }



    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.option_PFD:
                if (checked) radioType = "Purchase for Distribution";
                break;
            case R.id.option_PPF:
                if (checked) radioType = "Printing/Postage Fees";
                break;
            case R.id.option_Reimb:
                if (checked) radioType = "Reimbursement";
                break;
            case R.id.option_Other:
                if (checked) radioType = "Other Expense";
                break;
        }
    }

}