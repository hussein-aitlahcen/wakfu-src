package com.ankamagames.framework.graphics.engine.text;

public enum TextAlignment
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
                return (int)(-Math.max(0.0, componentWidth * 0.5));
            }
            case NORTH_EAST:
            case EAST:
            case SOUTH_EAST: {
                return -Math.max(0, componentWidth);
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
                return -Math.max(0, componentHeight);
            }
            case WEST:
            case CENTER:
            case EAST: {
                return (int)(-Math.max(0.0, componentHeight * 0.5));
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
}
