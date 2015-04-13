package com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers;

public class WaitTimedStrength extends TimedStrength
{
    private int m_wait;
    
    public WaitTimedStrength(final int wait, final int startDuration, final int middleDuration, final int endDuration) {
        super(startDuration, middleDuration, endDuration);
        this.m_wait = wait;
    }
    
    @Override
    public boolean update(final int deltaTime) {
        if (this.m_wait > 0) {
            this.m_wait -= deltaTime;
            return true;
        }
        return super.update(deltaTime);
    }
    
    @Override
    public float getStrength() {
        if (this.m_wait > 0) {
            return 0.0f;
        }
        return super.getStrength();
    }
}
