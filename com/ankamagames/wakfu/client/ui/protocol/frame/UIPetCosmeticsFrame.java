package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.pet.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.cosmetic.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIPetCosmeticsFrame implements MessageFrame
{
    private static final UIPetCosmeticsFrame INSTANCE;
    protected static final Logger m_logger;
    private PetDetailDialogView m_petView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIPetCosmeticsFrame getInstance() {
        return UIPetCosmeticsFrame.INSTANCE;
    }
    
    public void setPetView(final PetDetailDialogView petView) {
        this.m_petView = petView;
    }
    
    public PetDetailDialogView getPetView() {
        return this.m_petView;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19401: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int costumeId = msg.getIntValue();
                final PetEquipItemRequestMessage petEquipItemRequestMessage = new PetEquipItemRequestMessage();
                petEquipItemRequestMessage.setPetId(this.m_petView.getPetItem().getUniqueId());
                petEquipItemRequestMessage.setItemId(costumeId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(petEquipItemRequestMessage);
                final PetCosmeticsInventoryView petCosmeticsInventory = WakfuGameEntity.getInstance().getLocalPlayer().getPetCosmeticsInventory();
                petCosmeticsInventory.select(costumeId);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("petCosmeticsDialog") || id.startsWith("petDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIPetCosmeticsFrame.this);
                    }
                }
            };
            final PetCosmeticsInventoryView petCosmeticsInventory = WakfuGameEntity.getInstance().getLocalPlayer().getPetCosmeticsInventory();
            petCosmeticsInventory.select(this.m_petView.getPet().getEquippedRefItemId());
            petCosmeticsInventory.filter(this.m_petView.getPet().getDefinition());
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("petCosmeticsDialog", Dialogs.getDialogPath("petCosmeticsDialog"), 32768L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("pet", this.m_petView, "petCosmeticsDialog");
            Xulor.getInstance().putActionClass("wakfu.petCosmetics", PetCosmeticsDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("petCosmeticsDialog");
            Xulor.getInstance().removeActionClass("wakfu.petCosmetics");
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIPetCosmeticsFrame();
        m_logger = Logger.getLogger((Class)UIPetCosmeticsFrame.class);
    }
}
