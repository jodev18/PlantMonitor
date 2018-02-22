package dev.jojo.plantmonitor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by myxroft on 03/02/2018.
 */

public class PlantInfoDB extends SQLiteOpenHelper {

    public PlantInfoDB(Context ct){
        super(ct,"plantdb.db",null,1);

    }

    protected class PlantTable{

        public static final String TABLE_NAME = "tbl_plant_info";

        public static final String ID = "_id";

        public static final String SOIL_MOISTURE = "plant_soil_moisture";

        public static final String WATER_LEVEL = "plant_water_level";

        public static final String HUMIDITY = "plant_humidity";

        public static final String TEMPERATURE = "plant_temp";

        public static final String TABLE_CREATE = "CREATE TABLE " ;//+

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
