package com.ankamagames.wakfu.client.core.game.item.referenceItem;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;

public class MetaItem extends AbstractReferenceItem<ItemType> implements IMetaItem<EffectGroup>, RefItemFieldProvider
{
    private final ArrayList<EffectGroup> m_variableEffects;
    private final TIntArrayList m_subs;
    private final ReferenceItemDisplayer m_refItemDisplayer;
    private ItemActionVisual m_actionVisual;
    private ItemWorldUsageTarget m_usageTarget;
    
    MetaItem() {
        super();
        this.m_variableEffects = new ArrayList<EffectGroup>();
        this.m_subs = new TIntArrayList();
        this.m_refItemDisplayer = new ReferenceItemDisplayer(this);
    }
    
    void setUsageTarget(final ItemWorldUsageTarget target) {
        this.m_usageTarget = target;
    }
    
    void setSubs(final int[] subMetaIds) {
        this.m_subs.add(subMetaIds);
        this.m_subs.sort();
    }
    
    void setActionVisual(final ItemActionVisual actionVisual) {
        this.m_actionVisual = actionVisual;
    }
    
    @Override
    public ItemMetaType getMetaType() {
        return ItemMetaType.META_ITEM;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(15, this.getId(), new Object[0]);
    }
    
    @Override
    public boolean canBeEquiped() {
        return false;
    }
    
    @Override
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(16, this.getId(), new Object[0]);
    }
    
    @Override
    public String[] getFields() {
        throw new UnsupportedOperationException("Ridicule");
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        throw new UnsupportedOperationException("Ridicule");
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        throw new UnsupportedOperationException("Ridicule");
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
        throw new UnsupportedOperationException("Ridicule");
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
        throw new UnsupportedOperationException("Ridicule");
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        throw new UnsupportedOperationException("Ridicule");
    }
    
    @Override
    public ReferenceItemDisplayer getReferenceItemDisplayer() {
        return this.m_refItemDisplayer;
    }
    
    public void setVariablesEffects(final int... variableEffects) {
        this.m_variableEffects.clear();
        for (final int effectId : variableEffects) {
            this.m_variableEffects.add((EffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(effectId));
        }
    }
    
    @Override
    public Iterator<EffectGroup> variableEffectsIterator() {
        return this.m_variableEffects.iterator();
    }
    
    @Override
    public int[] getSubIds() {
        return this.m_subs.toNativeArray();
    }
    
    public int getLastSubId() {
        return this.m_subs.isEmpty() ? -1 : this.m_subs.get(this.m_subs.size() - 1);
    }
    
    public int getFirstSubId() {
        return this.m_subs.isEmpty() ? -1 : this.m_subs.get(0);
    }
    
    public ItemActionVisual getActionVisual() {
        return this.m_actionVisual;
    }
    
    public ItemWorldUsageTarget getUsageTarget() {
        return this.m_usageTarget;
    }
}
