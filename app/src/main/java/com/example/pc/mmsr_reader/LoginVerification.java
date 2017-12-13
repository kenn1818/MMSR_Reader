package com.example.pc.mmsr_reader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pc.mmsr_reader.Activity.LoginActivity;
import com.example.pc.mmsr_reader.Activity.MainActivity;
import com.example.pc.mmsr_reader.Class.Reader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc on 11/29/2017.
 */

public class LoginVerification extends AsyncTask<Void, Void, Void> {
    DatabaseHandler myDb;
    Context context;
    private ProgressDialog progressDialog;
    String email;
    String password;
    String IfError;
    GoogleApiClient googleAPiClient;

    public LoginVerification(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;
        progressDialog = new ProgressDialog(context);
        myDb = new DatabaseHandler(context);
        IfError = "Success";
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Login....");
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
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        String getStoryUrl = "http://tarucmmsr.pe.hu/reader_login.php?email=" + email + "&password=" + password;
        JsonArrayRequest request = new JsonArrayRequest(getStoryUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray ErrorResponse = response;
                for (int i = 0; i < ErrorResponse.length(); i++) {
                    try {
                        JSONObject storyObject = (JSONObject) response.get(i);
                        String error = storyObject.getString("error");
                        Log.e("error", error);
                        IfError = error;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                parseJsonResponse(response);


                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                VolleyLog.e("here", error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void parseJsonResponse(JSONArray response) {
        Log.e("IfError", IfError);
        if (IfError.equals("Fail")) {
            try {
                Auth.GoogleSignInApi.signOut(googleAPiClient).setResultCallback(
                        new ResultCallback<com.google.android.gms.common.api.Status>() {
                            @Override
                            public void onResult(com.google.android.gms.common.api.Status status) {
                            }
                        });
            } catch (Exception e) {

            }
            Toast.makeText(context, "Wrong email and password,Please try again", Toast.LENGTH_LONG).show();
        } else {
            try {
                Reader reader = new Reader();
                JSONArray arrayStory = response;
                for (int i = 0; i < arrayStory.length(); i++) {
                    JSONObject storyObject = (JSONObject) response.get(i);
                    String name = storyObject.getString("name");
                    String email = storyObject.getString("email");
                    String dob = storyObject.getString("dob");
                    reader.setUserName(name);
                    reader.setEmail(email);
                    reader.setUserDOB(dob);
                    myDb.deleteExistingRecordInReaderTable();
                }
                Toast.makeText(context, "Sign in successfully", Toast.LENGTH_LONG).show();
                Log.e("contributorname", reader.getUserName());
                Log.e("contributoremail", reader.getEmail());
                Log.e("contributordob", reader.getUserDOB());

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

                try {
                    LoginActivity.loginActivity.finish();
                    //createAccount.createAccountActivity.finish();
                } catch (NullPointerException e) {
                    Log.e("LoginVerification", e.toString());
                } catch (Exception e) {
                    Log.e("LoginVerification", e.toString());
                }
                //  login.loginActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } catch (JSONException e) {
                Log.e("error3", e.getMessage().toString());
            }
        }
    }
}
