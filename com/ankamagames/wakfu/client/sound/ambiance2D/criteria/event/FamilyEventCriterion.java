package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class FamilyEventCriterion implements EventCriterion
{
    public static final byte CRITERION_ID = 1;
    private int m_familyId;
    private short m_min;
    private short m_max;
    
    public FamilyEventCriterion() {
        super();
        this.m_min = -1;
        this.m_max = -1;
    }
    
    public FamilyEventCriterion(final int familyId, final short min, final short max) {
        super();
        this.m_min = -1;
        this.m_max = -1;
        this.m_familyId = familyId;
        this.m_min = min;
        this.m_max = max;
    }
    
    public void setFamilyId(final int familyId) {
        this.m_familyId = familyId;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public short getMin() {
        return this.m_min;
    }
    
    public void setMin(final short min) {
        this.m_min = min;
    }
    
    public short getMax() {
        return this.m_max;
    }
    
    public void setMax(final short max) {
        this.m_max = max;
    }
    
    @Override
    public boolean isValid(final SoundEvent e) {
        if (e.getSoundEventType() != 1) {
            return false;
        }
        final FamilySoundEvent event = (FamilySoundEvent)e;
        return event.getFamilyId() == this.m_familyId && (this.m_min == -1 || event.getQuantity() >= this.m_min) && (this.m_max == -1 || event.getQuantity() <= this.m_max);
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_familyId = is.readInt();
        this.m_min = is.readShort();
        this.m_max = is.readShort();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeInt(this.m_familyId);
        os.writeShort(this.m_min);
        os.writeShort(this.m_max);
    }
    
    @Override
    public byte getCriterionId() {
        return 1;
    }
    
    @Override
    public EventCriterion clone() {
        return new FamilyEventCriterion(this.m_familyId, this.m_min, this.m_max);
    }
}
