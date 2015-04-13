package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.validator.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class ClientBagContainer extends AbstractBagContainer implements FieldProvider, InventoryObserver
{
    private static final Logger m_logger;
    public static final String ALL_ITEMS_VIEW = "allItemsView";
    public static final String RECYCLABLE_ITEMS_VIEW = "recyclableItemsView";
    public static final String GEMMED_ITEMS_VIEW = "gemmedItemsView";
    public static final String CURRENT_INVENTORY_VIEW = "currentInventoryView";
    public static final String BAG_LIST_WITHOUT_POCKETS_FIELD = "bagListWithoutPockets";
    public static final String TYPED_BAG_LIST_WITHOUT_POCKETS_FIELD = "typedBagListWithoutPockets";
    public static final String CURRENT_BAG_TYPE = "currentBagType";
    public static final String BAG_TYPES = "bagTypes";
    private InventoryDisplayMode m_currentDisplayMode;
    private final InventoryDisplayModeView[] m_displayModes;
    public static final String[] FIELDS;
    private final QuestInventoryListener m_questListener;
    
    public ClientBagContainer() {
        super();
        this.m_currentDisplayMode = InventoryDisplayMode.BAGS;
        this.m_displayModes = new InventoryDisplayModeView[] { new InventoryDisplayModeView(InventoryDisplayMode.BAGS), new InventoryDisplayModeView(InventoryDisplayMode.QUEST) };
        this.m_questListener = new QuestInventoryListener() {
            @Override
            public void itemAdded(final QuestItem item) {
                final Item defaultItem = ReferenceItemManager.getInstance().getDefaultItem(item.getRefId());
                ItemFeedbackHelper.sendChatItemAddedMessage(defaultItem, item.getQuantity());
                PropertiesProvider.getInstance().firePropertyValueChanged(ClientBagContainer.this, "currentInventoryView");
            }
            
            @Override
            public void itemRemoved(final QuestItem item) {
                final Item defaultItem = ReferenceItemManager.getInstance().getDefaultItem(item.getRefId());
                ItemFeedbackHelper.sendChatItemRemovedMessage(defaultItem, item.getQuantity());
                PropertiesProvider.getInstance().firePropertyValueChanged(ClientBagContainer.this, "currentInventoryView");
            }
            
            @Override
            public void itemQuantityChanged(final QuestItem item, final int delta) {
                final Item defaultItem = ReferenceItemManager.getInstance().getDefaultItem(item.getRefId());
                if (delta > 0) {
                    ItemFeedbackHelper.sendChatItemAddedMessage(defaultItem, (short)delta);
                }
                else {
                    ItemFeedbackHelper.sendChatItemRemovedMessage(defaultItem, (short)Math.abs(delta));
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(ClientBagContainer.this, "currentInventoryView");
            }
        };
    }
    
    @Override
    public String[] getFields() {
        return ClientBagContainer.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        try {
            if (fieldName.equals("currentInventoryView")) {
                switch (this.m_currentDisplayMode) {
                    case BAGS: {
                        return this.getActualizedBagsList(true);
                    }
                    case QUEST: {
                        return this.getQuestInventory();
                    }
                    default: {
                        return null;
                    }
                }
            }
            else {
                if (fieldName.equals("bagTypes")) {
                    return this.m_displayModes;
                }
                if (fieldName.equals("currentBagType")) {
                    return this.m_currentDisplayMode;
                }
                if (fieldName.equals("gemmedItemsView")) {
                    return this.getGemmedItems();
                }
                if (fieldName.equals("recyclableItemsView")) {
                    return this.getRecyclableItems();
                }
                if (fieldName.equals("allItemsView")) {
                    return new AllBagsView(this);
                }
                if (fieldName.equals("bagListWithoutPockets")) {
                    return this.getActualizedBagsList(false);
                }
                if (fieldName.equals("typedBagListWithoutPockets")) {
                    return this.getActualizedBagsList(false);
                }
            }
        }
        catch (Exception e) {
            ClientBagContainer.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        return null;
    }
    
    private AllItemsWithValidatorView getRecyclableItems() {
        return new AllItemsWithValidatorView(this, new RecyclableItemValidator(UIEquipmentFrame.getCharacter()));
    }
    
    private AllItemsWithValidatorView getGemmedItems() {
        return new AllItemsWithValidatorView(this, GemmedItemValidator.INSTANCE);
    }
    
    private StackInventoryView[] getQuestInventory() {
        final StackInventoryView[] inventoryViews = new StackInventoryView[2];
        final QuestInventory inventory = (QuestInventory)UIEquipmentFrame.getCharacter().getInventory(InventoryType.QUEST);
        final StackInventoryView questInventoryView = new StackInventoryView(WakfuTranslator.getInstance().getString("questInventory"));
        final StackInventoryView tokenInventoryView = new StackInventoryView(WakfuTranslator.getInstance().getString("tokenInventory"));
        final ArrayList<QuestItemView> questItems = new ArrayList<QuestItemView>();
        final ArrayList<QuestItemView> tokenItems = new ArrayList<QuestItemView>();
        final ItemType tokenType = ItemTypeManager.getInstance().getItemType(603);
        inventory.forEach(new TObjectProcedure<QuestItem>() {
            @Override
            public boolean execute(final QuestItem questItem) {
                final Item item = new Item();
                item.initializeWithReferenceItem(ReferenceItemManager.getInstance().getReferenceItem(questItem.getRefId()));
                item.setQuantity(questItem.getQuantity());
                final QuestItemView questItemView = new QuestItemView(item);
                if (item.getType().isChildOf((AbstractItemType<AbstractItemType<AbstractItemType>>)tokenType)) {
                    tokenItems.add(questItemView);
                }
                else {
                    questItems.add(questItemView);
                }
                return true;
            }
        });
        questInventoryView.setItems(questItems);
        tokenInventoryView.setItems(tokenItems);
        inventoryViews[0] = tokenInventoryView;
        inventoryViews[1] = questInventoryView;
        return inventoryViews;
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
    
    public void actualizeBagListToXulor() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "bagListWithoutPockets", "currentInventoryView", "allItemsView", "recyclableItemsView", "gemmedItemsView");
    }
    
    private BagView[] getActualizedBagsList() {
        return this.getActualizedBagsList(true);
    }
    
    private BagView[] getActualizedBagsList(final boolean includePockets) {
        final int pocketIncr = includePockets ? 0 : -1;
        final BagView[] containerList = new BagView[6 + pocketIncr];
        final AbstractBag[] myBagList = new AbstractBag[this.m_bagsById.size()];
        this.m_bagsById.getValues(myBagList);
        for (final AbstractBag container : myBagList) {
            final byte position = container.getPosition();
            if (position == 0) {
                if (includePockets) {
                    containerList[0] = ((Bag)container).getBagView();
                }
            }
            else if (position > 0 && position + pocketIncr < containerList.length) {
                containerList[position + pocketIncr] = ((Bag)container).getBagView();
            }
        }
        return containerList;
    }
    
    public byte getFirstFreePlaceIndex(final boolean typedBag) {
        final BagView[] bagsList = this.getActualizedBagsList();
        if (typedBag) {
            for (byte i = 0; i < bagsList.length; ++i) {
                if (!AbstractBag.checkPosition(false, i)) {
                    if (bagsList[i] == null && AbstractBag.checkPosition(typedBag, i)) {
                        return i;
                    }
                }
            }
        }
        for (byte i = 0; i < bagsList.length; ++i) {
            if (bagsList[i] == null && AbstractBag.checkPosition(typedBag, i)) {
                return i;
            }
        }
        return -1;
    }
    
    public ArrayList<Item> getAllCraftUsefullItems(final List<CraftRecipe> recipes) {
        final ArrayList<Item> filteredItems = new ArrayList<Item>();
        for (final Item item : this.getAllItems()) {
            if (item != null) {
                for (final CraftRecipe craftRecipe : recipes) {
                    if (craftRecipe.containsIngredient(item.getReferenceId())) {
                        filteredItems.add(item);
                        break;
                    }
                }
            }
        }
        Collections.sort(filteredItems, new Comparator<Item>() {
            @Override
            public int compare(final Item o1, final Item o2) {
                if (o1.getType().getId() != o2.getType().getId()) {
                    return o1.getType().getId() - o2.getType().getId();
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        return filteredItems;
    }
    
    public int getItemQuantity(final int referenceId) {
        int count = 0;
        for (final Item item : this.getAllWithReferenceId(referenceId)) {
            if (!item.isRent()) {
                count += item.getQuantity();
            }
        }
        return count;
    }
    
    public Item[] getAllItems() {
        final List<Item> items = new ArrayList<Item>();
        final TLongObjectIterator<AbstractBag> it = this.getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag container = it.value();
            for (short i = 0; i < container.getMaximumSize(); ++i) {
                final Item absItem = container.getInventory().getFromPosition(i);
                if (absItem != null) {
                    items.add(absItem);
                }
            }
        }
        final Item[] contents = new Item[items.size()];
        return items.toArray(contents);
    }
    
    public InventoryDisplayMode getCurrentDisplayMode() {
        return this.m_currentDisplayMode;
    }
    
    public void setCurrentDisplayMode(final InventoryDisplayMode currentDisplayMode) {
        this.m_currentDisplayMode = currentDisplayMode;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentBagType", "currentInventoryView");
    }
    
    @Override
    protected void bagAdded(final AbstractBag bag) {
        bag.addObserver(this);
    }
    
    @Override
    protected void bagRemoved(final AbstractBag bag) {
        bag.removeObserver(this);
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        switch (event.getAction()) {
            case ITEM_ADDED:
            case ITEM_ADDED_AT:
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "allItemsView", "recyclableItemsView", "gemmedItemsView");
                break;
            }
        }
    }
    
    public void onInventoriesCreated() {
        final QuestInventory inventory = (QuestInventory)WakfuGameEntity.getInstance().getLocalPlayer().getInventory(InventoryType.QUEST);
        inventory.addListener(this.m_questListener);
    }
    
    public void updateGemmedView() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "gemmedItemsView");
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientBagContainer.class);
        FIELDS = new String[] { "currentInventoryView", "bagListWithoutPockets", "typedBagListWithoutPockets", "currentBagType", "bagTypes" };
    }
}
