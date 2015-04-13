package com.ankamagames.wakfu.common.game.xp.character;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.common.constants.*;

public class PlayerCharacterXp implements PlayerCharacterLevelable
{
    private static final Logger m_logger;
    private long m_currentXp;
    private short m_level;
    private final boolean m_enforceLevelCap;
    
    public PlayerCharacterXp(final boolean enforceLevelCap) {
        super();
        this.m_level = 1;
        this.m_enforceLevelCap = enforceLevelCap;
    }
    
    @Override
    public long getCurrentXp() {
        return this.m_currentXp;
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    public void reset() {
        this.m_currentXp = 0L;
        this.m_level = 1;
    }
    
    @Override
    public XpTable getXpTable() {
        return CharacterXpTable.getInstance();
    }
    
    @Override
    public float getCurrentLevelPercentage() {
        return this.getXpTable().getPercentageInLevel(this.m_level, this.m_currentXp);
    }
    
    public float getUnpenalizedLevelPercentage() {
        return this.getXpTable().getPercentageInLevel(this.m_level, this.m_currentXp);
    }
    
    private XpModification setLevelAndXpUnchecked(final short level, final long xp) {
        final long xpDiff = xp - this.getCurrentXp();
        final short levelDiff = (short)(level - this.getLevel());
        this.m_level = level;
        this.m_currentXp = xp;
        return new XpModification(xpDiff, levelDiff);
    }
    
    private XpModification setXpUnchecked(final long xp) {
        final short level = this.getXpTable().getLevelByXp(xp);
        return this.setLevelAndXpUnchecked(level, xp);
    }
    
    @Override
    public XpModification setXp(long xp) {
        if (!this.getXpTable().isXpValid(xp)) {
            PlayerCharacterXp.m_logger.error((Object)("Valeur d'exp\u00e9rience non valide : " + xp));
            return XpModification.NONE;
        }
        if (xp > this.getMaxXpConsideringCap()) {
            PlayerCharacterXp.m_logger.error((Object)"On essaye d'ajouter plus d'xp que ce que le cap permet", (Throwable)new Exception());
            xp = this.getMaxXpConsideringCap();
        }
        if (this.getCurrentXp() == xp) {
            return XpModification.NONE;
        }
        return this.setXpUnchecked(xp);
    }
    
    @Override
    public XpModification addXp(final long xp) {
        final long xpToAdd = this.xpToAdd(xp);
        if (xpToAdd <= 0L) {
            return XpModification.NONE;
        }
        return this.setXp(this.getCurrentXp() + xpToAdd);
    }
    
    private long xpToAdd(final long requestedXpToAdd) {
        return Math.max(0L, Math.min(requestedXpToAdd, this.getMaxXpConsideringCap() - this.getCurrentXp()));
    }
    
    public long getMaxXpConsideringCap() {
        return (this.m_enforceLevelCap && XpConstants.getPlayerCharacterLevelCap() < this.getXpTable().getMaxLevel()) ? this.getXpTable().getXpByLevel(XpConstants.getPlayerCharacterLevelCap()) : this.getXpTable().getMaxXp();
    }
    
    public short getMaxLevelConsideringCap() {
        return (this.m_enforceLevelCap && XpConstants.getPlayerCharacterLevelCap() < this.getXpTable().getMaxLevel()) ? XpConstants.getPlayerCharacterLevelCap() : CharacterXpTable.getInstance().getMaxLevel();
    }
    
    @Override
    public XpModification setPlayerCharacterLevel(short level) {
        if (!this.getXpTable().isLevelValid(level)) {
            PlayerCharacterXp.m_logger.error((Object)("Valeur de niveau non valide : " + level));
            return XpModification.NONE;
        }
        if (level > this.getMaxLevelConsideringCap()) {
            PlayerCharacterXp.m_logger.error((Object)("Valeur de niveau non valide : sup\u00e9rieure au cap ! : " + level), (Throwable)new Exception());
            level = this.getMaxLevelConsideringCap();
        }
        if (this.getLevel() == level) {
            return XpModification.NONE;
        }
        final long xp = this.getXpTable().getXpByLevel(level);
        return this.setLevelAndXpUnchecked(level, xp);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayerCharacterXp.class);
    }
}
