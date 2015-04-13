package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import gnu.trove.*;

public class UIMergeGemFrame implements MessageFrame, InventoryObserver, BagContainerListener
{
    protected static final Logger m_logger;
    private static final UIMergeGemFrame m_instance;
    static final int DELAY = 250;
    private static final byte MAX_RECYCLABLE_ITEMS_AT_ONCE = 1;
    private static final String PROGRESS_BAR = "progressBar";
    private static final String BUTTON = "startButton";
    private DialogUnloadListener m_dialogUnloadListener;
    WakfuClientMapInteractiveElement m_interactiveElement;
    private MobileStartPathListener m_listener;
    private ElementMap m_dialogElementMap;
    Item m_gemItem;
    private AbstractReferenceItem m_typeUp;
    private AbstractReferenceItem m_rarityUp;
    private AbstractReferenceItem m_levelUp;
    private ArrayList<Item> m_itemList;
    private boolean m_powderMerge;
    private byte m_currentMergeType;
    
    private UIMergeGemFrame() {
        super();
        this.m_interactiveElement = null;
    }
    
    public void reinitialize() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.getEquipmentInventory().addObserver(this);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().addObserver(this);
        }
        this.cleanItems();
        this.setGemItem(null);
        this.resetGemItemList();
    }
    
    public void clean() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.getEquipmentInventory().removeObserver(this);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().removeObserver(this);
        }
        this.cleanItems();
    }
    
    private void cleanItems() {
        PropertiesProvider.getInstance().removeProperty("mergeGem.item");
        this.setGemItem(null);
    }
    
    public void highLightIngredientSlots(final boolean highlight) {
        final RenderableContainer renderableContainer = (RenderableContainer)this.m_dialogElementMap.getElement("gemItem");
        final Widget container = (Widget)renderableContainer.getInnerElementMap().getElement("ingredientBackgroundContainer");
        container.setStyle(highlight ? "itemSelectedBackground" : "itemBackground");
    }
    
    private void setGemItem(final Item item) {
        this.m_gemItem = item;
        PropertiesProvider.getInstance().setPropertyValue("mergeGem.item", this.m_gemItem);
        this.m_levelUp = this.tryToMerge(this.m_gemItem, GemMergeType.LEVEL_UP);
        PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemLevelUp", this.m_levelUp);
        this.m_rarityUp = this.tryToMerge(this.m_gemItem, GemMergeType.RARITY_UP);
        PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemRarityUp", this.m_rarityUp);
        this.m_typeUp = this.tryToMerge(this.m_gemItem, GemMergeType.GEM_TYPE_UP);
        PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemTypeUp", this.m_typeUp);
        this.setQuantity((short)1, GemMergeType.GEM_TYPE_UP);
        this.setQuantity((short)1, GemMergeType.LEVEL_UP);
        this.setQuantity((short)1, GemMergeType.RARITY_UP);
    }
    
    private void setQuantity(final short quantity, final GemMergeType type) {
        final TextEditor text = this.getTextEditor(type);
        if (text == null) {
            return;
        }
        final AbstractReferenceItem item = this.getItemFromType(type);
        if (item == null) {
            text.setText("0");
            return;
        }
        final int mergeQuantity = this.m_gemItem.getQuantity() / type.getQuantityNeeded();
        final int min = Math.min(mergeQuantity, Math.min(quantity, item.getStackMaximumHeight()));
        text.setText(String.valueOf(min));
    }
    
    private TextEditor getTextEditor(final GemMergeType mergeType) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.getDialogId());
        if (map == null) {
            return null;
        }
        return (TextEditor)map.getElement("quantity" + mergeType.getId());
    }
    
    private void setQuantityToMax(final GemMergeType mergeType) {
        final AbstractReferenceItem refItem = this.getItemFromType(mergeType);
        final short max = (short)Math.min((refItem == null) ? 0 : refItem.getStackMaximumHeight(), this.m_gemItem.getQuantity() / mergeType.getQuantityNeeded());
        this.setQuantity(max, mergeType);
    }
    
    private Item getItemFromList(final int refId) {
        for (int i = 0, size = this.m_itemList.size(); i < size; ++i) {
            final Item item = this.m_itemList.get(i);
            if (item.getReferenceId() == refId) {
                return item;
            }
        }
        return null;
    }
    
    private AbstractReferenceItem getItemFromType(final GemMergeType type) {
        switch (type) {
            case RARITY_UP: {
                return this.m_rarityUp;
            }
            case LEVEL_UP: {
                return this.m_levelUp;
            }
            case GEM_TYPE_UP: {
                return this.m_typeUp;
            }
            default: {
                return null;
            }
        }
    }
    
    private AbstractReferenceItem tryToMerge(final Item item, final GemMergeType type) {
        if (item != null && type.canMergeItem(item.getReferenceItem())) {
            return GemsMergeHelper.computeMergedItem(item.getReferenceItem(), type);
        }
        return null;
    }
    
    @Override
    public void bagAdded(final AbstractBag bag) {
        bag.addObserver(this);
    }
    
    @Override
    public void bagRemoved(final AbstractBag bag) {
        bag.removeObserver(this);
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        switch (event.getAction()) {
            case ITEM_ADDED:
            case ITEM_ADDED_AT:
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT:
            case ITEM_QUANTITY_MODIFIED: {
                final InventoryItemModifiedEvent e = (InventoryItemModifiedEvent)event;
                final Item item = (Item)e.getConcernedItem();
                if (this.m_gemItem != null && item.getReferenceId() == this.m_gemItem.getReferenceId()) {
                    final ArrayList<Item> items = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getAllWithReferenceId(item.getReferenceId());
                    short qty = 0;
                    for (int i = items.size() - 1; i >= 0; --i) {
                        qty += items.get(i).getQuantity();
                    }
                    if (qty >= this.getQuantityNeeded()) {
                        this.m_gemItem.setQuantity(qty);
                        final Item itemInList = this.getItemFromList(this.m_gemItem.getReferenceId());
                        if (itemInList != null) {
                            itemInList.setQuantity(this.m_gemItem.getQuantity());
                        }
                    }
                    else {
                        this.setGemItem(null);
                        this.resetGemItemList();
                    }
                    break;
                }
                if (this.isValidItem(item)) {
                    this.resetGemItemList();
                    break;
                }
                break;
            }
            case CLEARED: {
                final ClientBagContainer bags = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
                if (this.m_gemItem != null && !bags.contains(this.m_gemItem.getUniqueId())) {
                    this.setGemItem(null);
                }
                this.resetGemItemList();
                break;
            }
        }
    }
    
    private void resetGemItemList() {
        final short qtyNeeded = this.getQuantityNeeded();
        final TIntShortHashMap items = new TIntShortHashMap();
        final ArrayList<Item> itemsInBag = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getAllWithValidator(new InventoryContentValidator<Item>() {
            @Override
            public boolean isValid(final Item content) {
                return UIMergeGemFrame.this.isValidItem(content);
            }
        });
        for (int i = itemsInBag.size() - 1; i >= 0; --i) {
            final Item item = itemsInBag.get(i);
            items.adjustOrPutValue(item.getReferenceId(), item.getQuantity(), item.getQuantity());
        }
        final ArrayList<Item> fakeItems = new ArrayList<Item>();
        final TIntShortIterator it = items.iterator();
        while (it.hasNext()) {
            it.advance();
            final short qty = it.value();
            if (qty < qtyNeeded) {
                continue;
            }
            final Item item2 = new FakeItem(ReferenceItemManager.getInstance().getReferenceItem(it.key()));
            item2.setQuantity(qty);
            fakeItems.add(item2);
        }
        Collections.sort(fakeItems, new Comparator<Item>() {
            @Override
            public int compare(final Item o1, final Item o2) {
                final AbstractReferenceItem gem1 = o1.getReferenceItem();
                final AbstractReferenceItem gem2 = o2.getReferenceItem();
                if (gem1.getGemElementType() != gem2.getGemElementType()) {
                    return gem1.getGemElementType().getId() - gem2.getGemElementType().getId();
                }
                if (gem1.getRarity() != gem2.getRarity()) {
                    return gem1.getRarity().compareTo(gem2.getRarity());
                }
                return gem1.getLevel() - gem2.getLevel();
            }
        });
        this.m_itemList = fakeItems;
        PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemList", this.m_itemList);
        this.setQuantity((short)1, GemMergeType.GEM_TYPE_UP);
        this.setQuantity((short)1, GemMergeType.LEVEL_UP);
        this.setQuantity((short)1, GemMergeType.RARITY_UP);
    }
    
    private short getQuantityNeeded() {
        if (this.m_powderMerge) {
            return GemMergeType.GEM_TYPE_UP.getQuantityNeeded();
        }
        return (short)Math.min(GemMergeType.LEVEL_UP.getQuantityNeeded(), GemMergeType.RARITY_UP.getQuantityNeeded());
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (UIFrameMouseKey.isKeyOrMouseMessage(message)) {
            return false;
        }
        switch (message.getId()) {
            case 16848: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int quantity = msg.getIntValue();
                final byte mergeType = msg.getByteValue();
                final GemMergeType type = GemMergeType.getFromId(mergeType);
                this.setQuantity((short)quantity, type);
                return false;
            }
            case 16849: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final byte mergeType2 = msg.getByteValue();
                final GemMergeType type2 = GemMergeType.getFromId(mergeType2);
                this.setQuantityToMax(type2);
                return false;
            }
            case 16846: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final long itemUID = msg.getLongValue();
                if (this.m_gemItem != null && this.m_gemItem.getUniqueId() == itemUID) {
                    this.setGemItem(null);
                }
                this.highLightIngredientSlots(true);
                return false;
            }
            case 16845: {
                if (PropertiesProvider.getInstance().getBooleanProperty("mergeGem.running")) {
                    return false;
                }
                final UIItemMessage msg2 = (UIItemMessage)message;
                final Item item = msg2.getItem();
                if (item != this.m_gemItem) {
                    return false;
                }
                this.setGemItem(null);
                PropertiesProvider.getInstance().firePropertyValueChanged(item, "usedInCurrentRecipe");
                return false;
            }
            case 16844: {
                final UIItemMessage msg2 = (UIItemMessage)message;
                final Item item = msg2.getItem();
                if (PropertiesProvider.getInstance().getBooleanProperty("mergeGem.running")) {
                    return false;
                }
                if (!this.isValidItem(item)) {
                    return false;
                }
                if (!(item instanceof FakeItem)) {
                    return false;
                }
                this.setGemItem(item);
                PropertiesProvider.getInstance().firePropertyValueChanged(item, "usedInCurrentRecipe");
                this.highLightIngredientSlots(false);
                return false;
            }
            case 16840: {
                final UIStartCraftMessage msg3 = (UIStartCraftMessage)message;
                this.m_currentMergeType = msg3.getByteValue();
                final GemMergeType mergeType3 = GemMergeType.getFromId(this.m_currentMergeType);
                final TextEditor textEditor = this.getTextEditor(mergeType3);
                short quantity2;
                if (textEditor == null || textEditor.getText().length() == 0) {
                    quantity2 = 1;
                }
                else {
                    quantity2 = PrimitiveConverter.getShort(textEditor.getText(), (short)1);
                }
                if (this.m_gemItem != null) {
                    final GemMergeRequestMessage netmsg = new GemMergeRequestMessage();
                    netmsg.setGemRefId(this.m_gemItem.getReferenceId());
                    netmsg.setQuantity(quantity2);
                    netmsg.setMergeType(this.m_currentMergeType);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netmsg);
                    PropertiesProvider.getInstance().setPropertyValue("mergeGem.running", true);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void setPowderMerge(final boolean powderMerge) {
        this.m_powderMerge = powderMerge;
    }
    
    public String getDialogId() {
        return this.m_powderMerge ? "mergePowderDialog" : "mergeGemDialog";
    }
    
    private boolean isValidItem(final Item item) {
        if (this.m_powderMerge) {
            return GemMergeType.GEM_TYPE_UP.canMergeItem(item.getReferenceItem());
        }
        return GemMergeType.LEVEL_UP.canMergeItem(item.getReferenceItem()) || GemMergeType.RARITY_UP.canMergeItem(item.getReferenceItem());
    }
    
    private static boolean possessesItem(final long itemId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.getEquipmentInventory().containsUniqueId(itemId) || localPlayer.getBags().contains(itemId);
    }
    
    public void onMergeResult(final boolean success) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.getDialogId());
        if (map != null) {
            final ProgressBar progressBar = (ProgressBar)map.getElement("progressBar" + this.m_currentMergeType);
            final ToggleButton button = (ToggleButton)map.getElement("startButton" + this.m_currentMergeType);
            final ParticleDecorator particleDecorator = new ParticleDecorator();
            particleDecorator.onCheckOut();
            particleDecorator.setAlignment(Alignment9.CENTER);
            particleDecorator.setLevel(1);
            particleDecorator.setFile(success ? "6001038.xps" : "6001039.xps");
            particleDecorator.setUseParentScissor(true);
            particleDecorator.setRemovable(false);
            button.getAppearance().add(particleDecorator);
            progressBar.setTweenDuration(0L);
            progressBar.setValue(0.0f);
        }
        PropertiesProvider.getInstance().setPropertyValue("mergeGem.running", false);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_listener = new MobileStartPathListener() {
                @Override
                public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                    WakfuGameEntity.getInstance().removeFrame(UIMergeGemFrame.getInstance());
                }
            };
            character.getActor().addStartPathListener(this.m_listener);
            final String dialogId = this.getDialogId();
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals(dialogId)) {
                        WakfuGameEntity.getInstance().removeFrame(UIMergeGemFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.reinitialize();
            final EventDispatcher dialog = Xulor.getInstance().load(dialogId, Dialogs.getDialogPath(dialogId), 32768L, (short)10000);
            if (dialog != null) {
                this.m_dialogElementMap = dialog.getElementMap();
            }
            Xulor.getInstance().putActionClass("wakfu.mergeGem", MergeGemDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("mergeGem.running", false);
            PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemLevelUp.quantityNeeded", GemMergeType.LEVEL_UP.getQuantityNeeded() + "x");
            PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemRarityUp.quantityNeeded", GemMergeType.RARITY_UP.getQuantityNeeded() + "x");
            PropertiesProvider.getInstance().setPropertyValue("mergeGem.itemTypeUp.quantityNeeded", GemMergeType.GEM_TYPE_UP.getQuantityNeeded() + "x");
            this.setQuantity((short)1, GemMergeType.GEM_TYPE_UP);
            this.setQuantity((short)1, GemMergeType.LEVEL_UP);
            this.setQuantity((short)1, GemMergeType.RARITY_UP);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeStartListener(this.m_listener);
            this.m_listener = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload(this.getDialogId());
            Xulor.getInstance().removeActionClass("wakfu.mergeGem");
            this.clean();
            if (this.m_interactiveElement != null) {
                this.m_interactiveElement.setState((short)1);
                this.m_interactiveElement.notifyViews();
            }
        }
    }
    
    public void setInteractiveElement(final WakfuClientMapInteractiveElement interactiveElement) {
        this.m_interactiveElement = interactiveElement;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public static UIMergeGemFrame getInstance() {
        return UIMergeGemFrame.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIMergeGemFrame.class);
        m_instance = new UIMergeGemFrame();
    }
}
