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

    ArrayList<String> treasureItems = new ArrayList<String>();

    ArrayAdapter<String> treasureItemsListAdapter;

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
                break;
            case 1:
                //Roll for CR 5-10
                break;
            case 2:
                //Roll for CR 11-16
                break;
            case 3:
                //Roll for CR 17+
                break;
        }
    }


    private void addToList (String text) {
        // Put some shit in the list
        treasureItems.add(text);
        treasureItemsListAdapter.notifyDataSetChanged();
    }

    private void rollCoins(Integer d6, Integer multiplier, String coinType) {
        Integer roll = 0;
        Random r = new Random();
        for (int i = 0; i < d6; i++) {
            roll += r.nextInt(6 - 1) + 1;
        }
        Integer coins = roll * multiplier;
        String listText = coins.toString() + " " + coinType;
        addToList(listText);
    }


}
