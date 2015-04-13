package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.gem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.ui.*;
import gnu.trove.*;

public class UIImproveGemFrame implements MessageFrame, InventoryObserver, BagContainerListener
{
    protected static final Logger m_logger;
    private static final UIImproveGemFrame m_instance;
    private static final String PROGRESS_BAR = "progressBar";
    private static final String BUTTON = "startButton";
    private static final int HAMMER_ARTICLE_ID = 5008;
    private Article m_hammerArticle;
    private DialogUnloadListener m_dialogUnloadListener;
    private MobileStartPathListener m_listener;
    private ElementMap m_dialogElementMap;
    Item m_gemmedItem;
    byte m_slotIndex;
    ReferenceItem m_slottedGemRefItem;
    GemsUpgradeData m_upgradeData;
    Item m_toolGem1;
    Item m_toolGem2;
    private int m_kamaNeeded;
    private InventoryContentValidator<Item> m_validator;
    private InventoryContentValidator<Item> m_validatorWithHammer;
    private boolean m_running;
    private boolean m_enableFrame;
    
    private UIImproveGemFrame() {
        super();
        this.m_running = false;
        this.m_enableFrame = true;
    }
    
    public void setEnableFrame(final boolean enableFrame) {
        this.m_enableFrame = enableFrame;
    }
    
    private static boolean checkSlottedGem(final Item gemmedItem, final byte index) {
        final int slottedGemRefId = gemmedItem.getGems().getGem(index);
        final ReferenceItem gem = ReferenceItemManager.getInstance().getReferenceItem(slottedGemRefId);
        if (gem.getMetaType() != ItemMetaType.SUB_META_ITEM) {
            return false;
        }
        if (!GemsDefinitionManager.INSTANCE.containsMetaGem(gem.getMetaId())) {
            return false;
        }
        final byte perfectionIndex = GemsDefinitionManager.INSTANCE.getPerfectionIndex(gem);
        return perfectionIndex < 9;
    }
    
