package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.external.*;

public enum TriggerTargetType implements ExportableEnum
{
    NONE((byte)0, "L'effet n'est pas d\u00e9clench\u00e9"), 
    EFFECT_ORIGINAL_TARGET((byte)1, "La cible sera la cible que l'on a d\u00e9j\u00e0 calcul\u00e9e"), 
    EFFECT_CARRIER((byte)2, "La cible sera le porteur cet effet"), 
    EFFECT_CASTER((byte)3, "La cible sera le lanceur cet effet"), 
    TRIGGERING_EFFECT_CASTER((byte)4, "La cible sera le lanceur de l'effet declencheur"), 
    TRIGGERING_EFFECT_TARGET((byte)5, "La cible sera la cible de l'effet declencheur");
    
    private byte m_id;
    private String m_comment;
    
    private TriggerTargetType(final byte id, final String comment) {
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
    
    public static TriggerTargetType getTriggerTargetTypeFromId(final byte id) {
        final TriggerTargetType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final TriggerTargetType value = values[i];
            if (value.m_id == id) {
                return value;
            }
        }
        return null;
    }
}
