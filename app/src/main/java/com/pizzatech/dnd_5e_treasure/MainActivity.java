package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    Fragment fragment;

    Resources res;

    static DBAccess dbAccess;

    private ArrayList<DrawerItem> leftDrawerItems = new ArrayList<>();
    private DrawerItemAdapter leftDrawerItemAdapter;
    private ListView leftDrawerList;
    private DrawerLayout leftDrawerLayout;
    private ActionBarDrawerToggle leftDrawerToggle;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        res = getResources();

        // Initialize drawer

        leftDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        String[] leftDrawerItemsStrings = res.getStringArray(R.array.left_drawer_items_strings);
        TypedArray leftDrawerItemsImgs = res.obtainTypedArray(R.array.left_drawer_items_imgs);

        for (int i = 0; i < leftDrawerItemsStrings.length; i++) {
            leftDrawerItems.add(new DrawerItem(leftDrawerItemsImgs.getResourceId(i, -1), leftDrawerItemsStrings[i]));
        }

        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        leftDrawerItemAdapter = new DrawerItemAdapter(this, R.layout.drawer_list_item, leftDrawerItems);
        leftDrawerList.setAdapter(leftDrawerItemAdapter);
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Action bar drawer toggle
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        leftDrawerToggle = new ActionBarDrawerToggle(this, leftDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        leftDrawerLayout.addDrawerListener(leftDrawerToggle);

        // Default to Loot fragment
        selectItem(0);

        // Ads
        MobileAds.initialize(this, res.getString(R.string.banner_ad_unit_id));
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Databases r kewl
        dbAccess = DBAccess.getInstance(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    /**
     * Drawer stuff!
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (leftDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        leftDrawerToggle.syncState();
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
        FragmentTransaction fragTran = fragMan.beginTransaction();

        switch (position) {
            case 0: //Treasure Hoard
                fragment = new TreasureHoardFragment();
                break;
            case 1: //Individual Loot
                fragment = new IndividualLootFragment();
                break;
            case 2: //Encounter
                fragment = new EncounterFragment();
                break;
            case 3: //About
                fragment = new AboutFragment();
                break;
        }

        fragTran.replace(R.id.content_frame, fragment);
        fragTran.commit();

        leftDrawerList.setItemChecked(position, true);
        setTitle(leftDrawerItems.get(position).getStr());
        leftDrawerLayout.closeDrawer(leftDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    public void rateInGP(View v) {
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
