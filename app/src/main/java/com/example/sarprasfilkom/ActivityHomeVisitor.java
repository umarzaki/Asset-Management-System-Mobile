package com.example.sarprasfilkom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sarprasfilkom.model.SharedPrefManager;


public class ActivityHomeVisitor extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_visitor);
        sharedPrefManager = new SharedPrefManager(this);
        Toolbar toolbar = findViewById(R.id.toolbarHomeVisitor);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_2);
        NavHostFragment navHostFragment2 = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_2);
        NavInflater inflater = navHostFragment2.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.mobile_navigation);
        graph.setStartDestination(R.id.nav_home_visitor);

        navHostFragment2.getNavController().setGraph(graph);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home_visitor).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, mAppBarConfiguration);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logoutvisitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BtnLogout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("apakah anda yakin untuk untuk keluar dari aplikasi?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_ACTIVE, false);
                                sharedPrefManager.clearAll();
                                startActivity(new Intent(ActivityHomeVisitor.this, ActivityLogin.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

}