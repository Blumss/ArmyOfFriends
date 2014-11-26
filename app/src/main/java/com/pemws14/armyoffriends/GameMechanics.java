package com.pemws14.armyoffriends;



/**
 * Created by Aquila on 26.11.2014.
 */
public class GameMechanics {

    /*
    Sieg = True = Random >= Threshold (entspricht Schwierigkeit)
    Niederlage = False = Random < Threshold (zu niedrig gewürfelt)

    Threshold = Gegner^3/(Gegner^3 + Selber^3)
    konvergiert gegen 0 für Selber->inf, gegen 1 für Gegner->inf
     */
    public static boolean getFightResult(int ownArmyStrength, int enemyArmyStrength){
        double r = Math.random();
        int pow = 3;
        double threshold =(double) Math.pow(enemyArmyStrength,pow)/(double) (Math.pow(ownArmyStrength,pow)+Math.pow(enemyArmyStrength,pow));
        return r >= threshold;
    }



}
