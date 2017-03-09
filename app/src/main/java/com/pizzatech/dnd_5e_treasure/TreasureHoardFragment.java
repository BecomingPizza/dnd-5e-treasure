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
 * Fragment for Treasure Hoard rolling
 */

public class TreasureHoardFragment extends Fragment {

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

        // Populate the CR spinner
        Spinner cr_spinner = (Spinner) v.findViewById(R.id.cr_selection_spinner);
        ArrayAdapter<CharSequence> cradapter = ArrayAdapter.createFromResource(getActivity(), R.array.cr_selection_array, android.R.layout.simple_spinner_item);
        cradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cr_spinner.setAdapter(cradapter);

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


        Bundle bundaru = getArguments();
        if (bundaru != null && bundaru.containsKey("CR")) {
        Double cr = bundaru.getDouble("CR");

            if (cr >=0 && cr <= 4) {
                cr_spinner.setSelection(0);
            } else if (cr >=5 && cr <=10) {
                cr_spinner.setSelection(1);
            } else if (cr >=11 && cr <=16) {
                cr_spinner.setSelection(2);
            } else if (cr >= 17) {
                cr_spinner.setSelection(3);
            }
            rollTreasure();
        }
    }

    void rollTreasure() {

        //Grab CR
        Spinner cr_spinner = (Spinner) v.findViewById(R.id.cr_selection_spinner);
        Integer cr_selected = cr_spinner.getSelectedItemPosition();


        TreasureRoller tr = new TreasureRoller(v.getContext(), cr_selected, getActivity());
        tr.rollStuff();
        treasureItemsListAdapter.notifyDataSetChanged();
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
