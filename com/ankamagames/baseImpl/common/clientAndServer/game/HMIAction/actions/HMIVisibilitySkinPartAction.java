package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIVisibilitySkinPartAction extends HMIAction
{
    private static final Logger m_logger;
    private boolean m_visibility;
    private String[] m_parts;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length < 2) {
                HMIVisibilitySkinPartAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut au moins 2 : visibility[true|false];parts....) : " + parameters));
                return false;
            }
            this.m_visibility = Boolean.parseBoolean(array[0]);
            System.arraycopy(array, 1, this.m_parts = new String[array.length - 1], 0, this.m_parts.length);
            return true;
        }
        catch (NumberFormatException e) {
            HMIVisibilitySkinPartAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.SKIN_PART_VISIBILITY;
    }
    
    public boolean getVisibility() {
        return this.m_visibility;
    }
    
    public String[] getParts() {
        return this.m_parts;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
