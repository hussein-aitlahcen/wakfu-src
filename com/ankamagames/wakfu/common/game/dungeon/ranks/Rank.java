package com.ankamagames.wakfu.common.game.dungeon.ranks;

import com.ankamagames.framework.external.*;
import org.apache.log4j.*;

public enum Rank implements ExportableEnum
{
    A, 
    B, 
    C, 
    D, 
    None;
    
    private static final Logger m_logger;
    
    public boolean isBetterThan(final Rank rank) {
        return this.ordinal() <= rank.ordinal();
    }
    
    public static Rank getRank(final byte id) {
        for (final Rank rank : values()) {
            if (rank.ordinal() == id) {
                return rank;
            }
        }
        Rank.m_logger.error((Object)("Rang inconnu " + id));
        return Rank.None;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.ordinal());
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return this.name();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Rank.class);
    }
}
