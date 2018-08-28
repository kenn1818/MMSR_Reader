package com.example.pc.mmsr_reader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pc.mmsr_reader.createAccountVerification;
import com.example.pc.mmsr_reader.Activity.CreateAccountActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ALVIN-PC on 14-Oct-17.
 */

public class createAccountVerification extends AsyncTask<Void, Void, Void> {
    Context context;
   // private ProgressDialog progressDialog;
    private String email;
    private String UserName;
    private String Status = "";
    private String password;
    private String Dob;
    public createAccountVerification(Context context, String email, String password,String UserName,String Dob) {
        this.context = context;
        this.email = email;
        this.password = password;
        this.UserName=UserName;
        this.Dob=Dob;
       // progressDialog = new ProgressDialog(context);

    }

    @Override
    protected void onPreExecute() {
//        progressDialog.setMessage("Verify...");
//        if (!this.progressDialog.isShowing()) {
//            this.progressDialog.show();
//        }
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        VerifyIDPassword();
        return null;
    }

    private void VerifyIDPassword() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String storybookUrl = "http://tarucmmsr.pe.hu/insert_reader.php";
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    storybookUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("response", response);
//                            if (progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
                            Status = response;
                            Log.e("Status", Status);
                            if (Integer.parseInt(response) == 1) {
                                Toast.makeText(context, "Your account was created!Please login again", Toast.LENGTH_LONG).show();
                                CreateAccountActivity.createAccountActivity.finish();
                            } else if (Integer.parseInt(response) == 2) {
                                Toast.makeText(context, "This email has been used,please try another", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("Error", "Something goes wrong");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    Log.e("userName",UserName);
                    Log.e("password", password);
                    Log.e("userDOB", Dob);
                    Log.e("email", email);
                    Log.e("points", "0");

                    params.put("userName",UserName);
                    params.put("password", password);
                    params.put("userDOB", Dob);
                    params.put("email", email);
                    params.put("points","0");
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
            e.printStackTrace();

        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("come to here d", "1");

    }
}

