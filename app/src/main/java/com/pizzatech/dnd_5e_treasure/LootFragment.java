package com.pizzatech.dnd_5e_treasure;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ashley on 27/09/2016.
 *
 * Fragment for loot rolling
 */

public class LootFragment extends Fragment {

    public static ArrayList<TreasureListItem> treasureItems = new ArrayList<>();
    static TreasureListItemAdapter treasureItemsListAdapter;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.treasure_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do setup stuff

        v = view;

        // Populate teh spinnor
        Spinner cr_spinner = (Spinner) v.findViewById(R.id.cr_selection_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cr_selection_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cr_spinner.setAdapter(adapter);

        // Hook up the list
        treasureItemsListAdapter = new TreasureListItemAdapter(getActivity(), R.layout.treasure_list_item, treasureItems);
        ListView listyMcListFace = (ListView) view.findViewById(R.id.results_list);
        listyMcListFace.setAdapter(treasureItemsListAdapter);

        // Listener to roll treasure
        Button rollBtn = (Button) v.findViewById(R.id.roll_button);
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollTreasure();
            }
        });

        // Listener for copy to clipboard
        Button copyBtn = (Button) v.findViewById(R.id.copy_button);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard();
            }
        });

    }

    void rollTreasure() {

        //Grab CR
        Spinner cr_spinner = (Spinner) v.findViewById(R.id.cr_selection_spinner);
        Integer cr_selected = cr_spinner.getSelectedItemPosition();


        TreasureRoller tr = new TreasureRoller(v.getContext(), cr_selected, getActivity());
        tr.execute();
    }



    void copyToClipboard() {
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
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(MainActivity.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("loot", stringyMcStringFace);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(v.getContext(), "List copied", Toast.LENGTH_SHORT).show();
        } else {
            // Y U DO DIS
            Toast.makeText(v.getContext(), "Y U DO DIS", Toast.LENGTH_SHORT).show();
        }
    }
}
