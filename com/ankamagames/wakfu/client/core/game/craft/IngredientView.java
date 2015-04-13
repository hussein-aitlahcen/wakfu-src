package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;

public class IngredientView extends ImmutableFieldProvider
{
    public static final String QUANTITY_FIELD = "quantity";
    public static final String IS_POSSESSED_FIELD = "isPossessed";
    public static final String NAME_QUANTITY = "nameAndQuantity";
    private final short m_quantity;
    private final ReferenceItem m_referenceItem;
    
    public IngredientView(final short quantity, final ReferenceItem referenceItem) {
        super();
        this.m_quantity = quantity;
        this.m_referenceItem = referenceItem;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("quantity")) {
            return this.m_quantity;
        }
        if (fieldName.equals("isPossessed")) {
            return this.isPossessed();
        }
        if (fieldName.equals("nameAndQuantity")) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            return sb.append(this.m_quantity).append("x ").append(this.m_referenceItem.getName()).finishAndToString();
        }
        return this.m_referenceItem.getFieldValue(fieldName);
    }
    
    public ReferenceItem getReferenceItem() {
        return this.m_referenceItem;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public boolean isPossessed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.possessedItemQuantity(this.m_referenceItem.getId(), this.m_quantity)) {
            return true;
        }
        final QuestInventory questInventory = (QuestInventory)localPlayer.getInventory(InventoryType.QUEST);
        final QuestItem questItem = questInventory.getItem(this.m_referenceItem.getId());
        return questItem != null && questItem.getQuantity() >= this.m_quantity;
    }
}
