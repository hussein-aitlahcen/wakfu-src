package com.ankamagames.baseImpl.graphics.alea.adviser.text.flying;

import com.ankamagames.baseImpl.graphics.alea.adviser.*;

public abstract class AbstractDelayedAdviser implements Adviser
{
    public static int INFINITE_DURATION;
    private int m_waitingTime;
    protected int m_elapsedLifeTime;
    protected int m_duration;
    
    public AbstractDelayedAdviser() {
        super();
        this.m_elapsedLifeTime = 0;
        this.m_duration = AbstractDelayedAdviser.INFINITE_DURATION;
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
    
    @Override
    public int getDuration() {
        return this.m_duration;
    }
    
    protected int getElapsedLifeTime() {
        return this.m_elapsedLifeTime;
    }
    
    @Override
    public boolean isAlive() {
        return this.m_duration == AbstractDelayedAdviser.INFINITE_DURATION || this.m_elapsedLifeTime <= this.m_duration + this.m_waitingTime;
    }
    
    @Override
    public void process(final int deltaTime) {
        if (this.m_waitingTime > 0) {
            this.m_waitingTime -= deltaTime;
            if (this.m_waitingTime <= 0) {
                this.setVisible(true);
            }
        }
        else {
            this.m_elapsedLifeTime += deltaTime;
        }
    }
    
    public void setWaitingTime(final int waitingTime) {
        assert waitingTime >= 0;
        this.m_waitingTime = waitingTime;
        if (this.m_waitingTime > 0) {
            this.setVisible(false);
        }
    }
    
    public abstract void setVisible(final boolean p0);
    
    static {
        AbstractDelayedAdviser.INFINITE_DURATION = -1;
    }
}
