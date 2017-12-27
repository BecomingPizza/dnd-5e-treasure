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
 * List adapter for parties
 */

class PartyListAdapter extends ArrayAdapter<Party> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Party> parties = null;

    PartyListAdapter(Context context, int layoutResourceId, ArrayList<Party> parties) {
        super(context, layoutResourceId, parties);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.parties= parties;
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

        Party p = parties.get(position);

        if (p != null) {
            TextView tv1 = (TextView) v.findViewById(R.id.party_list_text);

            if (tv1 != null) {
                tv1.setText(p.getName());
            }

            TextView tv2 = (TextView) v.findViewById(R.id.party_dropdown_list_text);

            if (tv2 != null) {
                tv2.setText(p.getName());
            }
        }
        return v;
    }

}