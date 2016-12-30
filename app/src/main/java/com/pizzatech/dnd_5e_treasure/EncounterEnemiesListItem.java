package com.pizzatech.dnd_5e_treasure;

/**
 * Created by ashley on 04/10/2016.
 *
 * Holds list entries for enemies in an encounter
 */

class EncounterEnemiesListItem {
    private Integer enemyId;
    private String name;
    private String cr;
    private String ref;
    private Integer quantity;

    EncounterEnemiesListItem (Integer enemyId, String name, String cr, String ref, Integer quantity) {
        super();
        this.enemyId = enemyId;
        this.name = name;
        this.cr = cr;
        this.ref = ref;
        this.quantity = quantity;
    }

    String getName() {
        return name;
    }

    String getSubText() {
        return "CR " + cr + " " + ref;
    }

    Integer getQuantity() {
        return quantity;
    }

    Integer getEnemyId() {
        return enemyId;
    }

    String getCr() { return cr; }
}
