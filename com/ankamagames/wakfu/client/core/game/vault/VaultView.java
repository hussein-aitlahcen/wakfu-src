package com.ankamagames.wakfu.client.core.game.vault;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.common.game.vault.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.vault.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.configuration.*;

public class VaultView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String CONTENT_FIELD = "content";
    public static final String MONEY_AMOUNT = "moneyAmount";
    public static final String UPGRADE_ARTICLE = "upgradeArticle";
    private Vault m_vault;
    private Article m_vaultUpgradeArticle;
    public static final String[] FIELDS;
    
    public VaultView(final VaultUpgrade upgrade) {
        super();
        this.m_vault = new Vault(WakfuGameEntity.getInstance().getLocalAccount().getAccountId(), upgrade);
        this.initArticle();
    }
    
    private void initArticle() {
        final VaultUpgrade upgrade = this.m_vault.getUpgrade();
        final VaultUpgrade nextUpgrade = upgrade.getNextUpgrade();
        if (nextUpgrade == null) {
            this.setArticle(null);
        }
        else {
            String shopKey = null;
            switch (nextUpgrade) {
                case VAULT_UPGRADE_1: {
                    shopKey = "vaultUpgrade1";
                    break;
                }
                case VAULT_UPGRADE_2: {
                    shopKey = "vaultUpgrade2";
                    break;
                }
                case VAULT_UPGRADE_3: {
                    shopKey = "vaultUpgrade3";
                    break;
                }
                case VAULT_UPGRADE_4: {
                    shopKey = "vaultUpgrade4";
                    break;
                }
                default: {
                    shopKey = null;
                    break;
                }
            }
            if (shopKey != null) {
                ArticlesCache.INSTANCE.loadArticleByKey(shopKey, new ArticlesKeyListener() {
                    @Override
                    public void onArticlesKey(final List<Article> articles) {
                        VaultView.this.setArticle(articles.isEmpty() ? null : articles.get(0));
                    }
                    
                    @Override
                    public void onError() {
                        VaultView.this.setArticle(null);
                    }
                });
            }
            else {
                this.setArticle(null);
            }
        }
    }
    
    void setArticle(final Article article) {
        this.m_vaultUpgradeArticle = article;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "upgradeArticle");
    }
    
    @Override
    public String[] getFields() {
        return VaultView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("content")) {
            if (this.m_vault == null) {
                return null;
            }
            final Collection<FieldProvider> result = new ArrayList<FieldProvider>(this.m_vault.getMaximumSize());
            final FieldProvider placeHolder = new ItemDisplayerImpl.ItemPlaceHolder();
            for (byte i = 0; i < this.m_vault.getMaximumSize(); ++i) {
                final Item item = this.m_vault.getItemFromPosition(i);
                result.add((item == null) ? placeHolder : item.getClone());
            }
            return result;
        }
        else {
            if (fieldName.equals("moneyAmount")) {
                return WakfuTranslator.getInstance().formatNumber(this.m_vault.getAmountOfCash());
            }
            if (fieldName.equals("upgradeArticle")) {
                return this.m_vaultUpgradeArticle;
            }
            return null;
        }
    }
    
    public void depositMoney(final int amount) {
        final VaultMoneyActionRequestMessage msg = new VaultMoneyActionRequestMessage(Math.abs(amount));
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void withdrawMoney(final int amount) {
        final VaultMoneyActionRequestMessage msg = new VaultMoneyActionRequestMessage(-Math.abs(amount));
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void sendAction(final InventoryAction action) {
        final VaultInventoryActionRequestMessage inventoryActionRequestMessage = new VaultInventoryActionRequestMessage(action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(inventoryActionRequestMessage);
    }
    
    public VaultOperationStatus executeAdd(final Item item, final short quantity, final byte position) {
        final InventoryAction action = new InventoryAddItemAction(item.getUniqueId(), quantity, position);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractReferenceItem ref = item.getReferenceItem();
        if (!localPlayer.hasSubscriptionRight(SubscriptionRight.VAULT)) {
            return VaultOperationStatus.BAD_RIGHTS;
        }
        if (ref.getCriterion(ActionsOnItem.DROP) != null && !ref.getCriterion(ActionsOnItem.DROP).isValid(localPlayer, position, ref, localPlayer.getEffectContext())) {
            return VaultOperationStatus.INVENTORY_ERROR;
        }
        if (!this.m_vault.canAdd(item)) {
            VaultView.m_logger.warn((Object)"Impossible d'ajouter l'item");
            return VaultOperationStatus.INVENTORY_ERROR;
        }
        this.sendAction(action);
        return VaultOperationStatus.NO_ERROR;
    }
    
    public VaultOperationStatus executeMove(final long itemUid, final byte destinationPosition) {
        final Item item = this.m_vault.getItemFromPosition(destinationPosition);
        if (item != null && item.getUniqueId() == itemUid) {
            VaultView.m_logger.error((Object)"Impossible de drop un item sur le meme slot");
            return VaultOperationStatus.INVENTORY_ERROR;
        }
        final InventoryAction action = new InventoryMoveItemAction(itemUid, destinationPosition);
        this.sendAction(action);
        return VaultOperationStatus.NO_ERROR;
    }
    
    public VaultOperationStatus executeRemove(final long itemUid, final short quantity, final long destinationUid, final byte destinationPosition) {
        final InventoryAction action = new InventoryRemoveItemAction(itemUid, quantity, destinationUid, destinationPosition);
        this.sendAction(action);
        return VaultOperationStatus.NO_ERROR;
    }
    
    public void clearInventory() {
        this.m_vault.clearInventory();
    }
    
    public void onVaultReceived(final RawVault rawVault) {
        this.m_vault.fromRaw(rawVault);
    }
    
    public boolean contains(final Item item) {
        return this.m_vault.getItem(item.getUniqueId()) != null;
    }
    
    public int getMoney() {
        return this.m_vault.getAmountOfCash();
    }
    
    static {
        m_logger = Logger.getLogger((Class)VaultView.class);
        SystemConfiguration.INSTANCE.addListener(new SystemConfigurationListener() {
            @Override
            public void onLoad() {
                ArticlesCache.INSTANCE.remove("vaultUpgrade1");
                ArticlesCache.INSTANCE.remove("vaultUpgrade2");
                ArticlesCache.INSTANCE.remove("vaultUpgrade3");
                ArticlesCache.INSTANCE.remove("vaultUpgrade4");
            }
        });
        FIELDS = new String[] { "content", "moneyAmount", "upgradeArticle" };
    }
}
