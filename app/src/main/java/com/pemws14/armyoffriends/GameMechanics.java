package com.pemws14.armyoffriends;

import com.pemws14.armyoffriends.database.DbSoldier;

import java.util.List;

public class GameMechanics {

    private final static int POW = 3;
    private final static double RANK_UP_FACTOR = 1.1;
    private final static double LEVEL_UP_BONUS = 0.1;
    private final static int MAX_RANK = 10;
    private final static int SIZE_MULT = 10;
    private final static int EP_BLOW_UP = 100; //mehr Nullen bei der Belohnung -> mehr Zufriedenheit


    /*
    Victory equals result > 0 = Random >= Threshold (equals difficulty)
    Defeat equals result == 0 = Random < Threshold (roll doesn't suffice)

    Result is also reward (EP) factor

    Threshold = enemy^POW/(enemy^POW + self^POW)
    converges to 0 for self->inf, to 1 for enemy->inf
     */
    public static double[] getFightResult(int ownArmyStrength, int enemyArmyStrength){
        double r = Math.random();
        double threshold = 0;
        if(ownArmyStrength != 0 || enemyArmyStrength != 0){
            threshold = Math.pow(enemyArmyStrength, POW)/(Math.pow(ownArmyStrength, POW)+Math.pow(enemyArmyStrength, POW));
        }
        return new double[]{r, threshold};
    }

    /*
    return n!
     */
    public static int factorial(int n){
        if(n < 2)
            return 1;
        else
            return factorial(n-1)*n;
    }

    /*
    return n!/(k! * (n-k)!), binomial coefficient
     */
    public static int binomial(int n, int k){
        if(n >= k && k >= 0)
            return factorial(n)/(factorial(k)*factorial(n-k));
        else
            System.out.println("Invalid Invocation of binomial in GameMechanics");
            return -1;
    }

    /*
    returns required level for Rank-up to reach given rank
    Fibonacci
    getLevelForRank(1) =    1 (0->1)
                           +1
    getLevelForRank(2) =    2 (1->2)
                           +2
    getLevelForRank(3) =    4 (2->3)
                           +3
    getLevelForRank(4) =    7 (3->4)
                           +5
    getLevelForRank(5) =   12 (4->5)
                           +8
    getLevelForRank(6) =   20 (5->6)
                          +13
    getLevelForRank(7)     33 (6->7)
                          +21
    getLevelForRank(8) =   54 (7->8)
                          +34
    getLevelForRank(9) =   88 (8->9)
                          +55
    getLevelForRank(10) = 143 (9->10)
     */
    public static int getLevelForRank(int rank){
        int fib = 0;
        for (int k = 0; k <= Math.floor((rank + 1) / 2); k++) {
            fib += binomial(rank + 1 - k, k);
        }
        return fib - 1;
    }

    /*
    Should be substituted by SQL Database Rank Lookup
     */
    public static int getRankByLevel(int level){
        if(level >= 143)
            return 10;
        if(level >= 88)
            return 9;
        if(level >= 54)
            return 8;
        if(level >= 33)
            return 7;
        if(level >= 20)
            return 6;
        if(level >= 12)
            return 5;
        if(level >= 7)
            return 4;
        if(level >= 4)
            return 3;
        if(level >= 2)
            return 2;
        return 1;
    }

    /*
    Returns amount of level ups necessary for next rank up
    result <= 0  ==>  rank = MAX_RANK
     */
    public static int getLevelForRankUp(int level){
        return getLevelForRank(Math.min(getRankByLevel(level) + 1, MAX_RANK)) - level;
    }

    /*
    Calculates Strength for given Level
    Level 1 = first Meet, freshly added
    Level 2 = second Meet, first Level up
    .
    .
    .
     */
    public static int getStrengthByLevel(int level){
        double soldierStrength = 1+(level-1)* LEVEL_UP_BONUS;
        int rank = getRankByLevel(level);
        soldierStrength *= Math.pow(RANK_UP_FACTOR,rank-1) * 10;
        return (int) soldierStrength;
    }

    /*
    Iterates over Array of Levels of each soldier in the army
    Sums up all respective strengths
     */
    public static int getArmyStrength(List<DbSoldier> soldiers) {
        int armyStrength = 0;
        for (DbSoldier var : soldiers) {
            armyStrength += getStrengthByLevel(var.getLevel());
        }
        return armyStrength;
    }

    /*
    returns maximum amount of soldiers usable in battle (and battle only) for given level aka Battle Experience
     */
    public static int getMaxArmySize(int playerLevel){
        return SIZE_MULT * playerLevel;
    }

    /*
    returns absolute amount of EP necessary for next player level
     */
    public static int getEpForPlayerLevelUp(int playerLevel){
        return (int) (EP_BLOW_UP*Math.pow(playerLevel,2.0));
    }

    /*
    returns player level for given player EP
     */
    public static int getPlayerLevelForEp(int ep){
        return (int) Math.floor(Math.sqrt(ep/EP_BLOW_UP)) + 1;
    }

    /*
    returns Base EP reward to be multiplied with getFightResult
    scales with enemy player level
    beating lvl 50 player & army strength 1000 yields more reward than
    beating lvl 45 player & army strength 1000
     */
    public static int getEpBaseReward(int enemyLevel){
        return EP_BLOW_UP*enemyLevel;
    }

    /*
    returns EP progress of current level in percentage
    */
    public static double getPlayerLevelProgress(int ep){
        double epToReachCurrentLevel = getEpForPlayerLevelUp(getPlayerLevelForEp(ep)-1);
        double epNextLevel = getEpForPlayerLevelUp(getPlayerLevelForEp(ep));
        return (ep-epToReachCurrentLevel)/(epNextLevel-epToReachCurrentLevel);
    }

    /*
    returns level multiplier used in randomEncounterLevel
    if r = 0 then 0.5
    if r = 1 then 1.5
     */
    public static double randomMult(){
        double r = Math.random();
        return 1.5*Math.asin(2*r-1)/Math.PI + 1.25;
    }

    /*
    calculates level & strength of daily challenge/random encounter for given own player Level
     */
    public static int[] randomEncounter(int ownLevel, int ownStrength){
        double rM = randomMult();
        int[] result = {Math.round((float) (ownLevel * rM)),Math.round((float) (ownStrength * rM))};
        return result;
    }

}
