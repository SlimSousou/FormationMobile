package com.slim.sousou.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private TextView goToSignUP, goToForgetPass;
    private EditText emailSignIn, passwordSignIn;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox remember;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        goToSignUP = findViewById(R.id.goToSignUp);
        goToForgetPass = findViewById(R.id.goToForgetPass);
        emailSignIn = findViewById(R.id.emailSignIn);
        passwordSignIn = findViewById(R.id.passwordSignIn);
        btnSignIn = findViewById(R.id.buttonSignIn);
        remember = findViewById(R.id.rememberMe);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //remember me
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox= preferences.getString("remember","");
        if (checkbox.equals("true")){
            startActivity(new Intent(SignInActivity.this,ProfileActivity.class));
        }else{
            Toast.makeText(this, "please login!", Toast.LENGTH_SHORT).show();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }else{
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });

        goToSignUP.setOnClickListener( v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
        goToForgetPass.setOnClickListener( v -> {
            startActivity(new Intent(SignInActivity.this,ForgetActivity.class));
        });
        btnSignIn.setOnClickListener(v -> {
            login(emailSignIn.getText().toString().trim(),passwordSignIn.getText().toString().trim());

        });
    }

    private void login(String email, String password) {
        progressDialog.setMessage("Please wait ...!");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else {
                    Toast.makeText(SignInActivity.this, "please verify that your data is correct !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user.isEmailVerified()){
            finish();
            startActivity(new Intent(SignInActivity.this,ProfileActivity.class));
        }
        else {
            Toast.makeText(this, "please check your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}