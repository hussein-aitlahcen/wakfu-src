package com.ankamagames.wakfu.client.core.dungeon.loader;

import com.ankamagames.wakfu.client.core.*;

public class ChallengeDefinition
{
    private final int m_id;
    private final float m_ratio;
    
    ChallengeDefinition(final int id, final float ratio) {
        super();
        this.m_id = id;
        this.m_ratio = ratio;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public float getRatio() {
        return this.m_ratio;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(114, this.m_id, new Object[0]);
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(115, this.m_id, new Object[0]);
    }
}
