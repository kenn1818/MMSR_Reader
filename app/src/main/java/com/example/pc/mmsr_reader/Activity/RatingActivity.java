package com.example.pc.mmsr_reader.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pc.mmsr_reader.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    private static final String INSERT_RATING_URL = "http://tarucmmsr.pe.hu/insert_rating.php";
    private static final String DEFAULT = "N/A";


    private int userId;
    private String bookId;
    private String bookTitle;

    private TextView tvTitle;
    private RatingBar rb;
    private Button btnRateSubmit;

    private String username;
    private float rateValue = -1;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = df.format(c.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userid = sharedPreferences.getString("userID", "");
        userId = Integer.parseInt(userid);
        Log.e("userName",userid);
        tvTitle = (TextView) findViewById(R.id.tvRateTitle);
        rb = (RatingBar) findViewById(R.id.ratingBar);
        btnRateSubmit = (Button) findViewById(R.id.btnRateSubmit);

        bookId = getIntent().getStringExtra("STORYBOOKID");
        bookTitle = getIntent().getStringExtra("TITLE");


        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = rating;
            }
        });

        tvTitle.setText(bookTitle);
    }

    public void rateStory(View view) {
        if (rateValue == -1) {
            Toast.makeText(this, "Please rate the story first.", Toast.LENGTH_SHORT).show();
        } else {
            makeServiceCall(this, INSERT_RATING_URL, userId, bookId, formattedDate, formattedDate, rateValue);
        }
    }

    public void makeServiceCall(final Context context, String url, final int userId, final String bookId, final String dateStartedReading, final String dateFinishedReading, final float rateValue) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("1")) {
                                Toast.makeText(context, "Successfully rated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to Rate!", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", String.valueOf(userId));
                    params.put("storybookID", bookId);
                    params.put("dateStartedReading", dateStartedReading);
                    params.put("dateFinishedReading", dateFinishedReading);
                    params.put("rateValue", String.valueOf(rateValue));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        }
    }
}
