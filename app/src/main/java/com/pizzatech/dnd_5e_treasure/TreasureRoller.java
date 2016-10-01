package com.pizzatech.dnd_5e_treasure;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

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

    private Random r = new Random();

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
        MainActivity.treasureItems.clear();

        switch (table) {
            case 0:
                //Roll for CR 0-4
                rollCoins(6, 100, copperStr);
                rollCoins(3, 100, silverStr);
                rollCoins(2, 10, goldStr);
                rollTreasureTableA();
                break;
            case 1:
                //Roll for CR 5-10
                rollCoins(2, 100, copperStr);
                rollCoins(2, 1000, silverStr);
                rollCoins(6, 100, goldStr);
                rollCoins(3, 10, platinumStr);
                rollTreasureTableB();
                break;
            case 2:
                //Roll for CR 11-16
                rollCoins(4, 1000, goldStr);
                rollCoins(5, 100, platinumStr);
                rollTreasureTableC();
                break;
            case 3:
                //Roll for CR 17+
                rollCoins(12, 1000, goldStr);
                rollCoins(8, 1000, platinumStr);
                rollTreasureTableD();
                break;
        }

        //update the list in the main thread because reasons
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.treasureItemsListAdapter.notifyDataSetChanged();
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
        MainActivity.treasureItems.add(t);
    }

    private void rollGems(Integer dice, Integer sides, Integer value) {
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

            MainActivity.treasureItems.add(t);
        }
        dbAccess.close();
    }

    private void rollArt(Integer dice, Integer sides, Integer value) {
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

            MainActivity.treasureItems.add(t);
        }
        dbAccess.close();
    }

    /**
     * See Treasure Hoard: Challenge 0-4 page 137
     * TODO: Shove all these into the db
     */
    private void rollTreasureTableA() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch (roll) {
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                rollGems(2, 6, 10);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
                rollArt(2, 4, 25);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
                rollGems(2, 6, 50);
                break;
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                rollGems(2, 6, 10);
                rollMagic(1, 6, "A");
                break;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
                rollArt(2, 4, 25);
                rollMagic(1, 6, "A");
                break;
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
                rollGems(2, 6, 50);
                rollMagic(1, 6, "A");
                break;
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
                rollGems(2, 6, 10);
                rollMagic(1, 4, "B");
                break;
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
                rollArt(2, 4, 25);
                rollMagic(1, 4, "B");
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                rollGems(2, 6, 50);
                rollMagic(1, 4, "B");
                break;
            case 76:
            case 77:
            case 78:
                rollGems(2, 6, 10);
                rollMagic(1, 4, "C");
                break;
            case 79:
            case 80:
                rollArt(2, 4, 25);
                rollMagic(1, 4, "C");
                break;
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
                rollGems(2, 6, 50);
                rollMagic(1, 4, "C");
                break;
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
                rollArt(2, 4, 25);
                rollMagic(1, 4, "F");
                break;
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
                rollGems(2, 6, 50);
                rollMagic(1, 4, "F");
                break;
            case 98:
            case 99:
                rollArt(2, 4, 25);
                rollMagic(1, 1, "G");
                break;
            case 100:
                rollGems(2, 6, 50);
                rollMagic(1, 1, "G");
                break;
        }

    }

    /**
     * See Treasure Hoard: Challenge 5-10 page 137
     */
    private void rollTreasureTableB() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch (roll) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                rollArt(2, 4, 25);
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                rollGems(3, 6, 50);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                rollGems(3, 6, 100);
                break;
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                rollArt(2, 4, 250);
                break;
            case 29:
            case 30:
            case 31:
            case 32:
                rollArt(2, 4, 25);
                rollMagic(1, 6, "A");
                break;
            case 33:
            case 34:
            case 35:
            case 36:
                rollGems(3, 6, 50);
                rollMagic(1, 6, "A");
                break;
            case 37:
            case 38:
            case 39:
            case 40:
                rollGems(3, 6, 100);
                rollMagic(1, 6, "A");
                break;
            case 41:
            case 42:
            case 43:
            case 44:
                rollArt(2, 4, 250);
                rollMagic(1, 6, "A");
                break;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                rollArt(2, 4, 25);
                rollMagic(1, 4, "B");
                break;
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                rollGems(3, 6, 50);
                rollMagic(1, 4, "B");
                break;
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
                rollGems(3, 6, 100);
                rollMagic(1, 4, "B");
                break;
            case 60:
            case 61:
            case 62:
            case 63:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "B");
                break;
            case 64:
            case 65:
            case 66:
                rollArt(2, 4, 25);
                rollMagic(1, 4, "C");
                break;
            case 67:
            case 68:
            case 69:
                rollGems(3, 6, 50);
                rollMagic(1, 4, "C");
                break;
            case 70:
            case 71:
            case 72:
                rollGems(3, 6, 100);
                rollMagic(1, 4, "C");
                break;
            case 73:
            case 74:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "C");
                break;
            case 75:
            case 76:
                rollArt(2, 4, 25);
                rollMagic(1, 1, "D");
                break;
            case 77:
            case 78:
                rollGems(3, 6, 50);
                rollMagic(1, 1, "D");
                break;
            case 79:
                rollGems(3, 6, 100);
                rollMagic(1, 1, "D");
                break;
            case 80:
                rollArt(2, 4, 250);
                rollMagic(1, 1, "D");
                break;
            case 81:
            case 82:
            case 83:
            case 84:
                rollArt(2, 4, 25);
                rollMagic(1, 4, "F");
                break;
            case 85:
            case 86:
            case 87:
            case 88:
                rollGems(3, 6, 50);
                rollMagic(1, 4, "F");
                break;
            case 89:
            case 90:
            case 91:
                rollGems(3, 6, 100);
                rollMagic(1, 4, "F");
                break;
            case 92:
            case 93:
            case 94:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "F");
                break;
            case 95:
            case 96:
                rollGems(3, 6, 100);
                rollMagic(1, 4, "G");
                break;
            case 97:
            case 98:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "G");
                break;
            case 99:
                rollGems(3, 6, 100);
                rollMagic(1, 1, "H");
                break;
            case 100:
                rollArt(2, 4, 250);
                rollMagic(1, 1, "H");
                break;
        }

    }

    /**
     * See Treasure Hoard: Challenge 11-16 page 138
     */
    private void rollTreasureTableC() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch (roll) {
            case 4:
            case 5:
            case 6:
                rollArt(2, 4, 250);
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                rollArt(2, 4, 750);
                break;
            case 11:
            case 12:
                rollGems(3, 6, 500);
                break;
            case 13:
            case 14:
            case 15:
                rollGems(3, 6, 1000);
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "A");
                rollMagic(1, 6, "B");
                break;
            case 20:
            case 21:
            case 22:
            case 23:
                rollArt(2, 4, 750);
                rollMagic(1, 4, "A");
                rollMagic(1, 6, "B");
                break;
            case 24:
            case 25:
            case 26:
                rollGems(3, 6, 500);
                rollMagic(1, 4, "A");
                rollMagic(1, 6, "B");
                break;
            case 27:
            case 28:
            case 29:
                rollGems(3, 6, 1000);
                rollMagic(1, 4, "A");
                rollMagic(1, 6, "B");
                break;
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
                rollArt(2, 4, 250);
                rollMagic(1, 6, "C");
                break;
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
                rollArt(2, 4, 750);
                rollMagic(1, 6, "C");
                break;
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
                rollGems(3, 6, 500);
                rollMagic(1, 6, "C");
                break;
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
                rollGems(3, 6, 1000);
                rollMagic(1, 6, "C");
                break;
            case 51:
            case 52:
            case 53:
            case 54:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "D");
                break;
            case 55:
            case 56:
            case 57:
            case 58:
                rollArt(2, 4, 750);
                rollMagic(1, 4, "D");
                break;
            case 59:
            case 60:
            case 61:
            case 62:
                rollGems(3, 6, 500);
                rollMagic(1, 4, "D");
                break;
            case 63:
            case 64:
            case 65:
            case 66:
                rollGems(3, 6, 1000);
                rollMagic(1, 4, "D");
                break;
            case 67:
            case 68:
                rollArt(2, 4, 250);
                rollMagic(1, 1, "E");
                break;
            case 69:
            case 70:
                rollArt(2, 4, 750);
                rollMagic(1, 1, "E");
                break;
            case 71:
            case 72:
                rollGems(3, 6, 500);
                rollMagic(1, 1, "E");
                break;
            case 73:
            case 74:
                rollGems(3, 6, 1000);
                rollMagic(1, 1, "E");
                break;
            case 75:
            case 76:
                rollArt(2, 4, 250);
                rollMagic(1, 1, "F");
                rollMagic(1, 4, "G");
                break;
            case 77:
            case 78:
                rollArt(2, 4, 750);
                rollMagic(1, 1, "F");
                rollMagic(1, 4, "G");
                break;
            case 79:
            case 80:
                rollGems(3, 6, 500);
                rollMagic(1, 1, "F");
                rollMagic(1, 4, "G");
                break;
            case 81:
            case 82:
                rollGems(3, 6, 1000);
                rollMagic(1, 1, "F");
                rollMagic(1, 4, "G");
                break;
            case 83:
            case 84:
            case 85:
                rollArt(2, 4, 250);
                rollMagic(1, 4, "H");
                break;
            case 86:
            case 87:
            case 88:
                rollArt(2, 4, 750);
                rollMagic(1, 4, "H");
                break;
            case 89:
            case 90:
                rollGems(3, 6, 500);
                rollMagic(1, 4, "H");
                break;
            case 91:
            case 92:
                rollGems(3, 6, 1000);
                rollMagic(1, 4, "H");
                break;
            case 93:
            case 94:
                rollArt(2, 4, 250);
                rollMagic(1, 1, "I");
                break;
            case 95:
            case 96:
                rollArt(2, 4, 750);
                rollMagic(1, 1, "I");
                break;
            case 97:
            case 98:
                rollGems(3, 6, 500);
                rollMagic(1, 1, "I");
                break;
            case 99:
            case 100:
                rollMagic(1, 1, "I");
                break;
        }

    }

    /**
     * See Treasure Hoard: Challenge 17+ page 139
     */
    private void rollTreasureTableD() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch (roll) {
            case 3:
            case 4:
            case 5:
                rollGems(3, 6, 1000);
                rollMagic(1, 8, "C");
                break;
            case 6:
            case 7:
            case 8:
                rollArt(1, 10, 2500);
                rollMagic(1, 8, "C");
                break;
            case 9:
            case 10:
            case 11:
                rollArt(1, 4, 7500);
                rollMagic(1, 8, "C");
                break;
            case 12:
            case 13:
            case 14:
                rollGems(1, 8, 5000);
                rollMagic(1, 8, "C");
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                rollGems(3, 6, 1000);
                rollMagic(1, 6, "D");
                break;
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                rollArt(1, 10, 2500);
                rollMagic(1, 6, "D");
                break;
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                rollArt(1, 4, 7500);
                rollMagic(1, 6, "D");
                break;
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
                rollGems(1, 8, 5000);
                rollMagic(1, 6, "D");
                break;
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
                rollGems(3, 6, 1000);
                rollMagic(1, 6, "E");
                break;
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                rollArt(1, 10, 2500);
                rollMagic(1, 6, "E");
                break;
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
                rollArt(1, 4, 7500);
                rollMagic(1, 6, "E");
                break;
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                rollGems(1, 8, 5000);
                rollMagic(1, 6, "E");
                break;
            case 69:
                rollGems(3, 6, 1000);
                rollMagic(1, 4, "G");
                break;
            case 70:
                rollArt(1, 10, 2500);
                rollMagic(1, 4, "G");
                break;
            case 71:
                rollArt(1, 4, 7500);
                rollMagic(1, 4, "G");
                break;
            case 72:
                rollGems(1, 8, 5000);
                rollMagic(1, 4, "G");
                break;
            case 73:
            case 74:
                rollGems(3, 6, 1000);
                rollMagic(1, 4, "H");
                break;
            case 75:
            case 76:
                rollArt(1, 10, 2500);
                rollMagic(1, 4, "H");
                break;
            case 77:
            case 78:
                rollArt(1, 4, 7500);
                rollMagic(1, 4, "H");
                break;
            case 79:
            case 80:
                rollGems(1, 8, 5000);
                rollMagic(1, 4, "H");
                break;
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
                rollGems(3, 6, 1000);
                rollMagic(1, 4, "I");
                break;
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
                rollArt(1, 10, 2500);
                rollMagic(1, 4, "I");
                break;
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
                rollArt(1, 4, 7500);
                rollMagic(1, 4, "I");
                break;
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
                rollGems(1, 8, 5000);
                rollMagic(1, 4, "I");
                break;
        }

    }

    /**
     * Determines which Magic table to roll on based on 'value'
     * and determines how many times to roll on it.
     *
     * @param dice  - The number of times to roll the dice
     * @param sides - The number of sides on the dice
     * @param table - The table to roll on
     */
    private void rollMagic(Integer dice, Integer sides, String table) {
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
            if (table == "G" && 12 <= tRoll && tRoll <= 14) {
                tRoll = r.nextInt(8 - 1) + 1;
                tTable = "figurine";
            } else if (table == "I" && tRoll == 76) {
                tRoll = r.nextInt(12 - 1) + 1;
                tTable = "armor";
            }

            TreasureListItem t = dbAccess.getLoot("magic", tTable, tRoll);

            MainActivity.treasureItems.add(t);
        }
        dbAccess.close();


    }

}