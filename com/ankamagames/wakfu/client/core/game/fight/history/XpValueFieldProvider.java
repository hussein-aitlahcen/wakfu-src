package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.client.core.*;

public class XpValueFieldProvider extends ImmutableFieldProvider
{
    public static final String XP_TOOLTIP_FIELD = "xp";
    public static final String LEVEL_FIELD = "level";
    public static final String PERCENTAGE_IN_LEVEL_FIELD = "percentageInLevel";
    public static final String PREVIOUS_PERCENTAGE_IN_LEVEL_FIELD = "previousPercentageInLevel";
    public static final String PREMIUM_PERCENTAGE_IN_LEVEL_FIELD = "premiumPercentageInLevel";
    public static final String[] FIELDS;
    private final XpTable m_xpTable;
    private final short m_level;
    private final short m_actualLevel;
    private final String m_xpTooltip;
    private final float m_percentageInLevel;
    private final float m_previousPercentageInLevel;
    private final float m_premiumPercentageInLevel;
    
    public XpValueFieldProvider(final XpTable xpTable, final short level, final long xp, final Long xpGained, final int levelDiff) {
        this(xpTable, level, level, xp, xpGained, -1L, levelDiff);
    }
    
    public XpValueFieldProvider(final XpTable xpTable, final short level, final long xp, final Long xpGained, final long xpPremium, final int levelDiff) {
        this(xpTable, level, level, xp, xpGained, xpPremium, levelDiff);
    }
    
    public XpValueFieldProvider(final XpTable xpTable, final short level, final short actualLevel, final long xp, final Long xpGained, final int levelDiff) {
        this(xpTable, level, actualLevel, xp, xpGained, -1L, levelDiff);
    }
    
    public XpValueFieldProvider(final XpTable xpTable, final short level, final short actualLevel, final long xp, final Long xpGained, final long xpPremium, final int levelDiff) {
        super();
        this.m_xpTable = xpTable;
        this.m_level = level;
        this.m_actualLevel = actualLevel;
        this.m_xpTooltip = this.xpTooltip(level, xp);
        this.m_percentageInLevel = this.percentageInLevel(level, xp);
        if (xpPremium != -1L && xpGained != null) {
            final long xpPremium2 = xp - xpGained + xpPremium;
            final short levelByXp = this.m_xpTable.getLevelByXp(xpPremium2);
            final long xpPrem = (levelByXp > level) ? this.m_xpTable.getXpByLevel(levelByXp) : xpPremium2;
            this.m_premiumPercentageInLevel = this.percentageInLevel(level, xpPrem);
        }
        else {
            this.m_premiumPercentageInLevel = 0.0f;
        }
        if (xpGained == null) {
            this.m_previousPercentageInLevel = this.m_percentageInLevel;
        }
        else if (xpGained < 0L) {
            this.m_previousPercentageInLevel = this.m_percentageInLevel;
        }
        else if (xpGained == -1L || levelDiff > 0) {
            this.m_previousPercentageInLevel = -1.0f;
        }
        else {
            this.m_previousPercentageInLevel = this.percentageInLevel(level, xp - xpGained);
        }
    }
    
    private String xpTooltip(final short level, final long xp) {
        final long levelExtent = this.m_xpTable.getLevelExtent(level);
        final short levelByXp = this.m_xpTable.getLevelByXp(xp);
        final long xpInLevel = (levelByXp > level) ? levelExtent : this.m_xpTable.getXpInLevel(xp);
        return WakfuTranslator.getInstance().formatNumber(xpInLevel) + '/' + WakfuTranslator.getInstance().formatNumber(levelExtent);
    }
    
    private float percentageInLevel(final short level, final long xp) {
        return this.m_xpTable.getPercentageInLevel(level, xp);
    }
    
    @Override
    public String[] getFields() {
        return XpValueFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("xp".equals(fieldName)) {
            return this.m_xpTooltip;
        }
        if ("level".equals(fieldName)) {
            return this.m_actualLevel;
        }
        if ("percentageInLevel".equals(fieldName)) {
            return this.m_percentageInLevel;
        }
        if ("previousPercentageInLevel".equals(fieldName)) {
            return this.m_previousPercentageInLevel;
        }
        if ("premiumPercentageInLevel".equals(fieldName)) {
            return this.m_premiumPercentageInLevel;
        }
        return null;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    static {
        FIELDS = new String[] { "xp", "level", "percentageInLevel", "previousPercentageInLevel", "premiumPercentageInLevel" };
    }
}
