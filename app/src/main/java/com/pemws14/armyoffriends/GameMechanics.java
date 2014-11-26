package com.pemws14.armyoffriends;

public class GameMechanics {


    private final int pow = 3;
    private final double rankUpFactor = 1.1;
    private final double levelUpBonus = 0.1;

    /*
    Victory = True = Random >= Threshold (equals difficulty)
    Defeat = False = Random < Threshold (roll doesn't suffice)

    Threshold = enemy^pow/(enemy^pow + self^pow)
    converges to 0 for self->inf, to 1 for enemy->inf
     */
    public boolean getFightResult(int ownArmyStrength, int enemyArmyStrength){
        double r = Math.random();
        double threshold = Math.pow(enemyArmyStrength,pow)/(Math.pow(ownArmyStrength,pow)+Math.pow(enemyArmyStrength,pow));
        return r >= threshold;
    }

    /*
    return n!
     */
    public int factorial(int n){
        if(n < 2)
            return 1;
        else
            return factorial(n-1)*n;
    }

    /*
    return n!/(k! * (n-k)!), binomial coefficient
     */
    public int binomial(int n, int k){
        if(n >= k && k >= 0)
            return factorial(n)/(factorial(k)*factorial(n-k));
        else
            System.out.println("Invalid Invocation of binomial in GameMechanics");
            return -1;
    }

    /*
    returns level for next rank-Up, current rank is parameter
    Fibonacci
    getLevelForRankUp(0->1) =    1
                                +1
    getLevelForRankUp(1->2) =    2
                                +2
    getLevelForRankUp(2->3) =    4
                                +3
    getLevelForRankUp(3->4) =    7
                                +5
    getLevelForRankUp(4->5) =   12
                                +8
    getLevelForRankUp(5->6) =   20
                               +13
    getLevelForRankUp(6->7) =   33
                               +21
    getLevelForRankUp(7->8) =   54
                               +34
    getLevelForRankUp(8->9) =   88
                               +55
    getLevelForRankUp(9->10) = 143
     */
    public int getLevelForRankUp(int rank){
        int fib = 0;
        for (int k = 0; k <= Math.floor((rank + 3 -1) / 2); k++) {
            fib += binomial(rank + 3 - k - 1, k);
        }
        return fib - 1;
    }

    /*
    Should be substituted by SQL Database Rank Lookup
     */
    public int getRankByLevel(int level){
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
    Calculates Strength for given Level
    Level 1 = first Meet, freshly added
    Level 2 = second Meet, first Level up
    .
    .
    .
     */
    public int getStrengthByLevel(int level){
        double soldierStrength = 1+(level-1)*levelUpBonus;
        int rank = getRankByLevel(level);
        soldierStrength *= Math.pow(rankUpFactor,rank-1) * 10;
        return (int) soldierStrength;
    }

    /*
    Iterates over Array of Levels of each soldier in the army
    Sums up all respective strengths
     */
    public int getArmyStrength(int[] soldiersLevels){
        int armyStrength = 0;
        for(int var : soldiersLevels){
            armyStrength += getStrengthByLevel(soldiersLevels[var]);
        }
        return armyStrength;
    }

}
