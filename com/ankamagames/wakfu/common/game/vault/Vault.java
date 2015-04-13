package com.ankamagames.wakfu.common.game.vault;

import com.ankamagames.wakfu.common.game.inventory.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;

public class Vault implements WalletHandler, ItemInventoryHandler
{
    private static final Logger m_logger;
    private final long m_ownerId;
    private final ArrayInventory<Item, RawInventoryItem> m_inventory;
    private final VaultUpgrade m_upgrade;
    private int m_amountOfCash;
    
    public Vault(final long ownerId, @NotNull final VaultUpgrade upgrade) {
        super();
        this.m_ownerId = ownerId;
        this.m_upgrade = upgrade;
        this.m_inventory = new ArrayInventory<Item, RawInventoryItem>(VaultContentProvider.INSTANCE, VaultContentChecker.INSTANCE, this.m_upgrade.getSize(), true);
    }
    
    public int getAmountOfCash() {
        return this.m_amountOfCash;
    }
    
    @NotNull
    public VaultUpgrade getUpgrade() {
        return this.m_upgrade;
    }
    
    @Override
    public boolean canAddCash(final int cash) {
        if (cash < 0) {
            return false;
        }
        final long realAmount = this.m_amountOfCash + cash;
        final int clampedAmount = MathHelper.ensurePositiveInt(realAmount);
        return clampedAmount == realAmount;
    }
    
    @Override
    public boolean addAmount(final int cash) {
        this.m_amountOfCash += cash;
        return true;
    }
    
    @Override
    public boolean canSubtractCash(final int cash) {
        if (cash < 0) {
            return false;
        }
        final long realAmount = this.m_amountOfCash - cash;
        return realAmount >= 0L;
    }
    
    @Override
    public boolean subtractAmount(final int cash) {
        this.m_amountOfCash -= cash;
        return true;
    }
    
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    @Override
    public boolean canAdd(final Item item) {
        return (!item.isBound() || !item.getBind().getType().isCharacter()) && this.m_inventory.canAdd(item);
    }
    
    @Override
    public boolean add(final Item item) {
        try {
            return this.canAdd(item) && this.m_inventory.add(item);
        }
        catch (InventoryException e) {
            Vault.m_logger.error((Object)"[VAULT] Impossible d'ajouter un item ? l'inventaire alors qu'on ? pourtant test? le canAdd", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canAdd(final Item item, final byte position) {
        return this.m_inventory.canAdd(item, position);
    }
    
    @Override
    public boolean add(final Item item, final byte position) {
        try {
            return this.canAdd(item, position) && this.m_inventory.addAt(item, position);
        }
        catch (InventoryException e) {
            Vault.m_logger.error((Object)"[VAULT] Impossible d'ajouter un item ? l'inventaire alors qu'on ? pourtant test? le canAdd", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canRemove(final Item item) {
        return this.m_inventory.canRemove(item);
    }
    
    @Override
    public boolean remove(final Item item) {
        return this.canRemove(item) && this.m_inventory.remove(item);
    }
    
    @Override
    public boolean canRemove(final long itemUid, final short qty) {
        return this.m_inventory.canRemove(itemUid, qty);
    }
    
    @Override
    public boolean remove(final long itemUid, final short qty) {
        return this.canRemove(itemUid, qty) && this.m_inventory.updateQuantity(itemUid, (short)(-qty));
    }
    
    @Override
    public boolean isRemote() {
        return false;
    }
    
    @Override
    public Item getItem(final long uniqueId) {
        return this.m_inventory.getWithUniqueId(uniqueId);
    }
    
    @Override
    public Item getItemFromPosition(final byte position) {
        return this.m_inventory.getFromPosition(position);
    }
    
    public int size() {
        return this.m_inventory.size();
    }
    
    public short getMaximumSize() {
        return this.m_inventory.getMaximumSize();
    }
    
    @Override
    public byte getPosition(final long uniqueId) {
        return (byte)this.m_inventory.getPosition(uniqueId);
    }
    
    public Iterator<Item> iterator() {
        return this.m_inventory.iterator(false);
    }
    
    public void toRaw(final RawVault rawVault) {
        for (final Item item : this.m_inventory) {
            final RawVault.Item innerItem = new RawVault.Item();
            innerItem.position = this.m_inventory.getPosition(item.getUniqueId());
            item.toRaw(innerItem.item);
            rawVault.items.add(innerItem);
        }
        rawVault.money = this.m_amountOfCash;
    }
    
    public void fromRaw(final RawVault rawVault) {
        this.m_inventory.destroyAll();
        final VaultContentProvider provider = (VaultContentProvider)this.m_inventory.getContentProvider();
        for (int i = 0, size = rawVault.items.size(); i < size; ++i) {
            final RawVault.Item innerItem = rawVault.items.get(i);
            final Item item = provider.unSerializeContent(innerItem.item);
            try {
                this.m_inventory.addAt(item, innerItem.position);
            }
            catch (InventoryException e) {
                Vault.m_logger.error((Object)"[STORAGE_BOX] Probl\u00e8me de r\u00e9cup\u00e9ration d'un item de compartiment", (Throwable)e);
            }
        }
        this.m_amountOfCash = rawVault.money;
    }
    
    @Override
    public String toString() {
        return "Vault{m_inventory=" + this.m_inventory + '}';
    }
    
    public void clearInventory() {
        this.m_inventory.destroyAll();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Vault.class);
    }
}
