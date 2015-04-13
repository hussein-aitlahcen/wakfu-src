package com.ankamagames.framework.sound.group.field;

import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;

public class RollOffParameter
{
    public static final RollOffParameter DEFAULT;
    private float m_rollOffFactor;
    private float m_refDistance;
    private float m_maxDistance;
    public static final String ROLL_OFF_FACTOR = "rollOffFactor";
    public static final String REF_DISTANCE = "refDistance";
    public static final String MAX_DISTANCE = "maxDistance";
    
    public RollOffParameter() {
        super();
    }
    
    public RollOffParameter(final float rollOffFactor, final float refDistance, final float maxDistance) {
        super();
        this.m_rollOffFactor = rollOffFactor;
        this.m_refDistance = refDistance;
        this.m_maxDistance = maxDistance;
    }
    
    public float getRollOffFactor() {
        return this.m_rollOffFactor;
    }
    
    public float getRefDistance() {
        return this.m_refDistance;
    }
    
    public float getMaxDistance() {
        return this.m_maxDistance;
    }
    
    public static RollOffParameter createFromXML(final DocumentEntry xml) {
        final RollOffParameter param = new RollOffParameter();
        for (final DocumentEntry entry : xml.getChildren()) {
            if (entry.getName().equals("rollOffFactor")) {
                param.m_rollOffFactor = entry.getParameterByName("value").getFloatValue();
            }
            else if (entry.getName().equals("refDistance")) {
                param.m_refDistance = entry.getParameterByName("value").getFloatValue();
            }
            else {
                if (!entry.getName().equals("maxDistance")) {
                    continue;
                }
                param.m_maxDistance = entry.getParameterByName("value").getFloatValue();
            }
        }
        return param;
    }
    
    public static IntObjectLightWeightMap<RollOffParameter> createListFromXML(final DocumentEntry xml) {
        final IntObjectLightWeightMap<RollOffParameter> list = new IntObjectLightWeightMap<RollOffParameter>();
        for (final DocumentEntry entry : xml.getChildren()) {
            if (entry.getName().equals("rollOff")) {
                list.put(entry.getParameterByName("id").getIntValue(), createFromXML(entry));
            }
        }
        return list;
    }
    
    static {
        DEFAULT = new RollOffParameter(1.0f, 10.0f, 18.0f);
    }
}
