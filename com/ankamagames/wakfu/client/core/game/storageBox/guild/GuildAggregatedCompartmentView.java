package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.storageBox.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import java.util.concurrent.atomic.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.guildStorage.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;

public class GuildAggregatedCompartmentView extends ImmutableFieldProvider implements CompartmentView
{
    public static final String CONTENT_FIELD = "content";
    public static final String UNLOCKED_FIELD = "unlocked";
    public static final String ENABLED_FIELD = "enabled";
    public static final String INDEX_FIELD = "index";
    public static final String SIZE_FIELD = "size";
    public static final String UNLOCKING_TEXT_FIELD = "unlockingText";
    public static final String SHORT_UNLOCKING_TEXT_FIELD = "shortUnlockingText";
    public static final String ITEM_NEEDED_FIELD = "itemNeeded";
    public static final String ICON_URL = "iconUrl";
    public static final String DISPLAYED = "displayed";
    public static final String NAME = "name";
    public static final String[] FIELDS;
    private final GuildAggregatedCompartment m_compartment;
    private final GuildStorageCompartmentLinkType m_guildStorageCompartmentLinkType;
    
    public GuildAggregatedCompartmentView(final GuildStorageCompartmentLinkType linkType) {
        super();
        this.m_compartment = new GuildAggregatedCompartment();
        this.m_guildStorageCompartmentLinkType = linkType;
    }
    
