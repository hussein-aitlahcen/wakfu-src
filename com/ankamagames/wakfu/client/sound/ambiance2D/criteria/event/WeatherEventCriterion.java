package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class WeatherEventCriterion implements EventCriterion
{
    public static final byte CRITERION_ID = 0;
    private WeatherEventType m_type;
    
    public WeatherEventCriterion() {
        super();
    }
    
    public WeatherEventCriterion(final WeatherEventType type) {
        super();
        this.m_type = type;
    }
    
    public void setType(final WeatherEventType type) {
        this.m_type = type;
    }
    
    public WeatherEventType getType() {
        return this.m_type;
    }
    
    @Override
    public boolean isValid(final SoundEvent e) {
        if (e.getSoundEventType() != 0) {
            return false;
        }
        final WeatherSoundEvent event = (WeatherSoundEvent)e;
        return event.getWeatherType() == this.m_type;
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_type = WeatherEventType.fromId(is.readByte());
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeByte(this.m_type.getId());
    }
    
    @Override
    public byte getCriterionId() {
        return 0;
    }
    
    @Override
    public EventCriterion clone() {
        return new WeatherEventCriterion(this.m_type);
    }
}
