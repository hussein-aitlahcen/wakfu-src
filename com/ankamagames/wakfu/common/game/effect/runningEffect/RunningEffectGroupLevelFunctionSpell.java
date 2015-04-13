package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RunningEffectGroupLevelFunctionSpell extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_SPELL;
    private int m_spellId;
    private float m_spellLevelFactor;
    private boolean m_checkOnCaster;
    private boolean m_usePermanentInventory;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionSpell.PARAMETERS_LIST_SET_FUNCTION_SPELL;
    }
    
    public RunningEffectGroupLevelFunctionSpell() {
        super();
        this.m_spellLevelFactor = 1.0f;
        this.m_checkOnCaster = true;
        this.m_usePermanentInventory = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionSpell newInstance() {
        RunningEffectGroupLevelFunctionSpell re;
        try {
            re = (RunningEffectGroupLevelFunctionSpell)RunningEffectGroupLevelFunctionSpell.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionSpell.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionSpell();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionSpell.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionSpell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        this.m_checkOnCaster = true;
        this.m_spellId = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 8) {
            this.m_checkOnCaster = (((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 9) {
            this.m_spellLevelFactor = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel());
        }
        else {
            this.m_spellLevelFactor = 1.0f;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 10) {
            final boolean inverseFactor = ((WakfuEffect)this.m_genericEffect).getParam(9, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
            if (inverseFactor) {
                this.m_spellLevelFactor = 1.0f / this.m_spellLevelFactor;
            }
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 11) {
            this.m_usePermanentInventory = (((WakfuEffect)this.m_genericEffect).getParam(10, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        EffectUser spellOwner;
        if (this.m_checkOnCaster) {
            spellOwner = this.m_caster;
        }
        else {
            spellOwner = this.m_target;
        }
        if (spellOwner == null) {
            return params;
        }
        if (!(spellOwner instanceof BasicCharacterInfo)) {
            return params;
        }
        Iterable<? extends AbstractSpellLevel> spellInventory;
        if (this.m_usePermanentInventory) {
            spellInventory = ((BasicCharacterInfo)spellOwner).getPermanentSpellInventory();
        }
        else {
            spellInventory = (Iterable<? extends AbstractSpellLevel>)((BasicCharacterInfo)spellOwner).getSpellInventory();
        }
        if (spellInventory == null) {
            return params;
        }
        final AbstractSpellLevel spell = this.getFirstWithReferenceId(spellInventory);
        if (spell == null) {
            return params;
        }
        final int forcedLevel = (int)Math.floor(this.m_spellLevelFactor * spell.getLevel());
        params.setForcedLevel(forcedLevel);
        return params;
    }
    
    private AbstractSpellLevel getFirstWithReferenceId(final Iterable<? extends AbstractSpellLevel> spellInventory) {
        for (final AbstractSpellLevel spellLevel : spellInventory) {
            if (spellLevel.getSpellId() == this.m_spellId) {
                return spellLevel;
            }
        }
        return null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_spellId = -1;
        this.m_checkOnCaster = true;
        this.m_usePermanentInventory = false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionSpell>() {
            @Override
            public RunningEffectGroupLevelFunctionSpell makeObject() {
                return new RunningEffectGroupLevelFunctionSpell();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_SPELL = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Transmet le niveau du sort (caster) comme niveau pour les effets du groupe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id du sort \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("\"Transmet le niveau du sort (cible) comme niveau pour les effets du groupe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id du sort \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0, Caster = 1 (defaut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Transmet le niveau du sort comme niveau pour les effets du groupe avec multiplicateur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id du sort \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0, Caster = 1 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur sur le niveau du sort", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Facteur sur le niveau du sort", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id du sort \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0, Caster = 1 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur sur le niveau du sort", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur inverse (0 = non (defaut), 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Utilise l'inventaire permanent", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id du sort \u00e0 consulter pour le niveau du groupe", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Check on Target = 0, Caster = 1 (defaut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur sur le niveau du sort", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Facteur inverse (0 = non (defaut), 1 = oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Utilise l'inventaire permanent (0 = non (defaut), 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
