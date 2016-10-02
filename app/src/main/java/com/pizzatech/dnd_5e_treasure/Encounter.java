package com.pizzatech.dnd_5e_treasure;

/**
 * Created by Ashley on 02/10/2016.
 *
 * Object for holding an encounter, consisting of ID / Name
 */

class Encounter {

    private Integer id;
    private String name;

    Encounter(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    Integer getId() {
        return id;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
