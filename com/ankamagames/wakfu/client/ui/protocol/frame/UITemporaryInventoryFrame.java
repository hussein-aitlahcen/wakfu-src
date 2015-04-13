package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UITemporaryInventoryFrame implements MessageFrame, DialogCloseRequestListener
{
    protected static final Logger m_logger;
    private static UITemporaryInventoryFrame m_instance;
    private boolean m_isMessageBoxOpened;
    private LocalPlayerCharacter m_character;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UITemporaryInventoryFrame() {
        super();
        this.m_isMessageBoxOpened = false;
    }
    
    public static UITemporaryInventoryFrame getInstance() {
        return UITemporaryInventoryFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19461: {
                final UIMessage msg = (UIMessage)message;
                final long characterId = msg.getLongValue();
                this.m_character = HeroesManager.INSTANCE.getHero(characterId);
                PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", this.m_character, "temporaryTransferInventoryDialog");
                return false;
            }
            case 16431: {
                this.askForTemporaryInventoryDestruction();
                return false;
            }
            case 16825: {
                final UIItemMessage msg2 = (UIItemMessage)message;
                final LocalPlayerCharacter destination = HeroUtils.getHeroWithBagUid(this.m_character.getOwnerId(), msg2.getDestinationUniqueId());
                final Item item = msg2.getItem();
                final byte destinationPosition = msg2.getDestinationPosition();
                if (msg2.getDestinationUniqueId() == 2L) {
                    if (msg2.getQuantity() != 1) {
                        return false;
                    }
                    final ItemEquipment equipmentInventory = destination.getEquipmentInventory();
                    final InventoryContentChecker<Item> contentChecker = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getContentChecker();
                    if (!contentChecker.checkCriterion(item, destination, destination.getAppropriateContext())) {
                        ErrorsMessageTranslator.getInstance().pushMessage(60, 3, new Object[0]);
                        return false;
                    }
                    if (contentChecker.canAddItem((Inventory<Item>)equipmentInventory, item, destinationPosition) < 0) {
                        ErrorsMessageTranslator.getInstance().pushMessage(60, 3, new Object[0]);
                        return false;
                    }
                }
                else if (msg2.getDestinationUniqueId() != -1L) {
                    final AbstractBag targetBag = destination.getBags().get(msg2.getDestinationUniqueId());
                    final Item itemInPlace = targetBag.getFromPosition(destinationPosition);
                    if (itemInPlace != null && (itemInPlace.getStackMaximumHeight() <= 1 || !itemInPlace.canStackWith(item) || itemInPlace.getQuantity() == itemInPlace.getStackMaximumHeight())) {
                        return false;
                    }
                }
                final TempInventoryMoveRequestMessage netMsg = new TempInventoryMoveRequestMessage();
                netMsg.setDestination(msg2.getDestinationUniqueId());
                netMsg.setDestinationPosition(destinationPosition);
                netMsg.setQuantity(msg2.getQuantity());
                netMsg.setItemUId(item.getUniqueId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public LocalPlayerCharacter getCharacter() {
        return this.m_character;
    }
    
    @Override
    public int onDialogCloseRequest(final String id) {
        if (id.equals("temporaryTransferInventoryDialog")) {
            this.askForTemporaryInventoryDestruction();
            return 2;
        }
        return 0;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("temporaryInventory.currentDragItemId", -1);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("temporaryTransferInventoryDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UITemporaryInventoryFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().addDialogCloseRequestListener(this);
            Xulor.getInstance().load("temporaryTransferInventoryDialog", Dialogs.getDialogPath("temporaryTransferInventoryDialog"), 32769L, (short)10000);
            this.m_character = WakfuGameEntity.getInstance().getLocalPlayer();
            PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", this.m_character, "temporaryTransferInventoryDialog");
            Xulor.getInstance().putActionClass("wakfu.temporaryInventory", TemporaryTransferInventoryDialogActions.class);
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("temporaryInventory.descr"), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 514L, 102, 1);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                Xulor.getInstance().unload("splitStackDialog");
            }
            PropertiesProvider.getInstance().removeProperty("temporaryInventory.currentDragItemId");
            PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", null, "temporaryTransferInventoryDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this);
            Xulor.getInstance().unload("temporaryTransferInventoryDialog");
            Xulor.getInstance().removeActionClass("wakfu.temporaryInventory");
            this.m_isMessageBoxOpened = false;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void askForTemporaryInventoryDestruction() {
        if (this.m_isMessageBoxOpened) {
            return;
        }
        final MessageBoxControler controller = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("temporaryInventory.reallyMove"), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 4102L, 102, 1);
        controller.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2) {
                    final TempInventoryClearRequestMessage netMessage = new TempInventoryClearRequestMessage();
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                    WakfuGameEntity.getInstance().removeFrame(UITemporaryInventoryFrame.getInstance());
                }
                UITemporaryInventoryFrame.this.m_isMessageBoxOpened = false;
            }
        });
        this.m_isMessageBoxOpened = true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UITemporaryInventoryFrame.class);
        UITemporaryInventoryFrame.m_instance = new UITemporaryInventoryFrame();
    }
}
