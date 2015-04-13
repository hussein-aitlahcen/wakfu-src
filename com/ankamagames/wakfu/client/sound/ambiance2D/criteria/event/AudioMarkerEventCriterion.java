package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AudioMarkerEventCriterion implements EventCriterion
{
    public static final byte CRITERION_ID = 3;
    private AudioMarkerType m_type;
    private short m_min;
    private short m_max;
    
    public AudioMarkerEventCriterion() {
        super();
        this.m_min = -1;
        this.m_max = -1;
    }
    
    public AudioMarkerEventCriterion(final AudioMarkerType type, final short min, final short max) {
        super();
        this.m_min = -1;
        this.m_max = -1;
        this.m_type = type;
        this.m_min = min;
        this.m_max = max;
    }
    
    public AudioMarkerType getType() {
        return this.m_type;
    }
    
    public void setType(final AudioMarkerType type) {
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
        if (e.getSoundEventType() != 3) {
            return false;
        }
        final AudioMarkerSoundEvent event = (AudioMarkerSoundEvent)e;
        return event.getAudioMarkerType() == this.m_type && (this.m_min == -1 || event.getQuantity() >= this.m_min) && (this.m_max == -1 || event.getQuantity() <= this.m_max);
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_type = AudioMarkerType.getFromId(is.readInt());
        this.m_min = is.readShort();
        this.m_max = is.readShort();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeInt(this.m_type.getId());
        os.writeShort(this.m_min);
        os.writeShort(this.m_max);
    }
    
    @Override
    public byte getCriterionId() {
        return 3;
    }
    
    @Override
    public EventCriterion clone() {
        return new AudioMarkerEventCriterion(this.m_type, this.m_min, this.m_max);
    }
}
