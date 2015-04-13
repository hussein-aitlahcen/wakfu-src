package com.ankamagames.xulor2.tween;

import java.util.*;

public abstract class AbstractTween<T>
{
    public static final int INFINITE_REPEAT = -1;
    protected TweenFunction m_function;
    protected int m_elapsedTime;
    protected int m_duration;
    protected int m_repeat;
    protected boolean m_reverseOnRepeat;
    protected int m_currentRepeat;
    protected int m_delay;
    protected T m_a;
    protected T m_b;
    protected boolean m_paused;
    protected TweenClient m_client;
    private ArrayList<TweenEventListener> m_listeners;
    
    public AbstractTween() {
        super();
        this.m_repeat = 1;
        this.m_reverseOnRepeat = true;
        this.m_currentRepeat = 0;
        this.m_delay = 0;
        this.m_paused = false;
        this.m_listeners = null;
    }
    
    public void addTweenEventListener(final TweenEventListener l) {
        if (this.m_listeners == null) {
            this.m_listeners = new ArrayList<TweenEventListener>(3);
        }
        this.m_listeners.add(l);
    }
    
    public void removeTweenEventListener(final TweenEventListener l) {
        if (this.m_listeners != null) {
            this.m_listeners.remove(l);
        }
    }
    
    public boolean isPaused() {
        return this.m_paused;
    }
    
    public void setPaused(final boolean paused) {
        this.m_paused = paused;
    }
    
    public long getDuration() {
        return this.m_duration;
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
    
    public int getDelay() {
        return this.m_delay;
    }
    
    public void setDelay(final int delay) {
        this.m_delay = delay;
    }
    
    public boolean isReverseOnRepeat() {
        return this.m_reverseOnRepeat;
    }
    
    public void setReverseOnRepeat(final boolean reverseOnRepeat) {
        this.m_reverseOnRepeat = reverseOnRepeat;
    }
    
    public T getA() {
        return this.m_a;
    }
    
    public void setA(final T a) {
        this.m_a = a;
    }
    
    public T getB() {
        return this.m_b;
    }
    
    public void setB(final T b) {
        this.m_b = b;
    }
    
    public int getRepeat() {
        return this.m_repeat;
    }
    
    public void setRepeat(final int repeat) {
        assert repeat > 0 : "La valeur de repeat d\u00e9finie n'est pas valide ( inf\u00e9rieure ou \u00e9gale \u00e0 0)";
        this.m_repeat = repeat;
    }
    
    public void setTweenFunction(final TweenFunction function) {
        this.m_function = function;
    }
    
    public void setTweenClient(final TweenClient client) {
        this.m_client = client;
    }
    
    public TweenClient getTweenClient() {
        return this.m_client;
    }
    
    public boolean process(final int deltaTime) {
        if (this.m_paused) {
            return true;
        }
        if (this.m_delay > 0) {
            this.m_delay -= deltaTime;
        }
        if (this.m_delay > 0) {
            return false;
        }
        if (this.m_delay < 0) {
            this.m_elapsedTime -= this.m_delay;
            this.m_delay = 0;
        }
        else {
            this.m_elapsedTime += deltaTime;
        }
        if (this.m_elapsedTime >= this.m_duration) {
            ++this.m_currentRepeat;
            if (this.m_currentRepeat != this.m_repeat) {
                if (this.m_reverseOnRepeat) {
                    final T tmp = this.m_a;
                    this.m_a = this.m_b;
                    this.m_b = tmp;
                }
                if (this.m_duration != 0) {
                    this.m_elapsedTime %= this.m_duration;
                }
                else {
                    this.m_elapsedTime = 0;
                }
            }
            else {
                this.m_elapsedTime = this.m_duration;
            }
        }
        if (this.m_repeat != -1 && this.m_currentRepeat >= this.m_repeat) {
            this.m_client.removeTween(this);
            return false;
        }
        return true;
    }
    
    public void onEnd() {
        this.cleanUp();
        if (this.m_listeners != null) {
            for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
                this.m_listeners.get(i).onTweenEvent(this, TweenEvent.TWEEN_ENDED);
            }
        }
    }
    
    public void cleanUp() {
    }
}
