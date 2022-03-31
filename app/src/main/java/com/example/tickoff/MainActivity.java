package com.example.tickoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RequestTask.OutResponse{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView username;
    private ProgressBar xpBar;
    private TextView xpStatus;

    private boolean loggingOut;
    private int sendDataTo;
    private DataToFragments dataToFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        init();
        //TODO: szerverről jön majd !! az xp
        xpBar.setProgress(25);
        xpStatus.setText("25/100");
        username.setText("username123");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Todos()).commit();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sendDataTo = R.id.main;
        RequestTask todos = new RequestTask(MainActivity.this, "http://10.0.2.2:5000/todo", "GET");
        todos.execute();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main:
                        RequestTask todos = new RequestTask(MainActivity.this, "http://10.0.2.2:5000/todo", "GET");
                        todos.execute();
                        sendDataTo = R.id.main;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Todos()).commit();
                        break;
                    case R.id.favorites:
                        sendDataTo = R.id.favorites;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Favorites()).commit();
                        break;
                    case R.id.profile:
                        RequestTask profile = new RequestTask(MainActivity.this, "http://10.0.2.2:5000/profile-data", "GET");
                        profile.execute();
                        sendDataTo = R.id.profile;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                        break;
                    case R.id.logout:
                        sendDataTo = R.id.logout;
                        SharedPreferences user = getSharedPreferences("TickOff", Context.MODE_PRIVATE);
                        user.edit().remove("login").commit();
                        user.edit().remove("pwd").commit();
                        loggingOut = true;
                        RequestTask logout = new RequestTask(MainActivity.this, "http://10.0.2.2:5000/logout", "GET");
                        logout.execute();
                        Intent login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void setdata(DataToFragments dataToFragments){
        this.dataToFragments = dataToFragments;
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.nav_header_username);
        xpBar = header.findViewById(R.id.nav_header_xp_bar);
        xpStatus = header.findViewById(R.id.nav_header_xp_status);

        loggingOut = false;
        sendDataTo = R.id.main;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences restartUser = getSharedPreferences("TickOff", Context.MODE_PRIVATE);
        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("email_or_username", restartUser.getString("login", ""));
        dataMap.put("password", restartUser.getString("pwd", ""));
        JSONObject dataJSON = new JSONObject(dataMap);
        RequestTask login = new RequestTask(MainActivity.this,"http://10.0.2.2:5000/login", "POST", dataJSON.toString());
        login.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!loggingOut){
            RequestTask logout = new RequestTask(MainActivity.this, "http://10.0.2.2:5000/logout", "GET");
            logout.execute();
        }
    }

    @Override
    public void response(Response response) {
        switch (sendDataTo){
            case R.id.favorites:
                dataToFragments.sendData(response);
                sendDataToReset();
                break;
            case R.id.profile:
                dataToFragments.sendData(response);
                sendDataToReset();
                break;
            case R.id.logout:
                Log.d("LOGOUT", response.getContent());
                sendDataToReset();
                break;
            default:
                Log.d("RES", String.valueOf(response == null));
                dataToFragments.sendData(response);
                sendDataToReset();
                break;

        }
    }

    private void sendDataToReset(){
        sendDataTo = -1;
    }
}