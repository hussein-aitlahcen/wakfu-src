package com.ankamagames.wakfu.client.core.game.storageBox;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.storageBox.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.storageBox.*;
import com.ankamagames.wakfu.client.core.game.storageBox.guild.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;

public class CompartmentViewImpl extends ImmutableFieldProvider implements CompartmentView
{
    protected static final Logger m_logger;
    public static final String ITEM_NEEDED_FIELD = "itemNeeded";
    public static final String CONTENT_FIELD = "content";
    public static final String UNLOCKED_FIELD = "unlocked";
    public static final String CAN_BE_UNLOCKED_FIELD = "canBeUnlocked";
    public static final String UNLOCKING_TEXT_FIELD = "unlockingText";
    public static final String SHORT_UNLOCKING_TEXT_FIELD = "shortUnlockingText";
    public static final String INDEX_FIELD = "index";
    public static final String SIZE_FIELD = "size";
    public static final String ICON_URL = "iconUrl";
    public static final String DISPLAYED = "displayed";
    public static final String NAME = "name";
    public static final String[] FIELDS;
    private byte m_index;
    private StorageBoxCompartment m_inventory;
    private final IEStorageBoxParameter.Compartment m_compartment;
    
    public CompartmentViewImpl(final IEStorageBoxParameter.Compartment compartment, final byte index) {
        super();
        this.m_index = 0;
        this.m_compartment = compartment;
        this.m_index = index;
    }
    
    public CompartmentViewImpl(final IEStorageBoxParameter.Compartment compartment, final byte index, final StorageBoxCompartment inventoryCompartment) {
        super();
        this.m_index = 0;
        this.m_compartment = compartment;
        this.m_inventory = inventoryCompartment;
        this.m_index = index;
    }
    
