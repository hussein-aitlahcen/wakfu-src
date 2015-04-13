package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public final class HMIChangeAnimAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_animName;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 1) {
                HMIChangeAnimAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 1 : Animstatix) : " + parameters));
                return false;
            }
            this.m_animName = array[0];
            return true;
        }
        catch (NumberFormatException e) {
            HMIChangeAnimAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.ANIM_CHANGE;
    }
    
    public String getAnimName() {
        return this.m_animName;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIChangeAnimAction.class);
    }
}
