package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMILightSourceAction extends HMIAction
{
    private static final Logger m_logger;
    private final float[] m_intensity;
    private float m_range;
    
    public HMILightSourceAction() {
        super();
        this.m_intensity = new float[3];
    }
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 4) {
                HMILightSourceAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 4 : R G B Range) : " + parameters));
                return false;
            }
            this.m_intensity[0] = Float.parseFloat(array[0]);
            this.m_intensity[1] = Float.parseFloat(array[1]);
            this.m_intensity[2] = Float.parseFloat(array[2]);
            this.m_range = Float.parseFloat(array[3]);
            return true;
        }
        catch (NumberFormatException e) {
            HMILightSourceAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.LIGHT_SOURCE;
    }
    
    public float[] getIntensity() {
        return this.m_intensity;
    }
    
    public float getRange() {
        return this.m_range;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
