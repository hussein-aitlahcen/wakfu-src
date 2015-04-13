package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import gnu.trove.*;

public class HavenWorldResourcesCollectorView extends ImmutableFieldProvider implements HavenWorldListener
{
    public static final String ITEM_LIST_FIELD = "itemList";
    public static final String BASE_RESOURCES_QUANTITY_VALUE_FIELD = "baseResourcesQuantityValue";
    public static final String RESOURCES_QUANTITY_VALUE_FIELD = "resourcesQuantityValue";
    public static final String RESOURCES_QUANTITY_TEXT_FIELD = "resourcesQuantityText";
    public static final String VALID_ENABLED_FIELD = "validEnabled";
    public static final String[] FIELDS;
    private final TLongObjectHashMap<FakeItem> m_items;
    private int m_currentResourcesQuantity;
    private int m_baseResourcesQuantity;
    
    @Override
    public String[] getFields() {
        return HavenWorldResourcesCollectorView.FIELDS;
    }
    
    public HavenWorldResourcesCollectorView(final int baseResourcesQuantity) {
        super();
        this.m_items = new TLongObjectHashMap<FakeItem>();
        this.m_baseResourcesQuantity = baseResourcesQuantity;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("itemList")) {
            final ArrayList<FakeItem> fakeItems = new ArrayList<FakeItem>();
            final TLongObjectIterator<FakeItem> it = this.m_items.iterator();
            while (it.hasNext()) {
                it.advance();
                fakeItems.add(it.value());
            }
            return fakeItems;
        }
        if (fieldName.equals("baseResourcesQuantityValue")) {
            return this.m_baseResourcesQuantity / 5.0E7f;
        }
        if (fieldName.equals("resourcesQuantityValue")) {
            return this.getTotalResourcesQuantity() / 5.0E7f;
        }
        if (fieldName.equals("resourcesQuantityText")) {
            final TextWidgetFormater twf = new TextWidgetFormater();
            twf.addColor((this.getTotalResourcesQuantity() > 50000000) ? new Color(1.0f, 0.0f, 0.0f, 1.0f).getRGBtoHex() : Color.WHITE.getRGBtoHex());
            twf.append(WakfuTranslator.getInstance().formatNumber(this.getTotalResourcesQuantity()));
            twf.closeText();
            twf.append(" / " + WakfuTranslator.getInstance().formatNumber(50000000L));
            return twf.finishAndToString();
        }
        if (fieldName.equals("validEnabled")) {
            return this.m_currentResourcesQuantity > 0;
        }
        return null;
    }
    
    private int getTotalResourcesQuantity() {
        return this.m_baseResourcesQuantity + this.m_currentResourcesQuantity;
    }
    
    public TLongObjectHashMap<FakeItem> getItems() {
        return this.m_items;
    }
    
    public int getCurrentResourcesQuantity() {
        return this.m_currentResourcesQuantity;
    }
    
    public void removeItem(final FakeItem item) {
        final long itemUniqueId = item.getUniqueId();
        if (!this.m_items.containsKey(itemUniqueId)) {
            return;
        }
        this.m_items.remove(itemUniqueId);
        this.m_currentResourcesQuantity -= (int)HavenWorldResourcesCollectorUtil.calculateRessources(item, item.getQuantity());
        final Item itemFromInventories = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getItemFromInventories(itemUniqueId);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, HavenWorldResourcesCollectorView.FIELDS);
        PropertiesProvider.getInstance().firePropertyValueChanged(itemFromInventories, "movable", "deletable");
    }
    
    public void addItem(final Item item, final short quantity) {
        final long uniqueId = item.getUniqueId();
        if (this.m_items.containsKey(uniqueId)) {
            return;
        }
        final FakeItem fakeItem = new FakeItem(item.getReferenceItem());
        fakeItem.setUniqueId(uniqueId);
        fakeItem.setQuantity(quantity);
        this.m_items.put(uniqueId, fakeItem);
        this.m_currentResourcesQuantity += (int)HavenWorldResourcesCollectorUtil.calculateRessources(fakeItem, quantity);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, HavenWorldResourcesCollectorView.FIELDS);
        PropertiesProvider.getInstance().firePropertyValueChanged(item, "movable", "deletable");
    }
    
    public boolean containsItem(final long itemId) {
        return this.m_items.containsKey(itemId);
    }
    
    public void clearItems() {
        this.m_items.clear();
        this.m_currentResourcesQuantity = 0;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, HavenWorldResourcesCollectorView.FIELDS);
    }
    
    @Override
    public void buildingAdded(final Building building) {
    }
    
    @Override
    public void guildChanged(final GuildInfo guildInfo) {
    }
    
    @Override
    public void buildingRemoved(final Building building) {
    }
    
    @Override
    public void partitionAdded(final Partition partition) {
    }
    
    @Override
    public void partitionChanged(final Partition partition) {
    }
    
    @Override
    public void resourcesChanged(final int resources) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "resourcesQuantityText", "resourcesQuantityValue");
    }
    
    public int size() {
        return this.m_items.size();
    }
    
    public boolean isEmpty() {
        return this.m_items.isEmpty();
    }
    
    static {
        FIELDS = new String[] { "itemList", "resourcesQuantityValue", "resourcesQuantityText", "validEnabled" };
    }
}
