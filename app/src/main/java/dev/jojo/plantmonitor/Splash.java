package dev.jojo.plantmonitor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import dev.jojo.plantmonitor.core.Globals;

public class Splash extends AppCompatActivity {

    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        h = new Handler(this.getMainLooper());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Splash.this);

        final Boolean hasRegistered = sp.getBoolean(Globals.PREF_HAS_REGISTERED, false);

        Boolean hasApproved = sp.getBoolean(Globals.PREF_HAS_OBTAINED_PERMISSION,false);

        if(!hasApproved){
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ).withListener(new MultiplePermissionsListener() {

                @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor e = sp.edit();

                    e.putBoolean(Globals.PREF_HAS_OBTAINED_PERMISSION,true);
                    e.commit();

                    checkRegistered(hasRegistered);
                }
                @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
            }).check();
        }
        else{
            checkRegistered(hasRegistered);
        }


    }

    private void checkRegistered(Boolean hasRegistered){

        if(!hasRegistered){

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),IntroActivity.class));
                    finish();
                }
            },2000);

        }
        else{
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MonitorActivity.class));
                    finish();
                }
            },2000);

        }
    }
}
