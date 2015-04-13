package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.sound.group.*;

public class TimeSoundEvent extends SoundEvent
{
    public static final byte TIME_SOUND_EVENT_TYPE_ID = 4;
    private int m_time;
    
    public TimeSoundEvent(final int time) {
        super(EventType.TIME_EVENT);
        this.m_time = time;
    }
    
    public TimeSoundEvent(final ObservedSource source, final int time) {
        super(EventType.TIME_EVENT, source);
        this.m_time = time;
    }
    
    public int getTime() {
        return this.m_time;
    }
    
    @Override
    public byte getSoundEventType() {
        return 4;
    }
    
    @Override
    public int getSignature() {
        return 4;
    }
    
    @Override
    public String getEventTitle() {
        return "Time - " + this.getType().getDescription();
    }
    
    @Override
    public String getParamDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("time = ").append(this.m_time);
        final ObservedSource source = this.getSource();
        if (source != null) {
            sb.append(" Position = [").append(source.getObservedX()).append(",").append(source.getObservedY()).append("]");
        }
        return sb.toString();
    }
}
