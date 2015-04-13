package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SetEffectAreaFunctionSpellLevel extends SetEffectArea
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetEffectAreaFunctionSpellLevel.PARAMETERS_LIST_SET;
    }
    
    public SetEffectAreaFunctionSpellLevel() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SetEffectAreaFunctionSpellLevel newInstance() {
        SetEffectAreaFunctionSpellLevel re;
        try {
            re = (SetEffectAreaFunctionSpellLevel)SetEffectAreaFunctionSpellLevel.m_staticPool.borrowObject();
            re.m_pool = SetEffectAreaFunctionSpellLevel.m_staticPool;
        }
        catch (Exception e) {
            re = new SetEffectAreaFunctionSpellLevel();
            re.m_pool = null;
            re.m_isStatic = false;
            SetEffectAreaFunctionSpellLevel.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetEffectAreaFunctionSpellLevel : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)2);
        this.extractZoneLevel(level);
        this.m_shouldBeInfinite = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
    }
    
    private void extractZoneLevel(final short level) {
        final short spellId = (short)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_caster == null || !(this.m_caster instanceof BasicCharacterInfo)) {
            this.m_zoneLevel = 0;
            return;
        }
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = ((BasicCharacterInfo)this.m_caster).getSpellInventory();
        if (spellInventory == null) {
            this.m_zoneLevel = 0;
            return;
        }
        final AbstractSpellLevel spell = spellInventory.getFirstWithReferenceId(spellId);
        if (spell == null) {
            this.m_zoneLevel = 0;
            return;
        }
        this.m_zoneLevel = spell.getLevel();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetEffectAreaFunctionSpellLevel>() {
            @Override
            public SetEffectAreaFunctionSpellLevel makeObject() {
                return new SetEffectAreaFunctionSpellLevel();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la zone", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("Id du sort pour le level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("La zone reste apres la fin de cet effet (1 = Oui, 0 = Non)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
