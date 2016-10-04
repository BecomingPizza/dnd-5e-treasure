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

class EncounterEnemiesListAdapter extends ArrayAdapter<EncounterEnemiesListItem> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<EncounterEnemiesListItem> enemies = null;

    EncounterEnemiesListAdapter(Context context, int layoutResourceId, ArrayList<EncounterEnemiesListItem> enemies) {
        super(context, layoutResourceId, enemies);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.enemies = enemies;
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

        EncounterEnemiesListItem e = enemies.get(position);

        if (e != null) {
            TextView tv1 = (TextView) v.findViewById(R.id.encounter_enemies_list_main_text);

            if (tv1 != null) {
                tv1.setText(e.getName());
            }

            TextView tv2 = (TextView) v.findViewById(R.id.encounter_enemies_list_sub_text);

            if (tv2 != null) {
                tv2.setText(e.getSubText());
            }

            TextView tv3 = (TextView) v.findViewById(R.id.encounter_enemies_list_quantity);

            if (tv3 != null) {
                tv3.setText(e.getQuantity().toString());
            }

            // Listeners on +/-
            ImageButton ibplus = (ImageButton) v.findViewById(R.id.encounter_enemies_list_plus_button);
            ibplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EncounterFragment.enemyQuantityIncrease(position);
                }
            });

            ImageButton ibminus = (ImageButton) v.findViewById(R.id.encounter_enemies_list_minus_button);
            ibminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EncounterFragment.enemyQuantityDecrease(position);
                }
            });
        }
        return v;
    }
}
