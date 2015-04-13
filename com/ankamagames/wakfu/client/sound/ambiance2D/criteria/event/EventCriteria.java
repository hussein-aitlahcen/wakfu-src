package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.wakfu.client.sound.ambiance2D.*;

public enum EventCriteria
{
    WEATHER_TYPE((byte)0, "Ev\u00e8nement m\u00e9t\u00e9o", (EventCriterionFactory)new EventCriterionFactory() {
        @Override
        public EventCriterion createCriterion() {
            return new WeatherEventCriterion(WeatherEventType.RAIN_BEGIN);
        }
    }), 
    FAMILY_TYPE((byte)1, "Id de famille", (EventCriterionFactory)new EventCriterionFactory() {
        @Override
        public EventCriterion createCriterion() {
            return new FamilyEventCriterion(-1, (short)(-1), (short)(-1));
        }
    }), 
    GEOGRAPHY_TYPE((byte)2, "Ev\u00e8nement g\u00e9ographique", (EventCriterionFactory)new EventCriterionFactory() {
        @Override
        public EventCriterion createCriterion() {
            return new GeographyEventCriterion(GeographyEventType.SEA, (short)(-1), (short)(-1));
        }
    }), 
    AUDIO_MARKER_TYPE((byte)3, "Marqueur Audio", (EventCriterionFactory)new EventCriterionFactory() {
        @Override
        public EventCriterion createCriterion() {
            return new AudioMarkerEventCriterion(AudioMarkerType.ROCKFALL, (short)(-1), (short)(-1));
        }
    }), 
    TIME_TYPE((byte)4, "Temps", (EventCriterionFactory)new EventCriterionFactory() {
        @Override
        public EventCriterion createCriterion() {
            return new TimeEventCriterion("");
        }
    });
    
    private final EventCriterionFactory m_factory;
    private final byte m_id;
    private final String m_description;
    
    private EventCriteria(final byte id, final String desc, final EventCriterionFactory factory) {
        this.m_factory = factory;
        this.m_description = desc;
        this.m_id = id;
    }
    
    public static EventCriteria getFromId(final byte id) {
        for (final EventCriteria crit : values()) {
            if (crit.getId() == id) {
                return crit;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public EventCriterion createCriterion() {
        return this.m_factory.createCriterion();
    }
    
    @Override
    public String toString() {
        return this.m_description;
    }
}
