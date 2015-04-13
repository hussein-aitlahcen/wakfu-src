package com.ankamagames.baseImpl.graphics.alea.display.effects;

import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.engine.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;

public abstract class Effect implements EffectBase
{
    protected static final float HALF_CELL_WIDTH = 43.0f;
    protected static final float HALF_CELL_HEIGHT = 21.5f;
    protected static final float ELEVATION_UNIT = 10.0f;
    private static int m_nextFreeId;
    private final int m_id;
    private boolean m_isActivated;
    protected IsoCamera m_camera;
    private StrengthHandler m_strengthHandler;
    
    static int nextUID() {
        if (Effect.m_nextFreeId == Integer.MAX_VALUE) {
            Effect.m_nextFreeId = 0;
        }
        return Effect.m_nextFreeId++;
    }
    
    public Effect() {
        super();
        this.m_id = nextUID();
        this.reset();
    }
    
    @Override
    public final int getId() {
        return this.m_id;
    }
    
    public float getStrength() {
        return this.m_strengthHandler.getStrength();
    }
    
    @Override
    public void activate(final boolean activate) {
        this.m_isActivated = activate;
    }
    
    @Override
    public boolean isActivated() {
        return this.m_isActivated;
    }
    
    public void setCamera(final IsoCamera camera) {
        this.m_camera = camera;
    }
    
    @Override
    public abstract void clear();
    
    @Override
    public void reset() {
        this.m_strengthHandler = new StrengthHandler(1.0f);
    }
    
    @Override
    public void update(final int timeIncrement) {
        if (!this.m_strengthHandler.update(timeIncrement)) {
            this.activate(false);
        }
    }
    
    @Override
    public abstract void render(final Renderer p0);
    
    public void start(@NotNull final StrengthHandler strengthHandler) {
        this.m_strengthHandler = strengthHandler;
        this.activate(true);
    }
    
    @Override
    public void stop(final int timeBeforeKill) {
        this.m_strengthHandler = new EaseOutStrength(timeBeforeKill, this.getStrength());
    }
    
    static {
        Effect.m_nextFreeId = 1;
    }
}
