package com.slim.sousou.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private EditText fullName,email,cin,phone;
    private Button edit,logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser loggedUser;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.editFullName);
        email = findViewById(R.id.editEmail);
        cin = findViewById(R.id.editCin);
        phone = findViewById(R.id.editPhone);
        edit = findViewById(R.id.editProfile);
        logout = findViewById(R.id.logout);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loggedUser = firebaseAuth.getCurrentUser();
        reference = firebaseDatabase.getReference().child("Users").child(loggedUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameS = snapshot.child("fullName").getValue().toString();
                String emailS = snapshot.child("email").getValue().toString();
                String cinS = snapshot.child("cin").getValue().toString();
                String phoneS = snapshot.child("phone").getValue().toString();

                fullName.setText(fullNameS);
                email.setText(emailS);
                cin.setText(cinS);
                phone.setText(phoneS);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(v-> {
            SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember","false");
            editor.apply();
            startActivity(new Intent(ProfileActivity.this,SignInActivity.class));
        });

        edit.setOnClickListener(v->{
            fullName.setFocusableInTouchMode(true);
            cin.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);
            edit.setText("save");
            edit.setOnClickListener(v1 -> {
                String editFullName = fullName.getText().toString();
                String editCin = cin.getText().toString();
                String editPhone = phone.getText().toString();
                reference.child("fullName").setValue(editFullName);
                reference.child("cin").setValue(editCin);
                reference.child("phone").setValue(editPhone);
                Toast.makeText(this, "your data has been modified!", Toast.LENGTH_SHORT).show();
            });
        });

    }
}