package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public final class HMIChangeMovementStyle extends HMIAction
{
    private static final Logger m_logger;
    private String m_walkStyle;
    private String m_runStyle;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 2) {
                HMIChangeMovementStyle.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 1 : deltaAltitude) : " + parameters));
                return false;
            }
            this.m_walkStyle = array[0];
            this.m_runStyle = array[1];
            return true;
        }
        catch (Exception e) {
            HMIChangeMovementStyle.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + " : " + parameters + " n'est pas valide"));
            return false;
        }
    }
    
    public String getWalkStyle() {
        return this.m_walkStyle;
    }
    
    public String getRunStyle() {
        return this.m_runStyle;
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.CHANGE_MOVEMENT_STYLE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIChangeMovementStyle.class);
    }
}
