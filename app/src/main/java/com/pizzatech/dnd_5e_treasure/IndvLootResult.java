package com.pizzatech.dnd_5e_treasure;

/**
 * Created by Ashley on 06/01/2017.
 *
 * Hold all the shiz for individual loot for 1 monster
 */

public class IndvLootResult {
    private String enemyName;
    private Integer table;
    private Integer copperQuantity;
    private Integer silverQuantity;
    private Integer electrumQuantity;
    private Integer goldQuantity;
    private Integer platinumQuantity;

    IndvLootResult(String enemyName, Integer table, Integer copperQuantity, Integer silverQuantity, Integer electrumQuantity, Integer goldQuantity, Integer platinumQuantity) {
        super();
        this.enemyName = enemyName;
        this.table = table;
        this.copperQuantity = copperQuantity;
        this.silverQuantity = silverQuantity;
        this.electrumQuantity = electrumQuantity;
        this.goldQuantity = goldQuantity;
        this.platinumQuantity = platinumQuantity;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public Integer getTable() {
        return table;
    }

    public void setCopperQuantity(Integer q) {
        this.copperQuantity = q;
    }

    public void setSilverQuantity(Integer q) {
        this.silverQuantity = q;
    }

    public void setElectrumQuantity(Integer q) {
        this.electrumQuantity = q;
    }

    public void setGoldQuantity(Integer q) {
        this.goldQuantity = q;
    }

    public void setPlatinumQuantity(Integer q) {
        this.platinumQuantity = q;
    }

    public String getCoinage() {
        String text = "";

        if (copperQuantity > 0) {
            text+= copperQuantity + " Copper";
        }

        if (silverQuantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= silverQuantity + " Silver";
        }

        if (electrumQuantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= electrumQuantity + " Electrum";
        }

        if (goldQuantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= goldQuantity + " Gold";
        }

        if (platinumQuantity > 0) {
            if (!text.equals("")) {
                text += " \n";
            }
            text+= platinumQuantity + " Platinum";
        }


        return text;
    }
}
