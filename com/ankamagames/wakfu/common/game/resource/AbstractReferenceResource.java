package com.ankamagames.wakfu.common.game.resource;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey.*;
import java.util.*;

public class AbstractReferenceResource<TStep extends AbstractResourceEvolutionStep>
{
    private final int m_id;
    protected short m_resourceType;
    private final float m_idealRainMin;
    private final float m_idealRainMax;
    private final short m_idealTemperatureMin;
    private final short m_idealTemperatureMax;
    protected boolean m_isBlocking;
    private final EnumSet<ResourcesProperty> m_properties;
    private final ByteObjectLightWeightMap<TStep> m_evolutionSteps;
    public static final byte WAIT_FOR_RESPAWN = 16;
    public static final byte STATE_DYING = 0;
    
    public AbstractReferenceResource(final int id, final short resourceType, final short idealTempMin, final short idealTempMax, final short idealRainMin, final short idealRainMax, final boolean resourceIsBlocking) {
        super();
        this.m_properties = EnumSet.noneOf(ResourcesProperty.class);
        this.m_evolutionSteps = new ByteObjectLightWeightMap<TStep>();
        this.m_id = id;
        this.m_resourceType = resourceType;
        this.m_isBlocking = resourceIsBlocking;
        this.m_idealTemperatureMin = idealTempMin;
        this.m_idealTemperatureMax = idealTempMax;
        this.m_idealRainMin = idealRainMin / 100.0f;
        this.m_idealRainMax = idealRainMax / 100.0f;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void addProperty(final ResourcesProperty prop) {
        this.m_properties.add(prop);
    }
    
    public void addProperties(final Collection<ResourcesProperty> props) {
        this.m_properties.addAll((Collection<?>)props);
    }
    
    public void removeProperty(final ResourcesProperty prop) {
        this.m_properties.remove(prop);
    }
    
    public void removeProperties(final Collection<ResourcesProperty> props) {
        this.m_properties.removeAll(props);
    }
    
    public boolean hasProperty(final ResourcesProperty prop) {
        return this.m_properties.contains(prop);
    }
    
    public Iterator<ResourcesProperty> getProperties() {
        return this.m_properties.iterator();
    }
    
    public short getResourceType() {
        return this.m_resourceType;
    }
    
    public boolean isBlockingMovement() {
        return this.m_isBlocking;
    }
    
    public void addEvolveStep(final TStep step) {
        this.m_evolutionSteps.put(step.getStepIndex(), step);
    }
    
    public final TStep getQuickEvolutionStep(final int index) {
        return this.m_evolutionSteps.getQuickValue(index);
    }
    
    public final int getEvolutionStepCount() {
        return this.m_evolutionSteps.size();
    }
    
    public void ensureEvolutionStepCapacity(final int capacity) {
        this.m_evolutionSteps.ensureCapacity(capacity);
    }
    
    public final TStep getEvolutionStep(final byte id) {
        if (this.m_evolutionSteps.size() == 0) {
            return null;
        }
        return this.m_evolutionSteps.get(id);
    }
    
    public float getIdealRainMin() {
        return this.m_idealRainMin;
    }
    
    public float getIdealRainMax() {
        return this.m_idealRainMax;
    }
    
    public short getIdealTemperatureMin() {
        return this.m_idealTemperatureMin;
    }
    
    public short getIdealTemperatureMax() {
        return this.m_idealTemperatureMax;
    }
    
    public short getExtendedTemperatureMin() {
        return (short)(this.m_idealTemperatureMin - (this.m_idealTemperatureMax - this.m_idealTemperatureMin) / 2);
    }
    
    public short getExtendedTemperatureMax() {
        return (short)(this.m_idealTemperatureMax + (this.m_idealTemperatureMax - this.m_idealTemperatureMin) / 2);
    }
}
