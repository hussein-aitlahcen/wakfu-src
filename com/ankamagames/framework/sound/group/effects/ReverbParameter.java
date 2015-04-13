package com.ankamagames.framework.sound.group.effects;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import gnu.trove.*;

public class ReverbParameter
{
    private static final Logger m_logger;
    public static final String DENSITY = "reverbdensity";
    public static final String GAIN = "reverbgain";
    public static final String DECAYTIME = "decaytime";
    public static final String DECAYLPF = "decayLPF";
    public static final String EARLYGAIN = "earlygain";
    public static final String PREDELAY = "predelay";
    private float m_reverbDensity;
    private float m_reverbGain;
    private float m_reverbDecayTime;
    private float m_decayLPF;
    private float m_earlyGain;
    private float m_preDelay;
    
    private ReverbParameter() {
        super();
        this.m_reverbDensity = 1.0f;
        this.m_reverbGain = 0.32f;
        this.m_reverbDecayTime = 1.49f;
        this.m_decayLPF = 0.83f;
        this.m_earlyGain = 1.26f;
        this.m_preDelay = 0.011f;
    }
    
    public ReverbParameter(final float reverbDensity, final float reverbGain, final float reverbDecayTime, final float decayLPF, final float earlyGain, final float preDelay) {
        super();
        this.m_reverbDensity = 1.0f;
        this.m_reverbGain = 0.32f;
        this.m_reverbDecayTime = 1.49f;
        this.m_decayLPF = 0.83f;
        this.m_earlyGain = 1.26f;
        this.m_preDelay = 0.011f;
        this.m_reverbDensity = reverbDensity;
        this.m_reverbGain = reverbGain;
        this.m_reverbDecayTime = reverbDecayTime;
        this.m_decayLPF = decayLPF;
        this.m_earlyGain = earlyGain;
        this.m_preDelay = preDelay;
    }
    
    public static ReverbParameter createFromXML(final DocumentEntry xml) {
        final ReverbParameter param = new ReverbParameter();
        for (final DocumentEntry entry : xml.getChildren()) {
            if (entry.getName().equals("reverbdensity")) {
                param.m_reverbDensity = MathHelper.clamp(entry.getParameterByName("value").getFloatValue(), 0.0f, 1.0f);
            }
            else if (entry.getName().equals("reverbgain")) {
                param.m_reverbGain = MathHelper.clamp(entry.getParameterByName("value").getFloatValue(), 0.0f, 1.0f);
            }
            else if (entry.getName().equals("decaytime")) {
                param.m_reverbDecayTime = MathHelper.clamp(entry.getParameterByName("value").getFloatValue(), 0.1f, 20.0f);
            }
            else if (entry.getName().equals("decayLPF")) {
                param.m_decayLPF = MathHelper.clamp(entry.getParameterByName("value").getFloatValue(), 0.1f, 2.0f);
            }
            else if (entry.getName().equals("earlygain")) {
                param.m_earlyGain = MathHelper.clamp(entry.getParameterByName("value").getFloatValue(), 0.0f, 10.0f);
            }
            else {
                if (!entry.getName().equals("predelay")) {
                    continue;
                }
                param.m_preDelay = MathHelper.clamp(entry.getParameterByName("value").getFloatValue(), 0.0f, 0.1f);
            }
        }
        return param;
    }
    
    public static TIntObjectHashMap<ReverbParameter> createListFromXML(final DocumentEntry xml) {
        final TIntObjectHashMap<ReverbParameter> list = new TIntObjectHashMap<ReverbParameter>();
        for (final DocumentEntry entry : xml.getChildren()) {
            if (entry.getName().equals("reverb")) {
                list.put(entry.getParameterByName("id").getIntValue(), createFromXML(entry));
            }
        }
        return list;
    }
    
    public float getReverbDensity() {
        return this.m_reverbDensity;
    }
    
    public float getReverbGain() {
        return this.m_reverbGain;
    }
    
    public float getReverbDecayTime() {
        return this.m_reverbDecayTime;
    }
    
    public float getDecayLPF() {
        return this.m_decayLPF;
    }
    
    public float getEarlyGain() {
        return this.m_earlyGain;
    }
    
    public float getPreDelay() {
        return this.m_preDelay;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReverbParameter.class);
    }
}