    @Override
    public String[] getFields() {
        return CompartmentViewImpl.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("itemNeeded")) {
            return this.getItemToUnlock();
        }
        if (fieldName.equals("unlocked")) {
            return this.isUnlocked();
        }
        if (fieldName.equals("canBeUnlocked")) {
            return this.localPlayerHasItemToUnlock();
        }
        if (fieldName.equals("index")) {
            return this.m_index + 1;
        }
        if (fieldName.equals("size")) {
            return this.m_compartment.getCapacity();
        }
        if (fieldName.equals("unlockingText")) {
            return this.getUnlockingText();
        }
        if (fieldName.equals("shortUnlockingText")) {
            return this.getUnlockingText();
        }
        if (fieldName.equals("content")) {
            if (this.m_inventory == null) {
                return null;
            }
            final Collection<FieldProvider> result = new ArrayList<FieldProvider>(this.m_inventory.getMaximumSize());
            final FieldProvider placeHolder = new ItemDisplayerImpl.ItemPlaceHolder();
            for (byte i = 0; i < this.m_inventory.getMaximumSize(); ++i) {
                final Item item = this.m_inventory.getItemFromPosition(i);
                result.add((item == null) ? placeHolder : item.getClone());
            }
            return result;
        }
        else {
            if (fieldName.equals("displayed")) {
                return this.isUnlocked() || this.isSerialized();
            }
            if (fieldName.equals("iconUrl")) {
                return StorageBoxType.HAVEN_BAG.getIconUrl();
            }
            if (fieldName.equals("name")) {
                return StorageBoxType.HAVEN_BAG.getName();
            }
            return null;
        }
    }
    
    private String getUnlockingText() {
        final ReferenceItem itemToUnlock = this.getItemToUnlock();
        if (itemToUnlock == null) {
            return null;
        }
        return WakfuTranslator.getInstance().getString("storageBox.unlockingInfo", itemToUnlock.getName());
    }
    
    public boolean localPlayerHasItemToUnlock() {
        final ReferenceItem itemToUnlock = this.getItemToUnlock();
        return itemToUnlock == null || WakfuGameEntity.getInstance().getLocalPlayer().getBags().getFirstItemFromInventory(itemToUnlock.getId()) != null;
    }
    
    @Override
    public boolean isUnlocked() {
        return this.getItemToUnlock() == null || this.m_inventory != null;
    }
    
    @Override
    public boolean isSerialized() {
        return this.isUnlocked();
    }
    
    @Override
    public void setInventory(final ItemInventoryHandler compartment) {
        this.m_inventory = (StorageBoxCompartment)compartment;
    }
    
    @Override
    public void clearInventory() {
        this.m_inventory = null;
    }
    
    @Override
    public void updateFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, CompartmentViewImpl.FIELDS);
    }
    
    @Override
    public void tryToUnlockCompartment() {
        final ReferenceItem itemToUnlock = this.getItemToUnlock();
        if (!this.localPlayerHasItemToUnlock()) {
            CompartmentViewImpl.m_logger.error((Object)("impossible de d\u00e9bloquer le compartiment d'index=" + this.getIndex()));
            return;
        }
        if (this.isUnlocked()) {
            return;
        }
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("storageBox.unlockQuestion", itemToUnlock.getName()), WakfuConfiguration.getInstance().getItemBigIconUrl(itemToUnlock.getGfxId()), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final UnlockStorageBoxCompartmentRequestMessage request = new UnlockStorageBoxCompartmentRequestMessage();
                    request.setCompartmentId(CompartmentViewImpl.this.getId());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
                    final BrowseStorageBoxOccupation browseStorageBoxOccupation = (BrowseStorageBoxOccupation)WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
                    final StorageBoxCompartment compartment = browseStorageBoxOccupation.createCompartment(CompartmentViewImpl.this.getCompartment());
                    CompartmentViewImpl.this.setInventory(compartment);
                    UIStorageBoxFrame.getInstance().selectCompartment(CompartmentViewImpl.this);
                }
            }
        });
    }
    
    @Override
    public byte getIndex() {
        return this.m_index;
    }
    
    public void sendAction(final InventoryAction action) {
        final StorageBoxInventoryActionRequestMessage inventoryActionRequestMessage = new StorageBoxInventoryActionRequestMessage(this.getId(), action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(inventoryActionRequestMessage);
    }
    
    @Override
    public void select() {
        final SelectStorageBoxCompartmentRequestMessage request = new SelectStorageBoxCompartmentRequestMessage();
        request.setCompartmentId(this.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
    }
    
    @Override
    public int getId() {
        return this.m_compartment.getId();
    }
    
    public ReferenceItem getItemToUnlock() {
        return ReferenceItemManager.getInstance().getReferenceItem(this.m_compartment.getUnlockRefItemId());
    }
    
    public IEStorageBoxParameter.Compartment getCompartment() {
        return this.m_compartment;
    }
    
    public void setInventory(final StorageBoxCompartment inventory) {
        this.m_inventory = inventory;
    }
    
    @Override
    public String toString() {
        return "CompartmentViewImpl{m_index=" + this.m_index + ", m_inventory=" + this.m_inventory + ", m_compartment=" + this.m_compartment + '}';
    }
    
    public boolean canAddItem(final Item item) {
        return this.m_inventory.canAdd(item);
    }
    
    @Override
    public boolean contains(final Item item) {
        return this.m_inventory.getItem(item.getUniqueId()) != null;
    }
    
    public Item getFromPosition(final byte position) {
        return this.m_inventory.getItemFromPosition(position);
    }
    
    @Override
    public GuildStorageOperationStatus executeAdd(final Item item, final short quantity, final byte position) {
        final InventoryAddItemAction action = new InventoryAddItemAction(item.getUniqueId(), quantity, position);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractReferenceItem ref = item.getReferenceItem();
        if (ref.getCriterion(ActionsOnItem.DROP) != null && !ref.getCriterion(ActionsOnItem.DROP).isValid(localPlayer, position, ref, localPlayer.getEffectContext())) {
            return GuildStorageOperationStatus.BAD_RIGHTS;
        }
        if (!this.canAddItem(item)) {
            CompartmentViewImpl.m_logger.warn((Object)"Impossible d'ajouter l'item");
            return GuildStorageOperationStatus.INVENTORY_ERROR;
        }
        this.sendAction(action);
        return GuildStorageOperationStatus.NO_ERROR;
    }
    
    @Override
    public GuildStorageOperationStatus executeMove(final long itemUid, final byte destinationPosition) {
        final Item item = this.getFromPosition(destinationPosition);
        if (item != null && item.getUniqueId() == itemUid) {
            CompartmentViewImpl.m_logger.error((Object)"Impossible de drop un item sur le meme slot");
            return GuildStorageOperationStatus.INVENTORY_ERROR;
        }
        final InventoryAction action = new InventoryMoveItemAction(itemUid, destinationPosition);
        this.sendAction(action);
        return GuildStorageOperationStatus.NO_ERROR;
    }
    
    @Override
    public GuildStorageOperationStatus executeRemove(final long itemUid, final short quantity, final long destinationUid, final byte destinationPosition) {
        final InventoryAction action = new InventoryRemoveItemAction(itemUid, quantity, destinationUid, destinationPosition);
        this.sendAction(action);
        return GuildStorageOperationStatus.NO_ERROR;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompartmentViewImpl.class);
        FIELDS = new String[] { "itemNeeded", "content", "unlocked", "canBeUnlocked", "index", "size", "unlockingText", "displayed" };
    }
}
