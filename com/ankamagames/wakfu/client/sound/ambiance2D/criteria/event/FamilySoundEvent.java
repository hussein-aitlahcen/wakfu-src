package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.sound.group.*;

public class FamilySoundEvent extends SoundEvent
{
    public static final byte FAMILY_SOUND_EVENT_TYPE_ID = 1;
    private int m_familyId;
    private short m_quantity;
    
    public FamilySoundEvent(final EventType type, final int familyId, final short qty) {
        super(type);
        this.m_familyId = familyId;
        this.m_quantity = qty;
    }
    
    public FamilySoundEvent(final EventType type, final ObservedSource source, final int familyId, final short qty) {
        super(type, source);
        this.m_familyId = familyId;
        this.m_quantity = qty;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public byte getSoundEventType() {
        return 1;
    }
    
    @Override
    public int getSignature() {
        return 1;
    }
    
    @Override
    public String getEventTitle() {
        return "Family - " + this.getType().getDescription();
    }
    
    @Override
    public String getParamDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id = ").append(this.m_familyId).append(" quantit\u00e9 = ").append(this.m_quantity);
        final ObservedSource source = this.getSource();
        if (source != null) {
            sb.append(" Position = [").append(source.getObservedX()).append(",").append(source.getObservedY()).append("]");
        }
        return sb.toString();
    }
}
