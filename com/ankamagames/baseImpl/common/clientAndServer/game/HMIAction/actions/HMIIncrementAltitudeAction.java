package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIIncrementAltitudeAction extends HMIAction
{
    private static final Logger m_logger;
    private float m_deltaAltitude;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 1) {
                HMIIncrementAltitudeAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 1 : deltaAltitude) : " + parameters));
                return false;
            }
            this.m_deltaAltitude = Float.parseFloat(array[0]);
            return true;
        }
        catch (Exception e) {
            HMIIncrementAltitudeAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + " : " + parameters + " n'est pas valide"));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.INCREMENT_ALTITUDE;
    }
    
    public float getDeltaAltitude() {
        return this.m_deltaAltitude;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIIncrementAltitudeAction.class);
    }
}
