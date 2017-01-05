package com.pizzatech.dnd_5e_treasure;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        loadEncounterEnemies(null);
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

        setEncounterSummary(encounter_id);

    }

    void setEncounterSummary(Integer encounter_id) {

        // Grab Total CR & XP
        int totalXP = 0;
        int XP = 0;
        int quantity = 0;
        String highestCR = "0";
        String CR = "0";

        for (int i = 0; i <encounterEnemiesList.size(); i++) {
            CR = encounterEnemiesList.get(i).getCr();
            quantity = encounterEnemiesList.get(i).getQuantity();
            // Convert to double for comparison
            if (tacticalParse(CR) > tacticalParse(highestCR)) {
                highestCR = CR;
            }

            // Lookup XP based on CR & add to total
            switch(CR) {
                case "0": XP = 0; break;
                case "1/8": XP = 25; break;
                case "1/4": XP = 50; break;
                case "1/2": XP = 100; break;
                case "1": XP = 200; break;
                case "2": XP = 450; break;
                case "3": XP = 700; break;
                case "4": XP = 1100; break;
                case "5": XP = 1800; break;
                case "6": XP = 2300; break;
                case "7": XP = 2900; break;
                case "8": XP = 3900; break;
                case "9": XP = 5000; break;
                case "10": XP = 5900; break;
                case "11": XP = 7200; break;
                case "12": XP = 8400; break;
                case "13": XP = 10000; break;
                case "14": XP = 11500; break;
                case "15": XP = 13000; break;
                case "16": XP = 15000; break;
                case "17": XP = 18000; break;
                case "18": XP = 20000; break;
                case "19": XP = 22000; break;
                case "20": XP = 25000; break;
                case "21": XP = 33000; break;
                case "22": XP = 41000; break;
                case "23": XP = 50000; break;
                case "24": XP = 62000; break;
                case "25": XP = 75000; break;
                case "26": XP = 90000; break;
                case "27": XP = 105000; break;
                case "28": XP = 120000; break;
                case "29": XP = 135000; break;
                case "30": XP = 155000; break;
            }
            totalXP += (XP * quantity);
        }

        String encounterSummaryTextLine1 = "Max CR: " + highestCR + "  Total XP: " + totalXP;
        TextView encounterSummaryTextView1 = (TextView) v.findViewById(R.id.encounter_summary_text_line_1);
        encounterSummaryTextView1.setText(encounterSummaryTextLine1);

        //TODO: Adjust XP & Difficulty w/ PC module
    }

    double tacticalParse(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }

    void addNewEnemyDialog() {

        //Check there is an encounter to add stuff to otherwise it will crash
        if (encounterSpinner.getSelectedItem() == null) {

            Toast.makeText(v.getContext(), "Add an encounter first", Toast.LENGTH_SHORT).show();

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add an Enemy");

            // Set the alert to use the encounter_enemy_add layout
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View v = inflater.inflate(R.layout.encounter_enemy_add, null);
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

            final ArrayList<Integer> currentIds = new ArrayList<>();
            for (int i = 0; i < encounterEnemiesList.size(); i++) {
                currentIds.add(encounterEnemiesList.get(i).getEnemyId());
            }

            dbAccess.open();
            enemiesList = dbAccess.getEnemies(currentIds, null, null);
            dbAccess.close();

            // Hook up list
            ListView addEnemyAlertListView = (ListView) v.findViewById(R.id.add_enemy_alert_list);
            EnemiesListAdapter addEnemyListViewAdapter = new EnemiesListAdapter(getActivity(), R.layout.enemies_list_item, enemiesList, EncounterFragment.this, alert);
            addEnemyAlertListView.setAdapter(addEnemyListViewAdapter);

            // Hook up CR spinner
            final Spinner enemy_cr_spinner = (Spinner) v.findViewById(R.id.enemy_cr_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.enemy_cr_spinner_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            enemy_cr_spinner.setAdapter(adapter);


            final EditText enemyNameFilterEditText = (EditText) v.findViewById(R.id.enemy_name_filter) ;

            // Add listener to CR Spinner to trigger filtering
            enemy_cr_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String nameFilter = enemyNameFilterEditText.getText().toString();
                    String cr = enemy_cr_spinner.getSelectedItem().toString();

                    dbAccess.open();
                    enemiesList = dbAccess.getEnemies(currentIds, cr, nameFilter);
                    dbAccess.close();

                    ListView addEnemyAlertListView = (ListView) v.findViewById(R.id.add_enemy_alert_list);
                    EnemiesListAdapter addEnemyListViewAdapter = new EnemiesListAdapter(getActivity(), R.layout.enemies_list_item, enemiesList, EncounterFragment.this, alert);
                    addEnemyAlertListView.setAdapter(addEnemyListViewAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Add listener to name filter to trigger filtering
            enemyNameFilterEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    String cr = enemy_cr_spinner.getSelectedItem().toString();
                    String nameFilter = enemyNameFilterEditText.getText().toString();

                    dbAccess.open();
                    enemiesList = dbAccess.getEnemies(currentIds, cr, nameFilter);
                    dbAccess.close();

                    ListView addEnemyAlertListView = (ListView) v.findViewById(R.id.add_enemy_alert_list);
                    EnemiesListAdapter addEnemyListViewAdapter = new EnemiesListAdapter(getActivity(), R.layout.enemies_list_item, enemiesList, EncounterFragment.this, alert);
                    addEnemyAlertListView.setAdapter(addEnemyListViewAdapter);
                }
            });

        }

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
