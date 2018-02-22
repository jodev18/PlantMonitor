package dev.jojo.plantmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jojo.plantmonitor.core.Globals;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.btnSavePlant) Button saveInfo;

    @BindView(R.id.etPlantName) EditText ePlantName;
    @BindView(R.id.etUserName) EditText eUserName;
    @BindView(R.id.etPlantSciName) EditText ePlantSci;

    private Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        h = new Handler(this.getMainLooper());

        ButterKnife.bind(this);

        setSaveInfo();

    }

    private void setSaveInfo(){

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String plantName = ePlantName.getText().toString();
                String plantSci = ePlantSci.getText().toString();
                String userName = eUserName.getText().toString();

                if(plantName.length() > 0
                        && plantSci.length() > 0
                        && userName.length() > 0){
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(IntroActivity.this);

                    SharedPreferences.Editor e = sp.edit();

                    e.putString("PLANT_NAME", plantName);
                    e.putString("PLANT_SCI", plantSci);
                    e.putString("USER_NAME", userName);
                    e.putBoolean(Globals.PREF_HAS_REGISTERED,true);

                    e.commit();

                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(), MonitorActivity.class));
                            finish();
                        }
                    },2000);
                }
                else{
                    Toast.makeText(getApplicationContext(),"User info saved!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}


