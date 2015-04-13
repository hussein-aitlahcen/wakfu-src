package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

public enum TargetType
{
    NONE("none"), 
    CASTER("caster"), 
    TARGET("target"), 
    EVENT_TRIGGERER("eventTriggerer"), 
    EVENT_TARGET("eventTarget"), 
    POSITION("position");
    
    private final String m_targetName;
    
    private TargetType(final String name) {
        this.m_targetName = name;
    }
    
    public String getTargetName() {
        return this.m_targetName;
    }
    
    public static TargetType getFromName(final String name) {
        for (final TargetType type : values()) {
            if (type.m_targetName.equals(name)) {
                return type;
            }
        }
        return TargetType.NONE;
    }
}
