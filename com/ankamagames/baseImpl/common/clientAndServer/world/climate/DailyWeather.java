package com.ankamagames.baseImpl.common.clientAndServer.world.climate;

import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;
import java.util.*;

public class DailyWeather
{
    private final int m_dayOfMonth;
    private float m_precipitations;
    private float m_windForce;
    private float m_tMin;
    private float m_tMax;
    private final float[] m_rainEventDurations;
    private final float[] m_rainEventIntensity;
    private ArrayList<float[]> m_rainEventSchedule;
    private final float[] m_winds;
    private final Random m_random;
    private int m_randomSeed;
    private float m_currentTemperature;
    private float[] m_currentRainEventSchedule;
    private float m_currentWind;
    private ArrayList<WeatherModifier> m_modifiers;
    private float m_modifiersPrecipitations;
    private float m_modifierWind;
    private float m_modifierTemperature;
    
    public DailyWeather(final int dayOfMonth) {
        super();
        this.m_rainEventDurations = new float[6];
        this.m_rainEventIntensity = new float[6];
        this.m_winds = new float[6];
        this.m_random = new Random(0L);
        this.m_dayOfMonth = dayOfMonth;
    }
    
    public void addModifier(final WeatherModifier modifier, final Climate climate) {
        if (this.m_modifiers == null) {
            this.m_modifiers = new ArrayList<WeatherModifier>();
        }
        else if (this.m_modifiers.contains(modifier)) {
            return;
        }
        this.m_modifiers.add(modifier);
        this.m_modifiersPrecipitations += modifier.precipitations();
        final float climateTMax = climate.getTMax();
        final float climateTMin = climate.getTMin();
        final float tMod = modifier.temperature();
        this.m_modifierTemperature = MathHelper.clamp(this.m_modifierTemperature + tMod, climateTMin - this.m_tMin, climateTMax - this.m_tMax);
        this.m_modifierWind += modifier.wind();
    }
    
    public void setModifier(final WeatherModifier modifier, final Climate climate) {
        if (this.m_modifiers == null) {
            this.m_modifiers = new ArrayList<WeatherModifier>();
        }
        else if (this.m_modifiers.contains(modifier)) {
            return;
        }
        this.m_modifiers.clear();
        this.m_modifiersPrecipitations = 0.0f;
        this.m_modifierTemperature = 0.0f;
        this.m_modifierWind = 0.0f;
        this.m_modifiers.add(modifier);
        this.m_modifiersPrecipitations += modifier.precipitations();
        final float climateTMax = climate.getTMax();
        final float climateTMin = climate.getTMin();
        final float tMod = modifier.temperature();
        this.m_modifierTemperature = MathHelper.clamp(this.m_modifierTemperature + tMod, climateTMin - this.m_tMin, climateTMax - this.m_tMax);
        this.m_modifierWind += modifier.wind();
    }
    
    public ArrayList<WeatherModifier> getModifiers() {
        return this.m_modifiers;
    }
    
    public void setRandomSeed(final int randomSeed) {
        this.m_randomSeed = randomSeed;
    }
    
    public int getDayOfMonth() {
        return this.m_dayOfMonth;
    }
    
    public float getPrecipitations() {
        return this.m_precipitations;
    }
    
    public void setPrecipitations(final float precipitations) {
        this.m_precipitations = precipitations;
    }
    
    public float getWindForce() {
        return this.m_windForce;
    }
    
    public void setWindForce(final float windForce) {
        this.m_windForce = windForce;
    }
    
    public float getTMin() {
        return this.m_tMin;
    }
    
    public void setTMin(final float tMin) {
        this.m_tMin = tMin;
    }
    
    public float getTMax() {
        return this.m_tMax;
    }
    
    public void setTMax(final float tMax) {
        this.m_tMax = tMax;
    }
    
    public byte getCurrentRainEventStartTime() {
        if (this.m_currentRainEventSchedule != null) {
            return (byte)(this.m_currentRainEventSchedule[0] * 6.0f);
        }
        return -1;
    }
    
    public short getCurrentRainEventDuration() {
        if (this.m_currentRainEventSchedule != null) {
            return (short)this.m_currentRainEventSchedule[1];
        }
        return 0;
    }
    
    public float getCurrentRainEventIntensity() {
        if (this.m_currentRainEventSchedule != null) {
            return this.m_currentRainEventSchedule[2];
        }
        return 0.0f;
    }
    
    public float getCurrentTemperature() {
        return this.m_currentTemperature;
    }
    
