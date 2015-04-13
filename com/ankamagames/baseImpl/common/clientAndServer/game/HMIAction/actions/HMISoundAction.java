package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMISoundAction extends HMIAction
{
    protected static final Logger m_logger;
    private long m_soundId;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            this.m_soundId = Long.parseLong(parameters);
            return true;
        }
        catch (NumberFormatException e) {
            HMISoundAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + " : " + parameters + " n'est pas un id de son valide"));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.SOUND;
    }
    
    public long getSoundId() {
        return this.m_soundId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMISoundAction.class);
    }
}
