package com.example.pc.mmsr_reader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.pc.mmsr_reader.Activity.LoginActivity;
import com.example.pc.mmsr_reader.Activity.MainActivity;
import com.example.pc.mmsr_reader.Class.Reader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 11/29/2017.
 */

public class createAccountVerificationGoogle extends AsyncTask<Void, Void, Void> {
    DatabaseHandler myDb;
    Context context;
    private ProgressDialog progressDialog;
    private String userDOB;
    private String email;
    private String password;
    private String name;

    public createAccountVerificationGoogle( Context context, String userDOB, String email, String password, String name) {
        this.context = context;
        this.userDOB = userDOB;
        this.email = email;
        this.password = password;
        this.name = name;
        progressDialog = new ProgressDialog(context);
        myDb = new DatabaseHandler(context);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Verifying...");
        if (!this.progressDialog.isShowing()) {
            this.progressDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
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
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (Integer.parseInt(response) == 1) {
                                Log.e("here", "new account created");
                                Reader reader = new Reader();
                                reader.setUserDOB(userDOB);
                                reader.setUserName(name);
                                reader.setEmail(email);
                                myDb.deleteExistingRecordInReaderTable();
                                boolean isInserted2 = myDb.addReaderProfile(reader);
                                Toast.makeText(context, "Welcome!" + name, Toast.LENGTH_LONG).show();
                                try {
                                    LoginActivity.loginActivity.finish();
                                    //createAccount.createAccountActivity.finish();
                                } catch (NullPointerException e) {
                                    Log.e("LoginVerification", e.toString());
                                } catch (Exception e) {
                                    Log.e("LoginVerification", e.toString());
                                }
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                // login.loginActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                            } else if (Integer.parseInt(response) == 0) {
                                // Toast.makeText(context, "This email has been used,please try another", Toast.LENGTH_LONG).show();
                                Log.e("createAccountGoogle", "account existed,retrieving user data");
                                LoginVerification loginverification = new LoginVerification(context, email, password);
                                loginverification.execute();

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

                    params.put("userName", name);
                    params.put("password", "");
                    params.put("userDOB", "0000-00-00");
                    params.put("email", email);

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
