package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIChangeAppearanceAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_appearanceId;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 1) {
                HMIChangeAppearanceAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvais nombre de param\u00e8tres (il en faut 1 : AppearanceId) : " + parameters));
                return false;
            }
            this.m_appearanceId = array[0];
            return true;
        }
        catch (NumberFormatException e) {
            HMIChangeAppearanceAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.APPEARANCE_CHANGE;
    }
    
    public String getAppearanceId() {
        return this.m_appearanceId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
