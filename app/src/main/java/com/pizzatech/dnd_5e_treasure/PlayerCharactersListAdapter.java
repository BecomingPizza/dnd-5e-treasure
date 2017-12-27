package com.pizzatech.dnd_5e_treasure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ashley on 04/10/2016.
 *
 * List adapter for encounter enemies
 */

class PlayerCharactersListAdapter extends ArrayAdapter<PlayerCharacter> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<PlayerCharacter> chars = null;
    private PartyFragment fragment;

    PlayerCharactersListAdapter(Context context, int layoutResourceId, ArrayList<PlayerCharacter> chars, PartyFragment fragment) {
        super(context, layoutResourceId, chars);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.chars = chars;
        this.fragment = fragment;
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(layoutResourceId, parent, false);
        }

        PlayerCharacter c = chars.get(position);

        if (c != null) {
            TextView tv1 = (TextView) v.findViewById(R.id.player_characters_list_main_text);

            if (tv1 != null) {
                tv1.setText(c.getMainText());
            }

            TextView tv2 = (TextView) v.findViewById(R.id.player_characters_list_sub_text);

            if (tv2 != null) {
                tv2.setText(c.getSubText());
            }

        }
        return v;
    }
}
