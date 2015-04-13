package com.ankamagames.wakfu.common.game.skill;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ActionVisual
{
    protected static final Logger m_logger;
    private final int m_visualId;
    private final String m_animLink;
    private final int m_mruGfx;
    private final String m_mruLabelKey;
    private final int[] m_associatedItems;
    private boolean m_enabled;
    private final boolean m_multiCell = false;
    
    public ActionVisual(final int visualId, final String animLink, final int mruGfx, final String mruLabelKey, final int[] associatedItems) {
        super();
        this.m_enabled = true;
        this.m_visualId = visualId;
        this.m_animLink = animLink;
        this.m_mruGfx = mruGfx;
        this.m_mruLabelKey = mruLabelKey;
        this.m_associatedItems = associatedItems;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public String getAnimLink() {
        return this.m_animLink;
    }
    
    public int getMruGfx() {
        return this.m_mruGfx;
    }
    
    public String getMruLabelKey() {
        return this.m_mruLabelKey;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public boolean isMultiCell() {
        return false;
    }
    
    public boolean needItem() {
        return this.m_associatedItems.length != 0;
    }
    
    public boolean isValidFor(final BasicCharacterInfo character) {
        return !this.needItem() || this.getFirstValidItemInInventory(character) != null;
    }
    
    public Item getFirstValidItemInInventory(final BasicCharacterInfo character) {
        assert this.needItem();
        final Item equippedItem = ((ArrayInventoryWithoutCheck<Item, R>)character.getEquipmentInventory()).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
        if (this.acceptItem(equippedItem)) {
            return equippedItem;
        }
        for (final int itemId : this.m_associatedItems) {
            final Item item = character.getBags().getFirstItemFromInventoryFromRefId(itemId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    private boolean acceptItem(final InventoryContent item) {
        if (item == null) {
            return false;
        }
        for (final int itemId : this.m_associatedItems) {
            if (item.getReferenceId() == itemId) {
                return true;
            }
        }
        return false;
    }
    
    public int[] getAssociatedItems() {
        return this.m_associatedItems.clone();
    }
    
    @Override
    public String toString() {
        return "ActionVisual{m_visualId=" + this.m_visualId + ", m_animLink='" + this.m_animLink + '\'' + ", m_mruGfx=" + this.m_mruGfx + ", m_mruLabelKey='" + this.m_mruLabelKey + '\'' + ", m_associatedItems=" + Arrays.toString(this.m_associatedItems) + ", m_enabled=" + this.m_enabled + ", m_multiCell=" + false + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActionVisual.class);
    }
}
