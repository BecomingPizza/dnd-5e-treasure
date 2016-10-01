package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by Ashley on 01/10/2016.
 *
 * Encounter fragment
 */

public class EncounterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.encounter_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do setup stuff

        // Populate encounter spinner with saved encounters
        Spinner encounter_spinner = (Spinner) view.findViewById(R.id.encounter_list_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.encounter_list_test_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        encounter_spinner.setAdapter(adapter);

        // Set +/- listeners
        ImageButton encounterListPlusButton = (ImageButton) view.findViewById(R.id.encounter_list_plus_button);
        ImageButton encounterListMinusButton = (ImageButton) view.findViewById(R.id.encounter_list_minus_button);
        encounterListPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEncounter(v);
            }
        });
        encounterListMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEncounter(v);
            }
        });
    }

    public void addEncounter(View v) {
        Log.e("!!!!!!", "addEncounter");
    }

    public void removeEncounter(View v) {
        Log.e("!!!!!!", "removeEncounter");

    }
}
