package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.bookcase.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.wakfu.common.game.interactiveElements.action.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;

public class UIBookcaseFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIBookcaseFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private BookcaseView m_bookcaseView;
    
    public static UIBookcaseFrame getInstance() {
        return UIBookcaseFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("bookcaseDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIBookcaseFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("bookcaseDialog", Dialogs.getDialogPath("bookcaseDialog"), 1L, (short)10000);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            Xulor.getInstance().putActionClass("wakfu.bookcase", BookcaseDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("bookcaseDialog");
            PropertiesProvider.getInstance().removeProperty("bookcase");
            WakfuSoundManager.getInstance().playGUISound(600013L);
            Xulor.getInstance().removeActionClass("wakfu.bookcase");
        }
    }
    
    private void addBook(final Item item, final short index) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!item.getReferenceItem().hasItemProperty(ItemProperty.BOOK)) {
            return;
        }
        final Bookcase bookcase = this.m_bookcaseView.getBookcase();
        final ItemizableInfo itemizableInfo = bookcase.getItemizableInfo();
        if (itemizableInfo.getOwnerId() != localPlayer.getId() && !MRUActionUtils.canManageInHavenWorld(itemizableInfo.getOwnerId())) {
            return;
        }
        if (!bookcase.getBag().canAdd(item, index)) {
            return;
        }
        final InteractiveElementParametrizedAction action = new BookcaseAddBookAtIndex(item.getUniqueId(), (byte)index);
        final InteractiveElementParametrizedActionMessage msg = new InteractiveElementParametrizedActionMessage();
        msg.setElementId(bookcase.getId());
        msg.setAction(action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void removeBook(final short index) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Bookcase bookcase = this.m_bookcaseView.getBookcase();
        final ItemizableInfo itemizableInfo = bookcase.getItemizableInfo();
        if (itemizableInfo.getOwnerId() != localPlayer.getId() && !MRUActionUtils.canManageInHavenWorld(itemizableInfo.getOwnerId())) {
            return;
        }
        final InteractiveElementParametrizedAction action = new RemoveBookFromIndex((byte)index);
        final InteractiveElementParametrizedActionMessage msg = new InteractiveElementParametrizedActionMessage();
        msg.setElementId(this.m_bookcaseView.getBookcase().getId());
        msg.setAction(action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void swapBook(final byte index, final byte index2) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Bookcase bookcase = this.m_bookcaseView.getBookcase();
        final ItemizableInfo itemizableInfo = bookcase.getItemizableInfo();
        if (itemizableInfo.getOwnerId() != localPlayer.getId() && !MRUActionUtils.canManageInHavenWorld(itemizableInfo.getOwnerId())) {
            return;
        }
        final InteractiveElementParametrizedAction action = new SwapBooksFromIndex(index, index2);
        final InteractiveElementParametrizedActionMessage msg = new InteractiveElementParametrizedActionMessage();
        msg.setElementId(this.m_bookcaseView.getBookcase().getId());
        msg.setAction(action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19316: {
                final UIItemMessage msg = (UIItemMessage)message;
                this.addBook(msg.getItem(), msg.getShortValue());
                return false;
            }
            case 19318: {
                final UIMessage msg2 = (UIMessage)message;
                final byte index1 = msg2.getByteValue();
                final byte index2 = (byte)msg2.getShortValue();
                this.swapBook(index1, index2);
                return false;
            }
            case 19317: {
                final UIItemMessage msg = (UIItemMessage)message;
                this.removeBook(msg.getShortValue());
                return false;
            }
            case 19319: {
                final UIItemMessage msg = (UIItemMessage)message;
                final Item item = msg.getItem();
                if (item != null) {
                    ((OpenBackgroundDisplayItemAction)item.getReferenceItem().getItemAction()).display();
                }
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
    
    public void setBookcase(final Bookcase bookcase) {
        this.m_bookcaseView = new BookcaseView(bookcase);
        PropertiesProvider.getInstance().setPropertyValue("bookcase", this.m_bookcaseView);
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIBookcaseFrame.class);
        m_instance = new UIBookcaseFrame();
    }
}
