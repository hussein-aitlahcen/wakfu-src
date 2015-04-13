package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMISetMonsterSkinAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_monsterId;
    private boolean m_displayEquipment;
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length != 2) {
                HMISetMonsterSkinAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 2 : monsterBreedId, displayEquipment) : " + parameters));
                return false;
            }
            this.m_monsterId = array[0];
            this.m_displayEquipment = PrimitiveConverter.getBoolean(array[1]);
            return true;
        }
        catch (NumberFormatException e) {
            HMISetMonsterSkinAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.SET_MONSTER_SKIN;
    }
    
    public boolean isDisplayEquipment() {
        return this.m_displayEquipment;
    }
    
    public String getMonsterId() {
        return this.m_monsterId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
