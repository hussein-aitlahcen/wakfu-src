package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.collector.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.collector.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import com.ankamagames.wakfu.client.core.game.collector.ui.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.collector.*;

public class UICollectMachineFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UICollectMachineFrame m_instance;
    private AbstractCollectorView m_abstractCollectMachineView;
    private boolean m_isAboutToBeRemoved;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UICollectMachineFrame getInstance() {
        return UICollectMachineFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("equipmentDialog") || id.equals("freeCollectMachineDialog") || (id.equals("lockedCollectMachineDialog") && WakfuGameEntity.getInstance().hasFrame(UICollectMachineFrame.getInstance()))) {
                        WakfuGameEntity.getInstance().removeFrame(UICollectMachineFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.collectMachine", CollectMachineDialogActions.class);
            this.m_isAboutToBeRemoved = true;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeActionClass("wakfu.collectMachine");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            if (WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
            }
            if (Xulor.getInstance().isLoaded("freeCollectMachineDialog")) {
                Xulor.getInstance().unload("freeCollectMachineDialog");
            }
            if (Xulor.getInstance().isLoaded("lockedCollectMachineDialog")) {
                Xulor.getInstance().unload("lockedCollectMachineDialog");
            }
            PropertiesProvider.getInstance().removeProperty("collectMachine");
        }
    }
    
    public void initialize(final CollectorOccupationProvider provider) {
        if (provider == null) {
            UICollectMachineFrame.m_logger.error((Object)"on tent d'afficher une provider null");
            return;
        }
        String dialogId;
        if (provider.getInfo().hasExpected()) {
            this.m_abstractCollectMachineView = new ExpectingCollectMachineView(provider);
            dialogId = "lockedCollectMachineDialog";
        }
        else {
            this.m_abstractCollectMachineView = new FreeCollectorView(provider);
            dialogId = "freeCollectMachineDialog";
        }
        PropertiesProvider.getInstance().setPropertyValue("collectMachine", this.m_abstractCollectMachineView);
        UIEquipmentFrame.getInstance().openEquipment();
        if (!Xulor.getInstance().isLoaded(dialogId)) {
            final Window w = (Window)Xulor.getInstance().load(dialogId, Dialogs.getDialogPath(dialogId), 17L, (short)10000);
            if (w == null) {
                UICollectMachineFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer la fen\u00eatre de r\u00e9ceptacle !!! loaded=" + Xulor.getInstance().isLoaded(dialogId)));
            }
            else {
                UIEquipmentFrame.getInstance().ensureInventoryLinkedWindowLayout(w);
            }
            WakfuGameEntity.getInstance().pushFrame(this);
        }
    }
    
    public AbstractCollectorView getAbstractCollectMachineView() {
        return this.m_abstractCollectMachineView;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19322: {
                final UICollectorMessage msg = (UICollectorMessage)message;
                int quantity = msg.getIntValue();
                final CollectorContentView contentView = msg.getCollectorContentView();
                final int maxQuantity = contentView.getMaxPlayerQuantity();
                if (quantity > maxQuantity || quantity == -1) {
                    quantity = maxQuantity;
                }
                contentView.setCurrentPlayerQuantity(quantity);
                PropertiesProvider.getInstance().firePropertyValueChanged(contentView, "currentPlayerQuantity", "canMax", "valid");
                return false;
            }
            case 19323: {
                final UICollectorMessage msg = (UICollectorMessage)message;
                final CollectorContentView contentView2 = msg.getCollectorContentView();
                final TIntIntHashMap items = new TIntIntHashMap();
                final BrowseCollectorOccupation browseCollectorOccupation = (BrowseCollectorOccupation)WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
                if (contentView2 instanceof ExpectingCollectMachineView.ExpectedCollectorItemView) {
                    items.put(((ExpectingCollectMachineView.ExpectedCollectorItemView)contentView2).getRefId(), contentView2.getCurrentPlayerQuantity());
                    browseCollectorOccupation.sendCollectorInventoryModificationRequest(items, 0);
                }
                else {
                    browseCollectorOccupation.sendCollectorInventoryModificationRequest(items, this.m_abstractCollectMachineView.getKamaQuantity());
                }
                return false;
            }
            case 19320: {
                final UIItemMessage msg2 = (UIItemMessage)message;
                final InventoryAddItemAction action = new InventoryAddItemAction(msg2.getLongValue(), msg2.getShortValue());
                sendMessageForAction(action);
                return false;
            }
            case 19321: {
                final UIItemMessage msg2 = (UIItemMessage)message;
                final long destination = msg2.getDestinationUniqueId();
                sendMessageForAction(new InventoryRemoveItemAction(msg2.getItem().getUniqueId(), msg2.getQuantity(), destination, msg2.getDestinationPosition()));
                CollectMachineDialogActions.setDraggedItemId(-1L);
                return false;
            }
            case 19324: {
                final int money = ((UIMessage)message).getIntValue();
                if (money == 0) {
                    return false;
                }
                sendMessageForAction(new InventoryAddMoneyAction(money));
                return false;
            }
            case 19325: {
                final int money = ((UIMessage)message).getIntValue();
                if (money == 0) {
                    return false;
                }
                sendMessageForAction(new InventoryRemoveMoneyAction(money));
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private static void sendMessageForAction(final InventoryAction action) {
        final CollectorInventoryFreeModificationRequestMessage request = new CollectorInventoryFreeModificationRequestMessage(action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
    }
    
    @Override
    public long getId() {
        return 11L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)UICollectMachineFrame.class);
        UICollectMachineFrame.m_instance = new UICollectMachineFrame();
    }
}
