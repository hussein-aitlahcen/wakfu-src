package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.*;

public class ThunderEffect extends Effect
{
    public static final Color DEFAULT_FLASH_COLOR;
    private int m_timeBeforeFlash;
    private int m_minTimeBetweenFlash;
    private int m_maxTimeBetweenFlash;
    private final Color m_flashColor;
    
    public ThunderEffect(final int minTimeBetweenFlash, final int maxTimeBetweenFlash) {
        super();
        this.m_flashColor = new Color(ThunderEffect.DEFAULT_FLASH_COLOR);
        this.m_minTimeBetweenFlash = minTimeBetweenFlash;
        this.m_maxTimeBetweenFlash = maxTimeBetweenFlash;
    }
    
    public ThunderEffect() {
        this(5000, 20000);
    }
    
    public void setColor(final Color c) {
        this.m_flashColor.set(c);
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public void reset() {
        super.reset();
        this.m_timeBeforeFlash = MathHelper.random(this.m_minTimeBetweenFlash, this.m_maxTimeBetweenFlash);
    }
    
    @Override
    public void update(final int timeIncrement) {
        super.update(timeIncrement);
        this.m_timeBeforeFlash -= timeIncrement;
        if (this.m_timeBeforeFlash < 0) {
            this.m_timeBeforeFlash = MathHelper.random(this.m_minTimeBetweenFlash, this.m_maxTimeBetweenFlash);
            WeatherEffectManager.throwThunder(this.m_camera, this.m_flashColor);
        }
    }
    
    @Override
    public void render(final Renderer renderer) {
    }
    
    static {
        DEFAULT_FLASH_COLOR = new Color(0.8f, 0.8f, 0.8f, 0.8f);
    }
}
