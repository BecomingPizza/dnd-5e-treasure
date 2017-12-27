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
    private String name;
    private ArrayList<PlayerCharacter> characters;

    Party(Integer partyId, String name, ArrayList<PlayerCharacter> playerCharacterIds) {
        super();
        this.partyId = partyId;
        this.name = name;
        this.characters = characters;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<PlayerCharacter> getPlayerCharacters() {
        return characters;
    }

}
