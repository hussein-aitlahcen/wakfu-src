package com.ankamagames.framework.kernel.core.maths;

import org.jetbrains.annotations.*;

public enum Direction8
{
    EAST(0, 1, -1), 
    SOUTH_EAST(1, 1, 0), 
    SOUTH(2, 1, 1), 
    SOUTH_WEST(3, 0, 1), 
    WEST(4, -1, 1), 
    NORTH_WEST(5, -1, 0), 
    NORTH(6, -1, -1), 
    NORTH_EAST(7, 0, -1), 
    TOP(8, 0, 0), 
    BOTTOM(9, 0, 0), 
    NONE(-1, 0, 0);
    
    private static final Direction8[] DIRECTION_8_VALUES;
    private static final Direction8[] DIRECTION_4_VALUES;
    private static final Direction8[] VALUES;
    public final int m_index;
    public final int m_x;
    public final int m_y;
    
    private Direction8(final int index, final int x, final int y) {
        this.m_index = index;
        this.m_x = x;
        this.m_y = y;
    }
    
    public int getIndex() {
        return this.m_index;
    }
    
    public Direction8 getOppositeDirection() {
        switch (this) {
            case EAST: {
                return Direction8.WEST;
            }
            case NORTH_EAST: {
                return Direction8.SOUTH_WEST;
            }
            case NORTH: {
                return Direction8.SOUTH;
            }
            case NORTH_WEST: {
                return Direction8.SOUTH_EAST;
            }
            case WEST: {
                return Direction8.EAST;
            }
            case SOUTH_WEST: {
                return Direction8.NORTH_EAST;
            }
            case SOUTH: {
                return Direction8.NORTH;
            }
            case SOUTH_EAST: {
                return Direction8.NORTH_WEST;
            }
            default: {
                return Direction8.NONE;
            }
        }
    }
    
    public static Direction8 getDirectionFromIndex(final int index) {
        for (int i = 0; i < Direction8.VALUES.length; ++i) {
            final Direction8 direction = Direction8.VALUES[i];
            if (direction.m_index == index) {
                return direction;
            }
        }
        return Direction8.NONE;
    }
    
    @Nullable
    public static Direction8 getDirectionFromVector(final int dx, final int dy) {
        assert -1 <= dx && dx <= 1;
        assert -1 <= dy && dy <= 1;
        if (dx == 0 && dy == 0) {
            return null;
        }
        for (int i = 0; i < Direction8.DIRECTION_8_VALUES.length; ++i) {
            final Direction8 dir = Direction8.DIRECTION_8_VALUES[i];
            if (dir.m_x == dx && dir.m_y == dy) {
                return dir;
            }
        }
        return null;
    }
    
    public boolean isDirection4() {
        return this.m_x == 0 || this.m_y == 0;
    }
    
    public static Direction8[] getDirection8Values() {
        return Direction8.DIRECTION_8_VALUES;
    }
    
    public static Direction8[] getDirection4Values() {
        return Direction8.DIRECTION_4_VALUES;
    }
    
    public Direction8 opposite() {
        switch (this) {
            case EAST: {
                return Direction8.WEST;
            }
            case SOUTH_EAST: {
                return Direction8.NORTH_WEST;
            }
            case SOUTH: {
                return Direction8.NORTH;
            }
            case SOUTH_WEST: {
                return Direction8.NORTH_EAST;
            }
            case WEST: {
                return Direction8.EAST;
            }
            case NORTH_WEST: {
                return Direction8.SOUTH_EAST;
            }
            case NORTH: {
                return Direction8.SOUTH;
            }
            case NORTH_EAST: {
                return Direction8.SOUTH_WEST;
            }
            case TOP: {
                return Direction8.BOTTOM;
            }
            case BOTTOM: {
                return Direction8.TOP;
            }
            default: {
                return Direction8.NONE;
            }
        }
    }
    
    public Direction8 getNextDirection8(final int increment) {
        int index = this.m_index;
        index += increment;
        index %= Direction8.DIRECTION_8_VALUES.length;
        if (index < 0) {
            index += Direction8.DIRECTION_8_VALUES.length;
        }
        return getDirectionFromIndex(index);
    }
    
    public static Direction8 getRandom() {
        final Direction8[] values = values();
        return values[MathHelper.random(values.length)];
    }
    
    public Direction8 getNextDirection4(final int increment) {
        int index = this.m_index;
        if (index % 2 == 0) {
            --index;
        }
        index += 2 * increment;
        index %= Direction8.DIRECTION_8_VALUES.length;
        if (index < 0) {
            index += Direction8.DIRECTION_8_VALUES.length;
        }
        return getDirectionFromIndex(index);
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
    static {
        DIRECTION_8_VALUES = new Direction8[] { Direction8.SOUTH_EAST, Direction8.SOUTH_WEST, Direction8.NORTH_WEST, Direction8.NORTH_EAST, Direction8.EAST, Direction8.WEST, Direction8.NORTH, Direction8.SOUTH };
        DIRECTION_4_VALUES = new Direction8[] { Direction8.SOUTH_EAST, Direction8.SOUTH_WEST, Direction8.NORTH_WEST, Direction8.NORTH_EAST };
        VALUES = values();
    }
}
