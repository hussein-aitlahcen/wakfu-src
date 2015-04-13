package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class PassiveSpellGain extends SeveralSpellsGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return PassiveSpellGain.PARAMETERS_LIST_SET;
    }
    
    public PassiveSpellGain() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public PassiveSpellGain newInstance() {
        PassiveSpellGain re;
        try {
            re = (PassiveSpellGain)PassiveSpellGain.m_staticPool.borrowObject();
            re.m_pool = PassiveSpellGain.m_staticPool;
        }
        catch (Exception e) {
            re = new PassiveSpellGain();
            re.m_pool = null;
            re.m_isStatic = false;
            PassiveSpellGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un PassiveSpellGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected void afterModification() {
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        ((BasicCharacterInfo)this.m_target).reloadPassiveSpells();
    }
    
    @Override
    protected List<AbstractSpellLevel<?>> getTargetSpells() {
        if (this.m_target == null) {
            return Collections.emptyList();
        }
        final Iterable<? extends AbstractSpellLevel> spellInventory = ((BasicCharacterInfo)this.m_target).getPermanentSpellInventory();
        if (spellInventory == null) {
            return Collections.emptyList();
        }
        final List<AbstractSpellLevel<?>> res = new ArrayList<AbstractSpellLevel<?>>();
        for (final AbstractSpellLevel spellLevel : spellInventory) {
            if (this.isPassiveSpell(spellLevel)) {
                res.add(spellLevel);
            }
        }
        return res;
    }
    
    private boolean isPassiveSpell(final AbstractSpellLevel spellLevel) {
        final AbstractSpell spell = spellLevel.getSpell();
        return spell != null && spell.isPassive();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PassiveSpellGain>() {
            @Override
            public PassiveSpellGain makeObject() {
                return new PassiveSpellGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Gain de niveau des sorts passifs", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (en niveaux)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
