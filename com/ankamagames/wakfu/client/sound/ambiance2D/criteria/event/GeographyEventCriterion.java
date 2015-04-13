package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class GeographyEventCriterion implements EventCriterion
{
    public static final byte CRITERION_ID = 2;
    private GeographyEventType m_type;
    private short m_min;
    private short m_max;
    
    public GeographyEventCriterion() {
        super();
        this.m_min = -1;
        this.m_max = -1;
    }
    
    public GeographyEventCriterion(final GeographyEventType type, final short min, final short max) {
        super();
        this.m_min = -1;
        this.m_max = -1;
        this.m_type = type;
        this.m_min = min;
        this.m_max = max;
    }
    
    public GeographyEventType getType() {
        return this.m_type;
    }
    
    public void setType(final GeographyEventType type) {
        this.m_type = type;
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
        if (e.getSoundEventType() != 2) {
            return false;
        }
        final GeographySoundEvent event = (GeographySoundEvent)e;
        return event.getGeographyEventType() == this.m_type && (this.m_min == -1 || event.getStrength() >= this.m_min) && (this.m_max == -1 || event.getStrength() <= this.m_max);
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_type = GeographyEventType.fromId(is.readByte());
        this.m_min = is.readShort();
        this.m_max = is.readShort();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeByte(this.m_type.m_id);
        os.writeShort(this.m_min);
        os.writeShort(this.m_max);
    }
    
    @Override
    public byte getCriterionId() {
        return 2;
    }
    
    @Override
    public EventCriterion clone() {
        return new GeographyEventCriterion(this.m_type, this.m_min, this.m_max);
    }
}
