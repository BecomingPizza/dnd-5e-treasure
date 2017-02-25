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
 * Created by Ashley on 25/02/2017
 */
class IndvLootResultListAdapter extends ArrayAdapter<IndvLootResult> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<IndvLootResult> indvLoot = null;

    IndvLootResultListAdapter(Context context, int layoutResourceId, ArrayList<IndvLootResult> indvLoot) {
        super(context, layoutResourceId, indvLoot);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.indvLoot = indvLoot;
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

        IndvLootResult t = indvLoot.get(position);

        if (t != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.ili_mainText);
            TextView tt2 = (TextView) v.findViewById(R.id.ili_subText);

            if (tt1 != null) {
                tt1.setText(t.getEnemyName());
            }

            if (tt2 != null) {
                tt2.setText(t.getCoinage());
            }
        }

        return v;
    }
}
