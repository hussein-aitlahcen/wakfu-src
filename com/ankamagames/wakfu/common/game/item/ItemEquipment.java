package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import gnu.trove.*;

public class ItemEquipment extends ArrayInventoryWithoutCheck<Item, RawInventoryItem> implements RawConvertible<RawInventoryItemInventory>, LoggableEntity
{
    public static final int EQUIPMENT_UID = 2;
    private static final Logger m_logger;
    private static final boolean STACKABLE = false;
    private static final boolean SERIALIZE_QUANTITY = false;
    private final TByteHashSet m_lockedPositions;
    
    public ItemEquipment() {
        super(Item.class, (InventoryContentProvider<Item, Object>)EquipmentItemProvider.getInstance(), EquipmentInventoryChecker.getInstance(), EquipmentPosition.getEquipmentSize(), false, false);
        this.m_lockedPositions = new TByteHashSet();
    }
    
    public void lockPosition(final EquipmentPosition position, final boolean lock) {
        if (lock) {
            this.m_lockedPositions.add(position.m_id);
        }
        else {
            this.m_lockedPositions.remove(position.m_id);
        }
    }
    
    public boolean isPositionLocked(final byte position) {
        return this.m_lockedPositions.contains(position);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        this.m_lockedPositions.clear();
    }
    
    @Override
    public final boolean toRaw(final RawInventoryItemInventory raw) {
        if (this.m_serializeQuantity) {
            ItemEquipment.m_logger.warn((Object)"Impossible d'ajouter l'information de quantit\u00e9 \u00e0 un RawInventoryItemInventory qui n'est pas pr\u00e9vu pour");
        }
        raw.clear();
        final TLongShortIterator it = this.m_idxByUniqueId.iterator();
        while (it.hasNext()) {
            it.advance();
            final short pos = it.value();
            try {
                final Item item = ((Item[])this.m_contents)[pos];
                if (!item.shouldBeSerialized()) {
                    continue;
                }
                final RawInventoryItemInventory.Content content = new RawInventoryItemInventory.Content();
                content.position = this.getPosition(item.getUniqueId());
                item.toRaw(content.item);
                raw.contents.add(content);
            }
            catch (ClassCastException e) {
                final Object content2 = ((Item[])this.m_contents)[pos];
                ItemEquipment.m_logger.error((Object)("erreur de cast : content is " + content2.getClass().getName()), (Throwable)e);
            }
            catch (Exception e2) {
                ItemEquipment.m_logger.error((Object)("Exception lev\u00e9e lors de la r\u00e9cup\u00e9ration de l'item \u00e0 la position " + pos + " de l'\u00e9quipement " + this), (Throwable)e2);
            }
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawInventoryItemInventory raw) {
        try {
            this.destroyAll();
            if (this.m_serializeQuantity) {
                ItemEquipment.m_logger.warn((Object)"Impossible de d\u00e9s\u00e9rialiser la quantit\u00e9 d'objets dans un inventaire, cette information n'est pas pr\u00e9sente dans le format RawInventory");
            }
            boolean valid = true;
            for (final RawInventoryItemInventory.Content content : raw.contents) {
                try {
                    final Item item = (Item)this.m_contentProvider.unSerializeContent((R)content.item);
                    if (item != null) {
                        if (((ArrayInventoryWithoutCheck<Item, R>)this).addAt(item, content.position)) {
                            continue;
                        }
                        valid = false;
                    }
                    else {
                        valid = false;
                        ItemEquipment.m_logger.error((Object)"Erreur lors de la d\u00e9-serialisation d'un ArrayInventory : item null");
                    }
                }
                catch (InventoryCapacityReachedException e) {
                    ItemEquipment.m_logger.error((Object)ExceptionFormatter.toString(e));
                    valid = false;
                }
                catch (ContentAlreadyPresentException e2) {
                    ItemEquipment.m_logger.error((Object)ExceptionFormatter.toString(e2));
                    valid = false;
                }
                catch (PositionAlreadyUsedException e3) {
                    ItemEquipment.m_logger.error((Object)ExceptionFormatter.toString(e3));
                    valid = false;
                }
                catch (Exception e4) {
                    ItemEquipment.m_logger.error((Object)ExceptionFormatter.toString(e4));
                    valid = false;
                }
            }
            return valid;
        }
        catch (ClassCastException e5) {
            ItemEquipment.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass() + " \u00e0 partir d'un " + raw.getClass() + " : RawArrayInventory attendu"), (Throwable)e5);
            return false;
        }
    }
    
    @Override
    public String getLogRepresentation() {
        return "equipment";
    }
    
    @Override
    public String toString() {
        return "ItemEquipment{m_lockedPositions=" + this.m_lockedPositions.size() + ", " + super.toString() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemEquipment.class);
    }
}
