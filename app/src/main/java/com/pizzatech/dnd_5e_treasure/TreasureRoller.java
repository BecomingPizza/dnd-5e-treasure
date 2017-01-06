package com.pizzatech.dnd_5e_treasure;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.util.Random;

import static com.pizzatech.dnd_5e_treasure.MainActivity.dbAccess;

/**
 * Created by Ashley on 17/09/2016.
 * <p>
 * Moving lots of shiz to an asynctask to maybe boost performance ¯\_(ツ)_/¯
 */
class TreasureRoller extends AsyncTask {

    private Integer table;
    private Context context;
    private Activity act;

    private Resources res;

    private static Random r = new Random();

    private String copperStr;
    private String silverStr;
    private String goldStr;
    private String platinumStr;
    TreasureRoller(Context context, Integer table, Activity act) {
        this.table = table;
        this.context = context;
        this.act = act;

        res = context.getResources();

        copperStr = res.getString(R.string.tr_coin_copper);
        silverStr = res.getString(R.string.tr_coin_silver);
        goldStr = res.getString(R.string.tr_coin_gold);
        platinumStr = res.getString(R.string.tr_coin_platinum);

    }

    @Override
    protected Integer doInBackground(Object[] params) {

        //Clear list
        TreasureHoardFragment.treasureItems.clear();

        // Get our d100 roll to start
        Integer roll = r.nextInt(100 - 1) + 1;
        String tbl = "";

        switch (table) {
            case 0:
                //Roll for CR 0-4
                rollCoins(6, 100, copperStr);
                rollCoins(3, 100, silverStr);
                rollCoins(2, 10, goldStr);
                tbl = "A";
                break;
            case 1:
                //Roll for CR 5-10
                rollCoins(2, 100, copperStr);
                rollCoins(2, 1000, silverStr);
                rollCoins(6, 100, goldStr);
                rollCoins(3, 10, platinumStr);
                tbl = "B";
                break;
            case 2:
                //Roll for CR 11-16
                rollCoins(4, 1000, goldStr);
                rollCoins(5, 100, platinumStr);
                tbl = "C";
                break;
            case 3:
                //Roll for CR 17+
                rollCoins(12, 1000, goldStr);
                rollCoins(8, 1000, platinumStr);
                tbl = "D";
                break;
        }



        // Do all the rolls

        dbAccess.open();
        TreasureFurtherRolls furtherRolls = dbAccess.getRolls(tbl, roll);
        dbAccess.close();
        furtherRolls.rolyPoly();

        //update the list in the main thread because reasons
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TreasureHoardFragment.treasureItemsListAdapter.notifyDataSetChanged();
            }
        });

        return table;
    }

    private void rollCoins(Integer dice, Integer multiplier, String coinType) {
        // Roll a d6 the specified number of times and sum the total (coins are always d6 rolls!)
        Integer roll = 0;
        for (int i = 0; i < dice; i++) {
            roll += (r.nextInt(6 - 1) + 1);
        }
        // Sum x multiplier
        Integer coins = roll * multiplier;
        // Build string and add to the list
        String listText = coins.toString() + " " + coinType;
        addToList(listText, null);
    }

    private void addToList(String mainText, String subText) {
        // Build TreasureListItem
        TreasureListItem t = new TreasureListItem(mainText, subText);

        // Put some shit in the list
        TreasureHoardFragment.treasureItems.add(t);
    }

    static void rollGems(Integer dice, Integer sides, Integer value) {
        //Determine number of times to roll
        Integer roll = 0;
        for (int i = 0; i < dice; i++) {
            roll += (r.nextInt(sides - 1) + 1);
        }

        dbAccess.open();
        for (int j = 0; j < roll; j++) {
            // What dice am roll
            Integer tSides = 0;
            switch (value) {
                case 10:
                case 50:
                    tSides = 12;
                    break;
                case 100:
                    tSides = 10;
                    break;
                case 500:
                    tSides = 6;
                    break;
                case 1000:
                    tSides = 8;
                    break;
                case 5000:
                    tSides = 4;
                    break;
            }
            Integer tRoll = r.nextInt(tSides - 1) + 1;

            TreasureListItem t = dbAccess.getLoot("gem", value.toString(), tRoll);

            TreasureHoardFragment.treasureItems.add(t);
        }
        dbAccess.close();
    }

    static void rollArt(Integer dice, Integer sides, Integer value) {
        Integer roll = 0;
        for (int i = 0; i < dice; i++) {
            roll += (r.nextInt(sides - 1) + 1);
        }

        dbAccess.open();
        for (int j = 0; j < roll; j++) {
            // What dice am roll
            Integer tSides = 0;
            switch (value) {
                case 25:
                case 250:
                case 750:
                case 2500:
                    tSides = 10;
                    break;
                case 7500:
                    tSides = 8;
                    break;
            }
            Integer tRoll = r.nextInt(tSides - 1) + 1;

            TreasureListItem t = dbAccess.getLoot("art", value.toString(), tRoll);

            TreasureHoardFragment.treasureItems.add(t);
        }
        dbAccess.close();
    }

    /**
     * Determines which Magic table to roll on based on 'value'
     * and determines how many times to roll on it.
     *
     * @param dice  - The number of times to roll the dice
     * @param sides - The number of sides on the dice
     * @param table - The table to roll on
     */
    static void rollMagic(Integer dice, Integer sides, String table) {
        Integer roll = 0;
        //Roll the dice to value of 'dice' times
        if (sides != 1) {
            for (int i = 0; i < dice; i++) {
                //simulate dice roll to determine what it lands on
                //sides determines the range of possible values
                roll += (r.nextInt(sides - 1) + 1);
            }
        } else {
            roll = 1;
        }

        dbAccess.open();
        for (int j = 0; j < roll; j++) {
            Integer tRoll = r.nextInt(100 - 1) + 1;
            String tTable = table;

            //Special cases for the weird armor n stuff
            if (table.equals("G") && 12 <= tRoll && tRoll <= 14) {
                tRoll = r.nextInt(8 - 1) + 1;
                tTable = "figurine";
            } else if (table.equals("I") && tRoll == 76) {
                tRoll = r.nextInt(12 - 1) + 1;
                tTable = "armor";
            }

            TreasureListItem t = dbAccess.getLoot("magic", tTable, tRoll);

            TreasureHoardFragment.treasureItems.add(t);
        }
        dbAccess.close();


    }

}