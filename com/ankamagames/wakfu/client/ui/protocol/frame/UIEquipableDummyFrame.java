package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.equipableDummy.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;

public class UIEquipableDummyFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIEquipableDummyFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private EquipableDummyView m_dummyView;
    
    public static UIEquipableDummyFrame getInstance() {
        return UIEquipableDummyFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("equipableDummyDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIEquipableDummyFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("equipableDummyDialog", Dialogs.getDialogPath("equipableDummyDialog"), 1L, (short)10000).getElementMap();
            WakfuSoundManager.getInstance().playGUISound(600012L);
            if (this.m_dummyView.getDummy().getItem() == null) {
                this.highlightItemSlot();
            }
            Xulor.getInstance().putActionClass("wakfu.equipableDummy", EquipableDummyDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("equipableDummyDialog");
            PropertiesProvider.getInstance().removeProperty("equipableDummy");
            WakfuSoundManager.getInstance().playGUISound(600013L);
            Xulor.getInstance().removeActionClass("wakfu.equipableDummy");
        }
    }
    
    private void highlightItemSlot() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("equipableDummyDialog");
        TweenHelper.fadeHighlight((Widget)map.getElement("pennon"));
    }
    
    private void equipItem(final Item item) {
        ChangeEquipableDummyItem action;
        if (item == null) {
            action = new ChangeEquipableDummyItem(-1L);
        }
        else {
            final AbstractReferenceItem referenceItem = item.getReferenceItem();
            final SimpleCriterion criterion = referenceItem.getCriterion(ActionsOnItem.EXCHANGE);
            if (item.isBound() || (criterion != null && !criterion.isValid(null, null, null, null))) {
                final String chatMessage = WakfuTranslator.getInstance().getString("error.cannotEquipNonExchangeableItem");
                ChatManager.getInstance().pushMessage(chatMessage, 3);
                return;
            }
            if (item.isRent()) {
                final String chatMessage = WakfuTranslator.getInstance().getString("error.cannotEquipRentItem");
                ChatManager.getInstance().pushMessage(chatMessage, 3);
                return;
            }
            final AbstractItemAction itemAction = referenceItem.getItemAction();
            final boolean isItemSet = itemAction instanceof SplitItemSetItemAction;
            final boolean isBadge = referenceItem.getItemType().getId() == 646;
            final boolean isCostume = referenceItem.getItemType().getId() == 647;
            if (!isItemSet && !isBadge && !isCostume) {
                return;
            }
            action = new ChangeEquipableDummyItem(item.getUniqueId());
        }
        final InteractiveElementParametrizedActionMessage msg = new InteractiveElementParametrizedActionMessage();
        msg.setElementId(this.m_dummyView.getDummy().getId());
        msg.setAction(action);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19314: {
                final UIItemMessage msg = (UIItemMessage)message;
                this.equipItem(msg.getItem());
                return false;
            }
            case 19315: {
                final UIItemMessage msg = (UIItemMessage)message;
                final Item item = this.m_dummyView.getDummy().getItem();
                if (item == null) {
                    return false;
                }
                if (item.getReferenceId() == msg.getIntValue()) {
                    this.equipItem(null);
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
    
    public void setEquipableDummy(final EquipableDummy dummy) {
        this.m_dummyView = new EquipableDummyView(dummy);
        PropertiesProvider.getInstance().setPropertyValue("equipableDummy", this.m_dummyView);
        WakfuGameEntity.getInstance().pushFrame(this);
        if (Xulor.getInstance().isLoaded("equipableDummyDialog") && this.m_dummyView.getDummy().getItem() == null) {
            this.highlightItemSlot();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIEquipableDummyFrame.class);
        m_instance = new UIEquipableDummyFrame();
    }
}
