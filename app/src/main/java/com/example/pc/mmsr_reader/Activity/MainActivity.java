package com.example.pc.mmsr_reader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pc.mmsr_reader.Background_Process.GetLibraryAsync;
import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.Fragment.AboutUsFragment;
import com.example.pc.mmsr_reader.Fragment.LibraryFragment;
import com.example.pc.mmsr_reader.Fragment.MyStorybookFragment;
import com.example.pc.mmsr_reader.Fragment.PrivacyStatementFragment;
import com.example.pc.mmsr_reader.Fragment.ProfileFragment;
import com.example.pc.mmsr_reader.PermissionVerify;
import com.example.pc.mmsr_reader.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//       Intent intent = new Intent(this, SplashActivity.class);
//        startActivity(intent);

 //       Intent intent = new Intent(this, LoginActivity.class);
 //       startActivity(intent);

        PermissionVerify.verifyStoragePermissions(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_privacy_statement) {
            PrivacyStatementFragment privacyStatementFragment = new PrivacyStatementFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, privacyStatementFragment).commit();
        }else if(id == R.id.action_update){
            //TODO put the update library code
            long counter =0;
            DatabaseHandler mydb = new DatabaseHandler(getApplicationContext());
            counter = mydb.countExistingRecordInStorybookTable();
            if(counter>0){
                Toast.makeText(getApplicationContext(),"Number of Storybooks" + counter, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"No Storybooks Available", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_activity_profile){
            ProfileFragment profileFragment = new ProfileFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, profileFragment).commit();
        }else if (id == R.id.nav_about_us) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, aboutUsFragment).commit();
        } else if (id == R.id.nav_library) {
            LibraryFragment libraryFragment = new LibraryFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, libraryFragment).commit();
        } else if (id == R.id.nav_my_story_books) {
            MyStorybookFragment myStorybookFragment = new MyStorybookFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, myStorybookFragment).commit();
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
