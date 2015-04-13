package com.ankamagames.wakfu.client.alea.graphics;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class SunShadowManager implements GameCalendarEventListener
{
    protected static final Logger m_logger;
    private static SunShadowManager m_instance;
    private static final float SHADOW_ATTENUATION_START_PERCENTAGE = 25.0f;
    private static final float DAY_SHADOW_INTENSITY = 1.0f;
    private static final float NIGHT_SHADOW_INTENSITY = 0.0f;
    private static final double START_ANGLE = -0.5235987755982988;
    private boolean m_shadowEnabled;
    private float m_shadowIntensity;
    private float m_shadowAngle;
    
    private SunShadowManager() {
        super();
        this.m_shadowEnabled = false;
        this.m_shadowIntensity = 0.0f;
        this.m_shadowAngle = 0.0f;
    }
    
    @Override
    public void onCalendarEvent(final CalendarEventType eventType, final GameCalendar gameCalendar) {
        switch (eventType) {
            case CALENDAR_UPDATED: {
                if (gameCalendar.isSynchronized()) {
                    this.update((WakfuGameCalendar)gameCalendar);
                    break;
                }
                break;
            }
        }
    }
    
    public void update(final WakfuGameCalendar calendar) {
    }
    
    public float getShadowIntensity() {
        return this.m_shadowIntensity;
    }
    
    public float getShadowAngle() {
        return this.m_shadowAngle;
    }
    
    public boolean isShadowEnabled() {
        return this.m_shadowEnabled;
    }
    
    public void setShadowEnabled(final boolean shadowEnabled) {
        this.m_shadowEnabled = shadowEnabled;
    }
    
    public static SunShadowManager getInstance() {
        return SunShadowManager.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SunShadowManager.class);
        SunShadowManager.m_instance = new SunShadowManager();
    }
}
