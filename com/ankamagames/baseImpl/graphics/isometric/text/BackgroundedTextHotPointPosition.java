package com.ankamagames.baseImpl.graphics.isometric.text;

public enum BackgroundedTextHotPointPosition
{
    SOUTH_WEST, 
    SOUTH, 
    SOUTH_EAST, 
    NORTH_WEST, 
    NORTH, 
    NORTH_EAST, 
    WEST, 
    EAST;
    
    public static BackgroundedTextHotPointPosition value(final String value) {
        final BackgroundedTextHotPointPosition[] arr$;
        final BackgroundedTextHotPointPosition[] values = arr$ = values();
        for (final BackgroundedTextHotPointPosition a : arr$) {
            if (a.name().equals(value.toUpperCase())) {
                return a;
            }
        }
        return values[0];
    }
}
