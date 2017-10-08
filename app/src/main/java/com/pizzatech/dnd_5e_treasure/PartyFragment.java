package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ashley on 08/10/2017.
 *
 * Track the party members and their XP
 *
 */

public class PartyFragment extends Fragment {
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.party_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do some setup stuff!

        v = view;
    }
}
