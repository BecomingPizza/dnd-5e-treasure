package com.pizzatech.dnd_5e_treasure;

/**
 * Created by Ashley on 01/10/2016.
 *
 */

public class DrawerItem {
    private int img;
    private String str;

    DrawerItem(int img, String str) {
        super();
        this.img = img;
        this.str = str;
    }

    int getImg () {
        return img;
    }

    String getStr () {
        return str;
    }
}
