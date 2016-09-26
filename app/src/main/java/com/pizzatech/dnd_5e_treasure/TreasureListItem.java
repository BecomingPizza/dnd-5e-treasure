package com.pizzatech.dnd_5e_treasure;

/**
 * Created by Ashley on 13/09/2016
 */
class TreasureListItem {

    private String mainText;
    private String subText;

    TreasureListItem(String mainText, String subText) {
        super();
        this.mainText = mainText;
        this.subText = subText;
    }

    String getMainText () {
        return mainText;
    }

    String getSubText () {
        return subText;
    }

}
