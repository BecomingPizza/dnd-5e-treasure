package com.pizzatech.dnd_5e_treasure;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashley on 13/09/2016.
 *
 * YEAH CUSTOM ADAPTER BITCH
 */
public class TreasureListItemAdapter extends ArrayAdapter<TreasureListItem> {

    Context context;
    int layoutResourceId;
    public ArrayList<TreasureListItem> treasure = null;

    public TreasureListItemAdapter(Context context, int layoutResourceId, ArrayList<TreasureListItem> treasure) {
        super(context, layoutResourceId,treasure);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.treasure = treasure;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.treasure_list_item, null);
        }

        TreasureListItem t = getItem(position);

        if (t != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.tli_mainText);
            TextView tt2 = (TextView) v.findViewById(R.id.tli_subText);
            ImageView imgv = (ImageView) v.findViewById(R.id.tli_drawable);

            if (tt1 != null) {
                tt1.setText(t.getMainText());
            }

            if (tt2 != null) {
                tt2.setText(t.getSubText());
            }

            if (imgv != null) {
                imgv.setImageResource(t.getImg());
            }
        }

        return v;
    }
}
