package com.ankamagames.wakfu.common.game.time.calendar;

public enum BoatEventType
{
    ARRIVAL(1), 
    DEPARTURE(2), 
    COLLECTOR_SUCCESS(3), 
    COLLECTOR_ERROR(4), 
    BOAT_FULL(5);
    
    private byte m_id;
    
    private BoatEventType(final int id) {
        this.m_id = (byte)id;
    }
    
    public static BoatEventType getById(final int id) {
        final BoatEventType[] types = values();
        for (int i = 0, size = types.length; i < size; ++i) {
            final BoatEventType boatEventType = types[i];
            if (boatEventType.m_id == id) {
                return boatEventType;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
