package com.example.pc.mmsr_reader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.mmsr_reader.Class.Reader;

public class MyProfileActivity extends AppCompatActivity {
    public static Reader reader = new Reader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }
}
