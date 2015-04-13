package com.ankamagames.wakfu.common.game.wakfu;

import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class WakfuGauge
{
    private int m_actionValue;
    private float m_userFriendlyValue;
    private final List<WakfuGaugeListener> m_listeners;
    
    public WakfuGauge() {
        this(0);
    }
    
    public WakfuGauge(final int actionValue) {
        super();
        this.m_listeners = new ArrayList<WakfuGaugeListener>();
        this.modifyActionValue(actionValue);
    }
    
    public int getActionValue() {
        return this.m_actionValue;
    }
    
    public float getUserFriendlyValue() {
        return this.m_userFriendlyValue;
    }
    
    static double calculateUserFriendlyValue(final int actionValue) {
        return -Integer.signum(actionValue) * StrictMath.expm1(-Math.abs(actionValue) / WakfuGaugeConstants.WAKFU_EXP_CONSTANT);
    }
    
    public static WakfuGauge fromUserFriendlyValue(final float userFriendlyValue) {
        final int actionValue = (int)(Math.signum(userFriendlyValue) * WakfuGaugeConstants.WAKFU_EXP_CONSTANT * StrictMath.log1p(-Math.abs(userFriendlyValue)));
        return new WakfuGauge(actionValue);
    }
    
    private void modifyActionValue(final int mod) {
        this.setValue(this.m_actionValue + mod);
    }
    
    public void setValue(final int actionValue) {
        this.m_actionValue = clampActionValue(actionValue);
        this.m_userFriendlyValue = (float)calculateUserFriendlyValue(this.m_actionValue);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onWakfuGaugeValueChanged();
        }
    }
    
    private static int clampActionValue(final int value) {
        return MathHelper.clamp(value, -10000, 10000);
    }
    
    public void addActionWakfu(final int actionValue) {
        final int ratedModification = (int)(modificationRate(this.m_userFriendlyValue, Integer.signum(actionValue)) * actionValue);
        this.modifyActionValue(ratedModification);
    }
    
    static double modificationRate(final float userFriendlyValue, final int modificationSign) {
        if (modificationSign * userFriendlyValue > 0.0f) {
            return 1.0;
        }
        return 1.0 + Math.max(0.0, 4.0 * (Math.abs(userFriendlyValue) - 0.5));
    }
    
    public boolean addListener(final WakfuGaugeListener l) {
        return !this.m_listeners.contains(l) && this.m_listeners.add(l);
    }
    
    public boolean removeListener(final WakfuGaugeListener l) {
        return this.m_listeners.remove(l);
    }
    
    @Override
    public String toString() {
        return "WakfuGauge{m_actionValue=" + this.m_actionValue + ", m_userFriendlyValue=" + this.m_userFriendlyValue + '}';
    }
}
