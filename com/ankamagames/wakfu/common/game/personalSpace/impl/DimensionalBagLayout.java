package com.ankamagames.wakfu.common.game.personalSpace.impl;

import com.ankamagames.wakfu.common.game.personalSpace.*;
import gnu.trove.*;
import java.util.*;

public class DimensionalBagLayout extends PersonalSpaceLayout
{
    private static final int[] m_gemsCount;
    
    @Override
    public byte roomCount() {
        return 9;
    }
    
    @Override
    public boolean isPositionValid(final byte layoutPosition) {
        return layoutPosition >= 0 && layoutPosition < this.roomCount();
    }
    
    @Override
    public short[] coordinatesFromPosition(final byte layoutPosition) {
        final int x = layoutPosition / 3 * 6;
        final int y = layoutPosition % 3 * 6;
        return new short[] { (short)x, (short)y };
    }
    
    @Override
    public boolean update() {
        return super.update();
    }
    
    @Override
    public void checkConsistency(final RoomFactory roomFactory) {
        final TByteHashSet expectedEntries = new TByteHashSet();
        for (byte i = 0; i < this.roomCount(); ++i) {
            expectedEntries.add(i);
        }
        final Set<Map.Entry<Byte, Room>> entries = this.m_rooms.entrySet();
        for (final Map.Entry<Byte, Room> entry : entries) {
            expectedEntries.remove(entry.getKey());
        }
        expectedEntries.forEach(new TByteProcedure() {
            @Override
            public boolean execute(final byte roomId) {
                final Room room = roomFactory.newRoom();
                DimensionalBagLayout.this.putRoom(roomId, room);
                return true;
            }
        });
    }
    
    static {
        m_gemsCount = new int[GemType.values().length];
    }
}
