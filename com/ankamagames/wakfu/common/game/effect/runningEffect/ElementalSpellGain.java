package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ElementalSpellGain extends SeveralSpellsGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_elementId;
    private final BinarSerialPart ADDITIONAL_DATA;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ElementalSpellGain.PARAMETERS_LIST_SET;
    }
    
    public ElementalSpellGain() {
        super();
        this.ADDITIONAL_DATA = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(ElementalSpellGain.this.m_elementId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ElementalSpellGain.this.m_elementId = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public ElementalSpellGain newInstance() {
        ElementalSpellGain re;
        try {
            re = (ElementalSpellGain)ElementalSpellGain.m_staticPool.borrowObject();
            re.m_pool = ElementalSpellGain.m_staticPool;
        }
        catch (Exception e) {
            re = new ElementalSpellGain();
            re.m_pool = null;
            re.m_isStatic = false;
            ElementalSpellGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un ElementalSpellGain : " + e.getMessage()));
        }
        re.m_elementId = this.m_elementId;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_elementId = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected void afterModification() {
        ((BasicCharacterInfo)this.m_target).updateElementMastery(Elements.getElementFromId((byte)this.m_elementId));
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
            final byte elementId = spellLevel.getSpell().getElementId();
            if (elementId == this.m_elementId) {
                res.add(spellLevel);
            }
        }
        return res;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONAL_DATA;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_elementId = -1;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ElementalSpellGain>() {
            @Override
            public ElementalSpellGain makeObject() {
                return new ElementalSpellGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Gain de niveau des sorts d'un element", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("element des sorts", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("valeur (en niveaux)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
