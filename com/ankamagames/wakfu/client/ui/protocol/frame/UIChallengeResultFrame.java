package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;

public class UIChallengeResultFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIChallengeResultFrame INSTANCE;
    private StaticChallengeView m_view;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIChallengeResultFrame getInstance() {
        return UIChallengeResultFrame.INSTANCE;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setChallenge(final ChallengeData challengeData) {
        (this.m_view = new StaticChallengeView(challengeData.copy())).computeScore();
        this.updateProperty();
    }
    
    private void updateProperty() {
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", this.m_view, "challengeResultDialog");
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("challengeResultDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIChallengeResultFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("challengeResultDialog", Dialogs.getDialogPath("challengeResultDialog"), 1L, (short)10000);
            this.updateProperty();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("challengeResultDialog");
            this.m_view = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIChallengeResultFrame.class);
        INSTANCE = new UIChallengeResultFrame();
    }
}
