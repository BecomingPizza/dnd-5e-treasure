package com.pizzatech.dnd_5e_treasure;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<TreasureListItem> treasureItems = new ArrayList<>();

    static TreasureListItemAdapter treasureItemsListAdapter;

    Resources res;

    ListView listyMcListFace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();


        //Populate teh spinnor
        Spinner cr_spinner = (Spinner) findViewById(R.id.cr_selection_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cr_selection_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cr_spinner.setAdapter(adapter);

        //Hook up the list
        treasureItemsListAdapter = new TreasureListItemAdapter(this, R.layout.treasure_list_item, treasureItems);
        listyMcListFace = (ListView) findViewById(R.id.results_list);
        listyMcListFace.setAdapter(treasureItemsListAdapter);

        //Ads
        MobileAds.initialize(this, res.getString(R.string.banner_ad_unit_id));
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


    }

   public void rollTreasure(View v) {

        //Grab CR
        Spinner cr_spinner = (Spinner) findViewById(R.id.cr_selection_spinner);
        Integer cr_selected = cr_spinner.getSelectedItemPosition();


        TreasureRoller tr = new TreasureRoller(this, cr_selected, this);
        tr.execute();
    }

    public void copyToClipboard(View v) {
        //Check we actually have something to copy
        if (treasureItems.size() != 0) {
            //Turn treasureItems into a lovely string
            String stringyMcStringFace = "";
            for (int i = 0; i < treasureItems.size(); i++) {
                stringyMcStringFace += (treasureItems.get(i).getMainText() + "\n");
                //Don't add null subtext
                if (treasureItems.get(i).getSubText() != null) {
                    stringyMcStringFace += (treasureItems.get(i).getSubText() + '\n');
                }
            }
            //clipboardy stuff
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("loot", stringyMcStringFace);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "List copied", Toast.LENGTH_SHORT).show();
        } else {
            // Y U DO DIS
            Toast.makeText(this, "Y U DO DIS", Toast.LENGTH_SHORT).show();
        }
    }

}
