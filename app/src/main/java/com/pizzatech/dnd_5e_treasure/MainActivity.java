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

    String copperStr = getString(R.string.tr_coin_copper);
    String silverStr = getString(R.string.tr_coin_silver);
    String goldStr = getString(R.string.tr_coin_gold);
    String platinumStr = getString(R.string.tr_coin_platinum);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                break;
            case 2:
                //Roll for CR 11-16
                rollCoins(4, 1000, goldStr);
                rollCoins(5, 100, platinumStr);
                break;
            case 3:
                //Roll for CR 17+
                rollCoins(12, 1000, goldStr);
                rollCoins(8, 1000, platinumStr);
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
        addToList(listText, null, R.drawable.coin_gold);
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
                    // TODO : BUILD THE OTHER GEMROLLERS
                    break;
                case 500:
                    break;
                case 1000:
                    break;
                case 5000:
                    break;
            }
        }
    }

    private void rollGem10GP() {
        // Roll d12 to determine which gem
        Integer roll;
        roll = r.nextInt(12 - 1) + 1;

        String gemText = "Gem (10GP) - ";
        String subText = "";

        switch(roll) {
            case 1:
                gemText += "Azurite";
                subText = "Opaque mottled deep blue";
                break;
            case 2:
                gemText += "Banded agate";
                subText = "Translucent striped brown, blue, white, or red";
                break;
            case 3:
                gemText += "Blue quartz";
                subText = "Transparent pale blue";
                break;
            case 4:
                gemText += "Eye agate";
                subText = "Translucent circles of gray, white, brown, blue, or green";
                break;
            case 5:
                gemText += "Hematite";
                subText = "Opaque gray-black";
                break;
            case 6:
                gemText += "Lapis lazuli";
                subText = "Opaque light and dark blue with yellow flecks";
                break;
            case 7:
                gemText += "Malachite";
                subText = "Opaque striated light and dark green";
                break;
            case 8:
                gemText += "Moss agate";
                subText = "Translucent pink or yellow-white with mossy gray or green markings";
                break;
            case 9:
                gemText += "Obsidian";
                subText = "Opaque black";
                break;
            case 10:
                gemText += "Rhodochrosite";
                subText = "Opaque light pink";
                break;
            case 11:
                gemText += "Tiger eye";
                subText = "Translucent brown with golden center";
                break;
            case 12:
                gemText += "Turquoise";
                subText = "Opaque light blue-green";
                break;
        }

        addToList(gemText, subText, R.drawable.coin_gold);
    }

    private void rollGem50GP() {
        // Roll d12 to determine which gem
        Integer roll;
        roll = r.nextInt(12 - 1) + 1;

        String gemText = "Gem (50GP) - ";
        String subText = "";

        switch(roll) {
            case 1:
                gemText += "Bloodstone";
                subText = "Opaque dark gray with red flecks";
                break;
            case 2:
                gemText += "Carnelian";
                subText = "Opaque orange to red-brown";
                break;
            case 3:
                gemText += "Chalcedony";
                subText = "Opaque white";
                break;
            case 4:
                gemText += "Chrysoprase";
                subText = "Translucent green";
                break;
            case 5:
                gemText += "Citrine";
                subText = "Transparent pale yellow-brown";
                break;
            case 6:
                gemText += "Jasper";
                subText = "Opaque blue, black, or brown";
                break;
            case 7:
                gemText += "Moonstone";
                subText = "Translucent white with pale blue glow";
                break;
            case 8:
                gemText += "Onyx";
                subText = "Opaque bands of black and white, or pure black or white";
                break;
            case 9:
                gemText += "Quartz";
                subText = "Transparent white, smoky gray, or yellow";
                break;
            case 10:
                gemText += "Sardonyx";
                subText = "Opaque bands of red and white";
                break;
            case 11:
                gemText += "Star rose quartz";
                subText = "Translucent rosy stone with white star-shaped center";
                break;
            case 12:
                gemText += "Zircon";
                subText = "Transparent pale blue-green";
                break;
        }

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
                    // TODO : BUILD THE OTHER ARTROLLERS
                    break;
                case 750:
                    break;
                case 2500:
                    break;
                case 7500:
                    break;
            }
        }
    }

    private void rollArt25GP() {
        // Roll d10 to determine which art
        Integer roll;
        roll = r.nextInt(10 - 1) + 1;

        String artText = "Art (25GP)";
        String subText = "";

        switch(roll) {
            case 1:
                subText += "Silver ewer";
                break;
            case 2:
                subText += "Carved bone statuette";
                break;
            case 3:
                subText += "Small gold bracelet";
                break;
            case 4:
                subText += "Cloth-of-gold vestments";
                break;
            case 5:
                subText += "Black velvet mask stitched with silver thread";
                break;
            case 6:
                subText += "Copper chalice with silver filigree";
                break;
            case 7:
                subText += "Pair of engraved bone dice";
                break;
            case 8:
                subText += "Small mirror set in a painted wooden frame";
                break;
            case 9:
                subText += "Embroidered silk handkerchief";
                break;
            case 10:
                subText += "Gold locket with a painted portrait inside";
                break;
        }

        addToList(artText, subText, R.drawable.coin_gold);
    }

    private void rollTreasureTableA() {
        // Roll d100
        Integer roll;
        roll = r.nextInt(100 - 1) + 1;

        switch(roll) {
            case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
                rollGems(2, 6, 10);
                break;
            // TODO: POPULATE OTHER CASES
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

}