    @Override
    public String[] getFields() {
        return GuildAggregatedCompartmentView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("unlocked")) {
            return this.isUnlocked();
        }
        if (fieldName.equals("index")) {
            return this.m_guildStorageCompartmentLinkType.getTypeIndex();
        }
        if (fieldName.equals("size")) {
            if (this.m_compartment == null) {
                return null;
            }
            return this.m_compartment.getMaximumSize();
        }
        else {
            if (fieldName.equals("enabled")) {
                return this.m_compartment.isEnabled();
            }
            if (fieldName.equals("unlockingText")) {
                switch (this.getUnlockType()) {
                    case HAVEN_WORLD_HOUSE: {
                        return StorageBoxType.HOUSE.getLongUnlockDescription();
                    }
                    case HAVEN_WORLD_MANSION: {
                        return StorageBoxType.MANSION.getLongUnlockDescription();
                    }
                    case HAVEN_WORLD_GUILD: {
                        return StorageBoxType.GUILD.getLongUnlockDescription();
                    }
                    case COLLECT: {
                        return StorageBoxType.THIEF.getLongUnlockDescription();
                    }
                }
            }
            if (fieldName.equals("shortUnlockingText")) {
                switch (this.getUnlockType()) {
                    case HAVEN_WORLD_HOUSE: {
                        return StorageBoxType.HOUSE.getShortUnlockDescription();
                    }
                    case HAVEN_WORLD_MANSION: {
                        return StorageBoxType.MANSION.getShortUnlockDescription();
                    }
                    case HAVEN_WORLD_GUILD: {
                        return StorageBoxType.GUILD.getShortUnlockDescription();
                    }
                    case COLLECT: {
                        return StorageBoxType.THIEF.getShortUnlockDescription();
                    }
                }
            }
            if (fieldName.equals("itemNeeded")) {
                return null;
            }
            if (fieldName.equals("content")) {
                if (this.m_compartment == null) {
                    return null;
                }
                final int maximumSize = this.getMaximumSize();
                final Collection<FieldProvider> result = new ArrayList<FieldProvider>(maximumSize);
                final FieldProvider placeHolder = new ItemDisplayerImpl.ItemPlaceHolder();
                final ArrayList<GuildStorageCompartment> inventories = this.m_compartment.getInventories();
                for (int i = 0, size = inventories.size(); i < size; ++i) {
                    final GuildStorageCompartment compartment = inventories.get(i);
                    for (byte j = 0; j < compartment.getMaximumSize(); ++j) {
                        final Item item = compartment.getItemFromPosition(j);
                        FieldProvider itemValue;
                        if (compartment.isEnabled()) {
                            itemValue = ((item == null) ? placeHolder : item.getClone());
                        }
                        else {
                            itemValue = ((item == null) ? new ItemDisplayerImpl.DisabledItem(placeHolder) : new ItemDisplayerImpl.DisabledItem(item));
                        }
                        result.add(itemValue);
                    }
                }
                for (int i = result.size(); i < maximumSize; ++i) {
                    result.add(new ItemDisplayerImpl.DisabledItem(placeHolder));
                }
                return result;
            }
            else {
                if (fieldName.equals("displayed")) {
                    return this.isUnlocked() || this.isSerialized();
                }
                if (!fieldName.equals("iconUrl")) {
                    if (fieldName.equals("name")) {
                        switch (this.getUnlockType()) {
                            case HAVEN_WORLD_HOUSE: {
                                return StorageBoxType.HOUSE.getName();
                            }
                            case HAVEN_WORLD_MANSION: {
                                return StorageBoxType.MANSION.getName();
                            }
                            case HAVEN_WORLD_GUILD: {
                                return StorageBoxType.GUILD.getName();
                            }
                            case COLLECT: {
                                return StorageBoxType.THIEF.getName();
                            }
                        }
                    }
                    return null;
                }
                switch (this.getUnlockType()) {
                    case HAVEN_WORLD_HOUSE: {
                        return StorageBoxType.HOUSE.getIconUrl();
                    }
                    case HAVEN_WORLD_MANSION: {
                        return StorageBoxType.MANSION.getIconUrl();
                    }
                    case HAVEN_WORLD_GUILD: {
                        return StorageBoxType.GUILD.getIconUrl();
                    }
                    case COLLECT: {
                        return StorageBoxType.THIEF.getIconUrl();
                    }
                    default: {
                        return null;
                    }
                }
            }
        }
    }
    
    private int getMaximumSize() {
        final AtomicInteger value = new AtomicInteger(0);
        this.m_guildStorageCompartmentLinkType.forEachType(new TObjectProcedure<GuildStorageCompartmentType>() {
            @Override
            public boolean execute(final GuildStorageCompartmentType object) {
                value.addAndGet(object.m_size);
                return true;
            }
        });
        return value.get();
    }
    
    private GuildStorageCompartmentUnlockType getUnlockType() {
        return GuildStorageCompartmentType.getFromId(this.m_guildStorageCompartmentLinkType.getFirstLinkedCompartmentId()).getUnlockType();
    }
    
    @Override
    public boolean isUnlocked() {
        if (this.m_compartment.getInventories().isEmpty()) {
            final GuildStorageBoxView storageBoxView = (GuildStorageBoxView)UIStorageBoxFrame.getInstance().getStorageBoxBoxView();
            final AtomicBoolean unlocked = new AtomicBoolean(false);
            this.m_guildStorageCompartmentLinkType.forEachType(new TObjectProcedure<GuildStorageCompartmentType>() {
                @Override
                public boolean execute(final GuildStorageCompartmentType type) {
                    if (storageBoxView.isUnlocked(type.m_id)) {
                        unlocked.set(true);
                        return false;
                    }
                    return true;
                }
            });
            return unlocked.get();
        }
        return this.m_compartment.isEnabled();
    }
    
    @Override
    public boolean isSerialized() {
        if (this.m_compartment.getInventories().isEmpty()) {
            final GuildStorageBoxView storageBoxView = (GuildStorageBoxView)UIStorageBoxFrame.getInstance().getStorageBoxBoxView();
            final AtomicBoolean serialized = new AtomicBoolean(false);
            this.m_guildStorageCompartmentLinkType.forEachType(new TObjectProcedure<GuildStorageCompartmentType>() {
                @Override
                public boolean execute(final GuildStorageCompartmentType type) {
                    if (storageBoxView.isSerialized(type.m_id)) {
                        serialized.set(true);
                        return false;
                    }
                    return true;
                }
            });
            return serialized.get();
        }
        return true;
    }
    
    @Override
    public void setInventory(final ItemInventoryHandler compartment) {
        this.m_compartment.addInventoryHandler((GuildStorageCompartment)compartment);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "content", "unlocked", "enabled");
    }
    
    @Override
    public void clearInventory() {
        this.m_compartment.clearInventories();
    }
    
    @Override
    public byte getIndex() {
        return (byte)this.m_guildStorageCompartmentLinkType.ordinal();
    }
    
    private boolean hasRightToRemoveChestItem() {
        final boolean hasRightToRemoveItem = WakfuGuildView.getInstance().localPlayerHasRight(GuildRankAuthorisation.REMOVE_CHEST_ITEM) || WakfuGuildView.getInstance().localPlayerHasRight(this.m_guildStorageCompartmentLinkType.getRemoveAuthorization());
        return hasRightToRemoveItem;
    }
    
    public void sendAction(final InventoryAction action, final GuildStorageCompartment compartment) {
        if (!this.hasRightToRemoveChestItem() && (action.getType() == InventoryActionType.REMOVE_ITEM || action.getType() == InventoryActionType.REMOVE_MONEY)) {
            final String message = WakfuTranslator.getInstance().getString("group.error.accessDenied");
            final String messageBoxIconUrl = WakfuMessageBoxConstants.getMessageBoxIconUrl(4);
            Xulor.getInstance().msgBox(message, messageBoxIconUrl, 515L, 102, 1);
            return;
        }
        final GuildStorageInventoryActionRequestMessage inventoryActionRequestMessage = new GuildStorageInventoryActionRequestMessage(compartment.getType().m_id, action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(inventoryActionRequestMessage);
    }
    
    @Override
    public void select() {
        final GuildSelectStorageCompartmentRequestMessage request = new GuildSelectStorageCompartmentRequestMessage();
        request.setCompartmentId(this.m_guildStorageCompartmentLinkType.getFirstLinkedCompartmentId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
    }
    
    @Override
    public void updateFields() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, GuildAggregatedCompartmentView.FIELDS);
    }
    
    @Override
    public void tryToUnlockCompartment() {
        final GuildStorageCompartmentUnlockRequestMessage request = new GuildStorageCompartmentUnlockRequestMessage();
        request.setCompartmentId(this.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
        UIStorageBoxFrame.getInstance().selectCompartment(this);
    }
    
    @Override
    public String toString() {
        return "GuildAggregatedCompartmentView{m_compartment=" + this.m_compartment + ", m_guildStorageCompartmentLinkType=" + this.m_guildStorageCompartmentLinkType + '}';
    }
    
    public boolean canAddItem(final Item item) {
        return this.m_compartment.canAdd(item);
    }
    
    @Override
    public boolean contains(final Item item) {
        return this.m_compartment.getItem(item.getUniqueId()) != null;
    }
    
    public Item getFromPosition(final byte position) {
        return this.m_compartment.getItemFromPosition(position);
    }
    
    @Override
    public int getId() {
        return this.getIndex();
    }
    
    @Override
    public GuildStorageOperationStatus executeAdd(final Item item, final short quantity, final byte requiredPosition) {
        if (requiredPosition != -1) {
            return this.doExecuteAdd(item, quantity, requiredPosition);
        }
        short remainingQuantity = quantity;
        final short maximumSize = this.m_compartment.getMaximumSize();
        for (byte pos = 0; pos < maximumSize; ++pos) {
            final Item itemAtPos = this.m_compartment.getItemFromPosition(pos);
            if (itemAtPos != null && itemAtPos.canStackWith(item)) {
                if (itemAtPos.getQuantity() != itemAtPos.getStackMaximumHeight()) {
                    final short availableSpace = MathHelper.ensureShort(itemAtPos.getStackMaximumHeight() - itemAtPos.getQuantity());
                    final short stackQuantity = MathHelper.ensureShort(Math.min(remainingQuantity, availableSpace));
                    final GuildStorageOperationStatus status = this.doExecuteAdd(item, stackQuantity, pos);
                    if (status != GuildStorageOperationStatus.NO_ERROR) {
                        return status;
                    }
                    remainingQuantity -= stackQuantity;
                    if (remainingQuantity == 0) {
                        return GuildStorageOperationStatus.NO_ERROR;
                    }
                }
            }
        }
        return this.doExecuteAdd(item, remainingQuantity, this.m_compartment.getFirstAvailableIndex());
    }
    
    private GuildStorageOperationStatus doExecuteAdd(final Item item, final short quantity, final byte position) {
        final byte realPosition = this.m_compartment.getCompartmentIndexFromIndex(position);
        final GuildStorageCompartment compartment = this.m_compartment.getCompartmentFromIndex(position);
        if (!compartment.isEnabled()) {
            return GuildStorageOperationStatus.INVENTORY_ERROR;
        }
        final InventoryAddItemAction action = new InventoryAddItemAction(item.getUniqueId(), quantity, realPosition);
        if (!this.m_compartment.canAdd(item)) {
            return GuildStorageOperationStatus.INVENTORY_ERROR;
        }
        this.sendAction(action, compartment);
        return GuildStorageOperationStatus.NO_ERROR;
    }
    
    @Override
    public GuildStorageOperationStatus executeMove(final long itemUid, final byte destinationPosition) {
        final byte srcPosition = this.m_compartment.getPosition(itemUid);
        final GuildStorageCompartment srcCompartment = this.m_compartment.getCompartmentFromIndex(srcPosition);
        final GuildStorageCompartment destCompartment = this.m_compartment.getCompartmentFromIndex(destinationPosition);
        final byte srcRelativePosition = this.m_compartment.getCompartmentIndexFromIndex(srcPosition);
        final byte destRelativePosition = this.m_compartment.getCompartmentIndexFromIndex(destinationPosition);
        if (srcCompartment == destCompartment) {
            final Item item = this.m_compartment.getItemFromPosition(destinationPosition);
            if (item != null && item.getUniqueId() == itemUid) {
                return GuildStorageOperationStatus.INVENTORY_ERROR;
            }
            final InventoryAction action = new InventoryMoveItemAction(itemUid, destRelativePosition);
            this.sendAction(action, srcCompartment);
        }
        else {
            final GuildMoveItemBetweenCompartmentsRequestMessage moveMsg = new GuildMoveItemBetweenCompartmentsRequestMessage();
            moveMsg.setSrcCompartment(srcCompartment.getType().m_id);
            moveMsg.setDestCompartment(destCompartment.getType().m_id);
            moveMsg.setSrcPosition(srcRelativePosition);
            moveMsg.setDestPosition(destRelativePosition);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(moveMsg);
        }
        return GuildStorageOperationStatus.NO_ERROR;
    }
    
    @Override
    public GuildStorageOperationStatus executeRemove(final long itemUid, final short quantity, final long destinationUid, final byte destinationPosition) {
        final byte position = this.m_compartment.getPosition(itemUid);
        final GuildStorageCompartment compartment = this.m_compartment.getCompartmentFromIndex(position);
        final InventoryAction action = new InventoryRemoveItemAction(itemUid, quantity, destinationUid, destinationPosition);
        this.sendAction(action, compartment);
        return GuildStorageOperationStatus.NO_ERROR;
    }
    
    static {
        FIELDS = new String[] { "content", "enabled", "unlocked", "index", "size", "itemNeeded" };
    }
}