    public void setCurrentTemperature(final float currentTemperature) {
        this.m_currentTemperature = currentTemperature;
    }
    
    public float getCurrentWind() {
        return this.m_currentWind;
    }
    
    public float getModifiersPrecipitations() {
        return this.m_modifiersPrecipitations;
    }
    
    public float getModifierWind() {
        return this.m_modifierWind;
    }
    
    public float getModifierTemperature() {
        return this.m_modifierTemperature;
    }
    
    public void generateRainEvents() {
        this.m_random.setSeed(this.m_randomSeed);
        final TIntArrayList candidates = new TIntArrayList();
        for (int i = 0; i < this.m_rainEventDurations.length; ++i) {
            candidates.add(i);
            this.m_rainEventDurations[i] = (this.m_rainEventIntensity[i] = 0.0f);
        }
        final float p = this.m_precipitations + this.m_modifiersPrecipitations;
        final float precipitations = Math.max(Math.min(p, 1.0f), 0.0f);
        final int numEvents = (int)Math.floor(precipitations * this.m_rainEventDurations.length);
        final float q = p / numEvents;
        float s = 0.0f;
        for (int j = 0; j < numEvents; ++j) {
            final int k = candidates.remove((int)(this.m_random.nextFloat() * candidates.size()));
            this.m_rainEventDurations[k] = 3600.0f;
            final float r = (p - q) * this.m_random.nextFloat();
            this.m_rainEventIntensity[k] = p - r;
            s += this.m_rainEventIntensity[k];
        }
        this.m_rainEventSchedule = new ArrayList<float[]>();
        if (numEvents > 0) {
            final float n = precipitations / s;
            for (int l = 0; l < this.m_rainEventDurations.length; ++l) {
                if (this.m_rainEventIntensity[l] > 0.0f) {
                    this.m_rainEventSchedule.add(new float[] { l / 6.0f, this.m_rainEventDurations[l], this.m_rainEventIntensity[l] });
                }
            }
        }
    }
    
    public void generateWinds(final float wMin, final float wMax) {
        this.m_random.setSeed(this.m_randomSeed);
        for (int i = 0; i < this.m_winds.length; ++i) {
            final float w = wMin + this.m_random.nextFloat() * (wMax - wMin) + this.m_modifierWind;
            this.m_winds[i] = MathHelper.clamp(w, wMin, wMax);
        }
    }
    
    public float getRainEventDurations(final float dayRatio) {
        final int i = (int)Math.floor(dayRatio * this.m_rainEventDurations.length);
        return this.m_rainEventDurations[i];
    }
    
    public float getRainEventIntensity(final float dayRatio) {
        final int i = (int)Math.floor(dayRatio * this.m_rainEventIntensity.length);
        return this.m_rainEventIntensity[i];
    }
    
    protected void initializeCurrentParameters(final float dayRatio) {
        final int h = (int)Math.floor(dayRatio * (this.m_winds.length - 1));
        this.m_currentWind = this.m_winds[h];
        final float d = 0.16666667f;
        for (final float[] schedule : this.m_rainEventSchedule) {
            if (dayRatio >= schedule[0] && dayRatio < schedule[0] + 0.16666667f) {
                this.m_currentRainEventSchedule = schedule;
                return;
            }
        }
        this.m_currentRainEventSchedule = null;
    }
    
    @Override
    public String toString() {
        return "DailyWeather{m_dayOfMonth=" + this.m_dayOfMonth + ", m_precipitations=" + this.m_precipitations + ", m_windForce=" + this.m_windForce + ", m_tMin=" + this.m_tMin + ", m_tMax=" + this.m_tMax + ", m_rainEventDurations=" + Arrays.toString(this.m_rainEventDurations) + ", m_rainEventIntensity=" + Arrays.toString(this.m_rainEventIntensity) + ", m_rainEventSchedule=" + this.m_rainEventSchedule + ", m_winds=" + Arrays.toString(this.m_winds) + ", m_random=" + this.m_random + ", m_randomSeed=" + this.m_randomSeed + ", m_currentTemperature=" + this.m_currentTemperature + ", m_currentRainEventSchedule=" + Arrays.toString(this.m_currentRainEventSchedule) + ", m_currentWind=" + this.m_currentWind + ", m_modifiers=" + this.m_modifiers + ", m_modifiersPrecipitations=" + this.m_modifiersPrecipitations + ", m_modifierWind=" + this.m_modifierWind + ", m_modifierTemperature=" + this.m_modifierTemperature + '}';
    }
}
