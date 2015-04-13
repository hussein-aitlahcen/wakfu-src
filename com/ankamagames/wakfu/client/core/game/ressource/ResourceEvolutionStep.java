package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.world.*;

public class ResourceEvolutionStep extends AbstractResourceEvolutionStep<CollectAction>
{
    private final ResourceSizeCategory m_sizeCategory;
    private final float[] m_lightIntensity;
    private final float m_lightRange;
    
    public ResourceEvolutionStep(final byte stepIndex, final ResourceSizeCategory sizeCategory, final float[] lightIntensity, final float lightRange) {
        super(stepIndex);
        this.m_sizeCategory = sizeCategory;
        this.m_lightIntensity = lightIntensity;
        this.m_lightRange = lightRange;
    }
    
    public ResourceSizeCategory getSizeCategory() {
        return this.m_sizeCategory;
    }
    
    public float[] getLightIntensity() {
        return this.m_lightIntensity;
    }
    
    public float getLightRange() {
        return this.m_lightRange;
    }
    
    public boolean hasLight() {
        return this.m_lightIntensity != null;
    }
}
