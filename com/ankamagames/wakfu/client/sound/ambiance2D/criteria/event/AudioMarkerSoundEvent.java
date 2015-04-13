package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.framework.sound.group.*;

public class AudioMarkerSoundEvent extends SoundEvent
{
    public static final byte AUDIO_MARKER_SOUND_EVENT_TYPE_ID = 3;
    private AudioMarkerType m_type;
    private short m_quantity;
    
    public AudioMarkerSoundEvent(final AudioMarkerType markerType, final short qty) {
        super(EventType.AUDIO_MARKER_EVENT);
        this.m_type = markerType;
        this.m_quantity = qty;
    }
    
    public AudioMarkerSoundEvent(final ObservedSource source, final AudioMarkerType markerType, final short qty) {
        super(EventType.AUDIO_MARKER_EVENT, source);
        this.m_type = markerType;
        this.m_quantity = qty;
    }
    
    public AudioMarkerType getAudioMarkerType() {
        return this.m_type;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public byte getSoundEventType() {
        return 3;
    }
    
    @Override
    public int getSignature() {
        return 3;
    }
    
    @Override
    public String getEventTitle() {
        return "AudioMarker - " + this.getType().getDescription();
    }
    
    @Override
    public String getParamDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id = ").append(this.m_type.getEnumLabel()).append(" quantit\u00e9 = ").append(this.m_quantity);
        final ObservedSource source = this.getSource();
        if (source != null) {
            sb.append(" Position = [").append(source.getObservedX()).append(",").append(source.getObservedY()).append("]");
        }
        return sb.toString();
    }
}
