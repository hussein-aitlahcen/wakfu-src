package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class Repell extends MovementEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Repell.PARAMETERS_LIST_SET;
    }
    
    public static Repell checkOut(final EffectContext<WakfuEffect> context, final int look, final EffectUser target, final WakfuEffect genericEffect, final EffectUser caster, final WakfuEffectContainer effectContainer) {
        Repell re;
        try {
            re = (Repell)Repell.m_staticPool.borrowObject();
            re.m_pool = Repell.m_staticPool;
        }
        catch (Exception e) {
            re = new Repell();
            re.m_pool = null;
            re.m_isStatic = false;
            Repell.m_logger.error((Object)("Erreur lors d'un checkOut sur un Repell : " + e.getMessage()));
        }
        re.m_id = RunningEffectConstants.REPELL.getId();
        re.m_status = RunningEffectConstants.REPELL.getObject().getRunningEffectStatus();
        re.setTriggersToExecute();
        re.m_target = target;
        re.m_caster = caster;
        re.m_value = look;
        re.m_effectContainer = (EC)effectContainer;
        re.m_maxExecutionCount = -1;
        re.m_context = (EffectContext<FX>)context;
        re.m_genericEffect = (FX)genericEffect;
        return re;
    }
    
    @Override
    public Repell newInstance() {
        Repell re;
        try {
            re = (Repell)Repell.m_staticPool.borrowObject();
            re.m_pool = Repell.m_staticPool;
        }
        catch (Exception e) {
            re = new Repell();
            re.m_pool = null;
            Repell.m_logger.error((Object)("Erreur lors d'une newInstance sur un Repell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(194);
    }
    
    @Override
    boolean getCloser() {
        return false;
    }
    
    @Override
    public boolean validatePrecondition() {
        return this.m_targetCell != null && !this.moverIsCarried() && !this.moverIsRooted() && !this.moverCantMoveAwayOrRepell();
    }
    
    @Override
    boolean doesCollide() {
        return false;
    }
    
    @Override
    public MovementEffectUser getMover() {
        if (this.m_caster instanceof MovementEffectUser) {
            return (MovementEffectUser)this.m_caster;
        }
        return null;
    }
    
    @Override
    Point3 getReferentialCell() {
        return this.m_targetCell;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        if (this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 11) {
            final AbstractSpellLevel spellLevel = (AbstractSpellLevel)this.m_effectContainer;
            if (spellLevel.getSpell() != null) {
                return !spellLevel.getSpell().hasToTestFreeCell();
            }
        }
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Repell>() {
            @Override
            public Repell makeObject() {
                return new Repell();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Distance a parcourir", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
