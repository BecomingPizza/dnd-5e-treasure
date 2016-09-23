package com.pizzatech.dnd_5e_treasure;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.ListView;
import java.util.Random;

/**
 * Created by Ashley on 17/09/2016.
 *
 * Moving lots of shiz to an asynctask to maybe boost performance ¯\_(ツ)_/¯
 */
class TreasureRoller extends AsyncTask {

    Integer table;
    Context context;
    ListView listyMcListFace;
    Activity act;

    Resources res;

    Random r = new Random();

    String copperStr;
    String silverStr;
    String goldStr;
    String platinumStr;

    String[] treasureArray;
    String[] treasureArraySub;
    String[] pageArraySub;
    String[] figurineArray;
    String[] armorArray;

    public TreasureRoller(Context context, Integer table, ListView list, Activity act) {
        this.table = table;
        this.context = context;
        this.listyMcListFace = list;
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

    private void addToList(String mainText, String subText)
    {
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

        for (int j = 0; j < roll; j++) {
            switch (value) {
                case 10:
                    rollGem10GP();
                    break;
                case 50:
                    rollGem50GP();
                    break;
                case 100:
                    rollGem100GP();
                    break;
                case 500:
                    rollGem500GP();
                    break;
                case 1000:
                    rollGem1000GP();
                    break;
                case 5000:
                    rollGem5000GP();
            }
        }
    }

    private void rollGem10GP() {
        // Roll d12 to determine which gem
        Integer roll = r.nextInt(12 - 1);

        treasureArray = res.getStringArray(R.array.tr_array_gem10gp);
        treasureArraySub = res.getStringArray(R.array.tr_array_gem10gp_sub);

        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText);
    }

    private void rollGem50GP() {
        // Roll d12 to determine which gem
        Integer roll = r.nextInt(12 - 1);

        treasureArray = res.getStringArray(R.array.tr_array_gem50gp);
        treasureArraySub = res.getStringArray(R.array.tr_array_gem50gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText);
    }

    private void rollGem100GP() {
        // Roll d10 to determine which gem
        Integer roll = r.nextInt(10 - 1);

        treasureArray = res.getStringArray(R.array.tr_array_gem100gp);
        treasureArraySub = res.getStringArray(R.array.tr_array_gem100gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText);
    }

    private void rollGem500GP() {
        // Roll d6 to determine which gem
        Integer roll = r.nextInt(6 - 1);

        treasureArray = res.getStringArray(R.array.tr_array_gem500gp);
        treasureArraySub = res.getStringArray(R.array.tr_array_gem500gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText);
    }

    private void rollGem1000GP() {
        // Roll d8 to determine which gem
        Integer roll = r.nextInt(8 - 1);

        treasureArray = res.getStringArray(R.array.tr_array_gem1000gp);
        treasureArraySub = res.getStringArray(R.array.tr_array_gem1000gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText);
    }

    private void rollGem5000GP() {
        // Roll d4 to determine which gem
        Integer roll = r.nextInt(4 - 1);

        treasureArray = res.getStringArray(R.array.tr_array_gem5000gp);
        treasureArraySub = res.getStringArray(R.array.tr_array_gem5000gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText);
    }

    private void rollArt(Integer dice, Integer sides, Integer value) {
        Integer roll = 0;
        for (int i = 0; i < dice; i++) {
            roll += (r.nextInt(sides - 1) + 1);
        }
        for (int j = 0; j < roll; j++) {
            switch (value) {
                case 25:
                    rollArt25GP();
                    break;
                case 250:
                    rollArt250GP();
                    break;
                case 750:
                    rollArt750GP();
                    break;
                case 2500:
                    rollArt2500GP();
                    break;
                case 7500:
                    rollArt7500GP();
                    break;
            }
        }
    }

