package com.ankamagames.framework.sound.group.effects;

import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import gnu.trove.*;

public class LowPassParameter
{
    public static final LowPassParameter DEFAULT;
    public static final String GAIN = "gain";
    public static final String GAIN_HF = "gainHF";
    private float m_gain;
    private float m_gainHF;
    
    private LowPassParameter() {
        super();
        this.m_gain = 0.5f;
        this.m_gainHF = 0.5f;
    }
    
    public LowPassParameter(final float gain, final float gainHF) {
        super();
        this.m_gain = 0.5f;
        this.m_gainHF = 0.5f;
        this.m_gain = gain;
        this.m_gainHF = gainHF;
    }
    
    public static LowPassParameter createFromXML(final DocumentEntry xml) {
        final LowPassParameter param = new LowPassParameter();
        for (final DocumentEntry entry : xml.getChildren()) {
            if (entry.getName().equals("gain")) {
                param.m_gain = entry.getParameterByName("value").getFloatValue();
            }
            else {
                if (!entry.getName().equals("gainHF")) {
                    continue;
                }
                param.m_gainHF = entry.getParameterByName("value").getFloatValue();
            }
        }
        return param;
    }
    
    public static TIntObjectHashMap<LowPassParameter> createListFromXML(final DocumentEntry xml) {
        final TIntObjectHashMap<LowPassParameter> list = new TIntObjectHashMap<LowPassParameter>();
        for (final DocumentEntry entry : xml.getChildren()) {
            if (entry.getName().equals("lowpass")) {
                list.put(entry.getParameterByName("id").getIntValue(), createFromXML(entry));
            }
        }
        return list;
    }
    
    public float getGain() {
        return this.m_gain;
    }
    
    public float getGainHF() {
        return this.m_gainHF;
    }
    
    static {
        DEFAULT = new LowPassParameter(0.5f, 0.5f);
    }
}
