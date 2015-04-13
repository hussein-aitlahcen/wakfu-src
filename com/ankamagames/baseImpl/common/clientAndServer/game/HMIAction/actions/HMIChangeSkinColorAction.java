package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIChangeSkinColorAction extends HMIAction
{
    private static final Logger m_logger;
    private float[] m_color;
    private String m_partName;
    
    public HMIChangeSkinColorAction() {
        super();
        this.m_color = new float[4];
    }
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || (array.length != 4 && array.length != 5)) {
                HMIChangeSkinColorAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut 5 : R;G;B;A;partName (couleur entre [0-1]) : " + parameters));
                return false;
            }
            this.m_partName = array[0];
            final float r = Float.parseFloat(array[1]);
            final float g = Float.parseFloat(array[2]);
            final float b = Float.parseFloat(array[3]);
            final float a = (array.length == 4) ? 1.0f : Float.parseFloat(array[4]);
            this.m_color = new float[] { r * a, g * a, b * a, a };
            return true;
        }
        catch (NumberFormatException e) {
            HMIChangeSkinColorAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.SKIN_COLOR_CHANGE;
    }
    
    public float[] getColor() {
        return this.m_color;
    }
    
    public String getPartName() {
        return this.m_partName;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
