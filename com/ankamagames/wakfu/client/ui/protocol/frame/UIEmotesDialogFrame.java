package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UIEmotesDialogFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIEmotesDialogFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIEmotesDialogFrame getInstance() {
        return UIEmotesDialogFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
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
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("emotesInventory", player.getEmoteHandler());
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("emotesInventoryDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIEmotesDialogFrame.getInstance());
                        UIControlCenterContainerFrame.getInstance().selectEmoteButton(false);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("emotesInventoryDialog", Dialogs.getDialogPath("emotesInventoryDialog"), 32769L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.emotesInventory", EmotesInventoryDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600012L);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().removeProperty("emotesInventory");
            PropertiesProvider.getInstance().removeProperty("overSmiley");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("emotesInventoryDialog");
            Xulor.getInstance().removeActionClass("wakfu.emotesInventory");
            WakfuSoundManager.getInstance().playGUISound(600013L);
        }
    }
    
    public void loadUnloadEmotesDialog() {
        if (WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().removeFrame(this);
            UIControlCenterContainerFrame.getInstance().selectEmoteButton(false);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(this);
            UIControlCenterContainerFrame.getInstance().selectEmoteButton(true);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIEmotesDialogFrame.class);
        m_instance = new UIEmotesDialogFrame();
    }
}
