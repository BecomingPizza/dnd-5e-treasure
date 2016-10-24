package com.pizzatech.dnd_5e_treasure;

import android.app.Dialog;
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

class EnemiesListAdapter extends ArrayAdapter<EnemiesListItem> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<EnemiesListItem> enemies = null;
    private EncounterFragment fragment;
    private Dialog dialog;

    EnemiesListAdapter(Context context, int layoutResourceId, ArrayList<EnemiesListItem> enemies, EncounterFragment fragment, Dialog dialog) {
        super(context, layoutResourceId, enemies);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.enemies = enemies;
        this.fragment = fragment;
        this.dialog  = dialog;
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

        EnemiesListItem e = enemies.get(position);

        if (e != null) {
            TextView tv1 = (TextView) v.findViewById(R.id.enemies_list_main_text);

            if (tv1 != null) {
                tv1.setText(e.getName());
            }

            TextView tv2 = (TextView) v.findViewById(R.id.enemies_list_sub_text);

            if (tv2 != null) {
                tv2.setText(e.getSubText());
            }

            // Listener on +
            ImageButton ibplus = (ImageButton) v.findViewById(R.id.enemies_list_plus_button);
            ibplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.addNewEnemy(position);
                    dialog.dismiss();
                }
            });
        }
        return v;
    }
}
