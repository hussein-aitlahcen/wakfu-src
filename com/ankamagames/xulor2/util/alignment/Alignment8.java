package com.ankamagames.xulor2.util.alignment;

public enum Alignment8
{
    NORTH_WEST, 
    NORTH, 
    NORTH_EAST, 
    WEST, 
    EAST, 
    SOUTH_WEST, 
    SOUTH, 
    SOUTH_EAST;
    
    public int getX(final int componentWidth) {
        switch (this) {
            case NORTH_WEST:
            case WEST:
            case SOUTH_WEST: {
                return 0;
            }
            case NORTH:
            case SOUTH: {
                return (int)Math.max(0.0, componentWidth * 0.5);
            }
            case NORTH_EAST:
            case EAST:
            case SOUTH_EAST: {
                return Math.max(0, componentWidth);
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getY(final int componentHeight) {
        switch (this) {
            case NORTH_WEST:
            case NORTH:
            case NORTH_EAST: {
                return Math.max(0, componentHeight);
            }
            case WEST:
            case EAST: {
                return (int)Math.max(0.0, componentHeight * 0.5);
            }
            case SOUTH_WEST:
            case SOUTH:
            case SOUTH_EAST: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getX(final int componentWidth, final int parentWidth) {
        switch (this) {
            case NORTH_WEST:
            case WEST:
            case SOUTH_WEST: {
                return 0;
            }
            case NORTH:
            case SOUTH: {
                return (int)Math.round((parentWidth - componentWidth) * 0.5);
            }
            case NORTH_EAST:
            case EAST:
            case SOUTH_EAST: {
                return Math.max(0, parentWidth - componentWidth);
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getY(final int componentHeight, final int parentHeight) {
        switch (this) {
            case NORTH_WEST:
            case NORTH:
            case NORTH_EAST: {
                return parentHeight - componentHeight;
            }
            case WEST:
            case EAST: {
                return (int)Math.round((parentHeight - componentHeight) * 0.5);
            }
            case SOUTH_WEST:
            case SOUTH:
            case SOUTH_EAST: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public Alignment8 getOpposite() {
        switch (this) {
            case NORTH_WEST: {
                return Alignment8.SOUTH_EAST;
            }
            case NORTH: {
                return Alignment8.SOUTH;
            }
            case NORTH_EAST: {
                return Alignment8.SOUTH_WEST;
            }
            case WEST: {
                return Alignment8.EAST;
            }
            case EAST: {
                return Alignment8.WEST;
            }
            case SOUTH_WEST: {
                return Alignment8.NORTH_EAST;
            }
            case SOUTH: {
                return Alignment8.NORTH;
            }
            case SOUTH_EAST: {
                return Alignment8.NORTH_WEST;
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean isNorth() {
        return this == Alignment8.NORTH_WEST || this == Alignment8.NORTH || this == Alignment8.NORTH_EAST;
    }
    
    public boolean isSouth() {
        return this == Alignment8.SOUTH_WEST || this == Alignment8.SOUTH || this == Alignment8.SOUTH_EAST;
    }
    
    public boolean isWest() {
        return this == Alignment8.NORTH_WEST || this == Alignment8.WEST || this == Alignment8.SOUTH_WEST;
    }
    
    public boolean isEast() {
        return this == Alignment8.NORTH_EAST || this == Alignment8.EAST || this == Alignment8.SOUTH_EAST;
    }
    
    public static Alignment8 value(final String value) {
        final Alignment8[] values = values();
        final String upper = value.toUpperCase();
        for (final Alignment8 a : values) {
            if (a.name().equals(upper)) {
                return a;
            }
        }
        return values[0];
    }
}
