package com.ankamagames.xulor2.util.alignment;

public enum Alignment4
{
    NORTH, 
    SOUTH, 
    EAST, 
    WEST;
    
    public int getX(final int componentWidth) {
        switch (this) {
            case WEST: {
                return 0;
            }
            case NORTH:
            case SOUTH: {
                return (int)Math.max(0.0, componentWidth * 0.5);
            }
            case EAST: {
                return Math.max(0, componentWidth);
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getY(final int componentHeight) {
        switch (this) {
            case NORTH: {
                return Math.max(0, componentHeight);
            }
            case WEST:
            case EAST: {
                return (int)Math.max(0.0, componentHeight * 0.5);
            }
            case SOUTH: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getX(final int componentWidth, final int parentWidth) {
        switch (this) {
            case WEST: {
                return 0;
            }
            case NORTH:
            case SOUTH: {
                return (int)Math.round((parentWidth - componentWidth) * 0.5);
            }
            case EAST: {
                return Math.max(0, parentWidth - componentWidth);
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getY(final int componentHeight, final int parentHeight) {
        switch (this) {
            case NORTH: {
                return parentHeight - componentHeight;
            }
            case WEST:
            case EAST: {
                return (int)Math.round((parentHeight - componentHeight) * 0.5);
            }
            case SOUTH: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public boolean isNorth() {
        return this == Alignment4.NORTH;
    }
    
    public boolean isSouth() {
        return this == Alignment4.SOUTH;
    }
    
    public boolean isWest() {
        return this == Alignment4.WEST;
    }
    
    public boolean isEast() {
        return this == Alignment4.EAST;
    }
    
    public static Alignment4 value(final String value) {
        final Alignment4[] values = values();
        final String upper = value.toUpperCase();
        for (final Alignment4 a : values) {
            if (a.name().equals(upper)) {
                return a;
            }
        }
        return values[0];
    }
}
