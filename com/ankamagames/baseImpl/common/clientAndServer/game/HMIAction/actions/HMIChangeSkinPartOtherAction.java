package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIChangeSkinPartOtherAction extends HMIAction
{
    private static final Logger m_logger;
    private String m_appearanceId;
    private String[] m_partsToChange;
    private int m_weight;
    
    @Override
    protected boolean initialize(String parameters) {
        try {
            final boolean allParts = parameters.endsWith("!");
            if (allParts) {
                parameters = parameters.substring(0, parameters.length() - 1);
            }
            final String[] array = StringUtils.split(parameters, ';');
            if (array != null && array.length >= 1) {
                this.m_appearanceId = array[0].intern();
                final int offset = 1 + this.tryGetWeight(array);
                this.m_partsToChange = (String[])(allParts ? null : getPartsToChange(array, offset));
                return true;
            }
            HMIChangeSkinPartOtherAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut au moins 2 : AppearanceId;parts...., ou un seul, mais avec un ! \u00e0 la fin) : " + parameters));
            return false;
        }
        catch (NumberFormatException e) {
            HMIChangeSkinPartOtherAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    private static String[] getPartsToChange(final String[] array, final int offset) {
        final String[] partsToChange = new String[array.length - offset];
        for (int i = 0; i < partsToChange.length; ++i) {
            final String s = array[i + offset];
            partsToChange[i] = ((s != null) ? s.intern() : null);
        }
        return partsToChange;
    }
    
    private int tryGetWeight(final String[] array) {
        if (array.length <= 1) {
            return 0;
        }
        try {
            this.m_weight = Integer.parseInt(array[1].replace("!", ""));
            return 1;
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.SKIN_PART_OTHER_CHANGE;
    }
    
    public String getAppearanceId() {
        return this.m_appearanceId;
    }
    
    public String[] getPartsToChange() {
        return this.m_partsToChange;
    }
    
    public int getWeight() {
        return this.m_weight;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
