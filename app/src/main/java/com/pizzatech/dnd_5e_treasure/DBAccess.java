package com.pizzatech.dnd_5e_treasure;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.PriorityQueue;

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
        String rQuery = "SELECT RESULT_ID FROM treasureroll " +
                "WHERE TYPE = '" + type + "' AND SUB_TYPE = '" + subTable + "' AND ROLL_UPPER >= " + roll + " AND ROLL_LOWER <= " + roll;
        Cursor rCursor = database.rawQuery(rQuery, null);
        rCursor.moveToFirst();
        Integer resultId = rCursor.getInt(0);
        rCursor.close();

        // Get the text for that result ID
        String tQuery = "SELECT MAIN_TEXT, SUB_TEXT FROM treasureresult " +
                "WHERE ID = " + resultId;
        Cursor tCursor = database.rawQuery(tQuery, null);
        tCursor.moveToFirst();
        TreasureListItem t = new TreasureListItem(tCursor.getString(0), tCursor.getString(1));
        tCursor.close();

        return t;
    }

    TreasureFurtherRolls getRolls(String table, Integer roll) {
        String query = "SELECT * FROM treasuretables " +
                "WHERE TTABLE = '" + table + "' AND ROLL_UPPER >= " + roll + " AND ROLL_LOWER <= " + roll;
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

        // This is in an if so we can be sure there are results and avoid certain doom
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Encounter e = new Encounter(cursor.getInt(0), cursor.getString(1));
                encounters.add(e);
                cursor.moveToNext();
            }
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

    ArrayList<EncounterEnemiesListItem> getEncounterEnemies(Integer encounter_id) {
        // Look up specific enemies currently in the encounter
        ArrayList<EncounterEnemiesListItem> enemiesList = new ArrayList<>();

        String query = "SELECT e.ID, e.NAME, e.CR, e.REFERENCE, ee.QUANTITY " +
                "FROM encounterenemies ee LEFT JOIN enemies e ON e.ID = ee.ENEMY_ID " +
                "WHERE ee.ENCOUNTER_ID = " + encounter_id;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                EncounterEnemiesListItem e = new EncounterEnemiesListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                enemiesList.add(e);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return enemiesList;
    }

    void updateEnemyQuantity(Integer encounterId, Integer enemyId, Integer quantity) {
        if (quantity == 0) {
            // now 0 of that enemy so remove the line
            database.delete("encounterenemies", "ENCOUNTER_ID = " + encounterId + " AND ENEMY_ID = " + enemyId, null);
        } else {
            // set to new quantity
            String sql = "UPDATE encounterenemies SET QUANTITY = " + quantity + " WHERE ENCOUNTER_ID = " + encounterId + " AND ENEMY_ID = " + enemyId;
            database.execSQL(sql);
        }
    }

    // Get All enemies except those on the list provided
    ArrayList<EnemiesListItem> getEnemies(ArrayList<Integer> currentIds, String cr, String name) {
        ArrayList<EnemiesListItem> enemiesList = new ArrayList<>();

        String sql = "SELECT ID, NAME, CR, REFERENCE FROM enemies";

        // Build where clause to exclude IDs already in the encounter
        Integer l = currentIds.size();
        if (l > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < l - 1; i++) {
                sb.append(currentIds.get(i));
                sb.append(", ");
            }
            sb.append(currentIds.get(l - 1));

            sql += " WHERE ID NOT IN (" + sb.toString() + ")";

        }

        // Add condition for CR filter
        if (cr != null && !cr.equals("All")) {
            if (sql.contains("WHERE")) {
                sql += "AND ";
            } else {
                sql += "WHERE ";
            }
            sql += "CR = '" + cr + "'";
        }

        // Add condition for name filter
        if (name != null && !name.equals("")) {
            if (sql.contains("WHERE")) {
                sql += "AND ";
            } else {
                sql += "WHERE ";
            }
            sql += "NAME LIKE '%" + name + "%'";
        }

        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                EnemiesListItem e = new EnemiesListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                enemiesList.add(e);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return enemiesList;
    }

    void addNewEnemy(Integer encounterId, Integer enemyId) {
        String sql = "INSERT INTO encounterenemies (ENCOUNTER_ID, ENEMY_ID, QUANTITY) VALUES (" + encounterId + ", " + enemyId + ", 1)";
        database.execSQL(sql);
    }
}
