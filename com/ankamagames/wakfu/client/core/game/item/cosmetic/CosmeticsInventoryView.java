package com.ankamagames.wakfu.client.core.game.item.cosmetic;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CosmeticsInventoryView extends ImmutableFieldProvider implements CosmeticsInventoryListener
{
    public static final String ITEMS = "items";
    public static final String SELECTED = "selectedItem";
    static final int NULL_REF_ID = 0;
    private final ArrayList<CosmeticsItemView> m_items;
    private CosmeticsItemView m_selectedItem;
    private static Comparator<CosmeticsItemView> COMPARATOR;
    
    public CosmeticsInventoryView() {
        super();
        (this.m_items = new ArrayList<CosmeticsItemView>()).add(new CosmeticsItemView(0));
    }
    
    @Override
    public String[] getFields() {
        return CosmeticsInventoryView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("items")) {
            return this.m_items;
        }
        if (fieldName.equals("selectedItem")) {
            return this.m_selectedItem;
        }
        return null;
    }
    
    public void select(final int itemRefId) {
        this.m_selectedItem = null;
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final CosmeticsItemView itemView = this.m_items.get(i);
            if (itemView.getRefId() == itemRefId) {
                this.m_selectedItem = itemView;
                break;
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedItem");
    }
    
    @Override
    public void itemAdded(final CosmeticsItem item) {
        this.m_items.add(new CosmeticsItemView(item.getRefId()));
        Collections.sort(this.m_items, CosmeticsInventoryView.COMPARATOR);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "items");
    }
    
    @Override
    public void itemRemoved(final int itemId) {
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final CosmeticsItemView itemView = this.m_items.get(i);
            if (itemView.getRefId() == itemId) {
                this.m_items.remove(i);
                break;
            }
        }
        Collections.sort(this.m_items, CosmeticsInventoryView.COMPARATOR);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "items");
    }
    
    public void refreshSelected() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.COSTUME.getId());
        this.select((item == null) ? 0 : item.getReferenceId());
    }
    
    static {
        CosmeticsInventoryView.COMPARATOR = new Comparator<CosmeticsItemView>() {
            @Override
            public int compare(final CosmeticsItemView o1, final CosmeticsItemView o2) {
                return o1.getRefId() - o2.getRefId();
            }
        };
    }
}
