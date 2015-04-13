package com.ankamagames.wakfu.client.core.game.secret;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import org.jetbrains.annotations.*;

public class SecretView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String ITEM = "item";
    public static final String DESCRIPTION = "description";
    public static final String LEVEL = "level";
    public static final String IS_COMPLETE = "isComplete";
    private final SecretData m_data;
    private ReferenceItem m_item;
    
    public SecretView(final SecretData data) {
        super();
        this.m_data = data;
        this.m_item = ReferenceItemManager.getInstance().getReferenceItem((int)this.m_data.getItemId());
    }
    
    public SecretData getData() {
        return this.m_data;
    }
    
    @Override
    public String[] getFields() {
        return SecretView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(15, this.m_data.getItemId(), new Object[0]);
        }
        if (fieldName.equals("item")) {
            return this.m_item;
        }
        if (fieldName.equals("description")) {
            return WakfuTranslator.getInstance().getString(143, this.m_data.getId(), new Object[0]);
        }
        if (fieldName.equals("level")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_data.getLevel());
        }
        if (fieldName.equals("isComplete")) {
            final QuestInventory inventory = (QuestInventory)WakfuGameEntity.getInstance().getLocalPlayer().getInventory(InventoryType.QUEST);
            return inventory.getItem(this.m_data.getItemId()) != null;
        }
        return null;
    }
}
