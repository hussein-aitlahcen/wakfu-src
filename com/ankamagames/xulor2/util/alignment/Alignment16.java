package com.ankamagames.xulor2.util.alignment;

public enum Alignment16
{
    NORTH_WEST, 
    NORTH_NORTH_WEST, 
    NORTH, 
    NORTH_NORTH_EAST, 
    NORTH_EAST, 
    WEST_NORTH_WEST, 
    EAST_NORTH_EAST, 
    WEST, 
    EAST, 
    WEST_SOUTH_WEST, 
    EAST_SOUTH_EAST, 
    SOUTH_WEST, 
    SOUTH_SOUTH_WEST, 
    SOUTH, 
    SOUTH_SOUTH_EAST, 
    SOUTH_EAST;
    
    public int getX(final int componentWidth) {
        switch (this) {
            case NORTH_WEST:
            case WEST_NORTH_WEST:
            case WEST:
            case WEST_SOUTH_WEST:
            case SOUTH_WEST: {
                return 0;
            }
            case NORTH_NORTH_WEST:
            case SOUTH_SOUTH_WEST: {
                return (int)Math.max(0.0, componentWidth * 0.25);
            }
            case NORTH:
            case SOUTH: {
                return (int)Math.max(0.0, componentWidth * 0.5);
            }
            case NORTH_NORTH_EAST:
            case SOUTH_SOUTH_EAST: {
                return (int)Math.max(0.0, componentWidth * 0.75);
            }
            case NORTH_EAST:
            case EAST_NORTH_EAST:
            case EAST:
            case EAST_SOUTH_EAST:
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
            case NORTH_NORTH_WEST:
            case NORTH:
            case NORTH_NORTH_EAST:
            case NORTH_EAST: {
                return Math.max(0, componentHeight);
            }
            case WEST_NORTH_WEST:
            case EAST_NORTH_EAST: {
                return (int)Math.max(0.0, componentHeight * 0.75);
            }
            case WEST:
            case EAST: {
                return (int)Math.max(0.0, componentHeight * 0.5);
            }
            case WEST_SOUTH_WEST:
            case EAST_SOUTH_EAST: {
                return (int)Math.max(0.0, componentHeight * 0.25);
            }
            case SOUTH_WEST:
            case SOUTH_SOUTH_WEST:
            case SOUTH:
            case SOUTH_SOUTH_EAST:
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
            case WEST_NORTH_WEST:
            case WEST:
            case WEST_SOUTH_WEST:
            case SOUTH_WEST: {
                return 0;
            }
            case NORTH_NORTH_WEST:
            case SOUTH_SOUTH_WEST: {
                return (int)Math.round((parentWidth - componentWidth) * 0.25);
            }
            case NORTH:
            case SOUTH: {
                return (int)Math.round((parentWidth - componentWidth) * 0.5);
            }
            case NORTH_NORTH_EAST:
            case SOUTH_SOUTH_EAST: {
                return (int)Math.round((parentWidth - componentWidth) * 0.75);
            }
            case NORTH_EAST:
            case EAST_NORTH_EAST:
            case EAST:
            case EAST_SOUTH_EAST:
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
            case NORTH_NORTH_WEST:
            case NORTH:
            case NORTH_NORTH_EAST:
            case NORTH_EAST: {
                return parentHeight - componentHeight;
            }
            case WEST_NORTH_WEST:
            case EAST_NORTH_EAST: {
                return (int)Math.round((parentHeight - componentHeight) * 0.75);
            }
            case WEST:
            case EAST: {
                return (int)Math.round((parentHeight - componentHeight) * 0.5);
            }
            case WEST_SOUTH_WEST:
            case EAST_SOUTH_EAST: {
                return (int)Math.round((parentHeight - componentHeight) * 0.25);
            }
            case SOUTH_WEST:
            case SOUTH_SOUTH_WEST:
            case SOUTH:
            case SOUTH_SOUTH_EAST:
            case SOUTH_EAST: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public boolean isNorth() {
        return this == Alignment16.WEST_NORTH_WEST || this == Alignment16.NORTH_WEST || this == Alignment16.NORTH_NORTH_WEST || this == Alignment16.NORTH || this == Alignment16.NORTH_NORTH_EAST || this == Alignment16.NORTH_EAST || this == Alignment16.EAST_NORTH_EAST;
    }
    
    public boolean isSouth() {
        return this == Alignment16.WEST_SOUTH_WEST || this == Alignment16.SOUTH_WEST || this == Alignment16.SOUTH_SOUTH_WEST || this == Alignment16.SOUTH || this == Alignment16.SOUTH_SOUTH_WEST || this == Alignment16.SOUTH_EAST || this == Alignment16.WEST_SOUTH_WEST;
    }
    
    public boolean isWest() {
        return this == Alignment16.WEST_NORTH_WEST || this == Alignment16.NORTH_WEST || this == Alignment16.NORTH_NORTH_WEST || this == Alignment16.WEST || this == Alignment16.SOUTH_SOUTH_WEST || this == Alignment16.SOUTH_WEST || this == Alignment16.WEST_SOUTH_WEST;
    }
    
    public boolean isEast() {
        return this == Alignment16.EAST_NORTH_EAST || this == Alignment16.NORTH_EAST || this == Alignment16.NORTH_NORTH_EAST || this == Alignment16.EAST || this == Alignment16.SOUTH_SOUTH_EAST || this == Alignment16.SOUTH_EAST || this == Alignment16.EAST_SOUTH_EAST;
    }
    
    public static Alignment16 value(final String value) {
        final Alignment16[] values = values();
        final String upper = value.toUpperCase();
        for (final Alignment16 a : values) {
            if (a.name().equals(upper)) {
                return a;
            }
        }
        return values[0];
    }
}
