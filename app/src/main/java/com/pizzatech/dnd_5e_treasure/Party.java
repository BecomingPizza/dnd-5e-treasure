package com.pizzatech.dnd_5e_treasure;

import java.util.ArrayList;

/**
 * Created by ashley on 08/10/2017.
 *
 * Party name + PCs that are in it
 *
 */

public class Party {
    private Integer partyId;
    private ArrayList<PlayerCharacter> characters;

    Party(Integer partyId, ArrayList<PlayerCharacter> characters) {
        super();
        this.partyId = partyId;
        this.characters = characters;
    }

}
