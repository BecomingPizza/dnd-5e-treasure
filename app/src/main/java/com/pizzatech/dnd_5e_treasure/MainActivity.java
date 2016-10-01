package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    Fragment fragment;

    Resources res;

    static DBAccess dbAccess;

    private String[] leftDrawerItems;
    private ListView leftDrawerList;
    private DrawerLayout leftDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        res = getResources();

        // Initialize drawer
        leftDrawerItems = res.getStringArray(R.array.left_drawer_items);
        leftDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, leftDrawerItems));
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        // Default to Loot fragment for now
        selectItem(0);

        // Ads
        MobileAds.initialize(this, res.getString(R.string.banner_ad_unit_id));
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Databases r kewl
        dbAccess = DBAccess.getInstance(this);

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    // Swap fragments.... maybe?
    private void selectItem(int position) {
        // Create a new fragment
        FragmentManager fragMan = getFragmentManager();
        android.app.FragmentTransaction fragTran = fragMan.beginTransaction();

        switch (position) {
            case 0: //Loot
                fragment = new LootFragment();
                break;
            case 1: //About
                fragment = new AboutFragment();
        }

        fragTran.replace(R.id.content_frame, fragment);
        fragTran.commit();

        leftDrawerList.setItemChecked(position, true);
        setTitle(leftDrawerItems[position]);
        leftDrawerLayout.closeDrawer(leftDrawerList);
    }

    @Override
    public void setTitle(CharSequence title){
        getSupportActionBar().setTitle(title);
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



    public void rateInGP (View v) {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // Need these to get back to the app on a back button push supposedly
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

}