    public void openUI(final Item gemmedItem, final byte index) {
        if (!this.m_enableFrame) {
            return;
        }
        if (this.m_running) {
            return;
        }
        if (!checkSlottedGem(gemmedItem, index)) {
            return;
        }
        if (!ItemHelper.checkWePossessTheItem(gemmedItem)) {
            return;
        }
        this.m_gemmedItem = gemmedItem;
        this.m_slotIndex = index;
        this.m_validator = new InventoryContentValidator<Item>() {
            @Override
            public boolean isValid(final Item item) {
                final GemElementType type = item.getReferenceItem().getGemElementType();
                return type == GemElementType.GEM && item.getLevel() >= UIImproveGemFrame.this.m_gemmedItem.getLevel() && UIImproveGemFrame.this.m_upgradeData.isRarityValid(item.getReferenceItem().getRarity()) && (UIImproveGemFrame.this.m_toolGem1 != item || item.getQuantity() != 1) && (UIImproveGemFrame.this.m_toolGem2 != item || item.getQuantity() != 1);
            }
        };
        this.m_validatorWithHammer = new InventoryContentValidator<Item>() {
            @Override
            public boolean isValid(final Item content) {
                return UIImproveGemFrame.this.m_validator.isValid(content) || content.getReferenceId() == 16164 || content.getReferenceId() == 18258;
            }
        };
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.getEquipmentInventory().addObserver(this);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().addObserver(this);
        }
        this.reinitialize();
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().pushFrame(this);
        }
    }
    
    public void reinitialize() {
        final int slottedGemRefId = this.m_gemmedItem.getGems().getGem(this.m_slotIndex);
        this.m_slottedGemRefItem = ReferenceItemManager.getInstance().getReferenceItem(slottedGemRefId);
        final byte perfectionIndex = GemsDefinitionManager.INSTANCE.getPerfectionIndex(this.m_slottedGemRefItem);
        this.m_upgradeData = GemsUpgradeHelper.getNeededItems(perfectionIndex);
        PropertiesProvider.getInstance().setPropertyValue("improveGem.numGems", (byte)((this.m_upgradeData != null) ? this.m_upgradeData.getNumGems() : 0));
        PropertiesProvider.getInstance().setPropertyValue("improveGem.sourceGem", this.m_slottedGemRefItem);
        if (this.m_upgradeData == null) {
            return;
        }
        this.resetGemList();
        this.setToolGem1(null);
        this.setToolGem2(null);
        final short powderLevel = BasicGemsDefinitionManager.trimLevel(this.m_gemmedItem.getLevel());
        final AbstractReferenceItem gem = BasicGemsDefinitionManager.INSTANCE.getGem(GemElementType.GEM, this.m_upgradeData.getRarity(), powderLevel);
        final AbstractReferenceItem hammer = ReferenceItemManager.getInstance().getReferenceItem(16164);
        final ReferenceItem nextGem = GemsDefinitionManager.INSTANCE.getGem(this.m_slottedGemRefItem.getMetaId(), (byte)(perfectionIndex + 1));
        this.m_kamaNeeded = GemsUpgradeHelper.getKamaNeeded(this.m_gemmedItem, perfectionIndex);
        PropertiesProvider.getInstance().setPropertyValue("improveGem.hammer", hammer);
        PropertiesProvider.getInstance().setPropertyValue("improveGem.minGem", gem);
        PropertiesProvider.getInstance().setPropertyValue("improveGem.resultGem", nextGem);
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (WakfuGameEntity.getInstance().getLocalPlayer().getWallet().getAmountOfCash() < this.m_kamaNeeded) {
            sb.openText().addColor(Color.RED);
        }
        sb.append(this.m_kamaNeeded).append(" $");
        PropertiesProvider.getInstance().setPropertyValue("improveGem.kamaCost", sb.finishAndToString());
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
        PropertiesProvider.getInstance().removeProperty("improveGem.tool1");
        PropertiesProvider.getInstance().removeProperty("improveGem.tool2");
        this.m_toolGem1 = null;
        this.m_toolGem2 = null;
    }
    
    public void highLightIngredientSlots(final Item item, final boolean highlight) {
        if (this.m_validatorWithHammer.isValid(item)) {
            this.highlightSlot("tool1", highlight);
        }
        if (this.m_validator.isValid(item)) {
            this.highlightSlot("tool2", highlight);
        }
    }
    
    private void highlightSlot(final String id, final boolean highlight) {
        final RenderableContainer renderableContainer = (RenderableContainer)this.m_dialogElementMap.getElement(id);
        final Widget container = (Widget)renderableContainer.getInnerElementMap().getElement("ingredientBackgroundContainer");
        container.setStyle(highlight ? "itemSelectedBackground" : "itemBackground");
    }
    
    private void resetGemList() {
        final ClientBagContainer bags = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
        final ArrayList<Item> items = bags.getAllWithValidator(this.m_validatorWithHammer);
        PropertiesProvider.getInstance().setPropertyValue("improveGem.gemsList", items);
    }
    
    private void checkGemList() {
        if (this.m_toolGem1 == null || this.m_toolGem1.getQuantity() == 1) {
            this.resetGemList();
        }
        if (this.m_toolGem2 == null || this.m_toolGem2.getQuantity() == 1) {
            this.resetGemList();
        }
    }
    
    private void setToolGem1(final Item item) {
        if (item != null && !this.m_validatorWithHammer.isValid(item)) {
            return;
        }
        this.m_toolGem1 = item;
        if (this.m_toolGem1 != null) {
            ImproveGemDialogActions.openItemDetailWindow(this.m_toolGem1);
        }
        PropertiesProvider.getInstance().setPropertyValue("improveGem.tool1", item);
        this.onItemAdded(this.m_toolGem1);
        this.checkReadyStatus();
        this.checkGemList();
    }
    
    private void setToolGem2(final Item item) {
        if (item != null && !this.m_validator.isValid(item)) {
            return;
        }
        if (item != null && this.m_toolGem1 != null && this.m_toolGem1.getReferenceId() == item.getReferenceId() && this.getTotalQuantity(this.m_toolGem1.getReferenceId()) < 2) {
            return;
        }
        this.m_toolGem2 = item;
        PropertiesProvider.getInstance().setPropertyValue("improveGem.tool2", item);
        this.onItemAdded(this.m_toolGem2);
        this.checkReadyStatus();
        this.checkGemList();
    }
    
    private void onItemAdded(final Item item) {
        if (item != null) {
            ImproveGemDialogActions.openItemDetailWindow(item);
        }
    }
    
    private int getTotalQuantity(final int refId) {
        final ArrayList<Item> items = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getAllWithReferenceId(refId);
        int total = 0;
        for (int i = 0, size = items.size(); i < size; ++i) {
            final Item item = items.get(i);
            total += item.getQuantity();
        }
        return total;
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
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT: {
                final InventoryItemModifiedEvent e = (InventoryItemModifiedEvent)event;
                if (e.getConcernedItem() == this.m_gemmedItem) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                    return;
                }
            }
            case ITEM_ADDED:
            case ITEM_ADDED_AT:
            case ITEM_QUANTITY_MODIFIED: {
                final InventoryItemModifiedEvent e = (InventoryItemModifiedEvent)event;
                final Item item = (Item)e.getConcernedItem();
                if (item.getReferenceItem().getGemElementType() != GemElementType.GEM && item.getReferenceId() != 5008) {
                    return;
                }
            }
            case CLEARED: {
                this.resetGemList();
                final ClientBagContainer bags = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
                if (this.m_toolGem1 != null && !bags.contains(this.m_toolGem1.getUniqueId())) {
                    this.setToolGem1(null);
                }
                if (this.m_toolGem2 != null && !bags.contains(this.m_toolGem2.getUniqueId())) {
                    this.setToolGem2(null);
                    break;
                }
                break;
            }
        }
    }
    
    private void checkReadyStatus() {
        PropertiesProvider.getInstance().setPropertyValue("improveGem.ready", this.isReady());
    }
    
    private boolean isReady() {
        if (this.m_running) {
            return false;
        }
        final boolean withHammer = this.m_toolGem1 != null && (this.m_toolGem1.getReferenceId() == 16164 || this.m_toolGem1.getReferenceId() == 18258);
        if (withHammer) {
            if (this.m_toolGem2 == null && this.m_upgradeData.getNumGems() > 1) {
                return false;
            }
        }
        else {
            if (this.m_toolGem1 == null) {
                return false;
            }
            if (this.m_toolGem2 == null && this.m_upgradeData.getNumGems() > 1) {
                return false;
            }
        }
        return WakfuGameEntity.getInstance().getLocalPlayer().getWallet().getAmountOfCash() >= this.m_kamaNeeded;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (UIFrameMouseKey.isKeyOrMouseMessage(message)) {
            return false;
        }
        switch (message.getId()) {
            case 16846: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final long itemUID = msg.getLongValue();
                final Item item = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getItemFromInventories(itemUID);
                this.highLightIngredientSlots(item, true);
                return false;
            }
            case 16845: {
                if (PropertiesProvider.getInstance().getBooleanProperty("improveGem.running")) {
                    return false;
                }
                final UIItemMessage msg2 = (UIItemMessage)message;
                final Item item2 = msg2.getItem();
                final byte slot = msg2.getByteValue();
                switch (slot) {
                    case 1: {
                        if (item2 != this.m_toolGem1) {
                            return false;
                        }
                        this.setToolGem1(null);
                        break;
                    }
                    case 2: {
                        if (item2 != this.m_toolGem2) {
                            return false;
                        }
                        this.setToolGem2(null);
                        break;
                    }
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(item2, "usedInCurrentRecipe");
                return false;
            }
            case 16844: {
                final UIItemMessage msg2 = (UIItemMessage)message;
                final Item item2 = msg2.getItem();
                final byte slot = msg2.getByteValue();
                if (PropertiesProvider.getInstance().getBooleanProperty("improveGem.running")) {
                    return false;
                }
                final long itemId = item2.getUniqueId();
                if (!possessesItem(itemId)) {
                    return false;
                }
                switch (slot) {
                    case 1: {
                        this.setToolGem1(item2);
                        break;
                    }
                    case 2: {
                        this.setToolGem2(item2);
                        break;
                    }
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(item2, "usedInCurrentRecipe");
                this.highlightSlot("tool1", false);
                this.highlightSlot("tool2", false);
                return false;
            }
            case 16840: {
                final boolean withHammer = this.m_toolGem1 != null && (this.m_toolGem1.getReferenceId() == 16164 || this.m_toolGem1.getReferenceId() == 18258);
                if (withHammer) {
                    if (this.m_toolGem2 == null && this.m_upgradeData.getNumGems() > 1) {
                        return false;
                    }
                    final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmImproveGem"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                final GemImproveRequestMessage msg = new GemImproveRequestMessage();
                                msg.setGemmedItemId(UIImproveGemFrame.this.m_gemmedItem.getUniqueId());
                                msg.setSlotIndex(UIImproveGemFrame.this.m_slotIndex);
                                msg.setToolGemRefId((UIImproveGemFrame.this.m_toolGem2 != null) ? UIImproveGemFrame.this.m_toolGem2.getReferenceId() : 0);
                                msg.setWithHammer(true);
                                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                                UIImproveGemFrame.this.m_running = true;
                                PropertiesProvider.getInstance().setPropertyValue("improveGem.running", true);
                                UIImproveGemFrame.this.checkReadyStatus();
                            }
                        }
                    });
                }
                else {
                    if (this.m_toolGem1 == null) {
                        return false;
                    }
                    if (this.m_toolGem2 == null && this.m_upgradeData.getNumGems() > 1) {
                        return false;
                    }
                    final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmImproveGem"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                final GemImproveRequestMessage msg = new GemImproveRequestMessage();
                                msg.setGemmedItemId(UIImproveGemFrame.this.m_gemmedItem.getUniqueId());
                                msg.setSlotIndex(UIImproveGemFrame.this.m_slotIndex);
                                msg.setToolGemRefId(UIImproveGemFrame.this.m_toolGem1.getReferenceId());
                                msg.setToolGemRefId2((UIImproveGemFrame.this.m_toolGem2 != null) ? UIImproveGemFrame.this.m_toolGem2.getReferenceId() : 0);
                                msg.setWithHammer(false);
                                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                                UIImproveGemFrame.this.m_running = true;
                                PropertiesProvider.getInstance().setPropertyValue("improveGem.running", true);
                                UIImproveGemFrame.this.checkReadyStatus();
                            }
                        }
                    });
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private static boolean possessesItem(final long itemId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.getEquipmentInventory().containsUniqueId(itemId) || localPlayer.getBags().contains(itemId);
    }
    
    public void onImproveResult() {
        this.m_running = false;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("improveGemDialog");
        if (map != null) {
            final ProgressBar progressBar = (ProgressBar)map.getElement("progressBar");
            final ToggleButton button = (ToggleButton)map.getElement("startButton");
            final ParticleDecorator particleDecorator = new ParticleDecorator();
            particleDecorator.onCheckOut();
            particleDecorator.setAlignment(Alignment9.CENTER);
            particleDecorator.setLevel(1);
            particleDecorator.setFile("6001038.xps");
            particleDecorator.setUseParentScissor(true);
            particleDecorator.setRemovable(false);
            button.getAppearance().add(particleDecorator);
            progressBar.setTweenDuration(0L);
            progressBar.setValue(0.0f);
        }
        this.reinitialize();
        if (this.m_upgradeData == null) {
            WakfuGameEntity.getInstance().removeFrame(this);
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("improveGem.running", false);
    }
    
    public void buyHammerArticle() {
        if (this.m_hammerArticle != null) {
            UIWebShopFrame.getInstance().openArticleDialog(this.m_hammerArticle);
        }
    }
    
    private void loadHammerArticle() {
        final TIntArrayList list = new TIntArrayList();
        list.add(5008);
        ArticlesIdsLoader.INSTANCE.getArticlesByIds(list, new ArticlesIdsListener() {
            @Override
            public void onArticlesIds(final ArrayList<Article> articles) {
                UIImproveGemFrame.this.m_hammerArticle = (articles.isEmpty() ? null : articles.get(0));
                PropertiesProvider.getInstance().setPropertyValue("improveGem.hammerArticle", UIImproveGemFrame.this.m_hammerArticle);
            }
            
            @Override
            public void onError() {
                UIImproveGemFrame.m_logger.warn((Object)"Probl\u00e8me \u00e0 la r\u00e9ception de l'article du marteau de forgemage.");
            }
        });
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_listener = new MobileStartPathListener() {
                @Override
                public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                    WakfuGameEntity.getInstance().removeFrame(UIImproveGemFrame.getInstance());
                }
            };
            character.getActor().addStartPathListener(this.m_listener);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("improveGemDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIImproveGemFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.reinitialize();
            final EventDispatcher dialog = Xulor.getInstance().load("improveGemDialog", Dialogs.getDialogPath("improveGemDialog"), 32768L, (short)10000);
            if (dialog != null) {
                this.m_dialogElementMap = dialog.getElementMap();
            }
            Xulor.getInstance().putActionClass("wakfu.improveGem", ImproveGemDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("improveGem.running", false);
            UIWebShopFrame.getInstance().requestLockForUI("improveGemDialog");
            this.loadHammerArticle();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeStartListener(this.m_listener);
            this.m_listener = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("improveGemDialog");
            Xulor.getInstance().removeActionClass("wakfu.improveGem");
            this.clean();
            this.m_running = false;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public static UIImproveGemFrame getInstance() {
        return UIImproveGemFrame.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIImproveGemFrame.class);
        m_instance = new UIImproveGemFrame();
    }
}
