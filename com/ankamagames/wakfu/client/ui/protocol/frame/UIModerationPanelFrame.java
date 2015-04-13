package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.moderationNew.panel.*;

public class UIModerationPanelFrame implements MessageFrame
{
    public static final UIModerationPanelFrame INSTANCE;
    private final ModerationPanelView m_moderationPanelView;
    private final DialogUnloadListener m_dialogUnloadListener;
    
    public UIModerationPanelFrame() {
        super();
        this.m_moderationPanelView = new ModerationPanelView();
        this.m_dialogUnloadListener = new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if ("moderationPanelDialog".equals(id)) {
                    WakfuGameEntity.getInstance().removeFrame(UIModerationPanelFrame.INSTANCE);
                }
            }
        };
    }
    
    public void request(final short requestId, final Object... parameters) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setCommand(requestId);
        msg.setServerId((byte)2);
        for (final Object p : parameters) {
            msg.addParameter(p);
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public void requestWithServer(final short requestId, final byte server, final Object... parameters) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId(server);
        msg.setCommand(requestId);
        for (final Object p : parameters) {
            msg.addParameter(p);
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("moderationPanelDialog", Dialogs.getDialogPath("moderationPanelDialog"), 32769L, (short)10000);
            PropertiesProvider.getInstance().setPropertyValue("moderationPanelView", this.m_moderationPanelView);
            Xulor.getInstance().putActionClass("wakfu.moderationPanel", ModerationPanelDialogActions.class);
            if (!WakfuGameEntity.getInstance().hasFrame(NetModerationPanelFrame.getInstance())) {
                WakfuGameEntity.getInstance().pushFrame(NetModerationPanelFrame.getInstance());
            }
            if (this.m_moderationPanelView.getCurrentPage() == ModerationPanelPage.PLAYER) {
                this.request((short)1, (byte)3, this.m_moderationPanelView.getCurrentPlayer().getName());
            }
            if (this.m_moderationPanelView.getCurrentPage() == ModerationPanelPage.MAIN) {
                this.request((short)211, new Object[0]);
                this.request((short)210, new Object[0]);
            }
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            if (Xulor.getInstance().isLoaded("moderationPanelDialog")) {
                Xulor.getInstance().unload("moderationPanelDialog");
            }
            PropertiesProvider.getInstance().removeProperty("moderationPanelView");
            Xulor.getInstance().removeActionClass("wakfu.moderationPanel");
            if (WakfuGameEntity.getInstance().hasFrame(NetModerationPanelFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(NetModerationPanelFrame.getInstance());
            }
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    public ModerationPanelView getModerationPanelView() {
        return this.m_moderationPanelView;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIModerationPanelFrame();
    }
}
