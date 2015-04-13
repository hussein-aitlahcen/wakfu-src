package com.ankamagames.xulor2.util.alignment;

public enum Alignment5
{
    NORTH, 
    SOUTH, 
    EAST, 
    WEST, 
    CENTER;
    
    public int getX(final int componentWidth) {
        switch (this) {
            case WEST: {
                return 0;
            }
            case NORTH:
            case CENTER:
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
            case CENTER:
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
            case CENTER:
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
            case CENTER:
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
        return this == Alignment5.NORTH;
    }
    
    public boolean isSouth() {
        return this == Alignment5.SOUTH;
    }
    
    public boolean isWest() {
        return this == Alignment5.WEST;
    }
    
    public boolean isEast() {
        return this == Alignment5.EAST;
    }
    
    public static Alignment5 value(final String value) {
        final Alignment5[] values = values();
        final String upper = value.toUpperCase();
        for (final Alignment5 a : values) {
            if (a.name().equals(upper)) {
                return a;
            }
        }
        return values[0];
    }
}
