package com.pemws14.armyoffriends.army;

import android.graphics.Bitmap;

/**
 * Created by Schnabeltier on 07.12.2014.
 */
public class ChildRow
{
    private String name;
    private String level;
    private String levelNextRank;
    private Bitmap image;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getLevelNextRank()
    {
        return levelNextRank;
    }

    public void setLevelNextRank(String levelNextRank)
    {
        this.levelNextRank = levelNextRank;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }
}
