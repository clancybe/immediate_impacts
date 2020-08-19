package com.example.immediateimpacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddDistribution extends AppCompatActivity {

    private Button submit_distribution;
    public static final String EXTRA_TEXT = "com.example.immediateimpacts.EXTRA_TEXT";
    private String newId;

    private Spinner spinner_dist_year;
    private Spinner spinner_dist_month;
    private Spinner spinner_dist_day;
    private Spinner spinner_dist_hour;
    private Spinner spinner_dist_minute;
    private Spinner spinner_dist_ampm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_distribution);

        submit_distribution = (Button) findViewById(R.id.submit_distribution);
        submit_distribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDistribution();
            }
        });

        spinner_dist_year = (Spinner)findViewById(R.id.spinner_dist_year);
        spinner_dist_month = (Spinner)findViewById(R.id.spinner_dist_month);
        spinner_dist_day = (Spinner)findViewById(R.id.spinner_dist_day);
        spinner_dist_hour = (Spinner)findViewById(R.id.spinner_dist_hour);
        spinner_dist_minute = (Spinner)findViewById(R.id.spinner_dist_minute);
        spinner_dist_ampm = (Spinner)findViewById(R.id.spinner_dist_ampm);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sheet.best/api/sheets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GSheetsApi gSheetsApi = retrofit.create(GSheetsApi.class);

        Call<List<PostDistribution>> dataSet = gSheetsApi.getDistPosts();
        dataSet.enqueue(new Callback<List<PostDistribution>>() {
            @Override
            public void onResponse(Call<List<PostDistribution>> call, Response<List<PostDistribution>> response) {
                if (!response.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                    intent.putExtra(EXTRA_TEXT, "Non Successful Get (finding id)");
                    startActivity(intent);
                    return;
                }

                List<PostDistribution> posts = response.body();

                PostDistribution recentPost = posts.get(posts.size()-1);
                int prevId = Integer.parseInt(recentPost.getDistId().substring(2));
                newId = "D." + (prevId + 1);
            }

            @Override
            public void onFailure(Call<List<PostDistribution>> call, Throwable t) {
                Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                intent.putExtra(EXTRA_TEXT, "Failure on Get ID Call");
                startActivity(intent);
                return;
            }
        });
    }

    public void submitDistribution() {

        boolean hasName = true;

        String distMonth = String.valueOf(spinner_dist_month.getSelectedItem());
        String distDay = String.valueOf(spinner_dist_day.getSelectedItem());
        String distYear = String.valueOf(spinner_dist_year.getSelectedItem());
        String distHour = String.valueOf(spinner_dist_hour.getSelectedItem());
        String distMin = String.valueOf(spinner_dist_minute.getSelectedItem());
        String distAmPm = String.valueOf(spinner_dist_ampm.getSelectedItem());

        String datePat = "MM-dd-yyyy hh:mm a";
        DateFormat df = new SimpleDateFormat(datePat);
        String today = df.format(Calendar.getInstance().getTime());

        if (distMonth.equals("Month")) distMonth = today.substring(0,2);
        if (distDay.equals("Day")) distDay = today.substring(3,5);
        if (distYear.equals("Year")) distYear = today.substring(6,10);
        if (distHour.equals("Hour")) distHour = today.substring(11,13);
        if (distMin.equals("Minute")) distMin = today.substring(14,16);
        if (distAmPm.equals("am/pm")) distAmPm = today.substring(17);

        String distId = newId;
        String distDate = distMonth + "-" + distDay + "-" + distYear;
        String distTime = distHour + ":" + distMin + " " + distAmPm;
        String distDonee = ((EditText) findViewById(R.id.input_distDonee)).getText().toString();
        String distStreet = ((EditText) findViewById(R.id.input_distStreet)).getText().toString();
        String distCity = ((EditText) findViewById(R.id.input_distCity)).getText().toString();
        String distState = ((EditText) findViewById(R.id.input_distState)).getText().toString();
        String distItemName = ((EditText) findViewById(R.id.input_distItemName)).getText().toString();
        String distQuantity = ((EditText) findViewById(R.id.input_distQuantity)).getText().toString();
        String distDescription = ((EditText) findViewById(R.id.input_distDescription)).getText().toString();

        if (!(distDonee.length()>0)) distDonee = "(none)";
        if (!(distStreet.length()>0)) distStreet = "(none)";
        if (!(distCity.length()>0)) distCity = "(none)";
        if (!(distState.length()>0)) distState = "(none)";
        if (!(distItemName.length()>0)) hasName = false;
        if (!(distQuantity.length()>0)) distQuantity = "0";
        if (!(distDescription.length()>0)) distDescription = "(none)";

        if (!hasName) {
            Toast toast = Toast.makeText(getApplicationContext(), "Item Name cannot be left blank", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            PostDistribution post = new PostDistribution(distId, distDate, distTime, distDonee, distStreet, distCity, distState, distItemName, distQuantity, distDescription);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://sheet.best/api/sheets/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GSheetsApi gSheetsApi = retrofit.create(GSheetsApi.class);

            Call<PostDistribution> call = gSheetsApi.submitDistribution(post);
            call.enqueue(new Callback<PostDistribution>() {
                @Override
                public void onResponse(Call<PostDistribution> call, Response<PostDistribution> response) {
                    if (!response.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
                        intent.putExtra(EXTRA_TEXT, "Non Successful Post");
                        startActivity(intent);
                        return;
                    }

                    Intent intent = new Intent(getApplicationContext(), Distributions.class);
                    startActivity(intent);
                }

                //THIS IS BAD CODE THAT YOU NEED TO FIX
                //currently, the code is successfully posting, but it thinks it is failing
                //as a TEMPORARY fix, on failure, it sends you back to the distributions page, as if it was successful
                @Override
                public void onFailure(Call<PostDistribution> call, Throwable t) {
                    Intent intent = new Intent(getApplicationContext(), Distributions.class);
                    startActivity(intent);
                }
            });
        }
    }
}