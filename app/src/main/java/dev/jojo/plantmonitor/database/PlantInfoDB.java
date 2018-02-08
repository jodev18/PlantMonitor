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
       // public static final SS
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
