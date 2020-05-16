package com.example.thespell;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBSpells {
    private static final String DATABASE_NAME = "spell.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SPELL = "spells_ru";
    private static final String TABLE_FEATURES = "features";
    private static final String LOG_TAG = "mylogsDB";

    private int ansPoints;

    private InputStream inputstream;
    private SQLiteDatabase mDatabase;

    public DBSpells(Context context){
        inputstream = context.getResources().openRawResource(R.raw.spells_db);
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDatabase = mOpenHelper.getWritableDatabase();
        ansPoints = 0;
    }

    public int countSpells(){
        return (int)DatabaseUtils.queryNumEntries(mDatabase, TABLE_SPELL);
    }

    public Spell getSpellByID (long id){
        Cursor mcursor = mDatabase.query(TABLE_SPELL, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);

        mcursor.moveToFirst();
        Spell toreturn = new Spell();
        toreturn.setName(mcursor.getString(1));
        toreturn.setManacost(mcursor.getInt(2));
        toreturn.setDescription(mcursor.getString(3));
        String[] perfectDescriptions = null;
        String[] perfectTypes = null;
        if (mcursor.getString(5) != null) {
            perfectDescriptions = mcursor.getString(4).split("---");
            perfectTypes = mcursor.getString(5).split("---");
        }
        for (int i = 0; perfectTypes != null && i < perfectTypes.length; i++){
            toreturn.addToPerfectDescription(perfectTypes[i], perfectDescriptions[i]);
        }
        toreturn.setFeatures((mcursor.getString(6)));
        toreturn.setPoints(ansPoints);
        return toreturn;
    }

    public String getNameByID(long id) {
        Cursor mcursor = mDatabase.query(TABLE_SPELL, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);
        mcursor.moveToFirst();
        return mcursor.getString(1);
    }

    public long findSpellID (String[] chosen_features){
        chosen_features[3] = "m" + Integer.parseInt(chosen_features[3])/10*10;
        long curID = 100;
        long ansID = 0;
        int cur_max = 0;
        Cursor mcursor = mDatabase.query(TABLE_FEATURES, chosen_features, null, null, null, null, null);
        mcursor.moveToFirst();
        int columns = mcursor.getColumnCount();
        while(!mcursor.isAfterLast()){
            curID++;
            int curRowPoints = 0;
            for (int col_id = 0; col_id < columns; col_id++){
                curRowPoints += mcursor.getInt(col_id);
            }
            Log.d(LOG_TAG, "r - : " + String.valueOf(curRowPoints));
            if (curRowPoints > cur_max){
                cur_max = curRowPoints;
                ansID = curID;
            }
            mcursor.moveToNext();
        }
        mcursor.close();
        ansPoints = cur_max;
        if (cur_max>=100) return ansID;
        else{
            if (cur_max >= 95) return ansID*(-1);
            else return 0;
        }
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            String str = "";
            try{
                db.beginTransaction();
                while ((str = reader.readLine()) != null){
                    if (!(str.charAt(0) == '-' && str.charAt(1) == '-')){
                        db.execSQL(str);
                        Log.d(LOG_TAG, str);
                    }
                }
            } catch (Exception e){
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
