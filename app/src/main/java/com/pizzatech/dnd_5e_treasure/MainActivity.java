package com.pizzatech.dnd_5e_treasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> treasureItems = new ArrayList<String>();

    ArrayAdapter<String> treasureItemsListAdapter;

    Random r = new Random();

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
        treasureItemsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, treasureItems);
        ListView listyMcListFace = (ListView) findViewById(R.id.results_list);
        listyMcListFace.setAdapter(treasureItemsListAdapter);
    }

    public void rollTreasure(View v) {
        //Clear list
        treasureItems.clear();
        treasureItemsListAdapter.notifyDataSetChanged();

        //Grab CR
        Spinner cr_spinner = (Spinner) findViewById(R.id.cr_selection_spinner);
        Integer cr_selected = cr_spinner.getSelectedItemPosition();

        switch (cr_selected) {
            case 0:
                //Roll for CR 0-4
                rollCoins(6, 100, "Copper");
                rollCoins(3, 100, "Silver");
                rollCoins(2, 10, "Gold");
                rollTreasureTableA();
                break;
            case 1:
                //Roll for CR 5-10
                rollCoins(2, 100, "Copper");
                rollCoins(2, 1000, "Silver");
                rollCoins(6, 100, "Gold");
                rollCoins(3, 10, "Platinum");
                break;
            case 2:
                //Roll for CR 11-16
                rollCoins(4, 1000, "Gold");
                rollCoins(5, 100, "Platinum");
                break;
            case 3:
                //Roll for CR 17+
                rollCoins(12, 1000, "Gold");
                rollCoins(8, 1000, "Platinum");
                break;
        }
    }


    private void addToList (String text) {
        // Put some shit in the list
        treasureItems.add(text);
        treasureItemsListAdapter.notifyDataSetChanged();
    }

    private void rollCoins(Integer dice, Integer multiplier, String coinType) {
        // Roll a d6 the specified number of times and sum the total (coins are always d6 rolls!)
        Integer roll = 0;
        for (int i = 0; i < dice; i++) {
            roll += r.nextInt(6 - 1) + 1;
        }
        // Sum x multiplier
        Integer coins = roll * multiplier;
        // Build string and add to the list
        String listText = coins.toString() + " " + coinType;
        addToList(listText);
    }

    private void rollGems(Integer dice, Integer sides, Integer value) {
        Integer roll = 0;
        for (int i = 0; i < dice; i++)
        {
            roll += r.nextInt(sides - 1) + 1;
        }
        for (int j = 0; j < roll; j++)
        {
            switch(value) {
                case 10:
                    rollGem10GP();
                    break;
                case 50:
                    // TODO : BUILD THE OTHER GEMROLLERS
                    break;
                case 100:
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
        Integer roll = 0;
        roll = r.nextInt(12 - 1) + 1;

        String gemText = "Gem (10GP) - ";

        switch(roll) {
            case 1:
                gemText += "Azurite (opaque mottled deep blue)";
                break;
            case 2:
                gemText += "Banded agate (translucent striped brown, blue, white, or red)";
                break;
            case 3:
                gemText += "Blue quartz (transparent pale blue)";
                break;
            case 4:
                gemText += "Eye agate (translucent circles of gray, white, brown, blue, or green)";
                break;
            case 5:
                gemText += "Hematite (opaque gray-black)";
                break;
            case 6:
                gemText += "Lapis lazuli (opaque light and dark blue with yellow flecks)";
                break;
            case 7:
                gemText += "Malachite (opaque striated light and dark green)";
                break;
            case 8:
                gemText += "Moss agate (translucent pink or yellow-white with mossy gray or green markings)";
                break;
            case 9:
                gemText += "Obsidian (opaque black)";
                break;
            case 10:
                gemText += "Rhodochrosite (opaque light pink)";
                break;
            case 11:
                gemText += "Tiger eye (translucent brown with golden center)";
                break;
            case 12:
                gemText += "Turquoise (opaque light blue-green)";
                break;
        }

        addToList(gemText);
    }

    private void rollTreasureTableA() {
        // Roll d100
        Integer roll = 0;
        roll = r.nextInt(100 - 1) + 1;

        switch(roll) {
            case 1:case 2:case 3:case 4:case 5:case 6:
                break;
            case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
                rollGems(2, 6, 10);
                break;
            // TODO: POPULATE OTHER CASES
            case 17:case 18:case 19:case 20:case 21:case 22:case 23:case 24:case 25:case 26:
                break;
            case 27:case 28:case 29:case 30:case 31:case 32:case 33:case 34:case 35:case 36:
                break;
            case 37:case 38:case 39:case 40:case 41:case 42:case 43:case 44:
                rollGems(2, 6, 10);
                break;
            case 45:case 46:case 47:case 48:case 49:case 50:case 51:case 52:
                break;
            case 53:case 54:case 55:case 56:case 57:case 58:case 59:case 60:
                break;
            case 61:case 62:case 63:case 64:case 65:
                break;
            case 66:case 67:case 68:case 69:case 70:
                break;
            case 71:case 72:case 73:case 74:case 75:
                break;
            case 76:case 77:case 78:
                break;
            case 79:case 80:
                break;
            case 81:case 82:case 83:case 84:case 85:
                break;
            case 86:case 87:case 88:case 89:case 90:case 91:case 92:
                break;
            case 93:case 94:case 95:case 96:case 97:
                break;
            case 98:case 99:
                break;
            case 100:
                break;
        }

    }

}
