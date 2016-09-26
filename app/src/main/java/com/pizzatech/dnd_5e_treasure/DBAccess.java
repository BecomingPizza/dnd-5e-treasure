package com.pizzatech.dnd_5e_treasure;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        this.database = openHelper.getReadableDatabase();
    }

    void close() {
        if (database != null) {
            this.database.close();
        }
    }

    TreasureListItem getLoot(String type, String subTable, Integer roll) {

        // Look up the results ID
        String rQuery = "SELECT RESULT_ID FROM treasureroll WHERE TYPE ='" + type + "' AND SUB_TYPE = '" + subTable + "' AND ROLL_UPPER >= " + roll + " AND ROLL_LOWER <= " + roll;
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
}
