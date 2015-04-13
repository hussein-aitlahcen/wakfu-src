package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class UISitOccupationFrame implements MessageFrame
{
    private static final UISitOccupationFrame m_instance;
    
    public static UISitOccupationFrame getInstance() {
        return UISitOccupationFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 19992:
            case 19995: {
                final AbstractOccupation occupation = localPlayer.getCurrentOccupation();
                if (occupation == null || occupation.getOccupationTypeId() != 16) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                    return false;
                }
                final SitOccupation sitOccup = (SitOccupation)occupation;
                if (sitOccup.actorInTransition(localPlayer)) {
                    return false;
                }
                if (isLeftClic((UIWorldSceneMouseMessage)message)) {
                    final UIMessage result = duplicateMessage(message);
                    sitOccup.askFinishOccupation(new AnimationEndedListener() {
                        @Override
                        public void animationEnded(final AnimatedElement element) {
                            WakfuGameEntity.getInstance().removeFrame(UISitOccupationFrame.this);
                            if (result != null) {
                                Worker.getInstance().pushMessage(result);
                            }
                        }
                    });
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private static boolean isLeftClic(final UIWorldSceneMouseMessage msg) {
        switch (msg.getId()) {
            case 19992: {
                return msg.isButtonLeft();
            }
            case 19995: {
                return UIWorldSceneMouseMessage.isLeftButtonDown();
            }
            default: {
                return false;
            }
        }
    }
    
    private static UIMessage duplicateMessage(final Message msg) {
        switch (msg.getId()) {
            case 19992: {
                return duplicateMessage((UIWorldSceneMouseMessage)msg);
            }
            case 19995: {
                return duplicateMessage((UIWorldSceneMouseMessage)msg);
            }
            default: {
                return null;
            }
        }
    }
    
    private static UIMessage duplicateMessage(final UIWorldSceneMouseMessage original) {
        return original.duplicate();
    }
    
    public void clear() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = localPlayer.getCurrentOccupation();
        if (occupation != null && occupation.getOccupationTypeId() == 16) {
            ((SitOccupation)occupation).resetActor(localPlayer.getActor(), null);
            localPlayer.setCurrentOccupation(null);
        }
        WakfuGameEntity.getInstance().removeFrame(this);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_instance = new UISitOccupationFrame();
    }
}
