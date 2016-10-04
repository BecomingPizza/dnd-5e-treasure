package com.pizzatech.dnd_5e_treasure;

/**
 * Created by ashley on 04/10/2016.
 *
 * Holds list entries for enemies in an encounter
 */

class EncounterEnemiesListItem {
    private Integer enemyId;
    private String name;
    private String subText;
    private Integer quantiity;

    EncounterEnemiesListItem (Integer enemyId, String name, String subText, Integer quantity) {
        super();
        this.enemyId = enemyId;
        this.name = name;
        this.subText = subText;
        this.quantiity = quantity;
    }

    String getName() {
        return name;
    }

    String getSubText() {
        return subText;
    }

    Integer getQuantity() {
        return quantiity;
    }
}
