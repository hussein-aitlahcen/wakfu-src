package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMICameraShakeAction extends HMIAction
{
    private static final Logger m_logger;
    private int m_startDuration;
    private int m_midDuration;
    private int m_endDuration;
    private float m_amplitude;
    private float m_period;
    private String m_direction;
    
    public HMICameraShakeAction() {
        super();
        this.m_startDuration = 1000;
        this.m_midDuration = 2000;
        this.m_endDuration = 1000;
        this.m_amplitude = 0.5f;
        this.m_period = 77.0f;
        this.m_direction = "BOTH";
    }
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final String[] array = StringUtils.split(parameters, ';');
            if (array == null || array.length > 6) {
                HMICameraShakeAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", pas assez de param\u00e8tres (il en faut au maximum: startDuration, midDuration, endDuration, amplitude, period, [X,Y,BOTH]) : " + parameters));
                return false;
            }
            final int paramCount = array.length;
            if (paramCount >= 1) {
                this.m_startDuration = (int)Float.parseFloat(array[0]);
            }
            if (paramCount >= 2) {
                this.m_midDuration = (int)Float.parseFloat(array[1]);
            }
            if (paramCount >= 3) {
                this.m_endDuration = (int)Float.parseFloat(array[2]);
            }
            if (paramCount >= 4) {
                this.m_amplitude = Float.parseFloat(array[3]);
            }
            if (paramCount == 5) {
                this.m_period = Float.parseFloat(array[4]);
            }
            if (paramCount == 6) {
                this.m_direction = array[5].toUpperCase();
                if (this.m_direction.equals("XY")) {
                    this.m_direction = "BOTH";
                }
            }
            return true;
        }
        catch (NumberFormatException e) {
            HMICameraShakeAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.CAMERA_SHAKE;
    }
    
    public int getStartDuration() {
        return this.m_startDuration;
    }
    
    public int getMidDuration() {
        return this.m_midDuration;
    }
    
    public int getEndDuration() {
        return this.m_endDuration;
    }
    
    public float getAmplitude() {
        return this.m_amplitude;
    }
    
    public float getPeriod() {
        return this.m_period;
    }
    
    public String getDirection() {
        return this.m_direction;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMICameraShakeAction.class);
    }
}
