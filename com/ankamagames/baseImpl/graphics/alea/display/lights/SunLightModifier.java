package com.ankamagames.baseImpl.graphics.alea.display.lights;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class SunLightModifier implements GameCalendarEventListener, LitSceneModifier
{
    private static final Logger m_logger;
    public static final SunLightModifier INSTANCE;
    private static TByteObjectHashMap<DayLightDefinitionManager> m_modifiers;
    private LightColor m_sunLight;
    private DayPercentDelegate m_delegate;
    private float m_targetRed;
    private float m_targetGreen;
    private float m_targetBlue;
    private boolean m_targetColorChanged;
    private float m_red;
    private float m_green;
    private float m_blue;
    private int m_sunCycleDuration;
    private float m_lastDayPercentage;
    private boolean m_enable;
    
    public SunLightModifier() {
        super();
        this.m_sunLight = new LightColor(0.0f, 0.0f, 0.0f);
        this.m_enable = false;
    }
    
    public void setEnable(final boolean enable) {
        this.m_enable = enable;
    }
    
    public void setDayPercentDelegate(final DayPercentDelegate delegate) {
        this.m_delegate = delegate;
    }
    
    public void setSunCycleDuration(final int cycleDuration) {
        this.m_sunCycleDuration = cycleDuration;
    }
    
    @Override
    public void onCalendarEvent(final CalendarEventType event, final GameCalendar gameCalendar) {
        switch (event) {
            case CALENDAR_UPDATED: {
                this.setDayTime(gameCalendar);
                break;
            }
        }
    }
    
    public void resetLight() {
        final float n = 1.0f;
        this.m_red = n;
        this.m_targetRed = n;
        final float n2 = 1.0f;
        this.m_green = n2;
        this.m_targetGreen = n2;
        final float n3 = 1.0f;
        this.m_blue = n3;
        this.m_targetBlue = n3;
        this.m_sunLight.set(1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public int getPriority() {
        return 0;
    }
    
    @Override
    public boolean useless() {
        return Engine.isEqualColor(this.m_red, 1.0f) && Engine.isEqualColor(this.m_green, 1.0f) && Engine.isEqualColor(this.m_blue, 1.0f);
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        final int n = 0;
        colors[n] *= this.m_red;
        final int n2 = 1;
        colors[n2] *= this.m_green;
        final int n3 = 2;
        colors[n3] *= this.m_blue;
    }
    
    @Override
    public void update(final int deltaTime) {
        this.m_sunLight.update(deltaTime);
        this.m_red = this.m_sunLight.getRed();
        this.m_green = this.m_sunLight.getGreen();
        this.m_blue = this.m_sunLight.getBlue();
        if (this.m_targetColorChanged) {
            this.m_sunLight.fadeTo(this.m_targetRed, this.m_targetGreen, this.m_targetBlue, 1000);
            this.m_targetColorChanged = false;
        }
        final float globalLight = this.getGlobalLight();
        IsoSceneLightManager.INSTANCE.setLightIntensityFactor(globalLight);
    }
    
    private float getGlobalLight() {
        if (!this.m_enable) {
            return 1.0f;
        }
        final float MIN_INTENSITY = 0.3705f;
        final float coeff = 2.318223f;
        return 2.318223f * this.getDayLightIntensity() * this.getDayLightIntensity() + 1.0f - 2.318223f;
    }
    
    public float getDayLightIntensity() {
        return this.m_red * 0.212671f + this.m_green * 0.71516f + this.m_blue * 0.072169f;
    }
    
    public void readFromXML(final String fileName) throws Exception {
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer xml = accessor.getNewDocumentContainer();
        accessor.open(fileName);
        accessor.read(xml, new DocumentEntryParser[0]);
        accessor.close();
        final ArrayList<DocumentEntry> seasons = xml.getRootNode().getChildrenByName("season");
        if (seasons == null) {
            SunLightModifier.m_logger.error((Object)"Unable to find a season node");
            return;
        }
        for (int numSeasons = seasons.size(), seasonIndex = 0; seasonIndex < numSeasons; ++seasonIndex) {
            final DocumentEntry season = seasons.get(seasonIndex);
            final byte seasonId = season.getParameterByName("id").getByteValue();
            final ArrayList<DocumentEntry> hours = season.getChildrenByName("hour");
            if (hours == null) {
                SunLightModifier.m_logger.error((Object)"Unable to find hour node");
                return;
            }
            for (int numHours = hours.size(), hourIndex = 0; hourIndex < numHours; ++hourIndex) {
                final DocumentEntry hour = hours.get(hourIndex);
                final byte dayPercentage = hour.getParameterByName("dayPercentage").getByteValue();
                final float red = hour.getParameterByName("red").getIntValue() / 255.0f;
                final float green = hour.getParameterByName("green").getIntValue() / 255.0f;
                final float blue = hour.getParameterByName("blue").getIntValue() / 255.0f;
                setHourModifier(seasonId, dayPercentage, red, green, blue);
            }
        }
    }
    
    private static void setHourModifier(final byte season, final byte dayPercentage, final float red, final float green, final float blue) {
        DayLightDefinitionManager definitionsManager = SunLightModifier.m_modifiers.get(season);
        if (definitionsManager == null) {
            definitionsManager = new DayLightDefinitionManager();
            SunLightModifier.m_modifiers.put(season, definitionsManager);
        }
        definitionsManager.addDefinition(dayPercentage, red, green, blue);
    }
    
    private float computeDayPercent(final GameCalendar calendar) {
        if (this.m_delegate == null) {
            return calendar.getDayPercentage();
        }
        return this.m_delegate.getDayPercent(calendar);
    }
    
    public void forceSetDayTime(final GameCalendar calendar) {
        this.setDayTime(calendar);
        this.m_sunLight.set(this.m_targetRed, this.m_targetGreen, this.m_targetBlue);
    }
    
    public void setDayTime(final GameCalendar calendar) {
        if (!calendar.isSynchronized()) {
            return;
        }
        final GameDateConst date = calendar.getDate();
        if (date == null || date.isNull()) {
            SunLightModifier.m_logger.error((Object)"Pas de date d\u00e9finie dans le calendar");
            return;
        }
        final Season season = calendar.getSeason();
        if (season == null) {
            SunLightModifier.m_logger.error((Object)"Pas de saison d\u00e9finie dans le calendar");
            return;
        }
        final DayLightDefinitionManager definitions = SunLightModifier.m_modifiers.get(season.getIndex());
        if (definitions == null) {
            SunLightModifier.m_logger.error((Object)("Saison " + season + " inconnu du DayLightDefinitionManager"));
            return;
        }
        float percentage = this.computeDayPercent(calendar);
        this.m_lastDayPercentage = percentage;
        final DayLightDefinitionManager.DayLightDefinition def = definitions.get((int)percentage);
        final DayLightDefinitionManager.DayLightDefinition nextDef = definitions.getNext(def);
        final int prevPercentage = def.getDayPercentage();
        int nextPercentage = nextDef.getDayPercentage();
        if (percentage < prevPercentage) {
            percentage += 100.0f;
        }
        if (percentage > nextPercentage) {
            nextPercentage += 100;
        }
        final float amount = (percentage - prevPercentage) / (nextPercentage - prevPercentage);
        if (this.m_enable) {
            this.m_targetRed = MathHelper.lerp(def.getRed(), nextDef.getRed(), amount);
            this.m_targetGreen = MathHelper.lerp(def.getGreen(), nextDef.getGreen(), amount);
            this.m_targetBlue = MathHelper.lerp(def.getBlue(), nextDef.getBlue(), amount);
            this.m_targetColorChanged = true;
        }
        else {
            final float targetRed = 1.0f;
            this.m_targetBlue = targetRed;
            this.m_targetGreen = targetRed;
            this.m_targetRed = targetRed;
            this.m_targetColorChanged = true;
            this.m_sunLight.set(0.0f, 0.0f, 0.0f);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SunLightModifier.class);
        INSTANCE = new SunLightModifier();
        SunLightModifier.m_modifiers = new TByteObjectHashMap<DayLightDefinitionManager>();
    }
    
    public interface DayPercentDelegate
    {
        float getDayPercent(GameCalendar p0);
    }
}
