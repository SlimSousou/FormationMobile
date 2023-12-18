package com.slim.sousou.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {
    private Button goToSignIn,resetPassword;
    private EditText email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        goToSignIn = findViewById(R.id.goToSignInFromForget);
        resetPassword = findViewById(R.id.resetPassword);
        email = findViewById(R.id.emailForget);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(v->{
            firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgetActivity.this, "password Reset Email sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetActivity.this,SignInActivity.class));
                        finish();
                    }else{
                        Toast.makeText(ForgetActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(ForgetActivity.this,SignInActivity.class));
        });
    }
}