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
    private Integer xpCurrent;
    private Integer xpToNextLevel;
    private Integer level;

    PlayerCharacter (Integer characterId, String characterName, String playerName, Integer xpCurrent, Integer xpToNextLevel, Integer level) {
        super();
        this.characterId = characterId;
        this.characterName = characterName;
        this.playerName = playerName;
        this.xpCurrent = xpCurrent;
        this.xpToNextLevel = xpToNextLevel;
        this.level = level;
    }

    public String getMainText() {
        return characterName + " (" + playerName + ")";
    }

    public String getSubText() {
        return "Level: " + level + " (" + xpCurrent + "/" + xpToNextLevel + ")";
    }

}
