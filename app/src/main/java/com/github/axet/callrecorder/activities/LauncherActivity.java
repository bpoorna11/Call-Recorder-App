package com.github.axet.callrecorder.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LauncherActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp1 = getSharedPreferences("sdata", LoginActivity.MODE_PRIVATE);
        if (sp1.getString("login", "no").equals("yes")) {
            //login value is yes, so start mainactivity
            //  personEmail=mAuth.getCurrentUser().getEmail().toString();
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        } else {
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        }
        }
    }

