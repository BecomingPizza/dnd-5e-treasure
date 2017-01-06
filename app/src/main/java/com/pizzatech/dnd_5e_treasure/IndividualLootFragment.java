package com.pizzatech.dnd_5e_treasure;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashley on 06/01/2017.
 *
 * Roll Individual Loot
 *
 */

public class IndividualLootFragment extends Fragment {
    View v;

    Integer cr0To4Quantity = 0;
    Integer cr5To10Quantity = 0;
    Integer cr11To16Quantity = 0;
    Integer cr17PlusQuantity = 0;

    ArrayList<IndvLootResult> indvLootResults = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.individual_loot_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do setup stuff

        v = view;

        // Set enemies listener
        Button setEnemiesBtn = (Button) v.findViewById(R.id.indv_loot_set_button);
        setEnemiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnemiesDialog();
            }
        });

        // Generate loot listener
        Button generateBtn = (Button) v.findViewById(R.id.indv_loot_roll_button);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollIndividualLoot();
            }
        });

        // Copy to clipboard listener
        Button copyBtn = (Button) v.findViewById(R.id.indv_loot_copy_button);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard();
            }
        });

    }

    void setEnemiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add an Enemy");

        // Set the alert to use the encounter_enemy_add layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.individual_loot_set_enemies, null);
        builder.setView(v);

        final EditText cr0To4QuantityEditText = (EditText) v.findViewById(R.id.cr_0_4_quantity_box);
        final EditText cr5To10QuantityEditText = (EditText) v.findViewById(R.id.cr_5_10_quantity_box);
        final EditText cr11To16QuantityEditText = (EditText) v.findViewById(R.id.cr_11_16_quantity_box);
        final EditText cr17PlusQuantityEditText = (EditText) v.findViewById(R.id.cr_17_quantity_box);

        // Load current quantities
        cr0To4QuantityEditText.setText(cr0To4Quantity.toString());
        cr5To10QuantityEditText.setText(cr5To10Quantity.toString());
        cr11To16QuantityEditText.setText(cr11To16Quantity.toString());
        cr17PlusQuantityEditText.setText(cr17PlusQuantity.toString());


        // Set up the positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save Quantities to variables
                cr0To4Quantity = Integer.parseInt(cr0To4QuantityEditText.getText().toString());
                cr5To10Quantity = Integer.parseInt(cr5To10QuantityEditText.getText().toString());
                cr11To16Quantity = Integer.parseInt(cr11To16QuantityEditText.getText().toString());
                cr17PlusQuantity = Integer.parseInt(cr17PlusQuantityEditText.getText().toString());

                // Set the text at the top of the main screen that states this
                setDesc();

                // Set up an array of generic enemies to roll some loot for
                setIndvLootArray();
            }
        });

        // Set up the negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.show();
    }

    void setDesc() {
        TextView indvLootDesc = (TextView) v.findViewById(R.id.indv_loot_desc_label);
        String text = "";

        if (cr0To4Quantity > 0) {
            text+= cr0To4Quantity + " CR 0-4 Enemies";
        }

        if (cr5To10Quantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= cr5To10Quantity + " CR 5-10 Enemies";
        }

        if (cr11To16Quantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= cr11To16Quantity + " CR 11-16 Enemies";
        }

        if (cr17PlusQuantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= cr17PlusQuantity + " CR 17+ Enemies";
        }

        if (text.equals("")) {
            text = "No enemies set";
        }

        indvLootDesc.setText(text);
    }

    void setIndvLootArray() {
        indvLootResults.clear();

        for (int i = 0; i < cr0To4Quantity; i++) {
            indvLootResults.add(new IndvLootResult("CR 0-4 Enemy", 0, 0, 0, 0, 0, 0));
        }

        for (int i = 0; i < cr5To10Quantity; i++) {
            indvLootResults.add(new IndvLootResult("CR 5-10 Enemy", 1, 0, 0, 0, 0, 0));
        }

        for (int i = 0; i < cr11To16Quantity; i++) {
            indvLootResults.add(new IndvLootResult("CR 11-16 Enemy", 2, 0, 0, 0, 0, 0));
        }

        for (int i = 0; i < cr17PlusQuantity; i++) {
            indvLootResults.add(new IndvLootResult("CR 17+ Enemy", 3, 0, 0, 0, 0, 0));
        }
    }

    void rollIndividualLoot() {
        // TODO: Roll some loot, adapter some lists etc
    }

    void copyToClipboard() {

    }
}
