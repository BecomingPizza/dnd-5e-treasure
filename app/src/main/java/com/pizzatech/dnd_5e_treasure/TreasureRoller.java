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

    private Random r = new Random();

    private String copperStr;
    private String silverStr;
    private String goldStr;
    private String platinumStr;

    private String[] treasureArraySub;

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
            // TODO: Make less shit
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
            // TODO: Make less shit
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
                rollMagicG();
                break;
            case 100:
                rollGems(2, 6, 50);
                rollMagicG();
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
                rollMagicD();
                break;
            case 77:
            case 78:
                rollGems(3, 6, 50);
                rollMagicD();
                break;
            case 79:
                rollGems(3, 6, 100);
                rollMagicD();
                break;
            case 80:
                rollArt(2, 4, 250);
                rollMagicD();
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
                rollMagicH();
                break;
            case 100:
                rollArt(2, 4, 250);
                rollMagicH();
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
                rollMagicE();
                break;
            case 69:
            case 70:
                rollArt(2, 4, 750);
                rollMagicE();
                break;
            case 71:
            case 72:
                rollGems(3, 6, 500);
                rollMagicE();
                break;
            case 73:
            case 74:
                rollGems(3, 6, 1000);
                rollMagicE();
                break;
            case 75:
            case 76:
                rollArt(2, 4, 250);
                rollMagicF();
                rollMagic(1, 4, "G");
                break;
            case 77:
            case 78:
                rollArt(2, 4, 750);
                rollMagicF();
                rollMagic(1, 4, "G");
                break;
            case 79:
            case 80:
                rollGems(3, 6, 500);
                rollMagicF();
                rollMagic(1, 4, "G");
                break;
            case 81:
            case 82:
                rollGems(3, 6, 1000);
                rollMagicF();
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
                rollMagicI();
                break;
            case 95:
            case 96:
                rollArt(2, 4, 750);
                rollMagicI();
                break;
            case 97:
            case 98:
                rollGems(3, 6, 500);
                rollMagicI();
                break;
            case 99:
            case 100:
                rollMagicI();
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
        for (int i = 0; i < dice; i++) {
            //simulate dice roll to determine what it lands on
            //sides determines the range of possible values
            roll += (r.nextInt(sides - 1) + 1);
        }

        dbAccess.open();
        for (int j = 0; j < roll; j++) {
            Integer tRoll = r.nextInt(100 - 1) + 1;

            //Special cases for the weird armor n stuff
            if (table == "G" && 12 <= tRoll && tRoll <= 14) {
                tRoll = r.nextInt(8 - 1) + 1;
                table = "figurine";
            } else if (table == "I" && tRoll == 76) {
                tRoll = r.nextInt(12 - 1) + 1;
                table = "armor";
            }

            TreasureListItem t = dbAccess.getLoot("magic", "A", tRoll); //TODO: put back to table as 2nd param once db is populated

            MainActivity.treasureItems.add(t);
        }
        dbAccess.close();


    }

    /**
     * Roll a D100 on table B to determine
     * a Magic Item
     */
    private void rollMagicB() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        switch (roll) {
            //If roll is between 1 and 15 inclusive
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:

                magicText = treasureArraySub[215];
                subText = res.getString(R.string.dmg187);
                break;

            //If roll is between 16 and 22 inclusive
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:

                magicText = treasureArraySub[215];
                subText = res.getString(R.string.dmg187);
                break;

            //If roll is between 23 and 29 inclusive
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:

                magicText = treasureArraySub[225];
                subText = res.getString(R.string.dmg188);
                break;

            //If roll is between 30 and 34 inclusive
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:

                magicText = treasureArraySub[11];
                subText = res.getString(R.string.dmg150);
                break;

            //If roll is between  35 and 39 inclusive
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:

                magicText = treasureArraySub[205];
                subText = res.getString(R.string.dmg187);
                break;

            case 40:
            case 41:
            case 42:
            case 43:
            case 44:

                magicText = treasureArraySub[219];
                subText = res.getString(R.string.dmg187);
                break;


            case 45:
            case 46:
            case 47:
            case 48:
            case 49:

                magicText = treasureArraySub[216];
                subText = res.getString(R.string.dmg187);
                break;

            case 50:
            case 51:
            case 52:
            case 53:
            case 54:

                magicText = treasureArraySub[20];
                subText = res.getString(R.string.dmg188);
                break;

            case 55:
            case 56:
            case 57:
            case 58:
            case 59:

                magicText = treasureArraySub[288];
                subText = res.getString(R.string.dmg200);
                break;

            case 60:
            case 61:
            case 62:
            case 63:
            case 64:

                magicText = treasureArraySub[289];
                subText = res.getString(R.string.dmg200);
                break;

            case 65:
            case 66:
            case 67:

                magicText = treasureArraySub[50];
                subText = res.getString(R.string.dmg153);
                break;

            case 68:
            case 69:
            case 70:

                magicText = treasureArraySub[157];
                subText = res.getString(R.string.dmg179);
                break;

            case 71:
            case 72:
            case 73:

                magicText = treasureArraySub[194];
                subText = res.getString(R.string.dmg184);
                break;

            case 74:
            case 75:

                magicText = treasureArraySub[104];
                subText = res.getString(R.string.dmg166);
                break;

            case 76:
            case 77:

                magicText = treasureArraySub[105];
                subText = res.getString(R.string.dmg166);
                break;

            case 78:
            case 79:

                magicText = treasureArraySub[106];
                subText = res.getString(R.string.dmg166);
                break;

            case 80:
            case 81:

                magicText = treasureArraySub[111];
                subText = res.getString(R.string.dmg167);
                break;

            case 82:
            case 83:

                magicText = treasureArraySub[199];
                subText = res.getString(R.string.dmg184);
                break;

            case 84:

                magicText = treasureArraySub[10];
                subText = res.getString(R.string.dmg150);
                break;

            case 85:

                magicText = treasureArraySub[74];
                subText = res.getString(R.string.dmg157);
                break;

            case 86:

                magicText = treasureArraySub[87];
                subText = res.getString(R.string.dmg159);
                break;

            case 87:

                magicText = treasureArraySub[103];
                subText = res.getString(R.string.dmg166);
                break;

            case 88:

                magicText = treasureArraySub[130];
                subText = res.getString(R.string.dmg172);
                break;

            case 89:

                magicText = treasureArraySub[135];
                subText = res.getString(R.string.dmg173);
                break;

            case 90:

                magicText = treasureArraySub[146];
                subText = res.getString(R.string.dmg175);
                break;

            case 91:

                magicText = treasureArraySub[91];
                subText = res.getString(R.string.dmg179);
                break;

            case 92:

                magicText = treasureArraySub[182];
                subText = res.getString(R.string.dmg181);
                break;

            case 93:

                magicText = treasureArraySub[185];
                subText = res.getString(R.string.dmg182);
                break;

            case 94:

                magicText = treasureArraySub[224];
                subText = res.getString(R.string.dmg188);
                break;

            case 95:

                magicText = treasureArraySub[251];
                subText = res.getString(R.string.dmg193);
                break;

            case 96:

                magicText = treasureArraySub[263];
                subText = res.getString(R.string.dmg195);
                break;

            case 97:

                magicText = treasureArraySub[273];
                subText = res.getString(R.string.dmg197);
                break;

            case 98:

                magicText = treasureArraySub[275];
                subText = res.getString(R.string.dmg199);
                break;

            case 99:

                magicText = treasureArraySub[337];
                subText = res.getString(R.string.dmg211);
                break;

            case 100:

                magicText = treasureArraySub[341];
                subText = res.getString(R.string.dmg211);
                break;
        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table C to determine
     * a Magic Item
     */
    private void rollMagicC() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";


        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:

                magicText = treasureArraySub[229];
                subText = res.getString(R.string.dmg187);
                break;

            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:

                magicText = treasureArraySub[290];
                subText = res.getString(R.string.dmg200);
                break;

            case 23:
            case 24:
            case 25:
            case 26:
            case 27:

                magicText = treasureArraySub[12];
                subText = res.getString(R.string.dmg150);
                break;

            case 28:
            case 29:
            case 30:
            case 31:
            case 32:

                magicText = treasureArraySub[206];
                subText = res.getString(R.string.dmg187);
                break;

            case 33:
            case 34:
            case 35:
            case 36:
            case 37:

                magicText = treasureArraySub[209];
                subText = res.getString(R.string.dmg187);
                break;

            case 38:
            case 39:
            case 40:
            case 41:
            case 42:

                magicText = treasureArraySub[214];
                subText = res.getString(R.string.dmg187);
                break;

            case 43:
            case 44:
            case 45:
            case 46:
            case 47:

                magicText = treasureArraySub[213];
                subText = res.getString(R.string.dmg187);
                break;

            case 48:
            case 49:
            case 50:
            case 51:
            case 52:

                magicText = treasureArraySub[227];
                subText = res.getString(R.string.dmg187);
                break;

            case 53:
            case 54:
            case 55:
            case 56:
            case 57:

                magicText = treasureArraySub[218];
                subText = res.getString(R.string.dmg188);
                break;

            case 58:
            case 59:
            case 60:
            case 61:
            case 62:

                magicText = treasureArraySub[221];
                subText = res.getString(R.string.dmg188);
                break;

            case 63:
            case 64:
            case 65:
            case 66:
            case 67:

                magicText = treasureArraySub[223];
                subText = res.getString(R.string.dmg188);
                break;

            case 68:
            case 69:
            case 70:
            case 71:
            case 72:

                magicText = treasureArraySub[291];
                subText = res.getString(R.string.dmg200);
                break;

            case 73:
            case 74:
            case 75:

                magicText = treasureArraySub[112];
                subText = res.getString(R.string.dmg168);
                break;

            case 76:
            case 77:
            case 78:

                magicText = treasureArraySub[192];
                subText = res.getString(R.string.dmg183);
                break;

            case 79:
            case 80:
            case 81:

                magicText = treasureArraySub[210];
                subText = res.getString(R.string.dmg187);
                break;

            case 82:
            case 83:
            case 84:

                magicText = treasureArraySub[233];
                subText = res.getString(R.string.dmg188);
                break;

            case 85:
            case 86:
            case 87:

                magicText = treasureArraySub[278];
                subText = res.getString(R.string.dmg189);
                break;

            case 88:
            case 89:

                magicText = treasureArraySub[48];
                subText = res.getString(R.string.dmg152);
                break;

            case 90:
            case 91:

                magicText = treasureArraySub[54];
                subText = res.getString(R.string.dmg154);
                break;

            case 92:

                magicText = treasureArraySub[78];
                subText = res.getString(R.string.dmg158);
                break;

            case 93:

                magicText = treasureArraySub[95];
                subText = res.getString(R.string.dmg161);
                break;

            case 94:

                magicText = treasureArraySub[116];
                subText = res.getString(R.string.dmg168);
                break;

            case 95:

                magicText = treasureArraySub[120];
                subText = res.getString(R.string.dmg170);
                break;

            case 96:

                magicText = treasureArraySub[138];
                subText = res.getString(R.string.dmg174);
                break;

            case 97:

                magicText = treasureArraySub[145];
                subText = res.getString(R.string.dmg175);
                break;

            case 98:

                magicText = treasureArraySub[187];
                subText = res.getString(R.string.dmg182);
                break;

            case 99:

                magicText = treasureArraySub[196];
                subText = res.getString(R.string.dmg184);
                break;

            case 100:

                magicText = treasureArraySub[196];
                subText = res.getString(R.string.dmg184);
                break;

        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table D to determine
     * a Magic Item
     */
    private void rollMagicD() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);

        String magicText = "";
        String subText = "";

        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
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
            case 17:
            case 18:
            case 19:
            case 20:

                magicText = treasureArraySub[230];
                subText = res.getString(R.string.dmg187);
                break;

            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:

                magicText = treasureArraySub[220];
                subText = res.getString(R.string.dmg188);
                break;

            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:

                magicText = treasureArraySub[226];
                subText = res.getString(R.string.dmg188);
                break;

            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:

                magicText = treasureArraySub[292];
                subText = res.getString(R.string.dmg200);
                break;

            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:

                magicText = treasureArraySub[293];
                subText = res.getString(R.string.dmg200);
                break;

            case 58:
            case 59:
            case 60:
            case 61:
            case 62:

                magicText = treasureArraySub[13];
                subText = res.getString(R.string.dmg150);
                break;

            case 63:
            case 64:
            case 65:
            case 66:
            case 67:

                magicText = treasureArraySub[193];
                subText = res.getString(R.string.dmg184);
                break;

            case 68:
            case 69:
            case 70:
            case 71:
            case 72:

                magicText = treasureArraySub[212];
                subText = res.getString(R.string.dmg187);
                break;

            case 73:
            case 74:
            case 75:
            case 76:
            case 77:

                magicText = treasureArraySub[208];
                subText = res.getString(R.string.dmg187);
                break;

            case 78:
            case 79:
            case 80:
            case 81:
            case 82:

                magicText = treasureArraySub[222];
                subText = res.getString(R.string.dmg188);
                break;

            case 83:
            case 84:
            case 85:
            case 86:
            case 87:

                magicText = treasureArraySub[231];
                subText = res.getString(R.string.dmg188);
                break;

            case 88:
            case 89:
            case 90:
            case 91:
            case 92:

                magicText = treasureArraySub[294];
                subText = res.getString(R.string.dmg200);
                break;

            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:

                magicText = treasureArraySub[190];
                subText = res.getString(R.string.dmg183);
                break;

            case 99:

                magicText = treasureArraySub[49];
                subText = res.getString(R.string.dmg153);
                break;

            case 100:

                magicText = treasureArraySub[204];
                subText = res.getString(R.string.dmg185);
                break;
        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table E to determine
     * a Magic Item
     */
    private void rollMagicE() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
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
            case 27:
            case 28:
            case 29:
            case 30:

                magicText = treasureArraySub[294];
                subText = res.getString(R.string.dmg200);
                break;

            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:

                magicText = treasureArraySub[228];
                subText = res.getString(R.string.dmg187);
                break;

            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:

                magicText = treasureArraySub[230];
                subText = res.getString(R.string.dmg187);
                break;

            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:

                magicText = treasureArraySub[295];
                subText = res.getString(R.string.dmg200);
                break;

            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:

                magicText = treasureArraySub[329];
                subText = res.getString(R.string.dmg209);
                break;

            case 99:
            case 100:

                magicText = treasureArraySub[286];
                subText = res.getString(R.string.dmg200);
                break;

        }

        addToList(magicText, subText);
    }


    /**
     * Roll a D100 on table F to determine
     * a Magic Item
     */
    private void rollMagicF() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);

        String magicText = "";
        String subText = "";

        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                magicText = treasureArraySub[348];
                subText = res.getString(R.string.dmg213);
                break;

            case 16:
            case 17:
            case 18:
                magicText = treasureArraySub[284];
                subText = res.getString(R.string.dmg200);
                break;

            case 19:
            case 20:
            case 21:
                magicText = treasureArraySub[280];
                subText = res.getString(R.string.dmg199);
                break;

            case 22:
            case 23:
                magicText = treasureArraySub[15];
                subText = res.getString(R.string.dmg150);
                break;

            case 24:
            case 25:
                magicText = treasureArraySub[62];
                subText = res.getString(R.string.dmg155);
                break;

            case 26:
            case 27:
                magicText = treasureArraySub[65];
                subText = res.getString(R.string.dmg156);
                break;

            case 28:
            case 29:
                magicText = treasureArraySub[68];
                subText = res.getString(R.string.dmg156);
                break;

            case 30:
            case 31:
                magicText = treasureArraySub[71];
                subText = res.getString(R.string.dmg156);
                break;

            case 32:
            case 33:
                magicText = treasureArraySub[72];
                subText = res.getString(R.string.dmg156);
                break;

            case 34:
            case 35:
                magicText = treasureArraySub[83];
                subText = res.getString(R.string.dmg158);
                break;

            case 36:
            case 37:
                magicText = treasureArraySub[85];
                subText = res.getString(R.string.dmg159);
                break;

            case 38:
            case 39:
                magicText = treasureArraySub[123];
                subText = res.getString(R.string.dmg171);
                break;

            case 40:
            case 41:
                magicText = treasureArraySub[132];
                subText = res.getString(R.string.dmg173);
                break;

            case 42:
            case 43:
                magicText = treasureArraySub[156];
                subText = res.getString(R.string.dmg178);
                break;

            case 44:
            case 45:
                magicText = treasureArraySub[195];
                subText = res.getString(R.string.dmg184);
                break;

            case 46:
            case 47:
                magicText = treasureArraySub[270];
                subText = res.getString(R.string.dmg197);
                break;

            case 48:
            case 49:
                magicText = treasureArraySub[285];
                subText = res.getString(R.string.dmg200);
                break;

            case 50:
            case 51:
                magicText = treasureArraySub[306];
                subText = res.getString(R.string.dmg203);
                break;

            case 52:
            case 53:
                magicText = treasureArraySub[308];
                subText = res.getString(R.string.dmg204);
                break;

            case 54:
            case 55:
                magicText = treasureArraySub[318];
                subText = res.getString(R.string.dmg206);
                break;

            case 56:
            case 57:
                magicText = treasureArraySub[328];
                subText = res.getString(R.string.dmg209);
                break;

            case 58:
            case 59:
                magicText = treasureArraySub[338];
                subText = res.getString(R.string.dmg211);
                break;

            case 60:
            case 61:
                magicText = treasureArraySub[342];
                subText = res.getString(R.string.dmg212);
                break;

            case 62:
            case 63:
                magicText = treasureArraySub[345];
                subText = res.getString(R.string.dmg212);
                break;

            case 64:
            case 65:
                magicText = treasureArraySub[347];
                subText = res.getString(R.string.dmg213);
                break;

            case 66:
                magicText = treasureArraySub[4];
                subText = res.getString(R.string.dmg150);
                break;

            case 67:
                magicText = treasureArraySub[5];
                subText = res.getString(R.string.dmg150);
                break;

            case 68:
                magicText = treasureArraySub[8];
                subText = res.getString(R.string.dmg150);
                break;

            case 69:
                magicText = treasureArraySub[51];
                subText = res.getString(R.string.dmg154);
                break;

            case 70:
                magicText = treasureArraySub[52];
                subText = res.getString(R.string.dmg154);
                break;

            case 71:
                magicText = treasureArraySub[53];
                subText = res.getString(R.string.dmg154);
                break;

            case 72:
                magicText = treasureArraySub[66];
                subText = res.getString(R.string.dmg156);
                break;

            case 73:
                magicText = treasureArraySub[79];
                subText = res.getString(R.string.dmg158);
                break;

            case 74:
                magicText = treasureArraySub[96];
                subText = res.getString(R.string.dmg161);
                break;

            case 75:
                magicText = treasureArraySub[114];
                subText = res.getString(R.string.dmg168);
                break;

            case 76:
                magicText = treasureArraySub[115];
                subText = res.getString(R.string.dmg168);
                break;

            case 77:
                magicText = treasureArraySub[117];
                subText = res.getString(R.string.dmg168);
                break;

            case 78:
                magicText = treasureArraySub[119];
                subText = res.getString(R.string.dmg169);
                break;

            case 79:
                magicText = treasureArraySub[124];
                subText = res.getString(R.string.dmg171);
                break;

            case 80:
                magicText = treasureArraySub[127];
                subText = res.getString(R.string.dmg172);
                break;

            case 81:
                magicText = treasureArraySub[128];
                subText = res.getString(R.string.dmg172);
                break;

            case 82:
                magicText = treasureArraySub[129];
                subText = res.getString(R.string.dmg172);
                break;

            case 83:
                magicText = treasureArraySub[133];
                subText = res.getString(R.string.dmg173);
                break;

            case 84:
                magicText = treasureArraySub[136];
                subText = res.getString(R.string.dmg174);
                break;

            case 85:
                magicText = treasureArraySub[149];
                subText = res.getString(R.string.dmg176);
                break;

            case 86:
                magicText = treasureArraySub[150];
                subText = res.getString(R.string.dmg176);
                break;

            case 87:
                magicText = treasureArraySub[151];
                subText = res.getString(R.string.dmg176);
                break;

            case 88:
                magicText = treasureArraySub[183];
                subText = res.getString(R.string.dmg181);
                break;

            case 89:
                magicText = treasureArraySub[186];
                subText = res.getString(R.string.dmg182);
                break;

            case 90:
                magicText = treasureArraySub[198];
                subText = res.getString(R.string.dmg184);
                break;

            case 91:
                magicText = treasureArraySub[200];
                subText = res.getString(R.string.dmg185);
                break;

            case 92:
                magicText = treasureArraySub[201];
                subText = res.getString(R.string.dmg185);
                break;

            case 93:
                magicText = treasureArraySub[244];
                subText = res.getString(R.string.dmg191);
                break;

            case 94:
                magicText = treasureArraySub[245];
                subText = res.getString(R.string.dmg191);
                break;

            case 95:
                magicText = treasureArraySub[255];
                subText = res.getString(R.string.dmg193);
                break;

            case 96:
                magicText = treasureArraySub[257];
                subText = res.getString(R.string.dmg193);
                break;

            case 97:
                magicText = treasureArraySub[234];
                subText = res.getString(R.string.dmg189);
                break;

            case 98:
                magicText = treasureArraySub[313];
                subText = res.getString(R.string.dmg205);
                break;

            case 99:
                magicText = treasureArraySub[352];
                subText = res.getString(R.string.dmg213);
                break;

            case 100:
                magicText = treasureArraySub[353];
                subText = res.getString(R.string.dmg214);
                break;


        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table G to determine
     * a Magic Item
     */
    private void rollMagicG() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);
        String[] figurineArray = res.getStringArray(R.array.tr_selection_array_figurine);

        String magicText = "";
        String subText = "";

        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                magicText = treasureArraySub[349];
                subText = res.getString(R.string.dmg213);
                break;

            /**
             *  For case 12-14 add another roll of a D8
             *  dice to determine which figurine of
             *  wondrous power is picked
             *  this will need another array
             *  in strings.xml called tr_selection_array_figurine
             */
            case 12:
            case 13:
            case 14:
                Integer figurineRoll = r.nextInt(8 - 1);
                magicText = figurineArray[figurineRoll];
                subText = res.getString(R.string.dmg169);
                break;

            case 15:
                magicText = treasureArraySub[3];
                subText = res.getString(R.string.dmg150);
                break;

            case 16:
                magicText = treasureArraySub[9];
                subText = res.getString(R.string.dmg150);
                break;

            case 17:
                magicText = treasureArraySub[14];
                subText = res.getString(R.string.dmg150);
                break;

            case 18:
                magicText = treasureArraySub[28];
                subText = res.getString(R.string.dmg152);
                break;

            case 19:
                magicText = treasureArraySub[47];
                subText = res.getString(R.string.dmg152);
                break;

            case 20:
                magicText = treasureArraySub[56];
                subText = res.getString(R.string.dmg155);
                break;

            case 21:
                magicText = treasureArraySub[59];
                subText = res.getString(R.string.dmg155);
                break;

            case 22:
                magicText = treasureArraySub[61];
                subText = res.getString(R.string.dmg155);
                break;

            case 23:
                magicText = treasureArraySub[63];
                subText = res.getString(R.string.dmg155);
                break;

            case 24:
                magicText = treasureArraySub[64];
                subText = res.getString(R.string.dmg155);
                break;

            case 25:
                magicText = treasureArraySub[67];
                subText = res.getString(R.string.dmg156);
                break;

            case 26:
                magicText = treasureArraySub[69];
                subText = res.getString(R.string.dmg156);
                break;

            case 27:
                magicText = treasureArraySub[70];
                subText = res.getString(R.string.dmg156);
                break;

            case 28:
                magicText = treasureArraySub[75];
                subText = res.getString(R.string.dmg157);
                break;

            case 29:
                magicText = treasureArraySub[77];
                subText = res.getString(R.string.dmg158);
                break;

            case 30:
                magicText = treasureArraySub[34];
                subText = res.getString(R.string.dmg152);
                break;


            case 31:
                magicText = treasureArraySub[21];
                subText = res.getString(R.string.dmg152);
                break;

            case 32:
                magicText = treasureArraySub[45];
                subText = res.getString(R.string.dmg152);
                break;

            case 33:
                magicText = treasureArraySub[22];
                subText = res.getString(R.string.dmg152);
                break;

            case 34:
                magicText = treasureArraySub[82];
                subText = res.getString(R.string.dmg158);
                break;

            case 35:
                magicText = treasureArraySub[86];
                subText = res.getString(R.string.dmg159);
                break;

            case 36:
                magicText = treasureArraySub[90];
                subText = res.getString(R.string.dmg159);
                break;

            case 37:
                magicText = treasureArraySub[92];
                subText = res.getString(R.string.dmg160);
                break;

            case 38:
                magicText = treasureArraySub[93];
                subText = res.getString(R.string.dmg161);
                break;

            case 39:
                magicText = treasureArraySub[100];
                subText = res.getString(R.string.dmg165);
                break;

            case 40:
                magicText = treasureArraySub[102];
                subText = res.getString(R.string.dmg166);
                break;

            case 41:
                magicText = treasureArraySub[113];
                subText = res.getString(R.string.dmg168);
                break;

            case 42:
                magicText = treasureArraySub[120];
                subText = res.getString(R.string.dmg170);
                break;

            case 43:
                magicText = treasureArraySub[125];
                subText = res.getString(R.string.dmg172);
                break;

            case 44:
                magicText = treasureArraySub[126];
                subText = res.getString(R.string.dmg172);
                break;

            case 45:
                magicText = treasureArraySub[80];
                subText = res.getString(R.string.dmg172);
                break;

            case 46:
                magicText = treasureArraySub[137];
                subText = res.getString(R.string.dmg174);
                break;

            case 47:
                magicText = treasureArraySub[140];
                subText = res.getString(R.string.dmg174);
                break;

            case 48:
                magicText = treasureArraySub[141];
                subText = res.getString(R.string.dmg175);
                break;

            case 49:
                magicText = treasureArraySub[148];
                subText = res.getString(R.string.dmg176);
                break;

            case 50:
                magicText = treasureArraySub[153];
                subText = res.getString(R.string.dmg176);
                break;

            case 51:
                magicText = treasureArraySub[161];
                subText = res.getString(R.string.dmg176);
                break;

            case 52:
                magicText = treasureArraySub[168];
                subText = res.getString(R.string.dmg176);
                break;

            case 53:
                magicText = treasureArraySub[170];
                subText = res.getString(R.string.dmg176);
                break;

            case 54:
                magicText = treasureArraySub[172];
                subText = res.getString(R.string.dmg176);
                break;

            case 55:
                magicText = treasureArraySub[154];
                subText = res.getString(R.string.dmg177);
                break;

            case 56:
                magicText = treasureArraySub[31];
                subText = res.getString(R.string.dmg152);
                break;

            case 57:
                magicText = treasureArraySub[24];
                subText = res.getString(R.string.dmg152);
                break;

            case 58:
                magicText = treasureArraySub[174];
                subText = res.getString(R.string.dmg179);
                break;

            case 59:
                magicText = treasureArraySub[175];
                subText = res.getString(R.string.dmg179);
                break;

            case 60:
                magicText = treasureArraySub[176];
                subText = res.getString(R.string.dmg180);
                break;

            case 61:
                magicText = treasureArraySub[177];
                subText = res.getString(R.string.dmg180);
                break;

            case 62:
                magicText = treasureArraySub[188];
                subText = res.getString(R.string.dmg182);
                break;

            case 63:
                magicText = treasureArraySub[197];
                subText = res.getString(R.string.dmg184);
                break;

            case 64:
                magicText = treasureArraySub[236];
                subText = res.getString(R.string.dmg189);
                break;

            case 65:
                magicText = treasureArraySub[239];
                subText = res.getString(R.string.dmg191);
                break;

            case 66:
                magicText = treasureArraySub[240];
                subText = res.getString(R.string.dmg191);
                break;

            case 67:
                magicText = treasureArraySub[242];
                subText = res.getString(R.string.dmg191);
                break;

            case 68:
                magicText = treasureArraySub[246];
                subText = res.getString(R.string.dmg191);
                break;

            case 69:
                magicText = treasureArraySub[248];
                subText = res.getString(R.string.dmg192);
                break;

            case 70:
                magicText = treasureArraySub[2];
                subText = res.getString(R.string.dmg192);
                break;

            case 71:
                magicText = treasureArraySub[253];
                subText = res.getString(R.string.dmg193);
                break;

            case 72:
                magicText = treasureArraySub[258];
                subText = res.getString(R.string.dmg193);
                break;

            case 73:
                magicText = treasureArraySub[259];
                subText = res.getString(R.string.dmg193);
                break;

            case 74:
                magicText = treasureArraySub[268];
                subText = res.getString(R.string.dmg197);
                break;

            case 75:
                magicText = treasureArraySub[271];
                subText = res.getString(R.string.dmg197);
                break;

            case 76:
                magicText = treasureArraySub[274];
                subText = res.getString(R.string.dmg197);
                break;

            case 77:
                magicText = treasureArraySub[36];
                subText = res.getString(R.string.dmg152);
                break;

            case 78:
                magicText = treasureArraySub[25];
                subText = res.getString(R.string.dmg152);
                break;

            case 79:
                magicText = treasureArraySub[282];
                subText = res.getString(R.string.dmg200);
                break;

            case 80:
                magicText = treasureArraySub[281];
                subText = res.getString(R.string.dmg200);
                break;

            case 81:
                magicText = treasureArraySub[299];
                subText = res.getString(R.string.dmg201);
                break;

            case 82:
                magicText = treasureArraySub[302];
                subText = res.getString(R.string.dmg202);
                break;

            case 83:
                magicText = treasureArraySub[305];
                subText = res.getString(R.string.dmg203);
                break;

            case 84:
                magicText = treasureArraySub[309];
                subText = res.getString(R.string.dmg204);
                break;

            case 85:
                magicText = treasureArraySub[311];
                subText = res.getString(R.string.dmg205);
                break;

            case 86:
                magicText = treasureArraySub[312];
                subText = res.getString(R.string.dmg205);
                break;

            case 87:
                magicText = treasureArraySub[314];
                subText = res.getString(R.string.dmg205);
                break;

            case 88:
                magicText = treasureArraySub[316];
                subText = res.getString(R.string.dmg206);
                break;

            case 89:
                magicText = treasureArraySub[319];
                subText = res.getString(R.string.dmg207);
                break;

            case 90:
                magicText = treasureArraySub[323];
                subText = res.getString(R.string.dmg208);
                break;

            case 91:
                magicText = treasureArraySub[330];
                subText = res.getString(R.string.dmg209);
                break;

            case 92:
                magicText = treasureArraySub[332];
                subText = res.getString(R.string.dmg209);
                break;

            case 93:
                magicText = treasureArraySub[333];
                subText = res.getString(R.string.dmg210);
                break;

            case 94:
                magicText = treasureArraySub[334];
                subText = res.getString(R.string.dmg210);
                break;

            case 95:
                magicText = treasureArraySub[335];
                subText = res.getString(R.string.dmg210);
                break;

            case 96:
                magicText = treasureArraySub[336];
                subText = res.getString(R.string.dmg211);
                break;

            case 97:
                magicText = treasureArraySub[339];
                subText = res.getString(R.string.dmg211);
                break;

            case 98:
                magicText = treasureArraySub[343];
                subText = res.getString(R.string.dmg212);
                break;

            case 99:
                magicText = treasureArraySub[346];
                subText = res.getString(R.string.dmg212);
                break;

            case 100:
                magicText = treasureArraySub[354];
                subText = res.getString(R.string.dmg214);
                break;


        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table H to determine
     * a Magic Item
     */
    private void rollMagicH() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                magicText = treasureArraySub[350];
                subText = res.getString(R.string.dmg213);
                break;

            case 11:
            case 12:
                magicText = treasureArraySub[16];
                subText = res.getString(R.string.dmg150);
                break;

            case 13:
            case 14:
                magicText = treasureArraySub[76];
                subText = res.getString(R.string.dmg157);
                break;

            case 15:
            case 16:
                magicText = treasureArraySub[89];
                subText = res.getString(R.string.dmg159);
                break;

            case 17:
            case 18:
                magicText = treasureArraySub[247];
                subText = res.getString(R.string.dmg191);
                break;

            case 19:
            case 20:
                magicText = treasureArraySub[249];
                subText = res.getString(R.string.dmg192);
                break;

            case 21:
            case 22:
                magicText = treasureArraySub[252];
                subText = res.getString(R.string.dmg193);
                break;

            case 23:
            case 24:
                magicText = treasureArraySub[260];
                subText = res.getString(R.string.dmg194);
                break;

            case 25:
            case 26:
                magicText = treasureArraySub[261];
                subText = res.getString(R.string.dmg194);
                break;

            case 27:
            case 28:
                magicText = treasureArraySub[264];
                subText = res.getString(R.string.dmg195);
                break;

            case 29:
            case 30:
                magicText = treasureArraySub[265];
                subText = res.getString(R.string.dmg196);
                break;

            case 31:
            case 32:
                magicText = treasureArraySub[269];
                subText = res.getString(R.string.dmg197);
                break;

            case 33:
            case 34:
                magicText = treasureArraySub[272];
                subText = res.getString(R.string.dmg197);
                break;

            case 35:
            case 36:
                magicText = treasureArraySub[277];
                subText = res.getString(R.string.dmg199);
                break;

            case 37:
            case 38:
                magicText = treasureArraySub[283];
                subText = res.getString(R.string.dmg200);
                break;

            case 39:
            case 40:
                magicText = treasureArraySub[300];
                subText = res.getString(R.string.dmg201);
                break;

            case 41:
            case 42:
                magicText = treasureArraySub[301];
                subText = res.getString(R.string.dmg202);
                break;

            case 43:
            case 44:
                magicText = treasureArraySub[303];
                subText = res.getString(R.string.dmg202);
                break;

            case 45:
            case 46:
                magicText = treasureArraySub[304];
                subText = res.getString(R.string.dmg203);
                break;

            case 47:
            case 48:
                magicText = treasureArraySub[310];
                subText = res.getString(R.string.dmg204);
                break;

            case 49:
            case 50:
                magicText = treasureArraySub[317];
                subText = res.getString(R.string.dmg206);
                break;

            case 51:
            case 52:
                magicText = treasureArraySub[340];
                subText = res.getString(R.string.dmg211);
                break;

            case 53:
            case 54:
                magicText = treasureArraySub[344];
                subText = res.getString(R.string.dmg212);
                break;

            case 55:
                magicText = treasureArraySub[6];
                subText = res.getString(R.string.dmg150);
                break;

            case 56:
                magicText = treasureArraySub[7];
                subText = res.getString(R.string.dmg150);
                break;

            case 57:
                magicText = treasureArraySub[17];
                subText = res.getString(R.string.dmg151);
                break;

            case 58:
                magicText = treasureArraySub[57];
                subText = res.getString(R.string.dmg155);
                break;

            case 59:
                magicText = treasureArraySub[58];
                subText = res.getString(R.string.dmg155);
                break;

            case 60:
                magicText = treasureArraySub[29];
                subText = res.getString(R.string.dmg152);
                break;

            case 61:
                magicText = treasureArraySub[20];
                subText = res.getString(R.string.dmg152);
                break;

            case 62:
                magicText = treasureArraySub[73];
                subText = res.getString(R.string.dmg157);
                break;

            case 63:
                magicText = treasureArraySub[0];
                subText = res.getString(R.string.dmg152);
                break;

            case 64:
                magicText = treasureArraySub[1];
                subText = res.getString(R.string.dmg152);
                break;

            case 65:
                magicText = treasureArraySub[81];
                subText = res.getString(R.string.dmg158);
                break;

            case 66:
                magicText = treasureArraySub[94];
                subText = res.getString(R.string.dmg161);
                break;

            case 67:
                magicText = treasureArraySub[99];
                subText = res.getString(R.string.dmg165);
                break;

            case 68:
                magicText = treasureArraySub[101];
                subText = res.getString(R.string.dmg165);
                break;

            case 69:
                magicText = treasureArraySub[107];
                subText = res.getString(R.string.dmg167);
                break;

            case 70:
                magicText = treasureArraySub[108];
                subText = res.getString(R.string.dmg167);
                break;

            case 71:
                magicText = treasureArraySub[109];
                subText = res.getString(R.string.dmg167);
                break;

            case 72:
                magicText = treasureArraySub[118];
                subText = res.getString(R.string.dmg169);
                break;

            case 73:
                magicText = treasureArraySub[122];
                subText = res.getString(R.string.dmg171);
                break;

            case 74:
                magicText = treasureArraySub[134];
                subText = res.getString(R.string.dmg173);
                break;

            case 75:
                magicText = treasureArraySub[142];
                subText = res.getString(R.string.dmg175);
                break;

            case 76:
                magicText = treasureArraySub[147];
                subText = res.getString(R.string.dmg176);
                break;

            case 77:
                magicText = treasureArraySub[159];
                subText = res.getString(R.string.dmg176);
                break;

            case 78:
                magicText = treasureArraySub[160];
                subText = res.getString(R.string.dmg176);
                break;

            case 79:
                magicText = treasureArraySub[162];
                subText = res.getString(R.string.dmg176);
                break;

            case 80:
                magicText = treasureArraySub[164];
                subText = res.getString(R.string.dmg176);
                break;

            case 81:
                magicText = treasureArraySub[165];
                subText = res.getString(R.string.dmg176);
                break;

            case 82:
                magicText = treasureArraySub[166];
                subText = res.getString(R.string.dmg176);
                break;

            case 83:
                magicText = treasureArraySub[171];
                subText = res.getString(R.string.dmg176);
                break;

            case 84:
                magicText = treasureArraySub[38];
                subText = res.getString(R.string.dmg152);
                break;

            case 85:
                magicText = treasureArraySub[178];
                subText = res.getString(R.string.dmg180);
                break;

            case 86:
                magicText = treasureArraySub[179];
                subText = res.getString(R.string.dmg180);
                break;

            case 87:
                magicText = treasureArraySub[180];
                subText = res.getString(R.string.dmg180);
                break;

            case 88:
                magicText = treasureArraySub[181];
                subText = res.getString(R.string.dmg181);
                break;

            case 89:
                magicText = treasureArraySub[184];
                subText = res.getString(R.string.dmg181);
                break;

            case 90:
                magicText = treasureArraySub[189];
                subText = res.getString(R.string.dmg183);
                break;

            case 91:
                magicText = treasureArraySub[191];
                subText = res.getString(R.string.dmg183);
                break;

            case 92:
                magicText = treasureArraySub[39];
                subText = res.getString(R.string.dmg152);
                break;

            case 93:
                magicText = treasureArraySub[297];
                subText = res.getString(R.string.dmg201);
                break;

            case 94:
                magicText = treasureArraySub[32];
                subText = res.getString(R.string.dmg152);
                break;

            case 95:
                magicText = treasureArraySub[26];
                subText = res.getString(R.string.dmg152);
                break;

            case 96:
                magicText = treasureArraySub[33];
                subText = res.getString(R.string.dmg152);
                break;

            case 97:
                magicText = treasureArraySub[27];
                subText = res.getString(R.string.dmg152);
                break;

            case 98:
                magicText = treasureArraySub[324];
                subText = res.getString(R.string.dmg208);
                break;

            case 99:
                magicText = treasureArraySub[325];
                subText = res.getString(R.string.dmg208);
                break;

            case 100:
                magicText = treasureArraySub[327];
                subText = res.getString(R.string.dmg209);
                break;

        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table I to determine
     * a Magic Item
     */
    private void rollMagicI() {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1) + 1;
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);
        String[] armorArray = res.getStringArray(R.array.tr_selection_array_armor);

        String magicText = "";
        String subText = "";

        switch (roll) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                magicText = treasureArraySub[98];
                subText = res.getString(R.string.dmg164);
                break;

            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                magicText = treasureArraySub[131];
                subText = res.getString(R.string.dmg173);
                break;

            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                magicText = treasureArraySub[173];
                subText = res.getString(R.string.dmg179);
                break;

            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                magicText = treasureArraySub[315];
                subText = res.getString(R.string.dmg206);
                break;

            case 21:
            case 22:
            case 23:
                magicText = treasureArraySub[139];
                subText = res.getString(R.string.dmg174);
                break;

            case 24:
            case 25:
            case 26:
                magicText = treasureArraySub[237];
                subText = res.getString(R.string.dmg190);
                break;

            case 27:
            case 28:
            case 29:
                magicText = treasureArraySub[243];
                subText = res.getString(R.string.dmg191);
                break;

            case 30:
            case 31:
            case 32:
                magicText = treasureArraySub[250];
                subText = res.getString(R.string.dmg193);
                break;

            case 33:
            case 34:
            case 35:
                magicText = treasureArraySub[266];
                subText = res.getString(R.string.dmg196);
                break;

            case 36:
            case 37:
            case 38:
                magicText = treasureArraySub[307];
                subText = res.getString(R.string.dmg203);
                break;

            case 39:
            case 40:
            case 41:
                magicText = treasureArraySub[331];
                subText = res.getString(R.string.dmg209);
                break;

            case 42:
            case 43:
                magicText = treasureArraySub[55];
                subText = res.getString(R.string.dmg155);
                break;

            case 44:
            case 45:
                magicText = treasureArraySub[37];
                subText = res.getString(R.string.dmg152);
                break;

            case 46:
            case 47:
                magicText = treasureArraySub[42];
                subText = res.getString(R.string.dmg152);
                break;

            case 48:
            case 49:
                magicText = treasureArraySub[43];
                subText = res.getString(R.string.dmg152);
                break;

            case 50:
            case 51:
                magicText = treasureArraySub[84];
                subText = res.getString(R.string.dmg158);
                break;

            case 52:
            case 53:
                magicText = treasureArraySub[88];
                subText = res.getString(R.string.dmg159);
                break;

            case 54:
            case 55:
                magicText = treasureArraySub[30];
                subText = res.getString(R.string.dmg152);
                break;

            case 56:
            case 57:
                magicText = treasureArraySub[155];
                subText = res.getString(R.string.dmg178);
                break;

            case 58:
            case 59:
                magicText = treasureArraySub[44];
                subText = res.getString(R.string.dmg152);
                break;

            case 60:
            case 61:
                magicText = treasureArraySub[35];
                subText = res.getString(R.string.dmg152);
                break;

            case 62:
            case 63:
                magicText = treasureArraySub[262];
                subText = res.getString(R.string.dmg194);
                break;

            case 64:
            case 65:
                magicText = treasureArraySub[267];
                subText = res.getString(R.string.dmg197);
                break;

            case 66:
            case 67:
                magicText = treasureArraySub[36];
                subText = res.getString(R.string.dmg152);
                break;

            case 68:
            case 69:
                magicText = treasureArraySub[276];
                subText = res.getString(R.string.dmg199);
                break;

            case 70:
            case 71:
                magicText = treasureArraySub[40];
                subText = res.getString(R.string.dmg152);
                break;

            case 72:
            case 73:
                magicText = treasureArraySub[41];
                subText = res.getString(R.string.dmg152);
                break;

            case 74:
            case 75:
                magicText = treasureArraySub[351];
                subText = res.getString(R.string.dmg213);
                break;


            /**
             *  For case 76 add another roll of a D12
             *  dice to determine which armor item
             *  is picked.
             */
            case 76:
                Integer armorRoll = r.nextInt(12 - 1);
                magicText = armorArray[armorRoll];
                subText = res.getString(R.string.dmg152);
                break;

            case 77:
                magicText = treasureArraySub[18];
                subText = res.getString(R.string.dmg151);
                break;

            case 78:
                magicText = treasureArraySub[19];
                subText = res.getString(R.string.dmg152);
                break;

            case 79:
                magicText = treasureArraySub[60];
                subText = res.getString(R.string.dmg155);
                break;

            case 80:
                magicText = treasureArraySub[91];
                subText = res.getString(R.string.dmg160);
                break;

            case 81:
                magicText = treasureArraySub[97];
                subText = res.getString(R.string.dmg162);
                break;

            case 82:
                magicText = treasureArraySub[110];
                subText = res.getString(R.string.dmg167);
                break;

            case 83:
                magicText = treasureArraySub[23];
                subText = res.getString(R.string.dmg152);
                break;

            case 84:
                magicText = treasureArraySub[143];
                subText = res.getString(R.string.dmg175);
                break;

            case 85:
                magicText = treasureArraySub[152];
                subText = res.getString(R.string.dmg176);
                break;

            case 86:
                magicText = treasureArraySub[163];
                subText = res.getString(R.string.dmg176);
                break;

            case 87:
                magicText = treasureArraySub[167];
                subText = res.getString(R.string.dmg176);
                break;

            case 88:
                magicText = treasureArraySub[169];
                subText = res.getString(R.string.dmg176);
                break;

            case 89:
                magicText = treasureArraySub[202];
                subText = res.getString(R.string.dmg185);
                break;

            case 90:
                magicText = treasureArraySub[203];
                subText = res.getString(R.string.dmg152);
                break;

            case 91:
                magicText = treasureArraySub[235];
                subText = res.getString(R.string.dmg190);
                break;

            case 92:
                magicText = treasureArraySub[238];
                subText = res.getString(R.string.dmg190);
                break;

            case 93:
                magicText = treasureArraySub[241];
                subText = res.getString(R.string.dmg190);
                break;

            case 94:
                magicText = treasureArraySub[254];
                subText = res.getString(R.string.dmg193);
                break;

            case 95:
                magicText = treasureArraySub[256];
                subText = res.getString(R.string.dmg190);
                break;

            case 96:
                magicText = treasureArraySub[298];
                subText = res.getString(R.string.dmg201);
                break;

            case 97:
                magicText = treasureArraySub[320];
                subText = res.getString(R.string.dmg207);
                break;

            case 98:
                magicText = treasureArraySub[321];
                subText = res.getString(R.string.dmg207);
                break;

            case 99:
                magicText = treasureArraySub[322];
                subText = res.getString(R.string.dmg207);
                break;

            case 100:
                magicText = treasureArraySub[326];
                subText = res.getString(R.string.dmg208);
                break;


        }

        addToList(magicText, subText);
    }

}