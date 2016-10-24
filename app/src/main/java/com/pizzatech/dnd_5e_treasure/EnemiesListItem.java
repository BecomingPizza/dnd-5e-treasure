package com.pizzatech.dnd_5e_treasure;

/**
 * Created by ashley on 04/10/2016.
 *
 * Holds list entries for enemies in an encounter
 */

class EnemiesListItem {
    private Integer enemyId;
    private String name;
    private String cr;
    private String ref;

    EnemiesListItem(Integer enemyId, String name, String cr, String ref) {
        super();
        this.enemyId = enemyId;
        this.name = name;
        this.cr = cr;
        this.ref = ref;
    }

    String getName() {
        return name;
    }

    String getSubText() {
        return "CR " + cr + " " + ref;
    }

    Integer getEnemyId() {return enemyId;}
}
