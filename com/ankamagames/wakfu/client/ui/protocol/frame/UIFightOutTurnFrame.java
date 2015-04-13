package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UIFightOutTurnFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static UIFightOutTurnFrame m_instance;
    private boolean m_active;
    
    public UIFightOutTurnFrame() {
        super();
        this.m_active = false;
    }
    
    public static UIFightOutTurnFrame getInstance() {
        return UIFightOutTurnFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (!this.m_active) {
            UIFightOutTurnFrame.m_logger.warn((Object)"Traitement d'un message alors que la frame n'est plus active");
        }
        switch (message.getId()) {
            case 18015: {
                UIFightFrame.getInstance();
                UIFightFrame.updateMovementDisplay();
                return true;
            }
            case 19992: {
                return true;
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (isAboutToBeAdded) {
            return;
        }
        this.m_active = true;
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        player.updateCharacteristicViews(FighterCharacteristicType.AP, FighterCharacteristicType.HP, FighterCharacteristicType.MP, FighterCharacteristicType.WP);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (isAboutToBeRemoved) {
            return;
        }
        this.m_active = false;
        UIFightFrame.getInstance();
        UIFightFrame.clearMovementDisplay();
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFightOutTurnFrame.class);
        UIFightOutTurnFrame.m_instance = new UIFightOutTurnFrame();
    }
}
