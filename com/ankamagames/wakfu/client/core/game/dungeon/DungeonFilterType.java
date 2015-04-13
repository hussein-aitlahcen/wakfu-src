package com.ankamagames.wakfu.client.core.game.dungeon;

import com.ankamagames.wakfu.client.core.*;

public enum DungeonFilterType
{
    ALL((byte)0), 
    AVAILABLE((byte)1);
    
    private final byte m_id;
    
    private DungeonFilterType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString("dungeon.filter." + this.m_id);
    }
    
    public boolean isValid(final DungeonView dungeon) {
        switch (this) {
            case AVAILABLE: {
                return dungeon.isLevelValid() && !dungeon.isLocked();
            }
            default: {
                return true;
            }
        }
    }
}
