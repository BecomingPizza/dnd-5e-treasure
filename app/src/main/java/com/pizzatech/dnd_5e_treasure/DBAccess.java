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
 *
 * YEAH DATABASE
 */

class DBAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBAccess instance;

    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Test we can read stuff from db
     */
    public void test() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT ID FROM treasuregemsroll", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        Log.e("YO", list.toString());
    }
}
