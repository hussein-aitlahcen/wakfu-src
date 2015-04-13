package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class WakfuEffectExecutionParameters extends EffectExecutionParameters
{
    public static final int NO_FORCED_LEVEL = -1;
    protected static final Logger m_logger;
    private static final ObjectPool m_pool;
    private boolean m_checkedOut;
    private boolean m_disableProbabilityComputation;
    private boolean m_disableCriterionCheck;
    private WakfuRunningEffect m_externalTriggeringEffect;
    private int m_forcedLevel;
    private boolean m_resetLimitedApplyCount;
    private int m_forcedValue;
    private boolean m_doNotNotify;
    private boolean m_isDisabled;
    private ForcedValueType m_valueForcedType;
    private List<WakfuRunningEffectListener> m_listeners;
    
    private WakfuEffectExecutionParameters() {
        super();
        this.m_forcedLevel = -1;
        this.m_resetLimitedApplyCount = true;
        this.m_forcedValue = 0;
        this.m_doNotNotify = false;
        this.m_isDisabled = false;
        this.m_valueForcedType = ForcedValueType.NONE;
        this.m_disableProbabilityComputation = false;
        this.m_disableCriterionCheck = false;
    }
    
    public static WakfuEffectExecutionParameters checkOut(final boolean disableProbabilityComputation, final boolean disableCriterionCheck, final WakfuRunningEffect parent) {
        WakfuEffectExecutionParameters params;
        try {
            params = (WakfuEffectExecutionParameters)WakfuEffectExecutionParameters.m_pool.borrowObject();
            params.m_checkedOut = true;
        }
        catch (Exception e) {
            params = new WakfuEffectExecutionParameters();
            WakfuEffectExecutionParameters.m_logger.error((Object)("Erreur lors d'un newInstance sur un ActionCost : " + e.getMessage()));
        }
        params.m_disableProbabilityComputation = disableProbabilityComputation;
        params.m_disableCriterionCheck = disableCriterionCheck;
        params.m_externalTriggeringEffect = parent;
        return params;
    }
    
    public boolean disableProbabilityComputation() {
        return this.m_disableProbabilityComputation;
    }
    
    public boolean disableCriterionCheck() {
        return this.m_disableCriterionCheck;
    }
    
    public WakfuRunningEffect getExternalTriggeringEffect() {
        return this.m_externalTriggeringEffect;
    }
    
    public int getForcedLevel() {
        return this.m_forcedLevel;
    }
    
    public void setForcedLevel(final int forcedLevel) {
        this.m_forcedLevel = forcedLevel;
    }
    
    public void setResetLimitedApplyCount(final boolean resetLimitedApplyCount) {
        this.m_resetLimitedApplyCount = resetLimitedApplyCount;
    }
    
    @Override
    public boolean resetLimitedApplyCount() {
        return this.m_resetLimitedApplyCount;
    }
    
    public boolean isExecutionDisabled() {
        return this.m_isDisabled;
    }
    
    public void setDisableExecution() {
        this.m_isDisabled = true;
    }
    
    public void setForcedValue(final int forcedValue) {
        this.m_valueForcedType = ForcedValueType.SET;
        this.m_forcedValue = forcedValue;
    }
    
    public void setForcedValue(final int forcedValue, final ForcedValueType type) {
        if (type == ForcedValueType.NONE) {
            WakfuEffectExecutionParameters.m_logger.error((Object)"On ne peut pas forcer une valeur avec le type NONE");
            return;
        }
        this.m_valueForcedType = type;
        this.m_forcedValue = forcedValue;
    }
    
    public int getForcedValue() {
        return this.m_forcedValue;
    }
    
    public boolean isValueForced() {
        return this.m_valueForcedType != ForcedValueType.NONE;
    }
    
    public ForcedValueType getValueForcedType() {
        return this.m_valueForcedType;
    }
    
    public boolean isDoNotNotify() {
        return this.m_doNotNotify;
    }
    
    public void setDoNotNotify(final boolean doNotNotify) {
        this.m_doNotNotify = doNotNotify;
    }
    
    public void addListener(final WakfuRunningEffectListener listener) {
        if (this.m_listeners == null) {
            this.m_listeners = new ArrayList<WakfuRunningEffectListener>();
        }
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final WakfuRunningEffectListener listener) {
        if (this.m_listeners == null) {
            return;
        }
        this.m_listeners.remove(listener);
    }
    
    public List<WakfuRunningEffectListener> getListeners() {
        return this.m_listeners;
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_disableProbabilityComputation = false;
        this.m_disableCriterionCheck = false;
        this.m_forcedLevel = -1;
        this.m_resetLimitedApplyCount = true;
        this.m_valueForcedType = ForcedValueType.NONE;
        this.m_listeners = null;
        this.m_forcedValue = 0;
        this.m_doNotNotify = false;
        this.m_externalTriggeringEffect = null;
        this.m_checkedOut = false;
        this.m_isDisabled = false;
    }
    
    @Override
    public void release() {
        if (!this.m_checkedOut) {
            WakfuEffectExecutionParameters.m_logger.error((Object)("Tentative de remettre un WakfuEffectExecutionParameters qui n'est pas checkout\u00e9 dans le pool " + ExceptionFormatter.currentStackTrace()));
            return;
        }
        try {
            WakfuEffectExecutionParameters.m_pool.returnObject(this);
        }
        catch (Exception e) {
            WakfuEffectExecutionParameters.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + " normalement impossible"));
        }
    }
    
    @Override
    public WakfuEffectExecutionParameters newInstance() {
        final WakfuEffectExecutionParameters res = checkOut(this.m_disableProbabilityComputation, this.m_disableCriterionCheck, this.m_externalTriggeringEffect);
        res.m_forcedLevel = this.m_forcedLevel;
        res.m_forcedValue = this.m_forcedValue;
        res.m_valueForcedType = this.m_valueForcedType;
        res.m_resetLimitedApplyCount = this.m_resetLimitedApplyCount;
        res.m_doNotNotify = this.m_doNotNotify;
        res.m_isDisabled = this.m_isDisabled;
        if (this.m_listeners != null) {
            res.m_listeners = new ArrayList<WakfuRunningEffectListener>(this.m_listeners);
        }
        return res;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuEffectExecutionParameters.class);
        m_pool = new MonitoredPool(new ObjectFactory<WakfuEffectExecutionParameters>() {
            @Override
            public WakfuEffectExecutionParameters makeObject() {
                return new WakfuEffectExecutionParameters(null);
            }
        });
    }
    
    public enum ForcedValueType
    {
        NONE, 
        SET, 
        PERCENT;
    }
}
