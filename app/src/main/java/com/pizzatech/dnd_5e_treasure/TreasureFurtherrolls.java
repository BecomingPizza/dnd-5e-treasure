package com.pizzatech.dnd_5e_treasure;

/**
 * Created by Ashley on 01/10/2016.
 *
 */

public class TreasureFurtherRolls {
    private Integer artDice;
    private Integer artSides;
    private Integer artValue;
    private Integer gemDice;
    private Integer gemSides;
    private Integer gemValue;
    private Integer magicOneDice;
    private Integer magicOneSides;
    private String magicOneSubtable;
    private Integer magicTwoDice;
    private Integer magicTwoSides;
    private String magicTwoSubtable;

    TreasureFurtherRolls(Integer artDice,
                         Integer artSides,
                         Integer artValue,
                         Integer gemDice,
                         Integer gemSides,
                         Integer gemValue,
                         Integer magicOneDice,
                         Integer magicOneSides,
                         String magicOneSubtable,
                         Integer magicTwoDice,
                         Integer magicTwoSides,
                         String magicTwoSubtable) {
        this.artDice = artDice;
        this.artSides = artSides;
        this.artValue = artValue;
        this.gemDice = gemDice;
        this.gemSides = gemSides;
        this.gemValue = gemValue;
        this.magicOneDice = magicOneDice;
        this.magicOneSides = magicOneSides;
        this.magicOneSubtable = magicOneSubtable;
        this.magicTwoDice = magicTwoDice;
        this.magicTwoSides = magicTwoSides;
        this.magicTwoSubtable = magicTwoSubtable;
    }

    TreasureFurtherRolls() {}

    void rolyPoly() {
        if (artDice != null) {
            TreasureRoller.rollArt(artDice, artSides, artValue);
        }

        if (gemDice != null) {
            TreasureRoller.rollGems(gemDice, gemSides, gemValue);
        }

        if (magicOneDice != null) {
            TreasureRoller.rollMagic(magicOneDice, magicOneSides, magicOneSubtable);
        }

        if (magicOneDice != null) {
            TreasureRoller.rollMagic(magicTwoDice, magicTwoSides, magicTwoSubtable);
        }
    }
}
