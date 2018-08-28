package com.example.pc.mmsr_reader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pc.mmsr_reader.Activity.CreateAccountActivity;
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
        myDb = new DatabaseHandler(context);
        IfError = "Success";

    }

    @Override
    protected void onPreExecute() {
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

    public void parseJsonResponse(JSONArray response) {
        Log.e("IfError", IfError);
        if (IfError.equals("Fail")) {
            Toast.makeText(context, "Wrong email and password,Please try again", Toast.LENGTH_LONG).show();
        } else {
            try {
                MyProfileActivity.reader = new Reader();
                Reader reader = MyProfileActivity.reader;
                JSONArray arrayStory = response;
                for (int i = 0; i < arrayStory.length(); i++) {
                    JSONObject storyObject = (JSONObject) response.get(i);
                    String userID = storyObject.getString("userID");
                    String userName = storyObject.getString("userName");
                    String email = storyObject.getString("email");
                    String userDOB = storyObject.getString("userDOB");
                    String points = storyObject.getString("points");
                    reader.setUserID(userID);
                    reader.setUserName(userName);
                    reader.setEmail(email);
                    reader.setUserDOB(userDOB);
                    reader.setPoints(points);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Log.e("userID", userID);
                    Log.e("userName", userName);
                    Log.e("email", email);
                    Log.e("userDOB", userDOB);
                    Log.e("points", points);

                    editor.putString("userID", userID);
                    editor.putString("userName", userName);
                    editor.putString("email",email);
                    editor.putString("userDOB",userDOB);
                    editor.putString("points",points);
                    editor.apply();
                    myDb.deleteExistingRecordInReaderTable();
                    boolean isInserted2 = myDb.addReaderProfile(reader);
                    savePersonalData();
                }
                Toast.makeText(context, "Sign in successfully", Toast.LENGTH_LONG).show();
//                Log.e("contributorname", contributor.getName());
//                Log.e("contributoremail", contributor.getEmail());
//                Log.e("contributordob", contributor.getDob());
//                Log.e("contributorlanguagecode", contributor.getLanguageCode());

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                try {
                    LoginActivity.loginActivity.finish();
                    CreateAccountActivity.createAccountActivity.finish();
                } catch (NullPointerException e) {
                    Log.e("LoginVerification", e.toString());
                } catch (Exception e) {
                    Log.e("LoginVerification", e.toString());
                }

            } catch (JSONException e) {
                Log.e("error3", e.getMessage().toString());
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }
    public void savePersonalData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences((context));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoginStatus", "LogIn");
        editor.apply();
    }
}