    private void rollArt25GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = res.getStringArray(R.array.tr_array_art25gp_sub);
        String artText = context.getString(R.string.tr_art_25gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText);
    }

    private void rollArt250GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = res.getStringArray(R.array.tr_array_art250gp_sub);
        String artText = context.getString(R.string.tr_art_250gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText);
    }

    private void rollArt750GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = res.getStringArray(R.array.tr_array_art750gp_sub);
        String artText = context.getString(R.string.tr_art_750gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText);
    }

    private void rollArt2500GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = res.getStringArray(R.array.tr_array_art2500gp_sub);
        String artText = context.getString(R.string.tr_art_2500gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText);
    }

    private void rollArt7500GP() {
        // Roll d8 to determine which art
        Integer roll = r.nextInt(8 - 1);

        treasureArraySub = res.getStringArray(R.array.tr_array_art7500gp_sub);
        String artText = context.getString(R.string.tr_art_7500gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText);
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
                rollMagic(1, 4, "C");
                break;
            case 98:
            case 99:
                rollArt(2, 4, 25);
                //TODO: remove comment
                //rollMagicG();
                break;
            case 100:
                rollGems(2, 6, 50);
                //TODO: remove comment
                //rollMagicG();
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
                rollGems(2, 4, 250);
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
                rollGems(2, 4, 250);
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
                rollGems(2, 4, 250);
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
                rollGems(2, 4, 250);
                rollMagic(1, 4, "C");
                break;
            case 75:
            case 76:
                rollArt(2, 4, 25);
                //ToDo: remove comment
                //rollMagicD();
                break;
            case 77:
            case 78:
                rollGems(3, 6, 50);
                //ToDo: remove comment
                //rollMagicD();
                break;
            case 79:
                rollGems(3, 6, 100);
                //ToDo: remove comment
                //rollMagicD();
                break;
            case 80:
                rollGems(2, 4, 250);
                //ToDo: remove comment
                //rollMagicD();
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
                rollGems(2, 4, 250);
                rollMagic(1, 4, "F");
                break;
            case 95:
            case 96:
                rollGems(3, 6, 100);
                rollMagic(1, 4, "G");
                break;
            case 97:
            case 98:
                rollGems(2, 4, 250);
                rollMagic(1, 4, "G");
                break;
            case 99:
                rollGems(3, 6, 100);
                //ToDo: remove comment
                //rollMagicH();
                break;
            case 100:
                rollGems(2, 4, 250);
                //ToDo: remove comment
                //rollMagicH();
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
                //ToDo: remove comment
                //rollMagicE();
                break;
            case 69:
            case 70:
                rollArt(2, 4, 750);
                //ToDo: remove comment
                //rollMagicE();
                break;
            case 71:
            case 72:
                rollGems(3, 6, 500);
                //ToDo: remove comment
                //rollMagicE();
                break;
            case 73:
            case 74:
                rollGems(3, 6, 1000);
                //ToDo: remove comment
                //rollMagicE();
                break;
            case 75:
            case 76:
                rollArt(2, 4, 250);
                //ToDo: remove comment
                //rollMagicF();
                rollMagic(1, 4, "G");
                break;
            case 77:
            case 78:
                rollArt(2, 4, 750);
                //ToDo: remove comment
                //rollMagicF();
                rollMagic(1, 4, "G");
                break;
            case 79:
            case 80:
                rollGems(3, 6, 500);
                //ToDo: remove comment
                //rollMagicF();
                rollMagic(1, 4, "G");
                break;
            case 81:
            case 82:
                rollGems(3, 6, 1000);
                //ToDo: remove comment
                //rollMagicF();
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
                //ToDo: remove comment
                //rollMagicI();
                break;
            case 95:
            case 96:
                rollArt(2, 4, 750);
                //ToDo: remove comment
                //rollMagicI();
                break;
            case 97:
            case 98:
                rollGems(3, 6, 500);
                //ToDo: remove comment
                //rollMagicI();
                break;
            case 99:
            case 100:
                //ToDo: remove comment
                //rollMagicI();
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
    private void rollMagic(Integer dice, Integer sides, String table)
    {
        Integer roll = 0;
        //Roll the dice to value of 'dice' times
        for (int i = 0; i < dice; i++)
        {
            //simulate dice roll to determine what it lands on
            //sides determines the range of possible values
            roll += (r.nextInt(sides - 1) + 1);
        }
        for (int j = 0; j < roll; j++)
        {
            /*determine which magic table to roll on
            * perform this action 'roll' times resulting in a list of loot
            * 'roll' in size
            */
            switch (table)
            {
                case "A":
                    rollMagicA();
                    break;
                case "B":
                    //TODO: remove comment
                    //rollMagicB();
                    break;
                case "C":
                    //TODO: remove comment
                    //rollMagicC();
                    break;
                case "D":
                    //TODO: remove comment
                    //rollMagicD();
                    break;
                case "E":
                    //TODO: remove comment
                    //rollMagicE();
                    break;
                case "F":
                    //TODO: remove comment
                    //rollMagicF();
                    break;
                case "G":
                    //TODO: remove comment
                    //rollMagicG();
                    break;
                case "H":
                    //TODO: remove comment
                    //rollMagicH();
                    break;
                case "I":
                    //TODO: remove comment
                    //rollMagicI();
                    break;
            }
        }


    }

    /**
     * Roll a D100 on table A to determine
     * a Magic Item
     */
    private void rollMagicA()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);

        String magicText = "";
        String subText = "";

        switch(roll)
        {
            //If roll is between 1 and 50 inclusive
            case 1: case 2: case 3: case 4: case 5:
            case 6: case 7: case 8: case 9: case 10:
            case 11: case 12: case 13: case 14: case 15:
            case 16: case 17: case 18: case 19: case 20:
            case 21: case 22: case 23: case 24: case 25:
            case 26: case 27: case 28: case 29: case 30:
            case 31: case 32: case 33: case 34: case 35:
            case 36: case 37: case 38: case 39: case 40:
            case 41: case 42: case 43: case 44: case 45:
            case 46: case 47: case 48: case 49: case 50:

            magicText = treasureArraySub[217];
            subText = res.getString(R.string.dmg187);
            break;

            //If roll is between 51 and 60 inclusive
            case 51: case 52: case 53: case 54: case 55:
            case 56: case 57: case 58: case 59: case 60:

            magicText = treasureArraySub[296];
            subText = res.getString(R.string.dmg200);
            break;

            //If roll is between 61 and 70 inclusive
            case 61: case 62: case 63: case 64: case 65:
            case 66: case 67: case 68: case 69: case 70:

            magicText = treasureArraySub[207];
            subText = res.getString(R.string.dmg187);
            break;

            //If roll is between 71 and 90 inclusive
            case 71: case 72: case 73: case 74: case 75:
            case 76: case 77: case 78: case 79: case 80:
            case 81: case 82: case 83: case 84: case 85:
            case 86: case 87: case 88: case 89: case 90:

            magicText = treasureArraySub[287];
            subText = res.getString(R.string.dmg200);
            break;

            //If roll is between 91 and 94 inclusive
            case 91: case 92: case 93: case 94:

            magicText = treasureArraySub[288];
            subText = res.getString(R.string.dmg200);
            break;

            //If roll is between 95 and 98 inclusive
            case 95: case 96: case 97: case 98:

            magicText = treasureArraySub[215];
            subText = res.getString(R.string.dmg187);
            break;

            //If roll is 99
            case 99:

                magicText = treasureArraySub[50];
                subText = res.getString(R.string.dmg152);
                break;

            //If roll is 100
            case 100:
                magicText = treasureArraySub[103];
                subText = res.getString(R.string.dmg166);
                break;

        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table B to determine
     * a Magic Item
     */
    private void rollMagicB()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        switch(roll)
        {
            //If roll is between 1 and 15 inclusive
            case 1: case 2: case 3: case 4: case 5:
            case 6: case 7: case 8: case 9: case 10:
            case 11: case 12: case 13: case 14: case 15:

            magicText = treasureArraySub[215];
            subText = res.getString(R.string.dmg187);
            break;

            //If roll is between 16 and 22 inclusive
            case 16: case 17: case 18: case 19: case 20:
            case 21:case 22:

            magicText = treasureArraySub[215];
            subText = res.getString(R.string.dmg187);
            break;

            //If roll is between 23 and 29 inclusive
            case 23: case 24: case 25: case 26: case 27:
            case 28: case 29:

            magicText = treasureArraySub[225];
            subText = res.getString(R.string.dmg188);
            break;

            //If roll is between 30 and 34 inclusive
            case 30: case 31: case 32: case 33: case 34:

            magicText = treasureArraySub[11];
            subText = res.getString(R.string.dmg150);
            break;

            //If roll is between  35 and 39 inclusive
            case 35: case 36: case 37: case 38: case 39:

            magicText = treasureArraySub[205];
            subText = res.getString(R.string.dmg187);
            break;

            case 40: case 41: case 42: case 43: case 44:

            magicText = treasureArraySub[219];
            subText = res.getString(R.string.dmg187);
            break;


            case 45: case 46: case 47: case 48: case 49:

            magicText = treasureArraySub[216];
            subText = res.getString(R.string.dmg187);
            break;

            case 50: case 51: case 52: case 53: case 54:

            magicText = treasureArraySub[20];
            subText = res.getString(R.string.dmg188);
            break;

            case 55: case 56: case 57: case 58: case 59:

            magicText = treasureArraySub[288];
            subText = res.getString(R.string.dmg200);
            break;

            case 60: case 61: case 62: case 63: case 64:

            magicText = treasureArraySub[289];
            subText = res.getString(R.string.dmg200);
            break;

            case 65: case 66: case 67:

            magicText = treasureArraySub[50];
            subText = res.getString(R.string.dmg153);
            break;

            case 68: case 69: case 70:

            magicText = treasureArraySub[157];
            subText = res.getString(R.string.dmg179);
            break;

            case 71: case 72: case 73:

            magicText = treasureArraySub[194];
            subText = res.getString(R.string.dmg184);
            break;

            case 74: case 75:

            magicText = treasureArraySub[104];
            subText = res.getString(R.string.dmg166);
            break;

            case 76: case 77:

            magicText = treasureArraySub[105];
            subText = res.getString(R.string.dmg166);
            break;

            case 78: case 79:

            magicText = treasureArraySub[106];
            subText = res.getString(R.string.dmg166);
            break;

            case 80: case 81:

            magicText = treasureArraySub[111];
            subText = res.getString(R.string.dmg167);
            break;

            case 82: case 83:

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
    private void rollMagicC()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        //TODO: Populate switch

        switch(roll)
        {
            case 1:case 2:case 3:case 4:case 5:
            case 6:case 7:case 8:case 9:case 10:
            case 11:case 12:case 13:case 14:case 15:

            magicText = treasureArraySub[229];
            subText = res.getString(R.string.dmg187);
            break;

            case 16:case 17:case 18:case 19:case 20:
            case 21:case 22:

            magicText = treasureArraySub[290];
            subText = res.getString(R.string.dmg200);
            break;

            case 23:case 24:case 25:case 26:case 27:

            magicText = treasureArraySub[12];
            subText = res.getString(R.string.dmg150);
            break;

            case 28:case 29:case 30:case 31:case 32:

            magicText = treasureArraySub[206];
            subText = res.getString(R.string.dmg187);
            break;

            case 33:case 34:case 35:case 36:case 37:

            magicText = treasureArraySub[209];
            subText = res.getString(R.string.dmg187);
            break;

            case 38:case 39:case 40:case 41:case 42:

            magicText = treasureArraySub[214];
            subText = res.getString(R.string.dmg187);
            break;

            case 43:case 44:case 45:case 46:case 47:

            magicText = treasureArraySub[213];
            subText = res.getString(R.string.dmg187);
            break;

            case 48:case 49:case 50:case 51:case 52:

            magicText = treasureArraySub[227];
            subText = res.getString(R.string.dmg187);
            break;

            case 53:case 54:case 55:case 56:case 57:

            magicText = treasureArraySub[218];
            subText = res.getString(R.string.dmg188);
            break;

            case 58:case 59:case 60:case 61:case 62:

            magicText = treasureArraySub[221];
            subText = res.getString(R.string.dmg188);
            break;

            case 63:case 64:case 65:case 66:case 67:

            magicText = treasureArraySub[223];
            subText = res.getString(R.string.dmg188);
            break;

            case 68:case 69:case 70:case 71:case 72:

            magicText = treasureArraySub[291];
            subText = res.getString(R.string.dmg200);
            break;

            case 73:case 74:case 75:

            magicText = treasureArraySub[112];
            subText = res.getString(R.string.dmg168);
            break;

            case 76:case 77:case 78:

            magicText = treasureArraySub[192];
            subText = res.getString(R.string.dmg183);
            break;

            case 79:case 80:case 81:

            magicText = treasureArraySub[210];
            subText = res.getString(R.string.dmg187);
            break;

            case 82:case 83:case 84:

            magicText = treasureArraySub[233];
            subText = res.getString(R.string.dmg188);
            break;

            case 85:case 86:case 87:

            magicText = treasureArraySub[278];
            subText = res.getString(R.string.dmg189);
            break;

            case 88:case 89:

            magicText = treasureArraySub[48];
            subText = res.getString(R.string.dmg152);
            break;

            case 90:case 91:

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
    private void rollMagicD()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        //TODO: Populate page array in strings.xml and add references to switch statement
        //TODO: Populate switch

        switch(roll)
        {
            case 1: case 2: case 3: case 4: case 5:
            case 6: case 7: case 8: case 9: case 10:
            case 11: case 12: case 13: case 14: case 15:
            case 16: case 17: case 18: case 19: case 20:

                magicText = treasureArraySub[230];
                subText = res.getString(R.string.dmg187);
                break;

            case 21: case 22: case 23: case 24: case 25:
            case 26: case 27: case 28: case 29: case 30:

            magicText = treasureArraySub[220];
            subText = res.getString(R.string.dmg188);
            break;

            case 31: case 32: case 33: case 34: case 35:
            case 36: case 37: case 38: case 39: case 40:

            magicText = treasureArraySub[226];
            subText = res.getString(R.string.dmg188);
            break;

            case 41: case 42: case 43: case 44: case 45:
            case 46: case 47: case 48: case 49: case 50:

            magicText = treasureArraySub[292];
            subText = res.getString(R.string.dmg200);
            break;

            case 51: case 52: case 53: case 54: case 55:
            case 56: case 57:

            magicText = treasureArraySub[293];
            subText = res.getString(R.string.dmg200);
            break;

            case 58: case 59: case 60: case 61: case 62:

            magicText = treasureArraySub[13];
            subText = res.getString(R.string.dmg150);
            break;

            case 63: case 64: case 65: case 66: case 67:

            magicText = treasureArraySub[193];
            subText = res.getString(R.string.dmg184);
            break;

            case 68: case 69: case 70: case 71: case 72:

            magicText = treasureArraySub[212];
            subText = res.getString(R.string.dmg187);
            break;

            case 73: case 74: case 75: case 76: case 77:

            magicText = treasureArraySub[208];
            subText = res.getString(R.string.dmg187);
            break;

            case 78: case 79: case 80: case 81: case 82:

            magicText = treasureArraySub[222];
            subText = res.getString(R.string.dmg188);
            break;

            case 83: case 84: case 85: case 86: case 87:

            magicText = treasureArraySub[231];
            subText = res.getString(R.string.dmg188);
            break;

            case 88: case 89: case 90: case 91: case 92:

            magicText = treasureArraySub[294];
            subText = res.getString(R.string.dmg200);
            break;

            case 93: case 94: case 95: case 96: case 97:
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
    private void rollMagicE()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        //TODO: Populate page array in strings.xml and add references to switch statement
        //TODO: Populate switch

        switch(roll)
        {


        }

        addToList(magicText, subText);
    }
    /**
     * Roll a D100 on table F to determine
     * a Magic Item
     */
    private void rollMagicF()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);

        String magicText = "";
        String subText = "";

        //TODO: Populate page array in strings.xml and add references to switch statement
        //TODO: Populate switch

        switch(roll)
        {


        }

        addToList(magicText, subText);
    }
    /**
     * Roll a D100 on table G to determine
     * a Magic Item
     */
    private void rollMagicG()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);
        figurineArray = res.getStringArray(R.array.tr_selection_array_figurine);

        String magicText = "";
        String subText = "";

        //TODO: Populate page array in strings.xml and add references to switch statement
        //TODO: Populate switch

        switch(roll)
        {
            /**
             *  For case 12-14 add another roll of a D8
             *  dice to determine which figurin of
             *  wonderous power is picked
             *  this will need another array
             *  in strings.xml called tr_selection_array_figurine
             */
            case 12:case 13: case 14:

            Integer figurineRoll = r.nextInt(8-1);
            if ((figurineRoll >= 6) && (figurineRoll <=7))
            {
                magicText = figurineArray[6];
                subText = roll.toString();
                // subText = pageArraySub[roll];
            }
            else
            {
                magicText = figurineArray[roll];
                subText = roll.toString();
                // subText = pageArraySub[roll];
            }
        }

        addToList(magicText, subText);
    }

    /**
     * Roll a D100 on table H to determine
     * a Magic Item
     */
    private void rollMagicH()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);


        String magicText = "";
        String subText = "";

        //TODO: Populate page array in strings.xml and add references to switch statement
        //TODO: Populate switch

        switch(roll)
        {


        }

        addToList(magicText, subText);
    }
    /**
     * Roll a D100 on table I to determine
     * a Magic Item
     */
    private void rollMagicI()
    {
        // Roll d100 to determine which magic item
        Integer roll = r.nextInt(100 - 1);
        treasureArraySub = res.getStringArray(R.array.tr_selection_array_magic_sub);
        armorArray = res.getStringArray(R.array.tr_selection_array_armor);

        String magicText = "";
        String subText = "";

        //TODO: Populate page array in strings.xml and add references to switch statement
        //TODO: Populate switch

        switch(roll)
        {
            /**
             *  For case 76 add another roll of a D12
             *  dice to determine which armor item
             *  is picked.
             */
            case 76:

            Integer armorRoll = r.nextInt(12-1);
            if ((armorRoll == 9) || (armorRoll == 10))
            {
                magicText = armorArray[5];
                subText = roll.toString();
                // subText = pageArraySub[roll];
            }
            else
            {
                magicText = armorArray[roll];
                subText = roll.toString();
                // subText = pageArraySub[roll];
            }

        }

        addToList(magicText, subText);
    }

}