package com.ankamagames.xulor2.util.alignment;

public enum Alignment9
{
    NORTH_WEST, 
    NORTH, 
    NORTH_EAST, 
    WEST, 
    CENTER, 
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
            case CENTER:
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
            case CENTER:
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
            case CENTER:
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
    
    public int getX(final int relativeX, final int relativeWidth, final int width) {
        switch (this) {
            case NORTH_WEST:
            case WEST:
            case SOUTH_WEST: {
                return relativeX - width;
            }
            case NORTH:
            case CENTER:
            case SOUTH: {
                return (int)Math.round((relativeWidth - width) * 0.5) + relativeX;
            }
            case NORTH_EAST:
            case EAST:
            case SOUTH_EAST: {
                return relativeX + relativeWidth;
            }
            default: {
                return 0;
            }
        }
    }
    
    public int getY(final int relativeY, final int relativeHeight, final int height) {
        switch (this) {
            case NORTH_WEST:
            case NORTH:
            case NORTH_EAST: {
                return relativeY + relativeHeight;
            }
            case WEST:
            case CENTER:
            case EAST: {
                return (int)Math.round((relativeHeight - height) * 0.5) + relativeY;
            }
            case SOUTH_WEST:
            case SOUTH:
            case SOUTH_EAST: {
                return relativeY - height;
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
            case CENTER:
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
    
    public boolean isNorth() {
        return this == Alignment9.NORTH_WEST || this == Alignment9.NORTH || this == Alignment9.NORTH_EAST;
    }
    
    public boolean isSouth() {
        return this == Alignment9.SOUTH_WEST || this == Alignment9.SOUTH || this == Alignment9.SOUTH_EAST;
    }
    
    public boolean isWest() {
        return this == Alignment9.NORTH_WEST || this == Alignment9.WEST || this == Alignment9.SOUTH_WEST;
    }
    
    public boolean isEast() {
        return this == Alignment9.NORTH_EAST || this == Alignment9.EAST || this == Alignment9.SOUTH_EAST;
    }
    
    public Alignment9 getXOpposite() {
        switch (this) {
            case NORTH_WEST: {
                return Alignment9.NORTH_EAST;
            }
            case NORTH_EAST: {
                return Alignment9.NORTH_WEST;
            }
            case WEST: {
                return Alignment9.EAST;
            }
            case EAST: {
                return Alignment9.WEST;
            }
            case SOUTH_WEST: {
                return Alignment9.SOUTH_EAST;
            }
            case SOUTH_EAST: {
                return Alignment9.SOUTH_WEST;
            }
            default: {
                return this;
            }
        }
    }
    
    public Alignment9 getYOpposite() {
        switch (this) {
            case NORTH_WEST: {
                return Alignment9.SOUTH_WEST;
            }
            case NORTH: {
                return Alignment9.SOUTH;
            }
            case NORTH_EAST: {
                return Alignment9.SOUTH_EAST;
            }
            case SOUTH_WEST: {
                return Alignment9.NORTH_WEST;
            }
            case SOUTH: {
                return Alignment9.NORTH;
            }
            case SOUTH_EAST: {
                return Alignment9.NORTH_EAST;
            }
            default: {
                return this;
            }
        }
    }
    
    public Alignment9 getOpposite() {
        final Alignment9[] values = values();
        return values[values.length - 1 - this.ordinal()];
    }
    
    public static Alignment9 value(final String value) {
        final Alignment9[] values = values();
        final String upper = value.toUpperCase();
        for (final Alignment9 a : values) {
            if (a.name().equals(upper)) {
                return a;
            }
        }
        return values[0];
    }
}
