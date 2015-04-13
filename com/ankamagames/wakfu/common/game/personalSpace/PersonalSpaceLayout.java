package com.ankamagames.wakfu.common.game.personalSpace;

import java.util.*;

public abstract class PersonalSpaceLayout implements Iterable<Room>
{
    private PersonalSpace m_personalSpace;
    protected final HashMap<Byte, Room> m_rooms;
    
    protected PersonalSpaceLayout() {
        super();
        this.m_rooms = new HashMap<Byte, Room>();
    }
    
    public void setPersonalSpace(final PersonalSpace personalSpace) {
        this.m_personalSpace = personalSpace;
    }
    
    public PersonalSpace getPersonalSpace() {
        return this.m_personalSpace;
    }
    
    public void putRoom(final byte layoutPosition, final Room room) {
        if (room != null && this.isPositionValid(layoutPosition)) {
            final short[] pos = this.coordinatesFromPosition(layoutPosition);
            room.setOriginX(pos[0]);
            room.setOriginY(pos[1]);
            room.setLayoutPosition(layoutPosition);
            room.setLayout(this);
            this.m_rooms.put(layoutPosition, room);
            room.update();
        }
    }
    
    public Room getRoom(final byte layoutPosition) {
        if (this.isPositionValid(layoutPosition)) {
            return this.m_rooms.get(layoutPosition);
        }
        return null;
    }
    
    @Override
    public Iterator<Room> iterator() {
        return this.m_rooms.values().iterator();
    }
    
    public Room getRoomFromUnit(final int x, final int y) {
        for (final Map.Entry<Byte, Room> entry : this.m_rooms.entrySet()) {
            final Room room = entry.getValue();
            if (room.unitWithinBounds(x, y)) {
                return room;
            }
        }
        return null;
    }
    
    public boolean update() {
        for (final Map.Entry<Byte, Room> entry : this.m_rooms.entrySet()) {
            if (!entry.getValue().update()) {
                return false;
            }
        }
        return true;
    }
    
    public void clear() {
        this.m_rooms.clear();
    }
    
    public abstract byte roomCount();
    
    public abstract boolean isPositionValid(final byte p0);
    
    public abstract short[] coordinatesFromPosition(final byte p0);
    
    public abstract void checkConsistency(final RoomFactory p0);
}
