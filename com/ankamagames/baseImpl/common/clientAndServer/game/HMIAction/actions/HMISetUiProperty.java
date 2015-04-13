package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMISetUiProperty extends HMIAction
{
    private static final Logger m_logger;
    private String m_uiObjectId;
    private String m_propertyName;
    private String m_propertyValue;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 3) {
                HMISetUiProperty.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 3) : " + parameters));
                return false;
            }
            this.m_uiObjectId = array[0];
            this.m_propertyName = array[1];
            this.m_propertyValue = array[2];
            return true;
        }
        catch (NumberFormatException e) {
            HMISetUiProperty.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.SET_UI_PROPERTY;
    }
    
    public String getUiObjectId() {
        return this.m_uiObjectId;
    }
    
    public String getPropertyName() {
        return this.m_propertyName;
    }
    
    public String getPropertyValue() {
        return this.m_propertyValue;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
