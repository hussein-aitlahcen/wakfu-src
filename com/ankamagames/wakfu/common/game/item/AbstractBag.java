package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import gnu.trove.*;

public abstract class AbstractBag implements Iterable<Item>, RawConvertible<RawBag>
{
    public static final int DEFAULT_POCKET_BAG_ID = 2175;
    public static final int ALL_BAG_COUNT = 5;
    private static final int TYPED_ONLY_BAG_COUNT = 2;
    private static final Logger m_logger;
    protected long m_uid;
    protected int m_referenceId;
    protected final ArrayInventory<Item, RawInventoryItem> m_inventory;
    protected byte m_position;
    private final InventoryContentChecker<Item> m_baseChecker;
    
    protected AbstractBag(final long uid, final int referenceId, final InventoryContentChecker checker, final short maximumSize) {
        super();
        this.m_uid = uid;
        this.m_inventory = new ArrayInventory<Item, RawInventoryItem>(ReferenceItemManager.getInstance(), checker, maximumSize, true);
        this.m_position = 0;
        this.m_referenceId = referenceId;
        this.m_baseChecker = (InventoryContentChecker<Item>)checker;
        this.setInventoryChecker();
    }
    
    private void setInventoryChecker() {
        final int[] validTypes = BagStoringManager.INSTANCE.getValidTypes(this.m_referenceId);
        if (validTypes != null) {
            this.m_inventory.setContentChecker(new TypedBagInventoryContentChecker(validTypes, this.m_baseChecker));
        }
        else {
            this.m_inventory.setContentChecker(this.m_baseChecker);
        }
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    public byte getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final byte position) {
        this.m_position = position;
    }
    
    public long getUid() {
        return this.m_uid;
    }
    
    public ArrayInventory<Item, RawInventoryItem> getInventory() {
        return this.m_inventory;
    }
    
    public short getMaximumSize() {
        return this.m_inventory.getMaximumSize();
    }
    
    public int size() {
        return this.m_inventory.size();
    }
    
    public boolean simulateUpdateQuantity(final Item addedItem, final short i) {
        return this.m_inventory.simulateUpdateQuantity(addedItem, i);
    }
    
    public boolean simulateUpdateQuantity(final long itemUid, final short i) {
        return this.m_inventory.simulateUpdateQuantity(itemUid, i);
    }
    
    public boolean canAdd(final Item item) {
        return this.m_inventory.canAdd(item);
    }
    
    public boolean canAdd(final Item item, final short position) {
        return this.m_inventory.canAdd(item, position);
    }
    
