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

    ArrayList<EnemiesListItem> enemiesList = new ArrayList<>();

    View v;

    private String addEncounterText;

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
        refreshList(null);

        // Set +/- listeners
        ImageButton encounterListPlusButton = (ImageButton) view.findViewById(R.id.encounter_list_plus_button);
        ImageButton encounterListMinusButton = (ImageButton) view.findViewById(R.id.encounter_list_minus_button);
        encounterListPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEncounter();
            }
        });
        encounterListMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEncounter();
            }
        });

        // Listener on add new enemy
        ImageButton encounterEnemiesListNewButton = (ImageButton) view.findViewById(R.id.encounter_enemies_list_new_button);
        encounterEnemiesListNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewEnemyDialog();
            }
        });
    }


    void refreshList(String valueToSet) {
        dbAccess.open();
        encounterList = dbAccess.getEncounters();
        dbAccess.close();

        // Populate encounter spinner with saved encounters
        encounterListAdapter = new EncounterListAdapter(getActivity(), R.layout.encounter_list_item, encounterList);
        encounterListAdapter.setDropDownViewResource(R.layout.encounter_list_dropdown_item);
        encounterSpinner.setAdapter(encounterListAdapter);

        if (valueToSet != null) {
            Integer pos;

            ArrayList<String> names = new ArrayList<>();
            for (int i = 0; i < encounterList.size(); i++) {
                names.add(encounterList.get(i).getName());
            }

            pos = names.indexOf(valueToSet);
            encounterSpinner.setSelection(pos);
        }
    }

    public void addEncounter() {
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
                addEncounterText = input.getText().toString();

                if (!addEncounterText.isEmpty()) {

                    // Insert in the db
                    dbAccess.open();
                    dbAccess.addEncounter(addEncounterText);
                    dbAccess.close();

                    refreshList(addEncounterText);
                }
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

        refreshList(null);
    }

    void changeEnemyQuantity(Integer pos, Integer quantityChange) {
        EncounterEnemiesListItem e = encounterEnemiesList.get(pos);
        Integer enemyId = e.getEnemyId();
        Integer quantity = e.getQuantity();
        Integer encounterId = encounterList.get(encounterSpinner.getSelectedItemPosition()).getId();

        quantity += quantityChange;

        dbAccess.open();
        dbAccess.updateEnemyQuantity(encounterId, enemyId, quantity);
        dbAccess.close();

        loadEncounterEnemies(encounterId);
    }

    void loadEncounterEnemies(Integer encounter_id) {
        // Get enemies from db
        dbAccess.open();
        encounterEnemiesList = dbAccess.getEncounterEnemies(encounter_id);
        dbAccess.close();

        // Refresh the list
        encounterEnemiesListView = (ListView) v.findViewById(R.id.encounter_enemies_list);
        encounterEnemiesListAdapter = new EncounterEnemiesListAdapter(getActivity(), R.layout.encounter_enemies_list_item, encounterEnemiesList, EncounterFragment.this);
        encounterEnemiesListView.setAdapter(encounterEnemiesListAdapter);

        // TODO: Calculate CR & XP
    }

    void addNewEnemyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add an Enemy");

        // Set the alert to use the encounter_enemy_add layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.encounter_enemy_add, null);
        builder.setView(v);

        // Set up the button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.show();

        // Get enemies not already in the encounter

        ArrayList<Integer> currentIds = new ArrayList<>();
        for (int i = 0; i < encounterEnemiesList.size(); i++) {
            currentIds.add(encounterEnemiesList.get(i).getEnemyId());
        }

        dbAccess.open();
        enemiesList = dbAccess.getAllEnemies(currentIds);
        dbAccess.close();


        // Hook up list
        ListView addEnemyAlertListView = (ListView) v.findViewById(R.id.add_enemy_alert_list);
        EnemiesListAdapter addEnemyListViewAdapter = new EnemiesListAdapter(getActivity(), R.layout.enemies_list_item, enemiesList, EncounterFragment.this, alert);
        addEnemyAlertListView.setAdapter(addEnemyListViewAdapter);

    }

    void addNewEnemy(Integer pos) {
        EnemiesListItem e = enemiesList.get(pos);
        Integer enemyId = e.getEnemyId();
        Integer encounterId = encounterList.get(encounterSpinner.getSelectedItemPosition()).getId();

        dbAccess.open();
        dbAccess.addNewEnemy(encounterId, enemyId);
        dbAccess.close();

        loadEncounterEnemies(encounterId);

    }
}
