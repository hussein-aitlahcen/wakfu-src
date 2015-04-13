package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;
import gnu.trove.*;

public class UIHavenWorldResourcesCollectorFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIHavenWorldResourcesCollectorFrame m_instance;
    private HavenWorldResourcesCollectorView m_havenWorldResourcesCollectorView;
    private HavenWorldResourcesCollector m_resourcesCollector;
    private int m_maxItemCount;
    private DialogCloseRequestListener m_dialogCloseListener;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIHavenWorldResourcesCollectorFrame getInstance() {
        return UIHavenWorldResourcesCollectorFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19363: {
                final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldResourcesCollectorConfirm", this.m_havenWorldResourcesCollectorView.getCurrentResourcesQuantity());
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final HavenWorldResourcesCollectorRequestMessage havenWorldResourcesCollectorRequestMessage = new HavenWorldResourcesCollectorRequestMessage(UIHavenWorldResourcesCollectorFrame.this.m_resourcesCollector.getId());
                            final TLongObjectIterator<FakeItem> it = UIHavenWorldResourcesCollectorFrame.this.m_havenWorldResourcesCollectorView.getItems().iterator();
                            while (it.hasNext()) {
                                it.advance();
                                final FakeItem fakeItem = it.value();
                                havenWorldResourcesCollectorRequestMessage.addItem(fakeItem.getUniqueId(), fakeItem.getQuantity());
                            }
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldResourcesCollectorRequestMessage);
                            UIHavenWorldResourcesCollectorFrame.this.m_havenWorldResourcesCollectorView.clearItems();
                        }
                    }
                });
                return false;
            }
            case 19365: {
                this.m_havenWorldResourcesCollectorView.removeItem(((UIFakeItemMessage)message).getFakeItem());
                return false;
            }
            case 19364: {
                if (this.m_havenWorldResourcesCollectorView.size() >= this.m_maxItemCount) {
                    final String errorMsg = WakfuTranslator.getInstance().getString("maxItemsCountReached");
                    final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                    chatErrorMsg.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                    return false;
                }
                final UIItemMessage uiItemMessage = (UIItemMessage)message;
                final long itemUid = uiItemMessage.getLongValue();
                final short quantity = uiItemMessage.getQuantity();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final ClientBagContainer bags = localPlayer.getBags();
                final Item item = (uiItemMessage.getItem() == null) ? bags.getItemFromInventories(itemUid) : uiItemMessage.getItem();
                final AbstractReferenceItem ref = item.getReferenceItem();
                if (item.isRent() || (ref.getCriterion(ActionsOnItem.DROP) != null && !ref.getCriterion(ActionsOnItem.DROP).isValid(localPlayer, -1, ref, localPlayer.getEffectContext()))) {
                    UIHavenWorldResourcesCollectorFrame.m_logger.warn((Object)"Impossible d'ajouter l'item");
                    final String errorMsg2 = WakfuTranslator.getInstance().getString("storageBox.addItem.unauthorized");
                    final ChatMessage chatErrorMsg2 = new ChatMessage(errorMsg2);
                    chatErrorMsg2.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg2);
                    return false;
                }
                this.m_havenWorldResourcesCollectorView.addItem(item, quantity);
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
            if (this.m_resourcesCollector == null || this.m_havenWorldResourcesCollectorView == null) {
                return;
            }
            PropertiesProvider.getInstance().setPropertyValue("havenWorldResourcesCollector", this.m_havenWorldResourcesCollectorView);
            this.m_dialogCloseListener = new DialogCloseRequestListener() {
                @Override
                public int onDialogCloseRequest(final String id) {
                    if (!UIHavenWorldResourcesCollectorFrame.getInstance().getHavenWorldResourcesCollectorView().isEmpty()) {
                        final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldResourcesCollectorClose");
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
                    if (id.equals("havenWorldResourcesCollectorDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIHavenWorldResourcesCollectorFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("havenWorldResourcesCollectorDialog", Dialogs.getDialogPath("havenWorldResourcesCollectorDialog"), 1L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("havenWorldResourcesCollectorDialog");
            final List l = (List)map.getElement("itemList");
            this.m_maxItemCount = l.getIdealSizeMaxColumns() * l.getIdealSizeMaxRows();
            Xulor.getInstance().putActionClass("wakfu.havenWorldResourcesCollector", HavenWorldResourcesCollectorDialogActions.class);
            if (!WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
                UIEquipmentFrame.getInstance().openEquipment();
            }
            else {
                WakfuGameEntity.getInstance().getLocalPlayer().getBags().actualizeBagListToXulor();
            }
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(this.m_resourcesCollector.getNetFrame());
            WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
            PropertiesProvider.getInstance().removeProperty("havenWorldResourcesCollector");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this.m_dialogCloseListener);
            Xulor.getInstance().unload("havenWorldResourcesCollectorDialog");
            Xulor.getInstance().removeActionClass("wakfu.havenWorldResourcesCollector");
        }
    }
    
    public void setResourcesCollector(final HavenWorldResourcesCollector resourcesCollector) {
        this.m_resourcesCollector = resourcesCollector;
    }
    
    public HavenWorldResourcesCollectorView getHavenWorldResourcesCollectorView() {
        return this.m_havenWorldResourcesCollectorView;
    }
    
    public void setHavenWorldResourcesCollectorView(final HavenWorldResourcesCollectorView havenWorldResourcesCollectorView) {
        this.m_havenWorldResourcesCollectorView = havenWorldResourcesCollectorView;
    }
    
    public boolean isItemUsed(final long itemId) {
        return this.m_havenWorldResourcesCollectorView.containsItem(itemId);
    }
    
    public void resetView() {
        PropertiesProvider.getInstance().setPropertyValue("havenWorldResourcesCollector", this.m_havenWorldResourcesCollectorView);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIHavenWorldResourcesCollectorFrame.class);
        UIHavenWorldResourcesCollectorFrame.m_instance = new UIHavenWorldResourcesCollectorFrame();
    }
}
