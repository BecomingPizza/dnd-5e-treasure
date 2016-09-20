package com.pizzatech.dnd_5e_treasure;

import android.graphics.drawable.Drawable;

/**
 * Created by Ashley on 13/09/2016
 */
public class TreasureListItem {

    public String mainText;
    public String subText;

    public TreasureListItem() {
        super();
    }

    public TreasureListItem(String mainText, String subText) {
        super();
        this.mainText = mainText;
        this.subText = subText;
    }

    public String getMainText () {
        return mainText;
    }

    public String getSubText () {
        return subText;
    }

}
