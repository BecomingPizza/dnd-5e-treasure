package com.pizzatech.dnd_5e_treasure;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Ashley on 26/09/2016.
 * <p>
 * YEAH DATABASE
 */

class DBAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBAccess instance;

    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    void open() {
        this.database = openHelper.getWritableDatabase();
    }

    void close() {
        if (database != null) {
            this.database.close();
        }
    }

    TreasureListItem getLoot(String type, String subTable, Integer roll) {

        // Look up the results ID
        String rQuery = "SELECT RESULT_ID FROM treasureroll WHERE TYPE = '" + type + "' AND SUB_TYPE = '" + subTable + "' AND ROLL_UPPER >= " + roll + " AND ROLL_LOWER <= " + roll;
        Cursor rCursor = database.rawQuery(rQuery, null);
        rCursor.moveToFirst();
        Integer resultId = rCursor.getInt(0);
        rCursor.close();

        // Get the text for that result ID
        String tQuery = "SELECT MAIN_TEXT, SUB_TEXT FROM treasureresult WHERE ID = " + resultId;
        Cursor tCursor = database.rawQuery(tQuery, null);
        tCursor.moveToFirst();
        TreasureListItem t = new TreasureListItem(tCursor.getString(0), tCursor.getString(1));
        tCursor.close();

        return t;
    }

    TreasureFurtherRolls getRolls(String table, Integer roll) {
        String query = "SELECT * FROM treasuretables WHERE TTABLE = '" + table + "' AND ROLL_UPPER >= " + roll + " AND ROLL_LOWER <= " + roll;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        TreasureFurtherRolls t = new TreasureFurtherRolls(
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getInt(8),
                cursor.getInt(9),
                cursor.getInt(10),
                cursor.getInt(11),
                cursor.getString(12),
                cursor.getInt(13),
                cursor.getInt(14),
                cursor.getString(15)
        );

        cursor.close();

        return t;
    }

    ArrayList<Encounter> getEncounters() {
        ArrayList<Encounter> encounters = new ArrayList<>();

        String query = "SELECT ID, NAME FROM encounters ORDER BY NAME ASC";
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Encounter e = new Encounter(cursor.getInt(0), cursor.getString(1));
            encounters.add(e);
            cursor.moveToNext();
        }

        cursor.close();
        return encounters;
    }

    void deleteEncounter(Integer id) {
        database.delete("encounters", "ID = " + id, null);
    }

    void addEncounter(String name) {
        String sql = "INSERT INTO encounters (NAME) VALUES ('" + name + "')";
        database.execSQL(sql);
    }
}
