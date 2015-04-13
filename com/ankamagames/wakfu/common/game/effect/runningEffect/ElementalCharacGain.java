package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ElementalCharacGain extends CharacGain
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ElementalCharacGain.PARAMETERS_LIST_SET;
    }
    
    public ElementalCharacGain() {
        super();
        this.setTriggersToExecute();
    }
    
    public ElementalCharacGain(final CharacteristicType charac) {
        super(charac);
    }
    
    @Override
    public ElementalCharacGain newInstance() {
        ElementalCharacGain re;
        try {
            re = (ElementalCharacGain)ElementalCharacGain.m_staticPool.borrowObject();
            re.m_pool = ElementalCharacGain.m_staticPool;
        }
        catch (Exception e) {
            re = new ElementalCharacGain();
            re.m_pool = null;
            re.m_isStatic = false;
            ElementalCharacGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un ElementalCharacGain : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int elementId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final Elements element = Elements.getElementFromId((byte)elementId);
        if (element == null) {
            ElementalCharacGain.m_logger.error((Object)("Mauvais param element inconnu " + elementId));
            return;
        }
        final FighterCharacteristicType damageBonusCharacteristic = element.getDamageBonusCharacteristic();
        int modificator = 0;
        if (this.m_caster.hasCharacteristic(damageBonusCharacteristic)) {
            modificator += this.m_caster.getCharacteristicValue(damageBonusCharacteristic);
        }
        if (this.m_caster.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            modificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT);
        }
        this.m_value += modificator * this.m_value / 100;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ElementalCharacGain>() {
            @Override
            public ElementalCharacGain makeObject() {
                return new ElementalCharacGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur + \u00e9l\u00e9ment", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
