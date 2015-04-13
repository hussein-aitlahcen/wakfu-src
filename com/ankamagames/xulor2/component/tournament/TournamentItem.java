package com.ankamagames.xulor2.component.tournament;

public class TournamentItem
{
    private Object m_value;
    private float m_x;
    private float m_y;
    
    public TournamentItem(final Object value, final float x, final float y) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_value = value;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    public float getX() {
        return this.m_x;
    }
    
    public void setX(final float x) {
        this.m_x = x;
    }
    
    public float getY() {
        return this.m_y;
    }
    
    public void setY(final float y) {
        this.m_y = y;
    }
}
