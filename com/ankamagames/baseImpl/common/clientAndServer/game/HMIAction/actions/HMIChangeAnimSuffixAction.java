package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIChangeAnimSuffixAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_animSuffix;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 1) {
                HMIChangeAnimSuffixAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 1 : AnimSuffix) : " + parameters));
                return false;
            }
            this.m_animSuffix = array[0];
            return true;
        }
        catch (NumberFormatException e) {
            HMIChangeAnimSuffixAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.ANIM_SUFFIX_CHANGE;
    }
    
    public String getAnimSuffix() {
        return this.m_animSuffix;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
