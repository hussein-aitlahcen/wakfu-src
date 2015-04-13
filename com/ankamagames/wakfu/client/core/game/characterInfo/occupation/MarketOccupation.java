package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class MarketOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private final MarketBoard m_board;
    
    public MarketOccupation(final MarketBoard board) {
        super();
        this.m_board = board;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 19;
    }
    
    @Override
    public boolean isAllowed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Actor localActor = localPlayer.getActor();
        return this.m_board.isInApproachPoint(localActor.getWorldCoordinates());
    }
    
    @Override
    public void begin() {
        MarketOccupation.m_logger.info((Object)("Lancement de l'occupation MARKET sur la board " + this.m_board));
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        this.m_board.fireAction(InteractiveElementAction.START_BROWSING, WakfuGameEntity.getInstance().getLocalPlayer());
        if (WakfuGameEntity.getInstance().hasFrame(UIMarketFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIMarketFrame.getInstance());
        }
        UIMarketFrame.getInstance().setMarketBoard(this.m_board);
        WakfuGameEntity.getInstance().pushFrame(UIMarketFrame.getInstance());
        WakfuGameEntity.getInstance().pushFrame(NetMarketFrame.getInstance());
        this.m_localPlayer.setCurrentOccupation(this);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        MarketOccupation.m_logger.info((Object)("On annule l'occupation MARKET sur la board " + this.m_board + " (fromServer=" + fromServeur + ", sendMessage=" + sendMessage + ")"));
        WakfuGameEntity.getInstance().removeFrame(UIMarketFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(NetMarketFrame.getInstance());
        if (sendMessage) {
            this.m_board.fireAction(InteractiveElementAction.STOP_BROWSING, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        return true;
    }
    
    @Override
    public boolean finish() {
        MarketOccupation.m_logger.info((Object)("On arr\u00eate l'occupation MARKET sur la board " + this.m_board));
        WakfuGameEntity.getInstance().removeFrame(NetMarketFrame.getInstance());
        this.m_board.fireAction(InteractiveElementAction.STOP_BROWSING, WakfuGameEntity.getInstance().getLocalPlayer());
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MarketOccupation.class);
    }
}
