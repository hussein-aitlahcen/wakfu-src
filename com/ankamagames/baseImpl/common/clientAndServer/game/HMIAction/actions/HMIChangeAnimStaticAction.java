package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIChangeAnimStaticAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_animStaticKey;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 1) {
                HMIChangeAnimStaticAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 1 : Animstatix) : " + parameters));
                return false;
            }
            this.m_animStaticKey = array[0];
            return true;
        }
        catch (NumberFormatException e) {
            HMIChangeAnimStaticAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.ANIM_STATIC_CHANGE;
    }
    
    public String getAnimStaticKey() {
        return this.m_animStaticKey;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
