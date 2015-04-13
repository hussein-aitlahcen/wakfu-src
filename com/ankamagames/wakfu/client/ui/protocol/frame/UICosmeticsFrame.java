package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.costume.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;

public class UICosmeticsFrame implements MessageFrame
{
    private static final UICosmeticsFrame INSTANCE;
    protected static final Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UICosmeticsFrame getInstance() {
        return UICosmeticsFrame.INSTANCE;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19400: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int costumeId = msg.getIntValue();
                final LocalPlayerCharacter player = UIEquipmentFrame.getCharacter();
                final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(costumeId);
                if (refItem != null) {
                    final SimpleCriterion criterion = refItem.getCriterion(ActionsOnItem.EQUIP);
                    if (criterion != null && !criterion.isValid(player, player, refItem, player.getAchievementsContext())) {
                        ErrorsMessageTranslator.getInstance().pushMessage(60, 3, new Object[0]);
                        return false;
                    }
                }
                final Message netMsg = new PutOnCostumeMessage(player.getId(), costumeId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
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
                    if (id.equals("cosmeticsDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UICosmeticsFrame.this);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("cosmeticsDialog", Dialogs.getDialogPath("cosmeticsDialog"), 32768L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.cosmetics", CosmeticsDialogActions.class);
            PropertiesProvider.getInstance().setLocalPropertyValue("localPlayer", UIEquipmentFrame.getCharacter(), "cosmeticsDialog");
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("cosmeticsDialog");
            Xulor.getInstance().removeActionClass("wakfu.cosmetics");
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
        INSTANCE = new UICosmeticsFrame();
        m_logger = Logger.getLogger((Class)UICosmeticsFrame.class);
    }
}
