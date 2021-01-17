package com.example.laharijan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.header);
        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance("https://lahari-jan-default-rtdb.firebaseio.com/").getReference().child("Name");

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Online", ServerValue.TIMESTAMP);
        userRef.child(mAuth.getCurrentUser().getUid()).child("Status").setValue(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Offline", ServerValue.TIMESTAMP);
        userRef.child(mAuth.getCurrentUser().getUid()).child("Status").setValue(map);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME) {
            System.out.println("\n\n\n\n\n\n\nKEYCODE_HOME\n\n\n\n\n\n\n");
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("\n\n\n\n\n\n\nKEYCODE_BACK\n\n\n\n\n\n\n");
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_MENU) {
            System.out.println("\n\n\n\n\n\n\nKEYCODE_MENU\n\n\n\n\n\n\n");
            return true;
        }
        return false;
    }
}