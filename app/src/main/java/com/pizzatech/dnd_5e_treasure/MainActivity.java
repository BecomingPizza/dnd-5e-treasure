package com.pizzatech.dnd_5e_treasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //TODO : Make some fancy ass list objects!

    ArrayList<TreasureListItem> treasureItems = new ArrayList<>();

    TreasureListItemAdapter treasureItemsListAdapter;

    Random r = new Random();

    String copperStr;
    String silverStr;
    String goldStr;
    String platinumStr;

    Integer copperDrw;
    Integer silverDrw;
    Integer goldDrw;
    Integer platinumDrw;

    String[] treasureArray;
    String[] treasureArraySub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set strings & drawables for coins
        copperStr = getResources().getString(R.string.tr_coin_copper);
        silverStr = getResources().getString(R.string.tr_coin_silver);
        goldStr = getResources().getString(R.string.tr_coin_gold);
        platinumStr = getResources().getString(R.string.tr_coin_platinum);

        copperDrw = R.drawable.coin_bronze;
        silverDrw = R.drawable.coin_silver;
        goldDrw = R.drawable.coin_gold;
        platinumDrw = R.drawable.coin_platinum;


        //Populate teh spinnor
        Spinner cr_spinner = (Spinner) findViewById(R.id.cr_selection_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cr_selection_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cr_spinner.setAdapter(adapter);

        //Hook up the list
        treasureItemsListAdapter = new TreasureListItemAdapter(this, R.layout.treasure_list_item, treasureItems);
        ListView listyMcListFace = (ListView) findViewById(R.id.results_list);
        listyMcListFace.setAdapter(treasureItemsListAdapter);
    }

    public void rollTreasure(View v) {
        //Clear list
        treasureItems.clear();

        //Grab CR
        Spinner cr_spinner = (Spinner) findViewById(R.id.cr_selection_spinner);
        Integer cr_selected = cr_spinner.getSelectedItemPosition();

        switch (cr_selected) {
            case 0:
                //Roll for CR 0-4
                rollCoins(6, 100, copperStr, copperDrw);
                rollCoins(3, 100, silverStr, silverDrw);
                rollCoins(2, 10, goldStr, goldDrw);
                rollTreasureTableA();
                break;
            case 1:
                //Roll for CR 5-10
                rollCoins(2, 100, copperStr, copperDrw);
                rollCoins(2, 1000, silverStr, silverDrw);
                rollCoins(6, 100, goldStr, goldDrw);
                rollCoins(3, 10, platinumStr, platinumDrw);
                rollTreasureTableB();
                break;
            case 2:
                //Roll for CR 11-16
                rollCoins(4, 1000, goldStr, goldDrw);
                rollCoins(5, 100, platinumStr, platinumDrw);
                rollTreasureTableC();
                break;
            case 3:
                //Roll for CR 17+
                rollCoins(12, 1000, goldStr, goldDrw);
                rollCoins(8, 1000, platinumStr, platinumDrw);
                rollTreasureTableD();
                break;
        }
    }


    private void addToList (String mainText, String subText, int img) {
        // Build TreasureListItem
        TreasureListItem t = new TreasureListItem(mainText, subText, img);

        // Put some shit in the list
        treasureItems.add(t);
        treasureItemsListAdapter.notifyDataSetChanged();
    }

    private void rollCoins(Integer dice, Integer multiplier, String coinType, int img) {
        // Roll a d6 the specified number of times and sum the total (coins are always d6 rolls!)
        Integer roll = 0;
        for (int i = 0; i < dice; i++) {
            roll += (r.nextInt(6 - 1) + 1);
        }
        // Sum x multiplier
        Integer coins = roll * multiplier;
        // Build string and add to the list
        String listText = coins.toString() + " " + coinType;
        addToList(listText, null, img);
    }

    private void rollGems(Integer dice, Integer sides, Integer value) {
        Integer roll = 0;
        for (int i = 0; i < dice; i++)
        {
            roll += (r.nextInt(sides - 1) + 1);
        }
        for (int j = 0; j < roll; j++)
        {
            switch(value) {
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
                    break;
            }
        }
    }

    private void rollGem10GP() {
        // Roll d12 to determine which gem
        Integer roll = r.nextInt(12 - 1);


        treasureArray = getResources().getStringArray(R.array.tr_array_gem10gp);
        treasureArraySub = getResources().getStringArray(R.array.tr_array_gem10gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];


        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollGem50GP() {
        // Roll d12 to determine which gem
        Integer roll = r.nextInt(12 - 1);

        treasureArray = getResources().getStringArray(R.array.tr_array_gem50gp);
        treasureArraySub = getResources().getStringArray(R.array.tr_array_gem50gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollGem100GP() {
        // Roll d10 to determine which gem
        Integer roll = r.nextInt(10 - 1);

        treasureArray = getResources().getStringArray(R.array.tr_array_gem100gp);
        treasureArraySub = getResources().getStringArray(R.array.tr_array_gem100gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollGem500GP() {
        // Roll d6 to determine which gem
        Integer roll = r.nextInt(6 - 1);

        treasureArray = getResources().getStringArray(R.array.tr_array_gem500gp);
        treasureArraySub = getResources().getStringArray(R.array.tr_array_gem500gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollGem1000GP() {
        // Roll d8 to determine which gem
        Integer roll = r.nextInt(8 - 1);

        treasureArray = getResources().getStringArray(R.array.tr_array_gem1000gp);
        treasureArraySub = getResources().getStringArray(R.array.tr_array_gem1000gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollGem5000GP() {
        // Roll d4 to determine which gem
        Integer roll = r.nextInt(4 - 1);

        treasureArray = getResources().getStringArray(R.array.tr_array_gem5000gp);
        treasureArraySub = getResources().getStringArray(R.array.tr_array_gem5000gp_sub);
        String gemText = treasureArray[roll];
        String subText = treasureArraySub[roll];

        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollArt(Integer dice, Integer sides, Integer value) {
        Integer roll = 0;
        for (int i = 0; i < dice; i++)
        {
            roll += (r.nextInt(sides - 1) + 1);
        }
        for (int j = 0; j < roll; j++)
        {
            switch(value) {
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

        treasureArraySub = getResources().getStringArray(R.array.tr_array_art25gp_sub);
        String artText = getString(R.string.tr_art_25gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText, R.drawable.coin_gold);
    }

    private void rollArt250GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = getResources().getStringArray(R.array.tr_array_art250gp_sub);
        String artText = getString(R.string.tr_art_250gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText, R.drawable.coin_gold);
    }

    private void rollArt750GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = getResources().getStringArray(R.array.tr_array_art750gp_sub);
        String artText = getString(R.string.tr_art_750gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText, R.drawable.coin_gold);
    }

    private void rollArt2500GP() {
        // Roll d10 to determine which art
        Integer roll = r.nextInt(10 - 1);

        treasureArraySub = getResources().getStringArray(R.array.tr_array_art2500gp_sub);
        String artText = getString(R.string.tr_art_2500gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText, R.drawable.coin_gold);
    }

    private void rollArt7500GP() {
        // Roll d8 to determine which art
        Integer roll = r.nextInt(8 - 1);

        treasureArraySub = getResources().getStringArray(R.array.tr_array_art7500gp_sub);
        String artText = getString(R.string.tr_art_7500gp);
        String subText = treasureArraySub[roll];

        addToList(artText, subText, R.drawable.coin_gold);
    }

    private void rollTreasureTableA() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch(roll) {
            case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
                rollGems(2, 6, 10);
                break;
            case 17:case 18:case 19:case 20:case 21:case 22:case 23:case 24:case 25:case 26:
                rollArt(2, 4, 25);
                break;
            case 27:case 28:case 29:case 30:case 31:case 32:case 33:case 34:case 35:case 36:
                rollGems(2, 6, 50);
                break;
            case 37:case 38:case 39:case 40:case 41:case 42:case 43:case 44:
                rollGems(2, 6, 10);
                break;
            case 45:case 46:case 47:case 48:case 49:case 50:case 51:case 52:
                rollArt(2, 4, 25);
                break;
            case 53:case 54:case 55:case 56:case 57:case 58:case 59:case 60:
                rollGems(2, 6, 50);
                break;
            case 61:case 62:case 63:case 64:case 65:
                rollGems(2, 6, 10);
                break;
            case 66:case 67:case 68:case 69:case 70:
                rollArt(2, 4, 25);
                break;
            case 71:case 72:case 73:case 74:case 75:
                rollGems(2, 6, 50);
                break;
            case 76:case 77:case 78:
                rollGems(2, 6, 10);
                break;
            case 79:case 80:
                rollArt(2, 4, 25);
                break;
            case 81:case 82:case 83:case 84:case 85:
                rollGems(2, 6, 50);
                break;
            case 86:case 87:case 88:case 89:case 90:case 91:case 92:
                rollArt(2, 4, 25);
                break;
            case 93:case 94:case 95:case 96:case 97:
                rollGems(2, 6, 50);
                break;
            case 98:case 99:
                rollArt(2, 4, 25);
                break;
            case 100:
                rollGems(2, 6, 50);
                break;
        }

    }

    private void rollTreasureTableB() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch(roll) {
            case 5:case 6:case 7:case 8:case 9:case 10:
                rollArt(2, 4, 25);
                break;
            case 11:case 12:case 13:case 14:case 15:case 16:
                rollGems(3, 6, 50);
                break;
            case 17:case 18:case 19:case 20:case 21:case 22:
                rollGems(3, 6, 100);
                break;
            case 23:case 24:case 25:case 26:case 27:case 28:
                rollGems(2, 4, 250);
                break;
            case 29:case 30:case 31:case 32:
                rollArt(2, 4, 25);
                break;
            case 33:case 34:case 35:case 36:
                rollGems(3, 6, 50);
                break;
            case 37:case 38:case 39:case 40:
                rollGems(3, 6, 100);
                break;
            case 41:case 42:case 43:case 44:
                rollGems(2, 4, 250);
                break;
            case 45:case 46:case 47:case 48:case 49:
                rollArt(2, 4, 25);
                break;
            case 50:case 51:case 52:case 53:case 54:
                rollGems(3, 6, 50);
                break;
            case 55:case 56:case 57:case 58:case 59:
                rollGems(3, 6, 100);
                break;
            case 60:case 61:case 62:case 63:
                rollGems(2, 4, 250);
                break;
            case 64:case 65:case 66:
                rollArt(2, 4, 25);
                break;
            case 67:case 68:case 69:
                rollGems(3, 6, 50);
                break;
            case 70:case 71:case 72:
                rollGems(3, 6, 100);
                break;
            case 73:case 74:
                rollGems(2, 4, 250);
                break;
            case 75:case 76:
                rollArt(2, 4, 25);
                break;
            case 77:case 78:
                rollGems(3, 6, 50);
                break;
            case 79:
                rollGems(3, 6, 100);
                break;
            case 80:
                rollGems(2, 4, 250);
                break;
            case 81:case 82:case 83:case 84:
                rollArt(2, 4, 25);
                break;
            case 85:case 86:case 87:case 88:
                rollGems(3, 6, 50);
                break;
            case 89:case 90:case 91:
                rollGems(3, 6, 100);
                break;
            case 92:case 93:case 94:
                rollGems(2, 4, 250);
                break;
            case 95:case 96:
                rollGems(3, 6, 100);
                break;
            case 97:case 98:
                rollGems(2, 4, 250);
                break;
            case 99:
                rollGems(3, 6, 100);
                break;
            case 100:
                rollGems(2, 4, 250);
                break;
        }

    }

    private void rollTreasureTableC() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch(roll) {
            case 4:case 5:case 6:
                rollArt(2, 4, 250);
                break;
            case 7:case 8:case 9:case 10:
                rollArt(2, 4, 750);
                break;
            case 11:case 12:
                rollGems(3, 6, 500);
                break;
            case 13:case 14:case 15:
                rollGems(3, 6, 1000);
                break;
            case 16:case 17:case 18:case 19:
                rollArt(2, 4, 250);
                break;
            case 20:case 21:case 22:case 23:
                rollArt(2, 4, 750);
                break;
            case 24:case 25:case 26:
                rollGems(3, 6, 500);
                break;
            case 27:case 28:case 29:
                rollGems(3, 6, 1000);
                break;
            case 30:case 31:case 32:case 33:case 34:case 35:
                rollArt(2, 4, 250);
                break;
            case 36:case 37:case 38:case 39:case 40:
                rollArt(2, 4, 750);
                break;
            case 41:case 42:case 43:case 44:case 45:
                rollGems(3, 6, 500);
                break;
            case 46:case 47:case 48:case 49:case 50:
                rollGems(3, 6, 1000);
                break;
            case 51:case 52:case 53:case 54:
                rollArt(2, 4, 250);
                break;
            case 55:case 56:case 57:case 58:
                rollArt(2, 4, 750);
                break;
            case 59:case 60:case 61:case 62:
                rollGems(3, 6, 500);
                break;
            case 63:case 64:case 65:case 66:
                rollGems(3, 6, 1000);
                break;
            case 67:case 68:
                rollArt(2, 4, 250);
                break;
            case 69:case 70:
                rollArt(2, 4, 750);
                break;
            case 71:case 72:
                rollGems(3, 6, 500);
                break;
            case 73:case 74:
                rollGems(3, 6, 1000);
                break;
            case 75:case 76:
                rollArt(2, 4, 250);
                break;
            case 77:case 78:
                rollArt(2, 4, 750);
                break;
            case 79:case 80:
                rollGems(3, 6, 500);
                break;
            case 81:case 82:
                rollGems(3, 6, 1000);
                break;
            case 83:case 84:case 85:
                rollArt(2, 4, 250);
                break;
            case 86:case 87:case 88:
                rollArt(2, 4, 750);
                break;
            case 89:case 90:
                rollGems(3, 6, 500);
                break;
            case 91:case 92:
                rollGems(3, 6, 1000);
                break;
            case 93:case 94:
                rollArt(2, 4, 250);
                break;
            case 95:case 96:
                rollArt(2, 4, 750);
                break;
            case 97:case 98:
                rollGems(3, 6, 500);
                break;
            case 99:case 100:
                break;
        }

    }

    private void rollTreasureTableD() {
        // Roll d100
        Integer roll = r.nextInt(100 - 1) + 1;

        switch(roll) {
            case 3: case 4:case 5:
                rollGems(3, 6, 1000);
                break;
            case 6:case 7:case 8:
                rollArt(1, 10, 2500);
                break;
            case 9:case 10:case 11:
                rollArt(1, 4, 7500);
                break;
            case 12:case 13:case 14:
                rollGems(1, 8, 5000);
                break;
            case 15:case 16:case 17:case 18:case 19:case 20:case 21:case 22:
                rollGems(3, 6, 1000);
                break;
            case 23:case 24:case 25:case 26:case 27:case 28:case 29:case 30:
                rollArt(1, 10, 2500);
                break;
            case 31:case 32:case 33:case 34:case 35:case 36:case 37:case 38:
                rollArt(1, 4, 7500);
                break;
            case 39:case 40:case 41:case 42:case 43:case 44:case 45:case 46:
                rollGems(1, 8, 5000);
                break;
            case 47:case 48:case 49:case 50:case 51:case 52:
                rollGems(3, 6, 1000);
                break;
            case 53:case 54:case 55:case 56:case 57:case 58:
                rollArt(1, 10, 2500);
                break;
            case 59:case 60:case 61:case 62:case 63:
                rollArt(1, 4, 7500);
                break;
            case 64:case 65:case 66:case 67:case 68:
                rollGems(1, 8, 5000);
                break;
            case 69:
                rollGems(3, 6, 1000);
                break;
            case 70:
                rollArt(1, 10, 2500);
                break;
            case 71:
                rollArt(1, 4, 7500);
                break;
            case 72:
                rollGems(1, 8, 5000);
                break;
            case 73:case 74:
                rollGems(3, 6, 1000);
                break;
            case 75:case 76:
                rollArt(1, 10, 2500);
                break;
            case 77:case 78:
                rollArt(1, 4, 7500);
                break;
            case 79:case 80:
                rollGems(1, 8, 5000);
                break;
            case 81:case 82:case 83:case 84:case 85:
                rollGems(3, 6, 1000);
                break;
            case 86:case 87:case 88:case 89:case 90:
                rollArt(1, 10, 2500);
                break;
            case 91:case 92:case 93:case 94:case 95:
                rollArt(1, 4, 7500);
                break;
            case 96:case 97:case 98:case 99:case 100:
                rollGems(1, 8, 5000);
                break;
        }

    }

}
