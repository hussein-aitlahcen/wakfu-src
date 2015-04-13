package com.ankamagames.wakfu.common.game.xp;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;

public class LevelableImpl
{
    protected static final Logger m_logger;
    private short m_level;
    private long m_xp;
    private final XpTable m_xpTable;
    
    public LevelableImpl(final XpTable xpTable) {
        super();
        this.m_xpTable = xpTable;
    }
    
    public XpTable getXpTable() {
        return this.m_xpTable;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public String getCurrentLevelString() {
        return new StringBuilder().append(this.getXpTable().getXpInLevel(this.m_xp)).append('/').append(this.getXpTable().getLevelExtent(this.m_level)).toString();
    }
    
    public float getCurrentLevelPercentage() {
        return this.getXpTable().getPercentageInLevel(this.m_level, this.m_xp);
    }
    
    public XpModification setLevelAndXp(final short level, final long xp) {
        if (!this.getXpTable().isLevelValid(level) || !this.getXpTable().isXpValid(xp)) {
            return XpModification.NONE;
        }
        return this.setLevelAndXpUnchecked(level, xp);
    }
    
    public XpModification setLevel(final short level, final boolean linkXp) {
        if (!this.getXpTable().isLevelValid(level)) {
            throw new IllegalArgumentException("Valeur de niveau non valide : " + level);
        }
        if (this.m_level == level) {
            return XpModification.NONE;
        }
        return this.setLevelAndXpUnchecked(level, linkXp ? this.getXpTable().getXpByLevel(level) : this.m_xp);
    }
    
    public XpModification setXp(final long xp, final boolean linkLevel) {
        if (!this.getXpTable().isXpValid(xp)) {
            throw new IllegalArgumentException("Valeur d'exp\u00e9rience non valide : " + xp);
        }
        if (this.m_xp == xp) {
            return XpModification.NONE;
        }
        return this.setLevelAndXpUnchecked(linkLevel ? this.getXpTable().getLevelByXp(xp) : this.m_level, xp);
    }
    
    public XpModification addXp(final long xpAdded) {
        if (xpAdded < 0L) {
            LevelableImpl.m_logger.error((Object)("Impossible d'ajouter une exp\u00e9rience n\u00e9gative (" + xpAdded + "). Il faut plut\u00f4t utiliser Levelable.removeXp"));
            return XpModification.NONE;
        }
        if (xpAdded == 0L) {
            return XpModification.NONE;
        }
        final long newXp = Math.min(this.m_xp + xpAdded, this.getXpTable().getMaxXp());
        if (newXp == this.m_xp) {
            return XpModification.NONE;
        }
        final short newLevel = this.getXpTable().getLevelByXp(newXp);
        return this.setLevelAndXpUnchecked(newLevel, newXp);
    }
    
    public XpModification removeXp(final long xpRemoved) {
        if (xpRemoved < 0L) {
            LevelableImpl.m_logger.error((Object)("Impossible de supprimer une exp\u00e9rience n\u00e9gative (" + xpRemoved + "). Il faut plut\u00f4t utiliser Levelable.addXp"));
            return XpModification.NONE;
        }
        if (xpRemoved == 0L) {
            return XpModification.NONE;
        }
        final long newXp = Math.max(this.m_xp - xpRemoved, this.getXpTable().getMinXp());
        if (newXp == this.m_xp) {
            return XpModification.NONE;
        }
        final short newLevel = this.getXpTable().getLevelByXp(newXp);
        return this.setLevelAndXpUnchecked(newLevel, newXp);
    }
    
    private XpModification setLevelAndXpUnchecked(final short level, final long xp) {
        final short levelDiff = (short)(level - this.m_level);
        final long xpDiff = xp - this.m_xp;
        this.m_level = level;
        this.m_xp = xp;
        return new XpModification(xpDiff, levelDiff);
    }
    
    public XpModification forceLevelUp(final boolean linkXp) {
        if (this.m_level + 1 > this.getXpTable().getMaxLevel()) {
            return XpModification.NONE;
        }
        return this.setLevel((short)(this.m_level + 1), linkXp);
    }
    
    public void clear() {
        this.m_xp = 0L;
        this.m_level = 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LevelableImpl.class);
    }
}
