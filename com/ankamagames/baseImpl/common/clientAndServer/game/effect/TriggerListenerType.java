package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.external.*;

public enum TriggerListenerType implements ExportableEnum
{
    NONE((byte)0, "Aucune \u00e9coute"), 
    CASTER_TRIGGER_LISTENER((byte)1, "Trigger qui se d\u00e9clenche quand le porteur est le lanceur de l'effet d\u00e9clencheur"), 
    TARGET_TRIGGER_LISTENER((byte)2, "Trigger qui se d\u00e9clenche quand le porteur est la cible de l'effet d\u00e9clencheur"), 
    GLOBAL_TRIGGER_LISTENER((byte)3, "Trigger qui se d\u00e9clenche lorsque l'\u00e9coute survient de quelque mani\u00e8re que ce soit");
    
    private byte m_id;
    private String m_comment;
    
    private TriggerListenerType(final byte id, final String comment) {
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
    
    public static TriggerListenerType getTriggerListenerTypeFromId(final byte id) {
        final TriggerListenerType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final TriggerListenerType value = values[i];
            if (value.m_id == id) {
                return value;
            }
        }
        return null;
    }
}
