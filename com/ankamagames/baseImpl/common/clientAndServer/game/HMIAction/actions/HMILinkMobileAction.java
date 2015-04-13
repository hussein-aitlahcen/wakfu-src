package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMILinkMobileAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_gfxId;
    private String m_anim;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || (array.length != 1 && array.length != 2)) {
                HMILinkMobileAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 2 : AppearanceId;AnimName(facultatif)) : " + parameters));
                return false;
            }
            this.m_gfxId = array[0];
            if (array.length == 2) {
                this.m_anim = array[1];
            }
            return true;
        }
        catch (Exception e) {
            HMILinkMobileAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + " : " + parameters + " n'est pas valide"));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.LINK_MOBILE;
    }
    
    public String getGfxId() {
        return this.m_gfxId;
    }
    
    public String getAnim() {
        return this.m_anim;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMILinkMobileAction.class);
    }
}
