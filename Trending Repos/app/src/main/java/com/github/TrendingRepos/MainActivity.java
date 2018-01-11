package com.github.TrendingRepos;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by BlackJack on 2017-12-31.
 */



                /*------ This is The Main Activity of this application-------*/
/*--It contains two Fragments, one to display The List of Repositories and The other for the Settings--*/
                /*--And Mangage the transactions between this two Fragments--*/
                /*it implements also the behaviour of the buttons (Settings,Trending) of The footer*/
public class MainActivity extends AppCompatActivity  {
    TrendingFragment trendingFragment;      //The First Fragment which contains the List of Repositories
    SettingsFragment settingsFragment;      //The Second Fragment which contains the he App's Settings
    FragmentTransaction fragmentTransaction;//to Manage The Transactions Between the two fragment above
    ImageButton bTrending,bSettings;        //Buttons of the footer
    TextView tvTrending,tvSettings;         //TextViews Below the buttons to describe them
    Toolbar toolbar;                        //The Header of the App used instead of the ActionBar because of its customizability



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /*Instanciate The  buttons and the Fragments*/
        bTrending = (ImageButton) findViewById(R.id.btrending);bTrending.setEnabled(false);
        bSettings = (ImageButton)findViewById(R.id.bsettings);
        tvTrending=  (TextView)findViewById(R.id.tvtrending);
        tvSettings=  (TextView)findViewById(R.id.tvsettings);
        trendingFragment = new TrendingFragment();
        settingsFragment = new SettingsFragment();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //replace the ActionBar by Costumized ToolBar
        //used the Support class to suport devices runing on Android 4 and below
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Load the first Fragment
        if (findViewById(R.id.fragment_container) != null) {
            fragmentTransaction =  getSupportFragmentManager().beginTransaction();
            // Add this fragment  in the fragment_container view
            fragmentTransaction.add(R.id.fragment_container, trendingFragment);
            fragmentTransaction.commit();
        }


        bTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change The Color of footer's buttons
                switchImageButtonColors();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in The fragment_container view with this Fragment
                fragmentTransaction.replace(R.id.fragment_container, trendingFragment);
                // Commit the transaction
                fragmentTransaction.commit();
            }
        });




        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchImageButtonColors();
                fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, settingsFragment);
                fragmentTransaction.commit();
            }
        });
    }


    public void switchImageButtonColors(){
        if(getResources().getColor(R.color.colorSelected )== tvTrending.getCurrentTextColor()){
            bSettings.setImageResource(R.drawable.ic_settings_selected);
            tvSettings.setTextColor(getResources().getColor(R.color.colorSelected ));
            bTrending.setImageResource(R.drawable.ic_star_not_selected);
            tvTrending.setTextColor(getResources().getColor(R.color.colorNotSelected ));
            bTrending.setEnabled(true);
            bSettings.setEnabled(false);
        }
        else{
            bSettings.setImageResource(R.drawable.ic_settings_not_selected);
            tvSettings.setTextColor(getResources().getColor(R.color.colorNotSelected ));
            bTrending.setImageResource(R.drawable.ic_star_selected);
            tvTrending.setTextColor(getResources().getColor(R.color.colorSelected ));
            bSettings.setEnabled(true);
            bTrending.setEnabled(false);

        }
    }


/** This section for the menu items to select the repositories per day or per week or by default per mounth*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ToolBar item clicks here
        Bundle bundle = new Bundle();
        int id = item.getItemId();
        switch (id) {
            case R.id.today:
                if(!item.isChecked()){
                    bundle.putInt("date", 1);
                    // Pass date parameter to the Fragment
                    trendingFragment = new TrendingFragment();
                    trendingFragment.setArguments(bundle);
                    fragmentTransaction =  getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, trendingFragment).commit();
                    item.setChecked(!item.isChecked());
                }
                break;
            case R.id.week:
                if(!item.isChecked()){
                    bundle.putInt("date", 7);
                    // Pass date parameter to the Fragment
                    trendingFragment = new TrendingFragment();
                    trendingFragment.setArguments(bundle);
                    fragmentTransaction =  getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, trendingFragment).commit();
                    item.setChecked(!item.isChecked());
                }
                break;
            case R.id.mounth:
                if(!item.isChecked()){
                    bundle.putInt("date", 30);
                    // Pass date parameter to the Fragment
                    trendingFragment = new TrendingFragment();
                    trendingFragment.setArguments(bundle);
                    fragmentTransaction =  getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, trendingFragment).commit();
                    item.setChecked(!item.isChecked());
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}