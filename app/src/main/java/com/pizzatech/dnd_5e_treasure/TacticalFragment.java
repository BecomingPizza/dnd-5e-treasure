package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Ashley on 27/09/2016.
 *
 * Fragment for loot rolling
 */

public class TacticalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.treasure_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do setup stuff

        // Populate teh spinnor
        Spinner cr_spinner = (Spinner) view.findViewById(R.id.cr_selection_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cr_selection_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cr_spinner.setAdapter(adapter);

        // Hook up the list
        MainActivity.treasureItemsListAdapter = new TreasureListItemAdapter(getActivity(), R.layout.treasure_list_item, MainActivity.treasureItems);
        ListView listyMcListFace = (ListView) view.findViewById(R.id.results_list);
        listyMcListFace.setAdapter(MainActivity.treasureItemsListAdapter);

        // Ads
        // TODO: This should probably sit outside of the fragment?
        MobileAds.initialize(getActivity(), getActivity().getResources().getString(R.string.banner_ad_unit_id));
        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
