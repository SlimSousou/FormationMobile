package com.slim.sousou.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.slim.sousou.myapplication2.model.Device;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ImageView icon;
    private DrawerLayout drawerLayout;
    private  NavigationView navigationView;
    private EditText deviceName , deviceValue;
    private Button addDevice;
    private ListView listDevices;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        icon = findViewById(R.id.icon_home);
        drawerLayout  = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view_home);
        deviceName = findViewById(R.id.device_name);
        deviceValue = findViewById(R.id.device_value);
        addDevice = findViewById(R.id.add_device);
        listDevices = findViewById(R.id.listDevices);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        addDevice.setOnClickListener(v->{
            String nameDevice = deviceName.getText().toString();
            String valueDevice = deviceValue.getText().toString();
            HashMap<String,String> deviceMap = new HashMap<>();
            deviceMap.put("name", nameDevice);
            deviceMap.put("value",valueDevice);
            databaseReference.child("Devices").push().setValue(deviceMap);
            deviceName.setText("");
            deviceValue.setText("");
            deviceName.clearFocus();
            deviceValue.clearFocus();
            Toast.makeText(this, "new device added successfully!", Toast.LENGTH_SHORT).show();
        });

        ArrayList<String> deviceArrayList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeActivity.this,R.layout.list_item,deviceArrayList);
        listDevices.setAdapter(adapter);
        DatabaseReference deviceReference = firebaseDatabase.getReference().child("Devices");
        deviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deviceArrayList.clear();
                for (DataSnapshot devicesSnapshot: snapshot.getChildren()){
                    Device device = devicesSnapshot.getValue(Device.class);
                    deviceArrayList.add(device.getName()+" : "+device.getValue()) ;

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, ""+error , Toast.LENGTH_SHORT).show();
            }
        });

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.devices:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.profile:
                    startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });
    }

    private void navigationDrawer() {
        navigationView.setCheckedItem(R.id.devices);
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