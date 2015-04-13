package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.stuffPreview.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIStuffPreviewFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final UIStuffPreviewFrame INSTANCE;
    private DialogUnloadListener m_dialogUnloadListener;
    private StuffPreview m_stuffPreview;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("stuffPreviewDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIStuffPreviewFrame.INSTANCE);
                    }
                }
            };
            PropertiesProvider.getInstance().setPropertyValue("stuffPreview", this.m_stuffPreview);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("stuffPreviewDialog", Dialogs.getDialogPath("stuffPreviewDialog"), 1L, (short)10000);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            Xulor.getInstance().putActionClass("wakfu.stuffPreview", StuffPreviewDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                localPlayer.getEquipmentInventory().removeObserver(this.m_stuffPreview);
            }
            StuffPreviewDialogActions.setDraggedItemId(-1L);
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("stuffPreviewDialog");
            PropertiesProvider.getInstance().removeProperty("stuffPreview");
            WakfuSoundManager.getInstance().playGUISound(600013L);
            Xulor.getInstance().removeActionClass("wakfu.stuffPreview");
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19368: {
                final UIItemMessage msg = (UIItemMessage)message;
                this.equipItem(msg.getItem(), msg.getByteValue());
                return false;
            }
            case 19369: {
                final UIItemMessage msg = (UIItemMessage)message;
                this.unequipItem(msg);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public StuffPreview getStuffPreview() {
        return this.m_stuffPreview;
    }
    
    private void equipItem(final Item item, final byte position) {
        this.m_stuffPreview.addEquipment(item, position);
    }
    
    private void unequipItem(final UIItemMessage msg) {
        this.m_stuffPreview.removeEquipment(msg.getByteValue());
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void equipItem(final ReferenceItem referenceItem) {
        final Item item = new Item();
        item.initializeWithReferenceItem(referenceItem);
        item.setQuantity((short)1);
        UIStuffPreviewFrame.INSTANCE.equipItem(item);
    }
    
    public void equipItem(final Item item) {
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            this.m_stuffPreview = new StuffPreview();
            WakfuGameEntity.getInstance().pushFrame(this);
        }
        final ItemEquipment equipmentInventory = WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory();
        byte position = (byte)equipmentInventory.getPosition(item.getUniqueId());
        if (position == -1) {
            final AbstractItemType itemType = item.getReferenceItem().getItemType();
            final EquipmentPosition[] arr$;
            final EquipmentPosition[] equipmentPositions = arr$ = itemType.getEquipmentPositions();
            for (final EquipmentPosition ep : arr$) {
                final byte pos = ep.getId();
                if (this.m_stuffPreview.getFromPosition(pos) == null) {
                    position = pos;
                }
            }
            if (position == -1) {
                position = equipmentPositions[0].getId();
            }
        }
        this.m_stuffPreview.addEquipment(item, position);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIStuffPreviewFrame.class);
        INSTANCE = new UIStuffPreviewFrame();
    }
}
