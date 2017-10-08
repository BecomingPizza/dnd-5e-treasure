package com.pizzatech.dnd_5e_treasure;

/**
 * Created by ashley on 08/10/2017.
 *
 * This object represents the PC, XP, and level
 *
 */

public class PlayerCharacter {
    private Integer characterId;
    private String characterName;
    private String playerName;
    private Integer xp;
    private Integer level;

    PlayerCharacter (Integer characterId, String characterName, String playerName, Integer xp, Integer level) {
        super();
        this.characterId = characterId;
        this.characterName = characterName;
        this.playerName = playerName;
        this.xp = xp;
        this.level = level;
    }

}
