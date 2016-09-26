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
 * Created by Ashley on 13/09/2016.
 *
 * YEAH CUSTOM ADAPTER BITCH
 */
class TreasureListItemAdapter extends ArrayAdapter<TreasureListItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<TreasureListItem> treasure = null;

    TreasureListItemAdapter(Context context, int layoutResourceId, ArrayList<TreasureListItem> treasure) {
        super(context, layoutResourceId,treasure);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.treasure = treasure;
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

        TreasureListItem t = treasure.get(position);

        if (t != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.tli_mainText);
            TextView tt2 = (TextView) v.findViewById(R.id.tli_subText);

            if (tt1 != null) {
                tt1.setText(t.getMainText());
            }

            if (tt2 != null) {
                tt2.setText(t.getSubText());
            }
        }

        return v;
    }
}
