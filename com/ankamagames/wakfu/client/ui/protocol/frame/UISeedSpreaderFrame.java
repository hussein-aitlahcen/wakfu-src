package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.seedSpreader.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import gnu.trove.*;

public class UISeedSpreaderFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UISeedSpreaderFrame m_instance;
    private FakeItem m_currentItem;
    private SeedSpreader m_spreader;
    private TLongShortHashMap m_itemsAdded;
    private DialogCloseRequestListener m_dialogCloseListener;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UISeedSpreaderFrame() {
        super();
        this.m_itemsAdded = new TLongShortHashMap();
    }
    
    public static UISeedSpreaderFrame getInstance() {
        return UISeedSpreaderFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19344: {
                final SeedSpreaderValidRequestMessage seedSpreaderValidRequestMessage = new SeedSpreaderValidRequestMessage(this.m_spreader.getId());
                final TLongShortIterator it = this.m_itemsAdded.iterator();
                while (it.hasNext()) {
                    it.advance();
                    seedSpreaderValidRequestMessage.addItem(it.key(), it.value());
                }
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(seedSpreaderValidRequestMessage);
                this.m_itemsAdded.clear();
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            case 19342: {
                final UIItemMessage uiItemMessage = (UIItemMessage)message;
                final long itemUid = uiItemMessage.getLongValue();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final ClientBagContainer bags = localPlayer.getBags();
                final Item item = (uiItemMessage.getItem() == null) ? bags.getItemFromInventories(itemUid) : uiItemMessage.getItem();
                if (item == null) {
                    return false;
                }
                if (!this.isMonsterSeedItem(item)) {
                    return false;
                }
                short quantity;
                final short uiItemMessageQuantity = quantity = uiItemMessage.getQuantity();
                if (this.m_currentItem != null) {
                    if (this.m_currentItem.getReferenceId() != item.getReferenceId()) {
                        final String chatMessage = WakfuTranslator.getInstance().getString("seedSpreader.notEmptyError");
                        ChatManager.getInstance().pushMessage(chatMessage, 3);
                        return false;
                    }
                    quantity = (short)Math.min(this.m_currentItem.getStackMaximumHeight(), this.m_currentItem.getQuantity() + uiItemMessageQuantity);
                }
                this.setCurrentItem(item.getReferenceId(), quantity);
                this.m_itemsAdded.put(item.getUniqueId(), uiItemMessageQuantity);
                PropertiesProvider.getInstance().setPropertyValue("seedSpreaderDirty", true);
                return false;
            }
            case 19343: {
                final SeedSpreaderRemoveSeedRequestMessage seedSpreaderRemoveSeedRequestMessage = new SeedSpreaderRemoveSeedRequestMessage(this.m_spreader.getId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(seedSpreaderRemoveSeedRequestMessage);
                this.m_currentItem = null;
                this.m_itemsAdded.clear();
                PropertiesProvider.getInstance().setPropertyValue("seedSpreaderItem", null);
                PropertiesProvider.getInstance().setPropertyValue("seedSpreaderDirty", false);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (this.m_spreader == null) {
                return;
            }
            PropertiesProvider.getInstance().setPropertyValue("seedSpreaderItem", this.m_currentItem);
            PropertiesProvider.getInstance().setPropertyValue("seedSpreaderDirty", false);
            this.m_dialogCloseListener = new DialogCloseRequestListener() {
                @Override
                public int onDialogCloseRequest(final String id) {
                    if (UISeedSpreaderFrame.this.isSeedSpreaderDirty()) {
                        final String msgText = WakfuTranslator.getInstance().getString("question.seedSpreaderClose");
                        final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
                        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                        controler.addEventListener(new MessageBoxEventListener() {
                            @Override
                            public void messageBoxClosed(final int type, final String userEntry) {
                                if (type == 8) {
                                    Xulor.getInstance().unload("havenWorldResourcesCollectorDialog");
                                }
                            }
                        });
                        return 2;
                    }
                    return 0;
                }
            };
            DialogClosesManager.getInstance().addDialogCloseRequestListener(this.m_dialogCloseListener);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("seedSpreaderDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UISeedSpreaderFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("seedSpreaderDialog", Dialogs.getDialogPath("seedSpreaderDialog"), 1L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.seedSpreader", SeedSpreaderDialogActions.class);
            UIEquipmentFrame.getInstance().openEquipment();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            SeedSpreaderDialogActions.setDraggedItem(null);
            this.m_currentItem = null;
            this.m_itemsAdded.clear();
            WakfuGameEntity.getInstance().removeFrame(this.m_spreader.getNetFrame());
            WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
            PropertiesProvider.getInstance().removeProperty("seedSpreaderItem");
            PropertiesProvider.getInstance().removeProperty("seedSpreaderDirty");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("seedSpreaderDialog");
            Xulor.getInstance().removeActionClass("wakfu.seedSpreader");
        }
    }
    
    public void setSpreader(final SeedSpreader spreader) {
        this.m_spreader = spreader;
    }
    
    public FakeItem getCurrentItem() {
        return this.m_currentItem;
    }
    
    public void setCurrentItem(final int referenceId, final short quantity) {
        if (referenceId <= 0) {
            return;
        }
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(referenceId);
        if (referenceItem == null) {
            UISeedSpreaderFrame.m_logger.error((Object)("Impossible de retrouver l'item d'id=" + referenceId));
            return;
        }
        if (this.m_currentItem == null) {
            this.m_currentItem = new FakeItem(referenceItem);
        }
        this.m_currentItem.setQuantity(quantity);
        PropertiesProvider.getInstance().setPropertyValue("seedSpreaderItem", this.m_currentItem);
    }
    
    public boolean isMonsterSeedItem(final Item item) {
        final AbstractItemAction itemAction = item.getReferenceItem().getItemAction();
        if (!(itemAction instanceof SeedItemAction)) {
            return false;
        }
        final SeedItemAction action = (SeedItemAction)itemAction;
        final int resourceId = action.getResourceId();
        final ReferenceResource reference = ReferenceResourceManager.getInstance().getReferenceResource(resourceId);
        return reference != null && reference instanceof MonsterReferenceResource;
    }
    
    public void highLightCustomSlot(final Item item) {
        if (!WakfuGameEntity.getInstance().hasFrame(getInstance())) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("seedSpreaderDialog");
        final boolean isCustomItem = this.isMonsterSeedItem(item);
        final Container container = (Container)map.getElement("seedSlot");
        final Color c = isCustomItem ? new Color(WakfuClientConstants.HIGHLIGHT_COLOR.get()) : Color.RED;
        this.addColorTween(container, c);
    }
    
    private void addColorTween(final Container container, final Color c) {
        final DecoratorAppearance appearance = container.getAppearance();
        final Color c2 = new Color(Color.WHITE.get());
        appearance.removeTweensOfType(ModulationColorTween.class);
        appearance.setModulationColor(c2);
        final ModulationColorTween tween = new ModulationColorTween(c2, c, appearance, 0, 300, -1, TweenFunction.PROGRESSIVE);
        appearance.addTween(tween);
    }
    
    public void resetSlot() {
        if (!WakfuGameEntity.getInstance().hasFrame(getInstance())) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("seedSpreaderDialog");
        final Container container = (Container)map.getElement("seedSlot");
        container.getAppearance().removeTweensOfType(ModulationColorTween.class);
    }
    
    public boolean isSeedSpreaderDirty() {
        return !this.m_itemsAdded.isEmpty();
    }
    
    public boolean isItemUsed(final long uniqueId) {
        return this.m_itemsAdded.contains(uniqueId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UISeedSpreaderFrame.class);
        UISeedSpreaderFrame.m_instance = new UISeedSpreaderFrame();
    }
}
