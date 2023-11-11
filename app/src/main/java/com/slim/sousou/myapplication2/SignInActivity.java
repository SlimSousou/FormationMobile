package com.slim.sousou.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    private TextView goToSignUP, goToForgetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        goToSignUP = findViewById(R.id.goToSignUp);
        goToForgetPass = findViewById(R.id.goToForgetPass);

        goToSignUP.setOnClickListener( v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
        goToForgetPass.setOnClickListener( v -> {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        });
    }
}