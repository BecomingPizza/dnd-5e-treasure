package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import static com.pizzatech.dnd_5e_treasure.MainActivity.dbAccess;

/**
 * Created by ashley on 08/10/2017.
 *
 * Track the party members and their XP
 *
 */

public class PartyFragment extends Fragment {

    ArrayList<Party> partyList = new ArrayList<>();
    PartyListAdapter partyListAdapter;
    Spinner partySpinner;

    ArrayList<PlayerCharacter> playerCharactersList = new ArrayList<>();
    PlayerCharactersListAdapter playerCharactersListAdapter;
    ListView playerCharactersListView;

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

        // Set selection listener to load the party
        partySpinner = (Spinner) v.findViewById(R.id.party_list_spinner);
        partySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadPartyCharacters(partyList.get(position).getPartyId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Load saved parties
        refreshPartyList(null);

    }

    void refreshPartyList(String valueToSet) {
        dbAccess.open();
        partyList = dbAccess.getParties();
        dbAccess.close();

        // Populate party spinner with saved parties
        partyListAdapter = new PartyListAdapter(getActivity(), R.layout.party_list_item, partyList);
        partyListAdapter.setDropDownViewResource(R.layout.party_list_dropdown_item);
        partySpinner.setAdapter(partyListAdapter);

        if (valueToSet != null) {
            Integer pos;

            ArrayList<String> names = new ArrayList<>();
            for (int i = 0; i < partyList.size(); i++) {
                names.add(partyList.get(i).getName());
            }

            pos = names.indexOf(valueToSet);
            partySpinner.setSelection(pos);
        }
    }

    void loadPartyCharacters(Integer party_id) {

        playerCharactersList = partyList.get(party_id).getPlayerCharacters();

        // Refresh the list
        playerCharactersListView = (ListView) v.findViewById(R.id.party_characters_list);
        playerCharactersListAdapter = new PlayerCharactersListAdapter(getActivity(), R.layout.player_characters_list_item, playerCharactersList, PartyFragment.this);
        playerCharactersListView.setAdapter(playerCharactersListAdapter);

        // Not sure we need this summary type box for parties?
        // setEncounterSummary(encounter_id);

    }
}