package com.ankamagames.wakfu.client.core.game.protector.snapshot;

import com.ankamagames.wakfu.common.game.protector.*;

public enum ProtectorMood
{
    NEUTRAL((byte)0, "AnimStatique"), 
    HAPPY((byte)1, "AnimStatique-Content"), 
    HANGRY((byte)2, "AnimStatique-Colere"), 
    SURPRISED((byte)3, "AnimStatique-Surpris");
    
    private byte m_id;
    private String m_animation;
    
    private ProtectorMood(final byte id, final String animation) {
        this.m_id = id;
        this.m_animation = animation;
    }
    
    public static ProtectorMood getMoodFromId(final byte id) {
        for (final ProtectorMood mood : values()) {
            if (mood.m_id == id) {
                return mood;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getAnimation() {
        return this.m_animation;
    }
    
    public static ProtectorMood getMoodFromSatisfaction(final ProtectorSatisfactionLevel protectorSatisfactionLevel) {
        switch (protectorSatisfactionLevel) {
            case UNSATISFIED: {
                return ProtectorMood.HANGRY;
            }
            case SATISFIED: {
                return ProtectorMood.HAPPY;
            }
            default: {
                return ProtectorMood.NEUTRAL;
            }
        }
    }
}
