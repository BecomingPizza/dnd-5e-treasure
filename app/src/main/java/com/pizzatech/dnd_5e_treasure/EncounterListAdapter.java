package com.pizzatech.dnd_5e_treasure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashley on 02/10/2016.
 *
 * List adapter for encounters
 */

class EncounterListAdapter extends ArrayAdapter<Encounter> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Encounter> encounters = null;

    EncounterListAdapter(Context context, int layoutResourceId, ArrayList<Encounter> encounters) {
        super(context, layoutResourceId, encounters);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.encounters = encounters;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(layoutResourceId, parent, false);
        }

        Encounter e = encounters.get(position);

        if (e != null) {
            TextView tv1 = (TextView) v.findViewById(R.id.encounter_list_text);

            if (tv1 != null) {
                tv1.setText(e.getName());
            }

            TextView tv2 = (TextView) v.findViewById(R.id.encounter_dropdown_list_text);

            if (tv2 != null) {
                tv2.setText(e.getName());
            }
        }
        return v;
    }

}