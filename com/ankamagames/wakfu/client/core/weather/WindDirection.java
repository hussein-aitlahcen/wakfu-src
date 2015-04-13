package com.ankamagames.wakfu.client.core.weather;

public enum WindDirection
{
    NORTH(-1.0f), 
    SOUTH(1.0f), 
    EAST(-1.0f), 
    WEST(1.0f);
    
    private float m_direction;
    
    private WindDirection(final float direction) {
        this.m_direction = direction;
    }
    
    float getDirection() {
        return this.m_direction;
    }
}
