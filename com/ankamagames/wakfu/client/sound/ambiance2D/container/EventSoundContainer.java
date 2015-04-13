package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import gnu.trove.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import java.util.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;

public class EventSoundContainer extends ParentSoundContainer
{
    public static TObjectProcedure<SoundContainer> STOP_PROCEDURE;
    private EventType m_event;
    private boolean m_eventIsLocalized;
    private EventCriterion m_eventCriterion;
    
    public void setEventCriterion(final EventCriterion c) {
        this.m_eventCriterion = c;
    }
    
    public EventType getEvent() {
        return this.m_event;
    }
    
    public void setEvent(final EventType event) {
        this.m_event = event;
    }
    
    public void setEventIsLocalized(final boolean eventIsLocalized) {
        this.m_eventIsLocalized = eventIsLocalized;
    }
    
    @Override
    public void play(final long time) {
    }
    
    @Override
    public void stop(final long time) {
        this.forEachSource(EventSoundContainer.STOP_PROCEDURE);
    }
    
    private boolean checkEventCriterion(final SoundEvent e) {
        return this.m_event == e.getType() && this.m_eventIsLocalized == e.isLocalized() && (this.m_eventCriterion == null || this.m_eventCriterion.isValid(e));
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list) {
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final SoundEvent event) {
        if (this.checkEventCriterion(event)) {
            super.getValidSoundSources(list);
        }
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final AudioMarkerType type) {
    }
    
    static {
        EventSoundContainer.STOP_PROCEDURE = new TObjectProcedure<SoundContainer>() {
            @Override
            public boolean execute(final SoundContainer sc) {
                sc.stop();
                return true;
            }
        };
    }
}