    public boolean simulateAddWithRemove(final Item item) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return this.m_inventory.simulateAddWithRemove(item);
    }
    
    public void removeAllObservers() {
        this.m_inventory.removeAllObservers();
    }
    
    public void removeObserver(final InventoryObserver observer) {
        if (observer == null) {
            return;
        }
        this.m_inventory.removeObserver(observer);
    }
    
    public void addObserver(final InventoryObserver observer) {
        this.m_inventory.addObserver(observer);
    }
    
    @Override
    public Iterator<Item> iterator() {
        return this.m_inventory.iterator();
    }
    
    public Item getFromPosition(final short i) {
        return this.m_inventory.getFromPosition(i);
    }
    
    @Override
    public boolean toRaw(final RawBag data) {
        data.uniqueId = this.m_uid;
        data.referenceId = this.m_referenceId;
        data.maximumSize = this.m_inventory.getMaximumSize();
        data.position = this.m_position;
        for (final Item item : this.m_inventory) {
            if (item.shouldBeSerialized()) {
                final RawInventoryItemInventory.Content content = new RawInventoryItemInventory.Content();
                content.position = this.m_inventory.getPosition(item.getUniqueId());
                final boolean ok = item.toRaw(content.item);
                if (!ok) {
                    AbstractBag.m_logger.error((Object)("Impossible de convertir l'item \u00e0 la position " + content.position + " sous forme d\u00e9s\u00e9rialis\u00e9e brute"));
                    return false;
                }
                data.inventory.contents.add(content);
            }
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawBag data) {
        this.m_uid = data.uniqueId;
        this.m_referenceId = data.referenceId;
        this.m_position = data.position;
        this.m_inventory.setMaximumSize(data.maximumSize);
        this.setInventoryChecker();
        if (!checkPosition(this.isTyped(), this.m_position)) {
            AbstractBag.m_logger.error((Object)("La position (" + this.m_position + ") du sac " + this.m_uid + " refId=" + this.m_referenceId + " est incorrecte"));
        }
        boolean ok = true;
        for (final RawInventoryItemInventory.Content rawContent : data.inventory.contents) {
            final Item item = this.m_inventory.getContentProvider().unSerializeContent(rawContent.item);
            if (item != null) {
                try {
                    if (this.addAt(item, rawContent.position)) {
                        continue;
                    }
                    ok = false;
                }
                catch (InventoryCapacityReachedException e) {
                    ok = false;
                    AbstractBag.m_logger.error((Object)e);
                }
                catch (ContentAlreadyPresentException e2) {
                    ok = false;
                    AbstractBag.m_logger.error((Object)e2);
                }
                catch (PositionAlreadyUsedException e3) {
                    ok = false;
                    AbstractBag.m_logger.error((Object)e3);
                }
            }
            else {
                AbstractBag.m_logger.error((Object)("D\u00e9s\u00e9rialisation d'un Item null \u00e0 la position " + rawContent.position));
                ok = false;
            }
        }
        return ok;
    }
    
    public int destroyAll() {
        return this.m_inventory.destroyAll();
    }
    
    public void destroyItems(final InventoryContentValidator<Item> validator) {
        this.m_inventory.destroyItems(validator);
    }
    
    public short getFirstFreeIndex() {
        return this.m_inventory.getFirstFreeIndex();
    }
    
    public short getFirstStackableIndeForContent(final Item item) {
        return this.m_inventory.getFirstStackableIndexForContent(item);
    }
    
    public boolean addAt(final Item item, final short index) throws InventoryCapacityReachedException, ContentAlreadyPresentException, PositionAlreadyUsedException {
        return this.addAt(item, index, null);
    }
    
    public boolean addAt(final Item item, final short index, @Nullable final AbstractBagsOperationVisitor visitor) throws InventoryCapacityReachedException, ContentAlreadyPresentException, PositionAlreadyUsedException {
        if (visitor != null) {
            this.m_inventory.addObserver(visitor);
        }
        try {
            return this.m_inventory.addAt(item, index);
        }
        finally {
            if (visitor != null) {
                this.m_inventory.removeObserver(visitor);
            }
        }
    }
    
    public short getPosition(final Item item) {
        return this.m_inventory.getPosition(item);
    }
    
    public short getPosition(final long uid) {
        return this.m_inventory.getPosition(uid);
    }
    
    public int removeAll() {
        return this.m_inventory.removeAll();
    }
    
    public Item removeAt(final short index) {
        return this.m_inventory.removeAt(index);
    }
    
    public boolean remove(final Item item) {
        return this.m_inventory.remove(item);
    }
    
    public boolean isEmpty() {
        return this.m_inventory.isEmpty();
    }
    
    public boolean containsReferenceId(final int id) {
        return this.m_inventory.containsReferenceId(id);
    }
    
    public boolean containsUniqueId(final long uniqueId) {
        return this.m_inventory.containsUniqueId(uniqueId);
    }
    
    public boolean updateQuantity(final long uniqueId, final short quantity) {
        return this.updateQuantity(uniqueId, quantity, null);
    }
    
    public boolean updateQuantity(final long uniqueId, final short quantity, @Nullable final AbstractBagsOperationVisitor visitor) {
        if (visitor != null) {
            this.m_inventory.addObserver(visitor);
        }
        try {
            return this.m_inventory.updateQuantity(uniqueId, quantity);
        }
        finally {
            if (visitor != null) {
                this.m_inventory.removeObserver(visitor);
            }
        }
    }
    
    public Item getWithUniqueId(final long itemUID) {
        return this.m_inventory.getWithUniqueId(itemUID);
    }
    
    public Item getFirstItemWithTypeIn(final int associatedItemType) {
        for (final Item item : this.m_inventory) {
            if (associatedItemType == item.getReferenceItem().getItemType().getId()) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemWithTypeChildOf(final AbstractItemType associatedItemType) {
        for (final Item item : this.m_inventory) {
            if (item.getReferenceItem().getItemType().isChildOf(associatedItemType)) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemWithProperty(final ItemProperty property) {
        for (final Item item : this.m_inventory) {
            if (item.getReferenceItem().hasItemProperty(property)) {
                return item;
            }
        }
        return null;
    }
    
    public InventoryContentChecker<Item> getContentChecker() {
        return this.m_inventory.getContentChecker();
    }
    
    public boolean add(final Item second) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return this.m_inventory.add(second);
    }
    
    public boolean add(final Item item, @Nullable final InventoryObserver visitor) throws InventoryCapacityReachedException, ContentAlreadyPresentException, PositionAlreadyUsedException {
        if (visitor != null) {
            this.m_inventory.addObserver(visitor);
        }
        try {
            return this.m_inventory.add(item);
        }
        finally {
            if (visitor != null) {
                this.m_inventory.removeObserver(visitor);
            }
        }
    }
    
    public boolean contains(final Item item) {
        return this.m_inventory.contains(item);
    }
    
    public Item getFirstWithReferenceId(final int refId) {
        return this.m_inventory.getFirstWithReferenceId(refId);
    }
    
    public Item getFirstWithReferenceId(final int refId, final InventoryContentValidator<Item> validator) {
        return this.m_inventory.getFirstWithReferenceId(refId, validator);
    }
    
    public Item removeWithUniqueId(final long itemUId) {
        return this.m_inventory.removeWithUniqueId(itemUId);
    }
    
    public int getNbFreePlaces() {
        final short size = this.m_inventory.getMaximumSize();
        int nbFreePlaces = 0;
        for (short pos = 0; pos < size; ++pos) {
            if (this.m_inventory.isPositionFree(pos)) {
                ++nbFreePlaces;
            }
        }
        return nbFreePlaces;
    }
    
    public ArrayList<Item> getAllWithReferenceId(final int referenceId) {
        return this.m_inventory.getAllWithReferenceId(referenceId);
    }
    
    public ArrayList<Item> getAllWithReferenceId(final int referenceId, final InventoryContentValidator<Item> validator) {
        return this.m_inventory.getAllWithReferenceId(referenceId, validator);
    }
    
    public ArrayList<Item> getAllWithValidator(final InventoryContentValidator<Item> validator) {
        return this.m_inventory.getAllWithValidator(validator);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append(" ").append(this.getUid()).append("{\n");
        builder.append("\t").append("RefId ").append(this.getReferenceId()).append("\n");
        for (short i = 0; i < this.m_inventory.getMaximumSize(); ++i) {
            final Item item = this.m_inventory.getFromPosition(i);
            builder.append("\t");
            if (item != null) {
                builder.append(i).append(" : ").append(item.getQuantity()).append("x").append(item.getReferenceId()).append("; ");
            }
            else {
                builder.append(i).append(" : ").append("null").append("; ");
            }
            if (i == this.m_inventory.getMaximumSize() / 2 - 1) {
                builder.append("\n");
            }
        }
        builder.append("}");
        return builder.toString();
    }
    
    public final boolean isDefaultPocketBag() {
        return this.getReferenceId() == 2175;
    }
    
    public boolean isTyped() {
        return BagStoringManager.INSTANCE.isTypedBag(this.m_referenceId);
    }
    
    public static boolean checkCriteria(final Item item, final BasicCharacterInfo player) {
        short playerLevel = player.getLevel();
        if (player.hasCharacteristic(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE)) {
            playerLevel += (short)player.getCharacteristicValue(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE);
        }
        if (item.getLevel() > playerLevel) {
            return false;
        }
        final SimpleCriterion equipCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP);
        return equipCriterion == null || equipCriterion.isValid(player, item, null, player.getEffectContext());
    }
    
    public static boolean checkPosition(final boolean isTyped, final byte destinationPosition) {
        final int limit = isTyped ? 5 : 3;
        return destinationPosition >= 0 && destinationPosition <= limit;
    }
    
    public boolean forEachItem(final TObjectProcedure<Item> procedure) {
        return this.m_inventory.forEach(procedure);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractBag.class);
    }
}
