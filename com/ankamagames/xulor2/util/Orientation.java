package com.ankamagames.xulor2.util;

public enum Orientation
{
    NORTH, 
    SOUTH, 
    EAST, 
    WEST;
    
    public boolean isVertical() {
        return this == Orientation.NORTH || this == Orientation.SOUTH;
    }
    
    public boolean isHorizontal() {
        return this == Orientation.WEST || this == Orientation.EAST;
    }
    
    public static Orientation value(final String value) {
        final Orientation[] arr$;
        final Orientation[] values = arr$ = values();
        for (final Orientation a : arr$) {
            if (a.name().equals(value.toUpperCase())) {
                return a;
            }
        }
        return values[0];
    }
}
