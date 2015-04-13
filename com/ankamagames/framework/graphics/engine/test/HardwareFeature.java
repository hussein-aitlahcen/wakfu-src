package com.ankamagames.framework.graphics.engine.test;

import java.util.*;

public enum HardwareFeature
{
    GL_RENDER_TARGET("GL render targets", "renderTargetsSupported"), 
    GL_MULTI_SAMPLING("GL multi-sampling", "multiSamplingSupported"), 
    GL_MULTI_TEXTURING("GL multi-texturing", "multiTexturingSupported"), 
    GL_TEXTURE_COMPRESSION("GL texture compression", "textureCompressionSupported"), 
    GL_FRAGMENT_SHADERS("GL fragment shaders", "fragmentShadersSupported"), 
    GL_VERTEX_SHADERS("GL vertex shaders", "vertexShadersSupported"), 
    GL_TEXTURE_NON_POWER_OF_TWO("GL texture non power of two", "textureNonPowerOfTwo"), 
    AL_EFFECT("AL effects", "alEffectsSupported"), 
    AL_FILTER("AL filters", "alFiltersSupported");
    
    private static final HashMap<String, HardwareFeature> m_featuresByProperty;
    private final String m_featureName;
    private final String m_propertyName;
    
    private HardwareFeature(final String featureName, final String propertyName) {
        this.m_featureName = featureName;
        this.m_propertyName = propertyName;
    }
    
    public String getFeatureName() {
        return this.m_featureName;
    }
    
    public String getPropertyName() {
        return this.m_propertyName;
    }
    
    public static HardwareFeature getFeatureByPropertyName(final String propertyName) {
        return HardwareFeature.m_featuresByProperty.get(propertyName);
    }
    
    static {
        m_featuresByProperty = new HashMap<String, HardwareFeature>();
        for (final HardwareFeature feature : values()) {
            HardwareFeature.m_featuresByProperty.put(feature.getPropertyName(), feature);
        }
    }
}
