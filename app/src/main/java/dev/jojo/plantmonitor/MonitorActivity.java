package dev.jojo.plantmonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jojo.plantmonitor.others.Constants;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.CommunicationCallback;
import me.itangqi.waveloadingview.WaveLoadingView;

public class MonitorActivity extends AppCompatActivity{

    @BindView(R.id.tvUserName) TextView tUser;
    @BindView(R.id.tvPlantName) TextView tPlant;
    @BindView(R.id.tvScientificName) TextView tSci;

    @BindView(R.id.waveWaterLevel) WaveLoadingView wvl;
    @BindView(R.id.graph1) GraphView g1;

    private Bluetooth bluetooth;
    private Integer x1 = 3, x2 = 3, x3 = 3;

    private LineGraphSeries<DataPoint> lSeries1;
    private LineGraphSeries<DataPoint> lSeries2;
    private LineGraphSeries<DataPoint> lSeries3;

    private List<Double> HUM_LIST = new ArrayList<>();
    private List<Double> TEMP_LIST = new ArrayList<>();
    private List<Double> SOIL = new ArrayList<>();
    private List<Double> WATER = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_monitor);

        ButterKnife.bind(this);
        initPreferences();

        bluetooth  = new Bluetooth(MonitorActivity.this);
//        if(!bluetooth.isEnabled())
//            bluetooth.enable();

        initBluetooth();
        initGraph();
    }

    private void initGraph(){

        lSeries1 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 0),
                new DataPoint(2, 0)
        });

        lSeries2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 0),
                new DataPoint(2, 0)
        });
        lSeries3 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 0),
                new DataPoint(2, 0)
        });

        lSeries1.setTitle("Humidity");
        lSeries1.setColor(Color.BLUE);
        lSeries1.setDrawDataPoints(true);

        lSeries2.setTitle("Temp");
        lSeries2.setColor(Color.MAGENTA);
        lSeries2.setDrawDataPoints(true);

        lSeries3.setTitle("Moisture");
        lSeries3.setColor(Color.RED);
        lSeries3.setDrawDataPoints(true);

        g1.setTitle("Plant Data");
        g1.setTitleTextSize(40.0f);

        g1.addSeries(lSeries1);
        g1.addSeries(lSeries2);
        g1.addSeries(lSeries3);

        g1.getLegendRenderer().setVisible(true);
        g1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    private void processData(String response){

//        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        String[] data = response.split(",");

        if(data.length == 4){

            Double humd = Double.parseDouble(data[0]);
            Double temp = Double.parseDouble(data[1]);
            Double soilM = Double.parseDouble(data[3]);
            Double waterLvl = Double.parseDouble(data[2]);

            HUM_LIST.add(humd);
            TEMP_LIST.add(temp);
            SOIL.add(soilM);
            WATER.add(waterLvl);

            lSeries1.appendData(new DataPoint(x1,humd),false,50);
            lSeries2.appendData(new DataPoint(x2,temp),false,50);
            lSeries3.appendData(new DataPoint(x3,soilM),false,50);


            x1++;
            x2++;
            x3++;

            wvl.setProgressValue(waterLvl.intValue());
        }
        else if(data.length == 3){

            Double humd = Double.parseDouble(data[0]);
            Double temp = Double.parseDouble(data[1]);
            Double waterL = Double.parseDouble(data[2]);

            lSeries1.appendData(new DataPoint(x1,humd),false,50);
            lSeries2.appendData(new DataPoint(x2,temp),false,50);
            lSeries3.appendData(new DataPoint(x3,waterL),false,50);

            HUM_LIST.add(humd);
            TEMP_LIST.add(temp);
            SOIL.add(waterL);

            x1++;
            x2++;
            x3++;

        }
        else{

        }

    }

    private void initBluetooth(){

        bluetooth.setCommunicationCallback(new CommunicationCallback() {

            @Override
            public void onConnect(BluetoothDevice device) {

                Toast.makeText(MonitorActivity.this, "Connected to "
                        + device.getName() + ".", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisconnect(BluetoothDevice device, String message) {

                Toast.makeText(MonitorActivity.this, "Disconnected " +
                        "from the device.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMessage(String message) {
//                Toast.makeText(MonitorActivity.this,
//                        "Messaged received: " + message, Toast.LENGTH_SHORT).show();

                processData(message);
            }

            @Override
            public void onError(String message) {

                Toast.makeText(MonitorActivity.this, "Error: "
                        + message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {

                Toast.makeText(MonitorActivity.this,
                        "Connect error!",Toast.LENGTH_LONG);

            }
        });

        bluetooth.setBluetoothCallback(new BluetoothCallback() {

            @Override
            public void onBluetoothTurningOn() {
                Toast.makeText(MonitorActivity.this, "Bluetooth turning on", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBluetoothOn() {
                Toast.makeText(MonitorActivity.this, "Bluetooth turned on", Toast.LENGTH_SHORT).show();
                bluetooth.connectToName("HC-05");
            }

            @Override
            public void onBluetoothTurningOff() {
                Toast.makeText(MonitorActivity.this, "Bluetooth turning off", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBluetoothOff() {
                Toast.makeText(MonitorActivity.this, "Bluetooth turned off", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUserDeniedActivation() {
                // when using bluetooth.showEnableDialog()
                // you will also have to call bluetooth.onActivityResult()
                Toast.makeText(MonitorActivity.this, "Bluetooth denied user activation", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initPreferences(){

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(MonitorActivity.this);

        String user = sp.getString("USER_NAME", "");
        String plant = sp.getString("PLANT_NAME", "");
        String scientific = sp.getString("PLANT_SCI", "");

        tUser.setText(user);
        tPlant.setText(plant);
        tSci.setText(scientific);

    }

}
