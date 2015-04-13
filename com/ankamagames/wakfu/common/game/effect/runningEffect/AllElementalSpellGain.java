package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class AllElementalSpellGain extends SeveralSpellsGain
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isCheckedOut;
    private boolean m_includeStasis;
    private static final ObjectPool POOL;
    private BinarSerialPart ADDITIONAL_DATAS;
    
    public AllElementalSpellGain() {
        super();
        this.m_includeStasis = true;
        this.ADDITIONAL_DATAS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(AllElementalSpellGain.this.m_includeStasis ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                AllElementalSpellGain.this.m_includeStasis = (buffer.get() == 1);
            }
            
            @Override
            public int expectedSize() {
                return 1;
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        AllElementalSpellGain re;
        try {
            re = (AllElementalSpellGain)AllElementalSpellGain.POOL.borrowObject();
            re.m_pool = AllElementalSpellGain.POOL;
        }
        catch (Exception e) {
            re = new AllElementalSpellGain();
            re.m_pool = null;
            re.m_isStatic = false;
            AllElementalSpellGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un AllElementalSpellGain : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                AllElementalSpellGain.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                AllElementalSpellGain.m_logger.error((Object)"Exception lors du retour au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_includeStasis = (((WakfuEffect)this.m_genericEffect).getParamsCount() < 2 || ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
    }
    
    @Override
    protected void afterModification() {
        ((BasicCharacterInfo)this.m_target).updateElementMastery(Elements.getElementFromId(Elements.AIR.getId()));
        ((BasicCharacterInfo)this.m_target).updateElementMastery(Elements.getElementFromId(Elements.FIRE.getId()));
        ((BasicCharacterInfo)this.m_target).updateElementMastery(Elements.getElementFromId(Elements.EARTH.getId()));
        ((BasicCharacterInfo)this.m_target).updateElementMastery(Elements.getElementFromId(Elements.WATER.getId()));
        if (this.m_includeStasis) {
            ((BasicCharacterInfo)this.m_target).updateElementMastery(Elements.getElementFromId(Elements.STASIS.getId()));
        }
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
            if (this.isElementalSpell(spellLevel)) {
                res.add(spellLevel);
            }
        }
        return res;
    }
    
    private boolean isElementalSpell(final AbstractSpellLevel spellLevel) {
        return spellLevel.getSpell().getElementId() == Elements.AIR.getId() || spellLevel.getSpell().getElementId() == Elements.EARTH.getId() || spellLevel.getSpell().getElementId() == Elements.WATER.getId() || spellLevel.getSpell().getElementId() == Elements.FIRE.getId() || (this.m_includeStasis && spellLevel.getSpell().getElementId() == Elements.STASIS.getId());
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AllElementalSpellGain.PARAMETERS_LIST_SET;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATAS;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Gain de niveau des sorts tous elements", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (en niveaux)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Gain de niveau des sorts tous elements, avec ou sans stasis", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (en niveaux)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("prise en compte du stasis (1=oui (defaut), 0=non", WakfuRunningEffectParameterType.CONFIG) }) });
        POOL = new MonitoredPool(new ObjectFactory<AllElementalSpellGain>() {
            @Override
            public AllElementalSpellGain makeObject() {
                return new AllElementalSpellGain();
            }
        });
    }
}
