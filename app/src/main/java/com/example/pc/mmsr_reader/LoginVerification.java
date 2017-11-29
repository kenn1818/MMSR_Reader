package com.example.pc.mmsr_reader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;

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
        return null;
    }
}
