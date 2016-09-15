package com.pizzatech.dnd_5e_treasure;

/**
 * Created by Ashley on 13/09/2016
 */
public class TreasureListItem {

    public String mainText;
    public String subText;
    public int img;

    public TreasureListItem() {
        super();
    }

    public TreasureListItem(String mainText, String subText, int img) {
        super();
        this.mainText = mainText;
        this.subText = subText;
        this.img = img;
    }

    public String getMainText () {
        return mainText;
    }

    public String getSubText () {
        return subText;
    }

    public int getImg () { return img;}

}
