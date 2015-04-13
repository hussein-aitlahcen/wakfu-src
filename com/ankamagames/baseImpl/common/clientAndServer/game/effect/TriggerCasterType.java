package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.external.*;

public enum TriggerCasterType implements ExportableEnum
{
    NONE((byte)0, "L'effet n'est pas d\u00e9clench\u00e9"), 
    EFFECT_ORIGINAL_CASTER((byte)1, "Le caster sera le caster original de l'effet"), 
    EFFECT_CARRIER((byte)2, "Le caster sera le porteur cet effet"), 
    EFFECT_TARGET((byte)3, "Le caster sera la cible de cet effet"), 
    TRIGGERING_EFFECT_CASTER((byte)4, "Le caster sera le caster de l'effet declencheur"), 
    TRIGGERING_EFFECT_TARGET((byte)5, "Le caster sera la cible de l'effet declencheur");
    
    private byte m_id;
    private String m_comment;
    
    private TriggerCasterType(final byte id, final String comment) {
        this.m_id = id;
        this.m_comment = comment;
    }
    
    @Override
    public String getEnumId() {
        return Byte.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return this.m_comment;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static TriggerCasterType getTriggerCasterTypeFromId(final byte id) {
        final TriggerCasterType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final TriggerCasterType value = values[i];
            if (value.m_id == id) {
                return value;
            }
        }
        return null;
    }
}
