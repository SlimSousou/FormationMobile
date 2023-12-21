package com.slim.sousou.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private EditText fullName,email,cin,phone;
    private Button edit,logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser loggedUser;
    private DatabaseReference reference;
    private ImageView icon;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


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

        icon = findViewById(R.id.icon_profile);
        drawerLayout  = findViewById(R.id.drawer_layout_profile);
        navigationView = findViewById(R.id.navigation_view_profile);

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.devices:
                    startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.profile:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });

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
    private void navigationDrawer() {
        navigationView.setCheckedItem(R.id.profile);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        icon.setOnClickListener(v ->{
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorApp));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}