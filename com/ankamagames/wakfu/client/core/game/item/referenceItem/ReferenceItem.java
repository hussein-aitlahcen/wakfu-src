package com.ankamagames.wakfu.client.core.game.item.referenceItem;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class ReferenceItem extends AbstractReferenceItem<ItemType> implements RefItemFieldProvider
{
    private final ReferenceItemDisplayer m_refItemDisplayer;
    private ItemActionVisual m_actionVisual;
    private ItemWorldUsageTarget m_usageTarget;
    
    ReferenceItem() {
        super();
        this.m_refItemDisplayer = new ReferenceItemDisplayer(this);
    }
    
    void setActionVisual(final ItemActionVisual actionVisual) {
        this.m_actionVisual = actionVisual;
    }
    
    void setUsageTarget(final ItemWorldUsageTarget usageTarget) {
        this.m_usageTarget = usageTarget;
    }
    
    @Override
    public ItemMetaType getMetaType() {
        return ItemMetaType.REFERENCE_ITEM;
    }
    
    public ItemActionVisual getActionVisual() {
        return this.m_actionVisual;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(15, this.getId(), new Object[0]);
    }
    
    @Override
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(16, this.getId(), new Object[0]);
    }
    
    @Override
    public String[] getFields() {
        return this.m_refItemDisplayer.getFields();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return this.m_refItemDisplayer.getFieldValue(fieldName);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public ReferenceItemDisplayer getReferenceItemDisplayer() {
        return this.m_refItemDisplayer;
    }
    
    public boolean isItemUsedInCraft() {
        return WakfuGameEntity.getInstance().hasFrame(UICraftTableFrame.getInstance()) && UICraftTableFrame.getInstance().isItemUsedInCurrentRecipe(this.getId());
    }
    
    @Override
    public boolean isCraftEnabled() {
        final SimpleCriterion criterion = this.getCriterion(ActionsOnItem.CRAFT);
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        return criterion == null || criterion.isValid(player, this, null, player.getEffectContext());
    }
    
    public ItemWorldUsageTarget getUsageTarget() {
        return this.m_usageTarget;
    }
    
    public void previewTradeEntry() {
        if (!this.canBeEquiped()) {
            return;
        }
        if (this.getItemType().getEquipmentPositions().length != 0) {
            UIStuffPreviewFrame.INSTANCE.equipItem(this);
            return;
        }
        final AbstractItemAction action = this.getItemAction();
        if (action instanceof SplitItemSetItemAction) {
            final SplitItemSetItemAction splitItemSetItemAction = (SplitItemSetItemAction)action;
            final short itemSetId = splitItemSetItemAction.getItemSetId();
            final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(itemSetId);
            if (itemSet != null) {
                for (final int itemRefId : itemSet.getItemIds()) {
                    final ReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(itemRefId);
                    UIStuffPreviewFrame.INSTANCE.equipItem(referenceItem);
                }
            }
        }
    }
    
    @Override
    public boolean canBeEquiped() {
        if (this.getItemType().getEquipmentPositions().length != 0) {
            return true;
        }
        final AbstractItemAction action = this.getItemAction();
        return action instanceof SplitItemSetItemAction;
    }
}
