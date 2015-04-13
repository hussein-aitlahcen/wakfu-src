package com.ankamagames.wakfu.common.game.craftNew.util;

import gnu.trove.*;

public class CraftXPUtil
{
    public static final int LEVEL_PER_PYRAMIDE = 50;
    public static final int LEVEL_BETWEEN_COMP = 5;
    public static final int RESSOURCE_BASE_XP = 10;
    public static final int RESSOURCE_PER_COMP = 5;
    public static final int RESSOURCE_FIRST_FACTOR = 2;
    public static final int RESSOURCE_LEVEL_FLOOR = 20;
    public static final int SECOND_PER_COMP = 30;
    public static final int CRAFT_LEVEL_FACTOR = 3;
    public static final int CRAFT_FIRST_FACTOR = 2;
    public static final int CRAFT_SECOND_FACTOR = 20;
    public static final int CRAFT_MIN_LEVEL = 1;
    public static final int CRAFT_MAX_LEVEL = 200;
    private static final TShortLongHashMap m_craftLevelByXp;
    
    private static void generateTable() {
        for (short level = 1; level <= 200; ++level) {
            CraftXPUtil.m_craftLevelByXp.put(level, getXpForCraftLevel(level));
        }
    }
    
    public static long getXpForResource(final int resourceLevel) {
        return (long)Math.floor(10.0 * Math.pow(2.0, resourceLevel / 20.0f));
    }
    
    public static long getXpForComponentRecipe(final int recipeLevel) {
        long xpResult = 0L;
        for (int level = recipeLevel - recipeLevel % 50; level <= recipeLevel; level += 5) {
            if (level % 50 == 0) {
                xpResult += getXpForResource(level) * 5L * 2L;
            }
            else {
                xpResult += getXpForResource(level) * 5L;
            }
        }
        return xpResult;
    }
    
    public static float getXpForComponentByMinute(final int craftLevel) {
        long compXpResult = 0L;
        long compXp = 0L;
        final int nbComp = (int)Math.floor(1 + craftLevel % 50 / 5);
        for (int level = craftLevel - craftLevel % 50; level <= craftLevel; level += 5) {
            compXp += getXpForComponentRecipe(level);
        }
        compXpResult = (long)(compXp / nbComp * 60.0f / 30.0f);
        return compXpResult;
    }
    
    public static long getXpForCraftLevel(final int craftLevel) {
        return (long)(getXpForComponentByMinute(craftLevel) * 3.0f * Math.pow(2.0, craftLevel / 20.0f));
    }
    
    public static short getCraftLevelForXp(final long xp) {
        short level = 0;
        final TShortLongIterator it = CraftXPUtil.m_craftLevelByXp.iterator();
        while (it.hasNext()) {
            it.advance();
            level = it.key();
            if (xp < it.value()) {
                break;
            }
        }
        return level;
    }
    
    public static void main(final String[] args) {
        String output = "";
        for (int i = 1; i <= 200; ++i) {
            if (i % 5 == 0) {
                output = output + " ||| Recipe level " + i + " xp=" + getXpForComponentRecipe(i);
                if (i % 10 == 0) {
                    output += "\n";
                }
            }
        }
        for (int i = 1; i <= 200; ++i) {
            output = output + " ||| Craft level " + i + " xp=" + getXpForCraftLevel(i);
            if (i % 5 == 0) {
                output += "\n";
            }
        }
        System.out.println(output);
    }
    
    static {
        m_craftLevelByXp = new TShortLongHashMap();
        generateTable();
    }
}
