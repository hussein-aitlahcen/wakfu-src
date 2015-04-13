package com.ankamagames.wakfu.client.core.game.characterInfo.colors;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.fileFormat.io.*;

public enum GroupDifficultyColor
{
    CHALLENGE(Integer.MIN_VALUE, "808080", (byte)(-1)), 
    CELEST(Integer.MIN_VALUE, "808080", (byte)3), 
    KING(Integer.MIN_VALUE, "808080", (byte)4), 
    WORTHLESS(Integer.MIN_VALUE, "808080", (byte)0), 
    VERY_EASY(-20, "6561ff", (byte)0), 
    EASY(-12, "61ff65", (byte)0), 
    NORMAL(-4, "ffffff", (byte)1), 
    HARD(4, "ffb761", (byte)2), 
    VERY_HARD(12, "ff0000", (byte)2), 
    TOO_HARD(20, "d200ff", (byte)2), 
    REPRODUCER(Integer.MAX_VALUE, "d200ff", (byte)5);
    
    private final int m_minDelta;
    private final String m_color;
    private final byte m_iconId;
    private static final Logger m_logger;
    
    private GroupDifficultyColor(final int minDelta, final String color, final byte iconId) {
        this.m_minDelta = minDelta;
        this.m_color = color;
        this.m_iconId = iconId;
    }
    
    public static GroupDifficultyColor getGroupDifficultyColor(final int playerLevel, final int groupLevel) {
        final int delta = groupLevel - playerLevel;
        GroupDifficultyColor ret = null;
        for (final GroupDifficultyColor c : values()) {
            if (c.m_minDelta <= delta) {
                ret = c;
            }
            else if (c.m_minDelta > delta) {
                break;
            }
        }
        return ret;
    }
    
    public static GroupDifficultyColor getInvocationLevelColor(final int level, final int maxLevel) {
        if (level <= maxLevel / 2) {
            return GroupDifficultyColor.EASY;
        }
        if (level <= maxLevel * 3 / 4) {
            return GroupDifficultyColor.NORMAL;
        }
        if (level <= maxLevel) {
            return GroupDifficultyColor.HARD;
        }
        return null;
    }
    
    public String getHexColor() {
        return this.m_color;
    }
    
    public String getIconURL(final int breedId) {
        switch (this) {
            case CHALLENGE: {
                try {
                    return WakfuConfiguration.getContentPath("groupDifficultyChallengeIconPath");
                }
                catch (PropertyException e) {
                    GroupDifficultyColor.m_logger.warn((Object)e.getMessage());
                    return null;
                }
                break;
            }
        }
        try {
            return ContentFileHelper.getPath(WakfuConfiguration.getInstance().getString("groupDifficultyIconsPath"), breedId, this.m_iconId);
        }
        catch (PropertyException e) {
            GroupDifficultyColor.m_logger.warn((Object)e.getMessage());
            return null;
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GroupDifficultyColor.class);
    }
}
