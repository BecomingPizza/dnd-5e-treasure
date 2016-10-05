package com.pizzatech.dnd_5e_treasure;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import static com.pizzatech.dnd_5e_treasure.MainActivity.dbAccess;

/**
 * Created by Ashley on 01/10/2016.
 * <p>
 * Encounter fragment
 */

public class EncounterFragment extends Fragment {

    ArrayList<Encounter> encounterList = new ArrayList<>();
    EncounterListAdapter encounterListAdapter;
    Spinner encounterSpinner;

    ArrayList<EncounterEnemiesListItem> encounterEnemiesList = new ArrayList<>();
    EncounterEnemiesListAdapter encounterEnemiesListAdapter;
    ListView encounterEnemiesListView;

    View v;

    private String add_encounter_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.encounter_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do setup stuff

        v = view;

        // Set selection listener to load the encounter
        encounterSpinner = (Spinner) v.findViewById(R.id.encounter_list_spinner);
        encounterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadEncounterEnemies(encounterList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Load saved encounters
        refreshList();

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
                removeEncounter();
            }
        });

        // Hook up enemies list
        encounterEnemiesListView = (ListView) v.findViewById(R.id.encounter_enemies_list);
        encounterEnemiesListAdapter = new EncounterEnemiesListAdapter(getActivity(), R.layout.encounter_enemies_list_item, encounterEnemiesList);
        encounterEnemiesListView.setAdapter(encounterEnemiesListAdapter);
    }


    void refreshList() {
        dbAccess.open();
        encounterList = dbAccess.getEncounters();
        dbAccess.close();

        // Populate encounter spinner with saved encounters
        encounterListAdapter = new EncounterListAdapter(getActivity(), R.layout.encounter_list_item, encounterList);
        encounterListAdapter.setDropDownViewResource(R.layout.encounter_list_dropdown_item);
        encounterSpinner.setAdapter(encounterListAdapter);
    }

    public void addEncounter(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Encounter Name:");

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_encounter_text = input.getText().toString();

                // Insert in the db
                dbAccess.open();
                dbAccess.addEncounter(add_encounter_text);
                dbAccess.close();

                refreshList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void removeEncounter() {
        Integer pos = encounterSpinner.getSelectedItemPosition();
        Encounter e = encounterList.get(pos);

        // Delete from db by ID
        dbAccess.open();
        dbAccess.deleteEncounter(e.getId());
        dbAccess.close();

        refreshList();
    }

    static void enemyQuantityIncrease(Integer pos) {
        // TODO: Write method
    }

    static void enemyQuantityDecrease(Integer pos) {
        // TODO: Write method
    }

    void loadEncounterEnemies(Integer encounter_id) {
        // Get enemies from db
        encounterEnemiesList = dbAccess.getEncounterEnemies(encounter_id);

        // Refresh the list
        encounterEnemiesListAdapter.notifyDataSetChanged();
    }
}