package dev.jojo.plantmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dev.jojo.plantmonitor.core.Globals;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

//

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(IntroActivity.this);

        Boolean hasRegistered = sp.getBoolean(Globals.PREF_HAS_REGISTERED, false);

        if(!hasRegistered){


        }
        else{
            startActivity(new Intent(getApplicationContext(),MonitorActivity.class));
            finish();
        }



    }

    @Override
    public void onResume(){
        super.onResume();
    }
}


