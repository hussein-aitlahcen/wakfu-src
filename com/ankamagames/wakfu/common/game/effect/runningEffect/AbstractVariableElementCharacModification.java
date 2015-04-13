package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class AbstractVariableElementCharacModification extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private ArrayList<CharacGain> m_characGains;
    
    public AbstractVariableElementCharacModification() {
        super();
        this.m_characGains = new ArrayList<CharacGain>();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractVariableElementCharacModification.PARAMETERS_LIST_SET;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final WakfuEffectContainer effectContainer = ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer();
        if (!(effectContainer instanceof Item)) {
            return;
        }
        final Item item = (Item)effectContainer;
        if (!item.randomElementsInit()) {
            return;
        }
        final HashSet<Elements> elements = item.getElementsForRunningEffect(this.getId());
        for (final Elements element : elements) {
            final CharacGain characGain = new CharacGain(this.getModifiedCharacteristic(element));
            characGain.forceValue(this.m_value);
            characGain.setTarget(this.m_target);
            characGain.setCaster(this.m_caster);
            characGain.setNotified(true);
            ((RunningEffect<WakfuEffect, EC>)characGain).setGenericEffect((WakfuEffect)this.m_genericEffect);
            characGain.executeOverride(linkedRE, trigger);
            characGain.m_executed = true;
            this.m_characGains.add(characGain);
        }
    }
    
    protected abstract FighterCharacteristicType getModifiedCharacteristic(final Elements p0);
    
    @Override
    public void unapplyOverride() {
        for (final CharacGain characGain : this.m_characGains) {
            characGain.unapplyOverride();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_characGains.clear();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_characGains.clear();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Modifie la valeur de la caract\u00e9ristique dans 1-n \u00e9l\u00e9ments tir\u00e9s au hasard \u00e0 la cr\u00e9ation de l'item", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Nombre d'\u00e9l\u00e9ments", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
