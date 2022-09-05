package com.example.sarprasfilkom;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sarprasfilkom.model.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    NavController navController;
    DrawerLayout drawer;
    NavHostFragment navHostFragment;
    NavInflater inflater;
    SharedPrefManager sharedPrefManager;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPrefManager = new SharedPrefManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bn_main);
        TextView labelName = findViewById(R.id.appbarLabelName);
        TextView labelRole = findViewById(R.id.appbarLabelRole);
        labelName.setText(sharedPrefManager.getSPNama());
        labelRole.setText(sharedPrefManager.getSPJabatan());
        role = sharedPrefManager.getSPRole();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.mobile_navigation);
        graph.setStartDestination(R.id.nav_home);

        navHostFragment.getNavController().setGraph(graph);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_admin, R.id.nav_guide, R.id.nav_logout, R.id.nav_complaint, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();

        setUpNavigation();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_profile:
                        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment);
                        navHostFragment.getNavController().navigate(R.id.nav_profile);
                        break;
                    case R.id.nav_complaint:
                        navHostFragment = (NavHostFragment)getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment);
                        navHostFragment.getNavController().navigate(R.id.nav_complaint);
                        break;
                    case R.id.nav_home:
                        navHostFragment = (NavHostFragment)getSupportFragmentManager()
                                .findFragmentById(R.id.nav_host_fragment);
                        navHostFragment.getNavController().navigate(R.id.nav_home);
                        break;
                }
                return false;
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sidebar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                navHostFragment = (NavHostFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                navHostFragment.getNavController().navigate(R.id.nav_notification);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setUpNavigation(){
        bottomNavigationView = findViewById(R.id.bn_main);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_home:
                NavigationUI.onNavDestinationSelected(item, navController);
                Toast.makeText(this, "Main Menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_home_visitor:
                NavigationUI.onNavDestinationSelected(item, navController);
                break;
            case R.id.nav_admin:
                if (role.equals("1")) {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    Toast.makeText(this, "admin dashboard", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "Anda tidak memiliki akses ke fitur ini", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_guide:
                NavigationUI.onNavDestinationSelected(item, navController);
                Toast.makeText(this, "Panduan Pengguna", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("apakah anda yakin untuk untuk keluar dari aplikasi?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_ACTIVE, false);
                                        sharedPrefManager.clearAll();
                                        startActivity(new Intent(ActivityHome.this, ActivityLogin.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}